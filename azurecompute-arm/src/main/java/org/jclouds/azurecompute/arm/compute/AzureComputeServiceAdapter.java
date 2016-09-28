/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.azurecompute.arm.compute;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.contains;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static org.jclouds.azurecompute.arm.compute.extensions.AzureComputeImageExtension.CONTAINER_NAME;
import static org.jclouds.azurecompute.arm.compute.extensions.AzureComputeImageExtension.CUSTOM_IMAGE_OFFER;
import static org.jclouds.azurecompute.arm.compute.functions.VMImageToImage.decodeFieldsFromUniqueId;
import static org.jclouds.azurecompute.arm.compute.functions.VMImageToImage.encodeFieldsToUniqueIdCustom;
import static org.jclouds.util.Closeables2.closeQuietly;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.azurecompute.arm.AzureComputeApi;
import org.jclouds.azurecompute.arm.compute.config.AzureComputeServiceContextModule.AzureComputeConstants;
import org.jclouds.azurecompute.arm.compute.options.AzureTemplateOptions;
import org.jclouds.azurecompute.arm.domain.DataDisk;
import org.jclouds.azurecompute.arm.domain.HardwareProfile;
import org.jclouds.azurecompute.arm.domain.IdReference;
import org.jclouds.azurecompute.arm.domain.ImageReference;
import org.jclouds.azurecompute.arm.domain.IpConfiguration;
import org.jclouds.azurecompute.arm.domain.IpConfigurationProperties;
import org.jclouds.azurecompute.arm.domain.Location;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceCard;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceCardProperties;
import org.jclouds.azurecompute.arm.domain.NetworkProfile;
import org.jclouds.azurecompute.arm.domain.OSDisk;
import org.jclouds.azurecompute.arm.domain.OSProfile;
import org.jclouds.azurecompute.arm.domain.Offer;
import org.jclouds.azurecompute.arm.domain.PublicIPAddress;
import org.jclouds.azurecompute.arm.domain.PublicIPAddressProperties;
import org.jclouds.azurecompute.arm.domain.ResourceProviderMetaData;
import org.jclouds.azurecompute.arm.domain.SKU;
import org.jclouds.azurecompute.arm.domain.StorageProfile;
import org.jclouds.azurecompute.arm.domain.StorageService;
import org.jclouds.azurecompute.arm.domain.StorageService.Status;
import org.jclouds.azurecompute.arm.domain.StorageServiceKeys;
import org.jclouds.azurecompute.arm.domain.VHD;
import org.jclouds.azurecompute.arm.domain.VMHardware;
import org.jclouds.azurecompute.arm.domain.VMImage;
import org.jclouds.azurecompute.arm.domain.VMSize;
import org.jclouds.azurecompute.arm.domain.Version;
import org.jclouds.azurecompute.arm.domain.VirtualMachine;
import org.jclouds.azurecompute.arm.domain.VirtualMachineProperties;
import org.jclouds.azurecompute.arm.features.OSImageApi;
import org.jclouds.azurecompute.arm.features.PublicIPAddressApi;
import org.jclouds.azurecompute.arm.functions.CleanupResources;
import org.jclouds.azurecompute.arm.util.BlobHelper;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.location.Region;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Defines the connection between the {@link AzureComputeApi} implementation and the jclouds
 * {@link org.jclouds.compute.ComputeService}.
 */
@Singleton
public class AzureComputeServiceAdapter implements ComputeServiceAdapter<VirtualMachine, VMHardware, VMImage, Location> {

   private final String azureGroup;
   private final CleanupResources cleanupResources;
   private final AzureComputeApi api;
   private final AzureComputeConstants azureComputeConstants;
   private final Supplier<Set<String>> regionIds;
   private final Predicate<String> publicIpAvailable;

   @Inject
   AzureComputeServiceAdapter(final AzureComputeApi api, final AzureComputeConstants azureComputeConstants,
         CleanupResources cleanupResources, @Region Supplier<Set<String>> regionIds,
         @Named("PublicIpAvailable") Predicate<String> publicIpAvailable) {
      this.api = api;
      this.azureComputeConstants = azureComputeConstants;
      this.azureGroup = azureComputeConstants.azureResourceGroup();
      this.cleanupResources = cleanupResources;
      this.regionIds = regionIds;
      this.publicIpAvailable = publicIpAvailable;
   }

   @Override
   public NodeAndInitialCredentials<VirtualMachine> createNodeWithGroupEncodedIntoName(
           final String group, final String name, final Template template) {

      AzureTemplateOptions templateOptions = template.getOptions().as(AzureTemplateOptions.class);

      // TODO Store group apart from the name to be able to identify nodes with custom names in the configured group
      // TODO ARM specific options
      // TODO user metadata and tags
      // TODO network ids => create one nic in each network
      // TODO inbound ports
      
      String locationName = template.getLocation().getId();
      String subnetId = templateOptions.getSubnetId();
      NetworkInterfaceCard nic = createNetworkInterfaceCard(subnetId, name, locationName); 
      StorageProfile storageProfile = createStorageProfile(name, template.getImage(), templateOptions.getBlob());
      HardwareProfile hardwareProfile = HardwareProfile.builder().vmSize(template.getHardware().getId()).build();
      OSProfile osProfile = createOsProfile(name, template);
      NetworkProfile networkProfile = NetworkProfile.builder().networkInterfaces(ImmutableList.of(IdReference.create(nic.id()))).build();
      VirtualMachineProperties virtualMachineProperties = VirtualMachineProperties.builder()
              .licenseType(null) // TODO
              .availabilitySet(null) // TODO
              .hardwareProfile(hardwareProfile)
              .storageProfile(storageProfile)
              .osProfile(osProfile)
              .networkProfile(networkProfile)
              .build();

      VirtualMachine virtualMachine = api.getVirtualMachineApi(azureGroup).create(name, template.getLocation().getId(), virtualMachineProperties);

      // Safe to pass null credentials here, as jclouds will default populate the node with the default credentials from the image, or the ones in the options, if provided.
      return new NodeAndInitialCredentials<VirtualMachine>(virtualMachine, name, null);
   }

   @Override
   public Iterable<VMHardware> listHardwareProfiles() {
      final List<VMHardware> hwProfiles = Lists.newArrayList();
      for (Location location : listLocations()) {
         Iterable<VMSize> vmSizes = api.getVMSizeApi(location.name()).list();
         for (VMSize vmSize : vmSizes){
            VMHardware hwProfile = VMHardware.create(
                    vmSize.name(),
                    vmSize.numberOfCores(),
                    vmSize.osDiskSizeInMB(),
                    vmSize.resourceDiskSizeInMB(),
                    vmSize.memoryInMB(),
                    vmSize.maxDataDiskCount(),
                    location.name(),
                    false);
            hwProfiles.add(hwProfile);
         }
      }
      return hwProfiles;
   }

   private List<VMImage> getImagesFromPublisher(String publisherName, String location) {
      List<VMImage> osImagesRef = Lists.newArrayList();
      OSImageApi osImageApi = api.getOSImageApi(location);
      Iterable<Offer> offerList = osImageApi.listOffers(publisherName);

      for (Offer offer : offerList) {
         Iterable<SKU> skuList = osImageApi.listSKUs(publisherName, offer.name());

         for (SKU sku : skuList) {
            Iterable<Version> versionList = osImageApi.listVersions(publisherName, offer.name(), sku.name());
            for (Version version : versionList) {
               VMImage vmImage = VMImage.azureImage().publisher(publisherName).offer(offer.name()).sku(sku.name())
                     .version(version.name()).location(location).build();
               osImagesRef.add(vmImage);
            }
         }
      }
      return osImagesRef;
   }

   private List<VMImage> listImagesByLocation(String location) {
      final List<VMImage> osImages = Lists.newArrayList();
      Iterable<String> publishers = Splitter.on(',').trimResults().omitEmptyStrings().split(this.azureComputeConstants.azureImagePublishers());
      for (String publisher : publishers) {
         osImages.addAll(getImagesFromPublisher(publisher, location));
      }
      return osImages;
   }

   @Override
   public Iterable<VMImage> listImages() {
      final List<VMImage> osImages = Lists.newArrayList();

      for (Location location : listLocations()){
         osImages.addAll(listImagesByLocation(location.name()));
      }
      // list custom images
      List<StorageService> storages = api.getStorageAccountApi(azureGroup).list();
      for (StorageService storage : storages) {
         String name = storage.name();
         StorageService storageService = api.getStorageAccountApi(azureGroup).get(name);
         if (storageService != null
               && Status.Succeeded == storageService.storageServiceProperties().provisioningState()) {
            String key = api.getStorageAccountApi(azureGroup).getKeys(name).key1();
            BlobHelper blobHelper = new BlobHelper(storage.name(), key);
            try {
               List<VMImage> images = blobHelper.getImages(CONTAINER_NAME, azureGroup, CUSTOM_IMAGE_OFFER,
                     storage.location());
               osImages.addAll(images);
            } finally {
               closeQuietly(blobHelper);
            }
         }
      }
      
      return osImages;
   }

   @Override
   public VMImage getImage(final String id) {
      VMImage image = decodeFieldsFromUniqueId(id);
      if (image.custom()) {
         VMImage customImage = null;
         StorageServiceKeys keys = api.getStorageAccountApi(azureGroup).getKeys(image.storage());
         if (keys == null) {
            // If the storage account for the image does not exist, it means the image was deleted
            return null;
         }
         
         BlobHelper blobHelper = new BlobHelper(image.storage(), keys.key1());
         try {
            if (blobHelper.customImageExists()) {
               List<VMImage> customImagesInStorage = blobHelper.getImages(CONTAINER_NAME, azureGroup,
                     CUSTOM_IMAGE_OFFER, image.location());
               customImage = find(customImagesInStorage, new Predicate<VMImage>() {
                  @Override
                  public boolean apply(VMImage input) {
                     return id.equals(encodeFieldsToUniqueIdCustom(input));
                  }
               }, null);
            }
         } finally {
            closeQuietly(blobHelper);
         }
         return customImage;
      }

      String location = image.location();
      String publisher = image.publisher();
      String offer = image.offer();
      String sku = image.sku();

      OSImageApi osImageApi = api.getOSImageApi(location);
      List<Version> versions = osImageApi.listVersions(publisher, offer, sku);
      if (!versions.isEmpty()) {
         return VMImage.azureImage().publisher(publisher).offer(offer).sku(sku).version(versions.get(0).name())
               .location(location).build();
      }
      return null;
   }

   @Override
   public Iterable<Location> listLocations() {
      final Iterable<String> vmLocations = FluentIterable.from(api.getResourceProviderApi().get("Microsoft.Compute"))
              .filter(new Predicate<ResourceProviderMetaData>() {
                 @Override
                 public boolean apply(ResourceProviderMetaData input) {
                    return input.resourceType().equals("virtualMachines");
                 }
              })
              .transformAndConcat(new Function<ResourceProviderMetaData, Iterable<String>>() {
                 @Override
                 public Iterable<String> apply(ResourceProviderMetaData resourceProviderMetaData) {
                    return resourceProviderMetaData.locations();
                 }
              });

      List<Location> locations = FluentIterable.from(api.getLocationApi().list())
              .filter(new Predicate<Location>() {
                 @Override
                 public boolean apply(Location location) {
                    return Iterables.contains(vmLocations, location.displayName());
                 }
              })
              .filter(new Predicate<Location>() {
                 @Override
                 public boolean apply(Location location) {
                    return regionIds.get().isEmpty() ? true : regionIds.get().contains(location.name());
                 }
              })
              .toList();

      return locations;
   }

   @Override
   public VirtualMachine getNode(final String id) {
      return api.getVirtualMachineApi(azureGroup).get(id);
   }

   @Override
   public void destroyNode(final String id) {
      checkState(cleanupResources.apply(id), "server(%s) and its resources still there after deleting!?", id);
   }

   @Override
   public void rebootNode(final String id) {
      api.getVirtualMachineApi(azureGroup).restart(id);
   }

   @Override
   public void resumeNode(final String id) {
      api.getVirtualMachineApi(azureGroup).start(id);
   }

   @Override
   public void suspendNode(final String id) {
      api.getVirtualMachineApi(azureGroup).stop(id);
   }

   @Override
   public Iterable<VirtualMachine> listNodes() {
      return api.getVirtualMachineApi(azureGroup).list();
   }

   @Override
   public Iterable<VirtualMachine> listNodesByIds(final Iterable<String> ids) {
      return filter(listNodes(), new Predicate<VirtualMachine>() {
         @Override
         public boolean apply(VirtualMachine virtualMachine) {
            return contains(ids, virtualMachine.id());
         }
      });
   }


   private OSProfile createOsProfile(String computerName, Template template) {
      String defaultLoginUser = template.getImage().getDefaultCredentials().getUser();
      String defaultLoginPassword = template.getImage().getDefaultCredentials().getOptionalPassword().get();
      String adminUsername = Objects.firstNonNull(template.getOptions().getLoginUser(), defaultLoginUser);
      String adminPassword = Objects.firstNonNull(template.getOptions().getLoginPassword(), defaultLoginPassword);
      OSProfile.Builder builder = OSProfile.builder().adminUsername(adminUsername).computerName(computerName);
      // prefer public key over password
      if (template.getOptions().getPublicKey() != null) {
         OSProfile.LinuxConfiguration linuxConfiguration = OSProfile.LinuxConfiguration.create("true",
                 OSProfile.LinuxConfiguration.SSH.create(ImmutableList.of(
                         OSProfile.LinuxConfiguration.SSH.SSHPublicKey.create(
                                 String.format("/home/%s/.ssh/authorized_keys", adminUsername),
                                 template.getOptions().getPublicKey())
                 ))
         );
         builder.linuxConfiguration(linuxConfiguration);
      } else {
         builder.adminPassword(adminPassword);
      }
      return builder.build();
   }

   private NetworkInterfaceCard createNetworkInterfaceCard(String subnetId, String name, String locationName) {
      final PublicIPAddressApi ipApi = api.getPublicIPAddressApi(azureGroup);

      PublicIPAddressProperties properties =
              PublicIPAddressProperties.builder()
                      .publicIPAllocationMethod("Static")
                      .idleTimeoutInMinutes(4)
                      .build();

      String publicIpAddressName = "public-address-" + name;
      PublicIPAddress ip = ipApi.createOrUpdate(publicIpAddressName, locationName, ImmutableMap.of("jclouds", name), properties);
      
      checkState(publicIpAvailable.apply(publicIpAddressName),
            "Public IP was not provisioned in the configured timeout");

      final NetworkInterfaceCardProperties networkInterfaceCardProperties =
              NetworkInterfaceCardProperties.builder()
                      .ipConfigurations(ImmutableList.of(
                              IpConfiguration.builder()
                                      .name("ipConfig-" + name)
                                      .properties(IpConfigurationProperties.builder()
                                              .privateIPAllocationMethod("Dynamic")
                                              .publicIPAddress(IdReference.create(ip.id()))
                                              .subnet(IdReference.create(subnetId))
                                              .build())
                                      .build()))
                      .build();

      String networkInterfaceCardName = "jc-nic-" + name;
      return api.getNetworkInterfaceCardApi(azureGroup).createOrUpdate(networkInterfaceCardName, locationName, networkInterfaceCardProperties, ImmutableMap.of("jclouds", name));
   }

   private StorageProfile createStorageProfile(String name, Image image, String blob) {
      VMImage imageRef = decodeFieldsFromUniqueId(image.getId());
      ImageReference imageReference = null;
      VHD sourceImage = null;
      String osType = null;
      
      if (!imageRef.custom()) {
         imageReference = ImageReference.builder()
               .publisher(image.getProviderId())
               .offer(image.getName())
               .sku(image.getVersion())
               .version("latest")
               .build();
      } else {
         sourceImage = VHD.create(image.getProviderId());

         // TODO: read the ostype from the image blob
         OsFamily osFamily = image.getOperatingSystem().getFamily();
         osType = osFamily == OsFamily.WINDOWS ? "Windows" : "Linux";
      }
      
      VHD vhd = VHD.create(blob + "vhds/" + name + ".vhd");
      OSDisk osDisk = OSDisk.create(osType, name, vhd, "ReadWrite", "FromImage", sourceImage);
      
      return StorageProfile.create(imageReference, osDisk, ImmutableList.<DataDisk>of());
   }
}
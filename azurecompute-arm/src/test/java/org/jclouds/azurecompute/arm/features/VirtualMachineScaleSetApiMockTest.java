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
package org.jclouds.azurecompute.arm.features;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.squareup.okhttp.mockwebserver.MockResponse;
import org.jclouds.azurecompute.arm.domain.DataDisk;
import org.jclouds.azurecompute.arm.domain.Extension;
import org.jclouds.azurecompute.arm.domain.ExtensionProfile;
import org.jclouds.azurecompute.arm.domain.ExtensionProfileSettings;
import org.jclouds.azurecompute.arm.domain.ExtensionProperties;
import org.jclouds.azurecompute.arm.domain.IdReference;
import org.jclouds.azurecompute.arm.domain.ImageReference;
import org.jclouds.azurecompute.arm.domain.IpConfiguration;
import org.jclouds.azurecompute.arm.domain.IpConfigurationProperties;
import org.jclouds.azurecompute.arm.domain.ManagedDiskParameters;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceCard;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceCardProperties;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceConfiguration;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceConfigurationProperties;
import org.jclouds.azurecompute.arm.domain.NetworkProfile;
import org.jclouds.azurecompute.arm.domain.NetworkProfile.NetworkInterface;
import org.jclouds.azurecompute.arm.domain.OSDisk;
import org.jclouds.azurecompute.arm.domain.OSProfile.LinuxConfiguration;
import org.jclouds.azurecompute.arm.domain.OSProfile.WindowsConfiguration.AdditionalUnattendContent;
import org.jclouds.azurecompute.arm.domain.OSProfile.WindowsConfiguration.WinRM.Protocol;
import org.jclouds.azurecompute.arm.domain.Secrets.SourceVault;
import org.jclouds.azurecompute.arm.domain.StorageProfile;
import org.jclouds.azurecompute.arm.domain.Subnet;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSet;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetDNSSettings;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetIpConfiguration;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetIpConfigurationProperties;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetNetworkProfile;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetNetworkSecurityGroup;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetOSProfile;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetPlan;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetProperties;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetPublicIPAddressConfiguration;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetPublicIPAddressProperties;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetSKU;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetUpgradePolicy;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetVirtualMachineProfile;
import org.jclouds.azurecompute.arm.internal.BaseAzureComputeApiMockTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.isEmpty;
import static org.jclouds.util.Predicates2.retry;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


@Test(groups = "unit", testName = "VirtualMachineApiMockTest", singleThreaded = true)
public class VirtualMachineScaleSetApiMockTest extends BaseAzureComputeApiMockTest {

    private final String subscriptionid = "SUBSCRIPTIONID";
    private final String resourcegroup = "myresourcegroup";
    private final String virtualNetwork = "myvirtualnetwork";
    private final String subnetName = "mysubnet";
    private final String apiVersion = "2016-04-30-preview";

//   public void testGet() throws Exception {
//      server.enqueue(jsonResponse("/virtualmachinescaleset.json"));
//      final VirtualMachineScaleSetApi vmAPI = api.getVirtualMachineScaleSetApi("groupname");
//      assertEquals(vmAPI.get("windowsmachine"),
//            getVMSS(Plan.create("thinkboxsoftware", "deadline-slave-7-2", "deadline7-2")));
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine?api-version=2016-04-30-preview");
//   }
//
//   public void testGetEmpty() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(404));
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      assertNull(vmAPI.get("windowsmachine"));
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine?api-version=2016-04-30-preview");
//   }
//
//   public void testGetInstanceDetails() throws Exception {
//      server.enqueue(jsonResponse("/virtualmachineInstance.json"));
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      VirtualMachineInstance actual = vmAPI.getInstanceDetails("windowsmachine");
//      VirtualMachineInstance expected = getVMInstance();
//
//      assertEquals(actual.statuses().get(0).code(), expected.statuses().get(0).code());
//      assertEquals(actual.statuses().get(0).displayStatus(), expected.statuses().get(0).displayStatus());
//      assertEquals(actual.statuses().get(0).level(), expected.statuses().get(0).level());
//      // assertEquals(actual.statuses().get(0).time().toString(),
//      // expected.statuses().get(0).time().toString());
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine/instanceView?api-version=2016-04-30-preview");
//   }
//
//   public void testGetInstanceDetailsEmpty() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(404));
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      assertNull(vmAPI.getInstanceDetails("windowsmachine"));
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine/instanceView?api-version=2016-04-30-preview");
//   }
//
//   public void testList() throws Exception {
//      server.enqueue(jsonResponse("/virtualmachines.json"));
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      assertEquals(vmAPI.list(), getVMList());
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines?api-version=2016-04-30-preview");
//   }
//
//   public void testListEmpty() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(404));
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      assertTrue(isEmpty(vmAPI.list()));
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines?api-version=2016-04-30-preview");
//   }

//    public void testCreateWithPlan() throws Exception {
//        server.enqueue(jsonResponse("/createvirtualmachineresponse.json"));
//        VirtualMachineScaleSetPlan plan = getVirtualMachineScaleSetPlan();
//        final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi("groupname");
//        VirtualMachineScaleSet vmss = vmssAPI.createOrUpdate("vmss", "eastus", getSKU(), ImmutableMap.of("foo", "bar"), getVirtualMachineScaleSetProperties());
//        assertEquals(vmss, getVMSS(plan));
//        assertSent(
//                server,
//                "PUT",
//                "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//                        + "/virtualMachines/windowsmachine?validating=false&api-version=2016-04-30-preview",
//                "{\"location\":\"westus\",\"properties\":"
//                        + "{\"vmId\":\"27ee085b-d707-xxxx-yyyy-2370e2eb1cc1\",\"licenseType\":\"Windows_Server\","
//                        + "\"availabilitySet\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/availabilitySets/myAVSet\"},"
//                        + "\"hardwareProfile\":{\"vmSize\":\"Standard_D1\"},"
//                        + "\"storageProfile\":{\"imageReference\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/providers/Microsoft.Compute/locations/westus/publishers/MicrosoftWindowsServerEssentials/artifactype/vmimage/offers/OFFER/skus/OFFER/versions/latest\","
//                        + "\"publisher\":\"publisher\",\"offer\":\"OFFER\",\"sku\":\"sku\",\"version\":\"ver\"},"
//                        + "\"osDisk\":{\"osType\":\"Windows\",\"name\":\"windowsmachine\","
//                        + "\"caching\":\"ReadWrite\",\"createOption\":\"FromImage\","
//                        + "\"managedDisk\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/disks/osDisk\",\"storageAccountType\":\"Standard_LRS\"}},"
//                        + "\"dataDisks\":[{\"name\":\"mydatadisk1\",\"diskSizeGB\":\"1\",\"lun\":0,\"createOption\":\"Empty\",\"caching\":\"ReadWrite\",\"managedDisk\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/disks/osDisk\",\"storageAccountType\":\"Standard_LRS\"}}]},"
//                        + "\"osProfile\":{\"computerName\":\"windowsmachine\",\"adminUsername\":\"azureuser\",\"adminPassword\":\"password\",\"customData\":\"\",\"windowsConfiguration\":{\"provisionVMAgent\":false,"
//                        + "\"winRM\":{\"listeners\":[{\"protocol\":\"https\",\"certificateUrl\":\"url-to-certificate\"}]},\"additionalUnattendContent\":[{\"pass\":\"oobesystem\",\"component\":\"Microsoft-Windows-Shell-Setup\",\"settingName\":\"FirstLogonCommands\",\"content\":\"<XML unattend content>\"}],"
//                        + "\"enableAutomaticUpdates\":true},"
//                        + "\"secrets\":[{\"sourceVault\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup1/providers/Microsoft.KeyVault/vaults/myvault1\"},\"vaultCertificates\":[{\"certificateUrl\":\"https://myvault1.vault.azure.net/secrets/SECRETNAME/SECRETVERSION\",\"certificateStore\":\"CERTIFICATESTORENAME\"}]}]},"
//                        + "\"networkProfile\":{\"networkInterfaces\":[{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Network/networkInterfaces/windowsmachine167\"}]},"
//                        + "\"diagnosticsProfile\":{\"bootDiagnostics\":{\"enabled\":true,\"storageUri\":\"https://groupname2760.blob.core.windows.net/\"}},\"provisioningState\":\"CREATING\"},"
//                        + "\"tags\":{\"foo\":\"bar\"},"
//                        + "\"plan\":{\"name\":\"deadline-slave-7-2\",\"publisher\":\"thinkboxsoftware\",\"product\":\"deadline7-2\"}}");
//    }


    // See https://docs.microsoft.com/en-us/rest/api/compute/virtualmachines/virtualmachines-create-or-update
    // for where part of the example json response comes from. Unfortunately examples in the microsoft docs
    // are not valid json (e.g. missing commas, illegal quotes). Therefore this example merges the original
    // real-world example (presumably taken from the jclouds wire log), and snippets from the microsoft docs.
//    public void testCreate() throws Exception {
//        server.enqueue(jsonResponse("/createvirtualmachineresponse.json"));
//
//        final VirtualMachineScaleSetApi vmAPI = api.getVirtualMachineScaleSetApi("groupname");
//        VirtualMachineScaleSet vmss = vmAPI.createOrUpdate("vmss", "eastus", getSKU(), ImmutableMap.of("foo", "bar"), getVirtualMachineScaleSetProperties());
//        assertEquals(vmss, getVMSS(null));
//        assertSent(
//                server,
//                "PUT",
//                "/subscriptions/SUBSCRIPTIONID/resourceGroups/{resourceGroup}/providers/Microsoft.Compute/VirtualMachineScaleSets/virtualmachinescaleset?validating=false&api-version=2017-03-01",
//                "{\"location\":\"westus\",\"properties\":"
//                        + "{\"vmId\":\"27ee085b-d707-xxxx-yyyy-2370e2eb1cc1\",\"licenseType\":\"Windows_Server\","
//                        + "\"availabilitySet\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/availabilitySets/myAVSet\"},"
//                        + "\"hardwareProfile\":{\"vmSize\":\"Standard_D1\"},"
//                        + "\"storageProfile\":{\"imageReference\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/providers/Microsoft.Compute/locations/westus/publishers/MicrosoftWindowsServerEssentials/artifactype/vmimage/offers/OFFER/skus/OFFER/versions/latest\","
//                        + "\"publisher\":\"publisher\",\"offer\":\"OFFER\",\"sku\":\"sku\",\"version\":\"ver\"},"
//                        + "\"osDisk\":{\"osType\":\"Windows\",\"name\":\"windowsmachine\","
//                        + "\"caching\":\"ReadWrite\",\"createOption\":\"FromImage\","
//                        + "\"managedDisk\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/disks/osDisk\",\"storageAccountType\":\"Standard_LRS\"}},"
//                        + "\"dataDisks\":[{\"name\":\"mydatadisk1\",\"diskSizeGB\":\"1\",\"lun\":0,\"createOption\":\"Empty\",\"caching\":\"ReadWrite\",\"managedDisk\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/disks/osDisk\",\"storageAccountType\":\"Standard_LRS\"}}]},"
//                        + "\"osProfile\":{\"computerName\":\"windowsmachine\",\"adminUsername\":\"azureuser\",\"adminPassword\":\"password\",\"customData\":\"\",\"windowsConfiguration\":{\"provisionVMAgent\":false,"
//                        + "\"winRM\":{\"listeners\":[{\"protocol\":\"https\",\"certificateUrl\":\"url-to-certificate\"}]},\"additionalUnattendContent\":[{\"pass\":\"oobesystem\",\"component\":\"Microsoft-Windows-Shell-Setup\",\"settingName\":\"FirstLogonCommands\",\"content\":\"<XML unattend content>\"}],"
//                        + "\"enableAutomaticUpdates\":true},"
//                        + "\"secrets\":[{\"sourceVault\":{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup1/providers/Microsoft.KeyVault/vaults/myvault1\"},\"vaultCertificates\":[{\"certificateUrl\":\"https://myvault1.vault.azure.net/secrets/SECRETNAME/SECRETVERSION\",\"certificateStore\":\"CERTIFICATESTORENAME\"}]}]},"
//                        + "\"networkProfile\":{\"networkInterfaces\":[{\"id\":\"/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Network/networkInterfaces/windowsmachine167\"}]},"
//                        + "\"diagnosticsProfile\":{\"bootDiagnostics\":{\"enabled\":true,\"storageUri\":\"https://groupname2760.blob.core.windows.net/\"}},\"provisioningState\":\"CREATING\"},"
//                        + "\"tags\":{\"foo\":\"bar\"}}");
//    }

//   public void testDeleteReturns404() throws Exception {
//      server.enqueue(response404());
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//
//      URI uri = vmAPI.delete("windowsmachine");
//
//      assertEquals(server.getRequestCount(), 1);
//      assertNull(uri);
//
//      assertSent(server, "DELETE", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine?api-version=2016-04-30-preview");
//   }
//
//   public void testDelete() throws Exception {
//      server.enqueue(response202WithHeader());
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//
//      URI uri = vmAPI.delete("windowsmachine");
//
//      assertEquals(server.getRequestCount(), 1);
//      assertNotNull(uri);
//
//      assertSent(server, "DELETE", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine?api-version=2016-04-30-preview");
//   }
//
//   public void testStart() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(204));
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//
//      vmAPI.start("windowsmachine");
//
//      assertSent(server, "POST", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine/start?api-version=2016-04-30-preview");
//   }
//
//   public void testRestart() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(204));
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//
//      vmAPI.restart("windowsmachine");
//
//      assertSent(server, "POST", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine/restart?api-version=2016-04-30-preview");
//   }
//
//   public void testStop() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(204));
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//
//      vmAPI.stop("windowsmachine");
//
//      assertSent(server, "POST", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/windowsmachine/powerOff?api-version=2016-04-30-preview");
//   }
//
//   public void testGeneralize() throws Exception {
//      server.enqueue(new MockResponse().setResponseCode(200));
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      vmAPI.generalize("vm"); // IllegalStateException if failed
//      assertSent(server, "POST", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/vm/generalize?api-version=2016-04-30-preview");
//   }
//
//   public void testCapture() throws Exception {
//      server.enqueue(response202WithHeader());
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      URI uri = vmAPI.capture("vm", "prefix", "container");
//      assertNotNull(uri);
//      assertSent(server, "POST", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/vm/capture?api-version=2016-04-30-preview",
//            "{\"vhdPrefix\":\"prefix\",\"destinationContainerName\":\"container\",\"overwriteVhds\":\"true\"}");
//   }
//
//   public void testCapture404() throws Exception {
//      server.enqueue(response404());
//
//      final VirtualMachineApi vmAPI = api.getVirtualMachineApi("groupname");
//      URI uri = vmAPI.capture("vm", "prefix", "container");
//      assertNull(uri);
//      assertSent(server, "POST", "/subscriptions/SUBSCRIPTIONID/resourceGroups/groupname/providers/Microsoft.Compute"
//            + "/virtualMachines/vm/capture?api-version=2016-04-30-preview",
//            "{\"vhdPrefix\":\"prefix\",\"destinationContainerName\":\"container\",\"overwriteVhds\":\"true\"}");
//   }
//
//   private VirtualMachineProperties getVMWithBlobDisksProperties() {
//      String licenseType = "Windows_Server";
//      IdReference availabilitySet = IdReference.create("/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/availabilitySets/myAVSet");
//      HardwareProfile hwProf = HardwareProfile.create("Standard_D1");
//      ImageReference imgRef = ImageReference.builder().publisher("publisher").offer("OFFER").sku("sku").version("ver")
//            .customImageId("/subscriptions/SUBSCRIPTIONID/providers/Microsoft.Compute/locations/westus/publishers/MicrosoftWindowsServerEssentials/artifactype/vmimage/offers/OFFER/skus/OFFER/versions/latest")
//            .build();
//      VHD vhd = VHD.create("https://groupname2760.blob.core.windows.net/vhds/windowsmachine201624102936.vhd");
//      List<DataDisk> dataDisks = ImmutableList.of(
//            DataDisk.create("mydatadisk1", "1", 0, VHD.create("http://mystorage1.blob.core.windows.net/vhds/mydatadisk1.vhd"),
//                  null, "Empty", null, null, null));
//      ManagedDiskParameters managedDiskParameters = ManagedDiskParameters.create("/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/disks/osDisk",
//            "Standard_LRS");
//      OSDisk osDisk = OSDisk.create("Windows", "windowsmachine", vhd, "ReadWrite", "FromImage", null, managedDiskParameters, null);
//      StorageProfile storageProfile = StorageProfile.create(imgRef, osDisk, dataDisks);
//      LinuxConfiguration linuxConfig = null;
//      OSProfile.WindowsConfiguration.WinRM winrm = OSProfile.WindowsConfiguration.WinRM.create(
//            ImmutableList.of(
//                  OSProfile.WindowsConfiguration.WinRM.ProtocolListener.create(Protocol.HTTPS, "url-to-certificate")));
//      List<AdditionalUnattendContent> additionalUnattendContent = ImmutableList.of(
//            AdditionalUnattendContent.create("oobesystem", "Microsoft-Windows-Shell-Setup", "FirstLogonCommands", "<XML unattend content>"));
//      OSProfile.WindowsConfiguration windowsConfig = OSProfile.WindowsConfiguration.create(false, winrm, additionalUnattendContent, true);
//      List<Secrets> secrets =  ImmutableList.of(
//            Secrets.create(SourceVault.create("/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup1/providers/Microsoft.KeyVault/vaults/myvault1"),
//                  ImmutableList.of(VaultCertificate.create("https://myvault1.vault.azure.net/secrets/SECRETNAME/SECRETVERSION", "CERTIFICATESTORENAME"))));
//      OSProfile osProfile = OSProfile.create("windowsmachine", "azureuser", "password", "", linuxConfig, windowsConfig, secrets);
//      NetworkInterface networkInterface = NetworkInterface.create("/subscriptions/SUBSCRIPTIONID"
//            + "/resourceGroups/groupname/providers/Microsoft.Network/networkInterfaces/" + "windowsmachine167", null);
//      List<NetworkInterface> networkInterfaces = new ArrayList<NetworkInterface>();
//      networkInterfaces.add(networkInterface);
//      NetworkProfile networkProfile = NetworkProfile.create(networkInterfaces);
//      DiagnosticsProfile.BootDiagnostics bootDiagnostics = DiagnosticsProfile.BootDiagnostics.create(true,
//            "https://groupname2760.blob.core.windows.net/");
//      DiagnosticsProfile diagnosticsProfile = DiagnosticsProfile.create(bootDiagnostics);
//      VirtualMachineProperties properties = VirtualMachineProperties.create("27ee085b-d707-xxxx-yyyy-2370e2eb1cc1",
//            licenseType, availabilitySet, hwProf, storageProfile, osProfile, networkProfile, diagnosticsProfile,
//            VirtualMachineProperties.ProvisioningState.CREATING);
//      return properties;
//   }
//
//   private VirtualMachineProperties getVMWithManagedDisksProperties() {
//      String licenseType = "Windows_Server";
//      IdReference availabilitySet = IdReference.create("/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/availabilitySets/myAVSet");
//      HardwareProfile hwProf = HardwareProfile.create("Standard_D1");
//      ImageReference imgRef = ImageReference.builder().publisher("publisher").offer("OFFER").sku("sku").version("ver")
//            .customImageId("/subscriptions/SUBSCRIPTIONID/providers/Microsoft.Compute/locations/westus/publishers/MicrosoftWindowsServerEssentials/artifactype/vmimage/offers/OFFER/skus/OFFER/versions/latest")
//            .build();
//      ManagedDiskParameters managedDiskParameters = ManagedDiskParameters.create("/subscriptions/SUBSCRIPTIONID/resourceGroups/myResourceGroup/providers/Microsoft.Compute/disks/osDisk",
//            "Standard_LRS");
//      List<DataDisk> dataDisks = ImmutableList.of(
//              DataDisk.builder().name("mydatadisk1").diskSizeGB("1").lun(0).managedDiskParameters(managedDiskParameters).createOption(DataDisk.DiskCreateOptionTypes.EMPTY).caching(DataDisk.CachingTypes.READ_WRITE).build());
//      OSDisk osDisk = OSDisk.builder().osType("Windows").name("windowsmachine").caching("ReadWrite").createOption("FromImage").managedDiskParameters(managedDiskParameters).build();
//      StorageProfile storageProfile = StorageProfile.create(imgRef, osDisk, dataDisks);
//      LinuxConfiguration linuxConfig = null;
//      OSProfile.WindowsConfiguration.WinRM winrm = OSProfile.WindowsConfiguration.WinRM.create(
//            ImmutableList.of(
//                  OSProfile.WindowsConfiguration.WinRM.ProtocolListener.create(Protocol.HTTPS, "url-to-certificate")));
//      List<AdditionalUnattendContent> additionalUnattendContent = ImmutableList.of(
//            AdditionalUnattendContent.create("oobesystem", "Microsoft-Windows-Shell-Setup", "FirstLogonCommands", "<XML unattend content>"));
//      OSProfile.WindowsConfiguration windowsConfig = OSProfile.WindowsConfiguration.create(false, winrm, additionalUnattendContent, true);
//      List<Secrets> secrets =  ImmutableList.of(
//            Secrets.create(SourceVault.create("/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup1/providers/Microsoft.KeyVault/vaults/myvault1"),
//                  ImmutableList.of(VaultCertificate.create("https://myvault1.vault.azure.net/secrets/SECRETNAME/SECRETVERSION", "CERTIFICATESTORENAME"))));
//      OSProfile osProfile = OSProfile.create("windowsmachine", "azureuser", "password", "", linuxConfig, windowsConfig, secrets);
//      NetworkInterface networkInterface = NetworkInterface.create("/subscriptions/SUBSCRIPTIONID"
//            + "/resourceGroups/groupname/providers/Microsoft.Network/networkInterfaces/" + "windowsmachine167", null);
//      List<NetworkInterface> networkInterfaces = new ArrayList<NetworkInterface>();
//      networkInterfaces.add(networkInterface);
//      NetworkProfile networkProfile = NetworkProfile.create(networkInterfaces);
//      DiagnosticsProfile.BootDiagnostics bootDiagnostics = DiagnosticsProfile.BootDiagnostics.create(true,
//            "https://groupname2760.blob.core.windows.net/");
//      DiagnosticsProfile diagnosticsProfile = DiagnosticsProfile.create(bootDiagnostics);
//      VirtualMachineProperties properties = VirtualMachineProperties.create("27ee085b-d707-xxxx-yyyy-2370e2eb1cc1",
//            licenseType, availabilitySet, hwProf, storageProfile, osProfile, networkProfile, diagnosticsProfile,
//            VirtualMachineProperties.ProvisioningState.CREATING);
//      return properties;
//   }

    private VirtualMachineScaleSet getVMSS(VirtualMachineScaleSetPlan plan) {
        VirtualMachineScaleSet vmss = VirtualMachineScaleSet.create("/subscriptions/SUBSCRIPTIONID/" + ""
                        + "resourceGroups/groupname/providers/Microsoft.Compute/VirtualMachineScaleSets/virtualmachinescaleset", "windowsmachine",
                "eastus", getSKU(), null, plan, getVirtualMachineScaleSetProperties());
        return vmss;
    }


//   private VirtualMachineInstance getVMInstance() {
//      List<Status> statuses = new ArrayList<Status>();
//      String testDate = "Wed May 04 01:38:52 PDT 2016";
//      DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
//      Date date = null;
//      try {
//         date = formatter.parse(testDate);
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
//      Status vmStatus = Status.create(
//            "ProvisioningState/succeeded", "Info", "Provisioning succeeded", null, date);
//      statuses.add(vmStatus);
//      Status vmStatus1 = Status.create(
//            "PowerState/running", "Info", "VM running", null, null);
//      statuses.add(vmStatus1);
//
//      VirtualMachineInstance machineInstance = VirtualMachineInstance
//            .create(null, null, ImmutableList.copyOf(statuses));
//      return machineInstance;
//   }
//
//   private List<VirtualMachine> getVMList() {
//      List<VirtualMachine> list = new ArrayList<VirtualMachine>();
//      VirtualMachineProperties propertiesWithManagedDisks = getVMWithManagedDisksProperties();
//      VirtualMachine machineWithManagedDisks = VirtualMachine.create("/subscriptions/SUBSCRIPTIONID/" + ""
//            + "resourceGroups/groupname/providers/Microsoft.Compute/virtualMachines/windowsmachine", "windowsmachine",
//            "Microsoft.Compute/virtualMachines", "westus", null, propertiesWithManagedDisks, null);
//      list.add(machineWithManagedDisks);
//      VirtualMachineProperties propertiesWithBlobDisks = getVMWithBlobDisksProperties();
//      VirtualMachine machineWithBlobDisks = VirtualMachine.create("/subscriptions/SUBSCRIPTIONID/" + ""
//                      + "resourceGroups/groupname/providers/Microsoft.Compute/virtualMachines/windowsmachine", "windowsmachine",
//              "Microsoft.Compute/virtualMachines", "westus", null, propertiesWithBlobDisks, null);
//      list.add(machineWithBlobDisks);
//      return list;
//   }

    public VirtualMachineScaleSetSKU getSKU() {
        return VirtualMachineScaleSetSKU.create("Standard_A1", "Standard", 10);
    }

    public VirtualMachineScaleSetProperties getVirtualMachineScaleSetProperties() {

        return VirtualMachineScaleSetProperties.create(null, null, getUpgradePolicy(), getVirtualMachineProfile());
    }

    private VirtualMachineScaleSetUpgradePolicy getUpgradePolicy() {
        return VirtualMachineScaleSetUpgradePolicy.create("Manual");
    }

    private List<DataDisk> getDataDisks() {
        List<DataDisk> datadisks = new ArrayList<DataDisk>();

        datadisks.add(DataDisk.create(null, "10", 1, null,
                null, "FromImage",
                "None", getManagedDiskParameters(),
                null));

        return datadisks;
    }

    private StorageProfile getStorageProfile() {
        return StorageProfile.create(getWindowsImageReference(), getWindowsOSDisk(), getDataDisks());
    }

    private StorageProfile getWindowsStorageProfile_Default() {
        return StorageProfile.create(getWindowsImageReference(), getWindowsOSDisk(), null);
    }

    private StorageProfile getLinuxStorageProfile_Default() {
        return StorageProfile.create(getLinuxImageReference(), getLinuxOSDisk(), null);
    }

    private ManagedDiskParameters getManagedDiskParameters() {
        return ManagedDiskParameters.create(null, "Standard_LRS");
    }

    private OSDisk getWindowsOSDisk() {
        return OSDisk.create("Windows", null, null, null, "FromImage",
                null, getManagedDiskParameters(), null);
    }

    private OSDisk getLinuxOSDisk() {
        return OSDisk.create("Linux", null, null, null, "FromImage",
                null, getManagedDiskParameters(), null);
    }

    private ImageReference getWindowsImageReference() {
        return ImageReference.create(null, "Microsoft.Windows", "Windows2016",
                "Enterprise", "latest");
    }

    private ImageReference getLinuxImageReference() {
        return ImageReference.create(null, "Canonical", "UbuntuServer",
                "16.04-LTS", "latest");
    }

    private VirtualMachineScaleSetOSProfile getOSProfile() {
        VirtualMachineScaleSetOSProfile.LinuxConfiguration linuxConfiguration =
                VirtualMachineScaleSetOSProfile.LinuxConfiguration.create("False", null);
        VirtualMachineScaleSetOSProfile.WindowsConfiguration windowsConfiguration = null;

        return VirtualMachineScaleSetOSProfile.create(getVMSSName(), "jclouds", "jClouds1!",
                linuxConfiguration, windowsConfiguration, null);
    }


    private VirtualMachineScaleSetNetworkProfile getNetworkProfile() {
        List<NetworkProfile.NetworkInterface> networkInterfacesList = new ArrayList<NetworkProfile.NetworkInterface>();
        Subnet subnet = createDefaultSubnet();

        NetworkInterfaceCard nic = createNetworkInterfaceCard("jclouds", "jc-nic-" + "mock", "eastus",
                "ipConfig-" + "mock", IdReference.create(subnet.id()));
        assertNotNull(nic);
        networkInterfacesList.add(NetworkProfile.NetworkInterface.create(nic.id(), NetworkProfile.NetworkInterface.NetworkInterfaceProperties.create(true)));

        List<NetworkInterfaceConfiguration> networkInterfaceConfigurations = new ArrayList<NetworkInterfaceConfiguration>();
        List<VirtualMachineScaleSetIpConfiguration> virtualMachineScaleSetIpConfigurations = new ArrayList<VirtualMachineScaleSetIpConfiguration>();


        VirtualMachineScaleSetPublicIPAddressConfiguration publicIPAddressConfiguration =
                VirtualMachineScaleSetPublicIPAddressConfiguration.create("pub1", VirtualMachineScaleSetPublicIPAddressProperties.create(15));

        VirtualMachineScaleSetIpConfigurationProperties virtualMachineScaleSetIpConfigurationProperties =
                VirtualMachineScaleSetIpConfigurationProperties.create(publicIPAddressConfiguration,
                        subnet, "IPv4", null,
                        null, null);

        VirtualMachineScaleSetIpConfiguration virtualMachineScaleSetIpConfiguration =
                VirtualMachineScaleSetIpConfiguration.create("ipconfig1", virtualMachineScaleSetIpConfigurationProperties);

        virtualMachineScaleSetIpConfigurations.add(virtualMachineScaleSetIpConfiguration);

        VirtualMachineScaleSetNetworkSecurityGroup networkSecurityGroup = null;

        ArrayList<String> dnsList = new ArrayList<String>();
        dnsList.add("8.8.8.8");
        VirtualMachineScaleSetDNSSettings dnsSettings = VirtualMachineScaleSetDNSSettings.create(dnsList);

        NetworkInterfaceConfigurationProperties networkInterfaceConfigurationProperties =
                NetworkInterfaceConfigurationProperties.create(true, false, networkSecurityGroup, dnsSettings, virtualMachineScaleSetIpConfigurations);
        NetworkInterfaceConfiguration networkInterfaceConfiguration = NetworkInterfaceConfiguration.create("nicconfig1", networkInterfaceConfigurationProperties);
        networkInterfaceConfigurations.add(networkInterfaceConfiguration);

        return VirtualMachineScaleSetNetworkProfile.create(networkInterfaceConfigurations);
    }


    private NetworkInterfaceCard createNetworkInterfaceCard(final String resourceGroupName, String networkInterfaceCardName, String locationName, String ipConfigurationName, IdReference subnetId) {
        //Create properties object
        final NetworkInterfaceCardProperties networkInterfaceCardProperties = NetworkInterfaceCardProperties
                .builder()
                .ipConfigurations(
                        Arrays.asList(IpConfiguration.create(ipConfigurationName, null, null, null, IpConfigurationProperties
                                .create(null, null, "Dynamic", subnetId, null, null, null)))).build();

        final Map<String, String> tags = ImmutableMap.of("jclouds", "livetest");
        return api.getNetworkInterfaceCardApi(resourceGroupName).createOrUpdate(networkInterfaceCardName, locationName, networkInterfaceCardProperties, tags);
    }


    private ExtensionProfile getExtensionProfile() {
        List<Extension> extensions = new ArrayList<Extension>();

        List<String> uris = new ArrayList<String>();
        uris.add("https://mystorage1.blob.core.windows.net/winvmextekfacnt/SampleCmd_1.cmd");
        ExtensionProfileSettings extensionProfileSettings = ExtensionProfileSettings.create(uris, "SampleCmd_1.cmd");

        Map<String, String> protectedSettings = new HashMap<String, String>();
        protectedSettings.put("StorageAccountKey", "jclouds-accountkey");

        ExtensionProperties extensionProperties = ExtensionProperties.create("Microsoft.compute", "CustomScriptExtension",
                "1.1", false, extensionProfileSettings,
                protectedSettings);

        Extension extension = Extension.create("extensionName", extensionProperties);
        extensions.add(extension);

        return ExtensionProfile.create(extensions);
    }

    private VirtualMachineScaleSetVirtualMachineProfile getVirtualMachineProfile() {
        return VirtualMachineScaleSetVirtualMachineProfile.create(getLinuxStorageProfile_Default(), getOSProfile(), getNetworkProfile(), getExtensionProfile());
    }

    private VirtualMachineScaleSetPlan getVirtualMachineScaleSetPlan() {
        return VirtualMachineScaleSetPlan.create("thinkboxsoftware", "deadline-slave-7-2", "deadline7-2");
    }

    private String getVMSSName() {
        return String.format("%3.24s", System.getProperty("user.name") + "mock" + this.getClass().getSimpleName()).toLowerCase().substring(0, 15);
    }

    private String getSubnet() {
        return String.format("s-%s-%s", this.getClass().getSimpleName().toLowerCase(), System.getProperty("user.name"));
    }

    private Subnet createDefaultSubnet()  {
        server.enqueue(jsonResponse("/getonesubnet.json").setResponseCode(200));
        final SubnetApi subnetApi = api.getSubnetApi("azurearmtesting", "myvirtualnetwork");
        Subnet subnet = subnetApi.get("mysubnet");
        String path = String.format("/subscriptions/%s/resourcegroups/%s/providers/Microsoft.Network/virtualNetworks/%s/subnets/%s?%s", subscriptionid, resourcegroup, virtualNetwork, subnetName, apiVersion);
        try {
            assertSent(server, "GET", path);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return subnet;
    }


}

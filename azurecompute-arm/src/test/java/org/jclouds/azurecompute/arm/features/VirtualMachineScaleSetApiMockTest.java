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

//import com.google.common.base.Predicate;
//import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
//import com.squareup.okhttp.mockwebserver.MockResponse;
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
//import org.jclouds.azurecompute.arm.domain.NetworkProfile.NetworkInterface;
import org.jclouds.azurecompute.arm.domain.OSDisk;
//import org.jclouds.azurecompute.arm.domain.OSProfile.LinuxConfiguration;
//import org.jclouds.azurecompute.arm.domain.OSProfile.WindowsConfiguration.AdditionalUnattendContent;
//import org.jclouds.azurecompute.arm.domain.OSProfile.WindowsConfiguration.WinRM.Protocol;
//import org.jclouds.azurecompute.arm.domain.Secrets.SourceVault;
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

//import java.net.URI;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.google.common.collect.Iterables.isEmpty;
//import static org.jclouds.util.Predicates2.retry;
//import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


@Test(groups = "unit", testName = "VirtualMachineApiMockTest", singleThreaded = true)
public class VirtualMachineScaleSetApiMockTest extends BaseAzureComputeApiMockTest {

    private final String subscriptionid = "SUBSCRIPTIONID";
    private final String resourcegroup = "myresourcegroup";
    private final String virtualNetwork = "myvirtualnetwork";
    private final String subnetName = "mysubnet";
    private final String apiVersion = "2016-04-30-preview";


    private VirtualMachineScaleSet getVMSS(VirtualMachineScaleSetPlan plan) {
        VirtualMachineScaleSet vmss = VirtualMachineScaleSet.create("/subscriptions/SUBSCRIPTIONID/" + ""
                        + "resourceGroups/groupname/providers/Microsoft.Compute/VirtualMachineScaleSets/virtualmachinescaleset", "windowsmachine",
                "eastus", getSKU(), null, plan, getVirtualMachineScaleSetProperties());
        return vmss;
    }



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

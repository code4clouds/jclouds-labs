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

import com.google.common.collect.ImmutableMap;
import com.sun.media.jfxmedia.logging.Logger;
import org.jclouds.azurecompute.arm.domain.DataDisk;
import org.jclouds.azurecompute.arm.domain.VirtualMachineInstance.PowerState;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSet;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetSKU;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetUpgradePolicy;
import org.jclouds.azurecompute.arm.domain.StorageProfile;
import org.jclouds.azurecompute.arm.domain.ManagedDiskParameters;
import org.jclouds.azurecompute.arm.domain.OSDisk;
import org.jclouds.azurecompute.arm.domain.ImageReference;
import org.jclouds.azurecompute.arm.domain.OSProfile;
import org.jclouds.azurecompute.arm.domain.NetworkProfile;
import org.jclouds.azurecompute.arm.domain.ExtensionProfile;
import org.jclouds.azurecompute.arm.domain.ExtensionProfileSettings;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetVirtualMachineProfile;
import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSetProperties;
import org.jclouds.azurecompute.arm.domain.Subnet;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceCard;
import org.jclouds.azurecompute.arm.domain.NetworkInterfaceCardProperties;
import org.jclouds.azurecompute.arm.domain.IpConfiguration;
import org.jclouds.azurecompute.arm.domain.IdReference;
import org.jclouds.azurecompute.arm.domain.IpConfigurationProperties;
import org.jclouds.azurecompute.arm.domain.Extension;
import org.jclouds.azurecompute.arm.domain.ExtensionProperties;
import org.jclouds.azurecompute.arm.internal.BaseAzureComputeApiLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.util.Predicates2.retry;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

@Test(groups = "live", testName = "VirtualMachineApiLiveTest")
public class VirtualMachineScaleSetApiLiveTest extends BaseAzureComputeApiLiveTest {

    private String subscriptionid;
    private String vmssName;
    private VirtualMachineScaleSetSKU SKU;
    private String nicName;
    private String virtualNetworkName;
    private String subnetId;

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        subscriptionid = getSubscriptionId();

        createTestResourceGroup();  //BASE: Creates a random resource group using the properties location

        virtualNetworkName = String.format("vn-%s-%s", this.getClass().getSimpleName().toLowerCase(), System.getProperty("user.name"));

        // Subnets belong to a virtual network so that needs to be created first
        assertNotNull(createDefaultVirtualNetwork(resourceGroupName, virtualNetworkName, "10.2.0.0/16", LOCATION));

        //Subnet needs to be up & running before NIC can be created
        String subnetName = String.format("s-%s-%s", this.getClass().getSimpleName().toLowerCase(), System.getProperty("user.name"));
        Subnet subnet = createDefaultSubnet(resourceGroupName, subnetName, virtualNetworkName, "10.2.0.0/23");
        assertNotNull(subnet);
        assertNotNull(subnet.id());
        subnetId = subnet.id();



        vmssName = String.format("%3.24s", System.getProperty("user.name") + RAND + this.getClass().getSimpleName()).toLowerCase().substring(0, 15);
        Logger.logMsg(Logger.INFO, "vmssName: " + vmssName);
    }

    @Test
    public void testCreate() {
//        VirtualMachineScaleSet vmss = api().createOrUpdate(vmssName, LOCATIONDESCRIPTION, getSKU(),
//                Collections.<String, String>emptyMap(), getProperties());
//        assertTrue(!vmss.name().isEmpty());
//        waitUntilReady(vmssName);
    }

    private VirtualMachineScaleSetApi api() {
        Logger.logMsg(Logger.INFO, "resourceGroupName: " + resourceGroupName);
        return api.getVirtualMachineScaleSetApi(resourceGroupName);
    }

    private boolean waitForState(String name, final PowerState state) {
        throw new NotImplementedException();
//        return api().getInstanceDetails(name).powerState().equals(state);
    }

    private void waitUntilReady(String vmssName) {
    }


    /**
     * Create a standard SKU
     *
     * @return VirtualMachineScaleSetSKU
     */
    public VirtualMachineScaleSetSKU getSKU() {
        return VirtualMachineScaleSetSKU.create("Standard_A1", "Standard", 10);
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
                null, getManagedDiskParameters(),  null);
    }

    private OSDisk getLinuxOSDisk() {
        return OSDisk.create("Linux", null, null, null, "FromImage",
                null, getManagedDiskParameters(),  null);
    }

    private ImageReference getWindowsImageReference() {
        return ImageReference.create(null, "Microsoft.Windows", "Windows2016",
                "Enterprise", "latest");
    }

    private ImageReference getLinuxImageReference() {
        return ImageReference.create(null, "Canonical", "UbuntuServer",
                "16.04-LTS", "latest");
    }

    private OSProfile getOSProfile() {
        OSProfile.LinuxConfiguration linuxConfiguration =
                OSProfile.LinuxConfiguration.create("False", null);
        OSProfile.WindowsConfiguration windowsConfiguration =
                OSProfile.WindowsConfiguration.create(true, null, null, true);

        return OSProfile.create(vmssName, "jclouds", "jClouds1!", null,
                linuxConfiguration, windowsConfiguration, null);
    }

    private NetworkProfile getNetworkProfile() {
        List<NetworkProfile.NetworkInterface> networkInterfacesList = new ArrayList<NetworkProfile.NetworkInterface>();

        NetworkInterfaceCard nic = createNetworkInterfaceCard(resourceGroupName, "jc-nic-" + RAND, LOCATION, "ipConfig-" + RAND);
        assertNotNull(nic);
        networkInterfacesList.add(NetworkProfile.NetworkInterface.create(nic.id(), NetworkProfile.NetworkInterface.NetworkInterfaceProperties.create(true)));
        return NetworkProfile.create(networkInterfacesList);
    }

    private ExtensionProfile getExtensionProfile() {
        List<Extension> extensions = new ArrayList<Extension>();

        List<String> uris = new ArrayList<String>();
        uris.add("https://mystorage1.blob.core.windows.net/winvmextekfacnt/SampleCmd_1.cmd");
        ExtensionProfileSettings extensionProfileSettings = ExtensionProfileSettings.create(uris, "SampleCmd_1.cmd");

        Map<String, String> protectedSettings = new HashMap<String, String>();
        protectedSettings.put("StorageAccountKey", "jclouds-accountkey");

        ExtensionProperties extensionProperties = ExtensionProperties.create( "Microsoft.compute", "CustomScriptExtension",
                "1.1", false, extensionProfileSettings,
                protectedSettings);

        Extension extension = Extension.create("extensionName",  extensionProperties);
        extensions.add(extension);

        return ExtensionProfile.create(extensions);
    }


    private VirtualMachineScaleSetVirtualMachineProfile getVirtualMachineProfile() {
        return VirtualMachineScaleSetVirtualMachineProfile.create(getLinuxStorageProfile_Default(), getOSProfile(), getNetworkProfile(), getExtensionProfile());
    }

    public VirtualMachineScaleSetProperties getProperties() {

        return VirtualMachineScaleSetProperties.create(null, null, getUpgradePolicy(), getVirtualMachineProfile());
    }

    private NetworkInterfaceCard createNetworkInterfaceCard(final String resourceGroupName, String networkInterfaceCardName, String locationName, String ipConfigurationName) {
        //Create properties object
        final NetworkInterfaceCardProperties networkInterfaceCardProperties = NetworkInterfaceCardProperties
                .builder()
                .ipConfigurations(
                        Arrays.asList(IpConfiguration.create(ipConfigurationName, null, null, null, IpConfigurationProperties
                                .create(null, null, "Dynamic", IdReference.create(subnetId), null, null, null)))).build();

        final Map<String, String> tags = ImmutableMap.of("jclouds", "livetest");
        return api.getNetworkInterfaceCardApi(resourceGroupName).createOrUpdate(networkInterfaceCardName, locationName, networkInterfaceCardProperties, tags);
    }

    public String getSubscriptionid() {
        return subscriptionid;
    }
}

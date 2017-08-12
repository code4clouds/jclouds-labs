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
import org.jclouds.azurecompute.arm.domain.ExtensionProfile;
import org.jclouds.azurecompute.arm.internal.BaseAzureComputeApiLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.rmi.runtime.Log;

import javax.xml.crypto.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;

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
    private String virtualNetworkName;

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        subscriptionid = getSubscriptionId();

        createTestResourceGroup();  //BASE: Creates a random resource group using the properties location

        virtualNetworkName = String.format("vn-%s-%s", this.getClass().getSimpleName().toLowerCase(), System.getProperty("user.name"));

        // Subnets belong to a virtual network so that needs to be created first
        assertNotNull(createDefaultVirtualNetwork(resourceGroupName, virtualNetworkName, "10.2.0.0/16", LOCATION));

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
        return StorageProfile.create(getImageReference(), getOSDisk(), getDataDisks());
    }

    private StorageProfile getStorageProfile_Default() {
        return StorageProfile.create(getImageReference(), getOSDisk(), null);
    }

    private ManagedDiskParameters getManagedDiskParameters() {
        return ManagedDiskParameters.create(null, "Standard_LRS");
    }

    private OSDisk getOSDisk() {
        return OSDisk.create("Windows", "OSDisk", null, null, "FromImage",
                null, getManagedDiskParameters(), "Standard_LRS");
    }

    private ImageReference getImageReference() {
        return ImageReference.create(null, "Microsoft.Windows", "Windows2014",
                "Enterprise", "latest");
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
        //networkInterfacesList.add(NetworkProfile.NetworkInterface.create(null, NetworkProfile.NetworkInterface.NetworkInterfaceProperties.create(true)));
        return NetworkProfile.create(networkInterfacesList);
    }

    private ExtensionProfile getExtensionProfile() {
        List<String> uris = new ArrayList<String>();
        uris.add("https://mystorage1.blob.core.windows.net/winvmextekfacnt/SampleCmd_1.cmd");
        ExtensionProfileSettings extensionProfileSettings = ExtensionProfileSettings.create(uris, "SampleCmd_1.cmd");

        Map<String, String> protectedSettings = new HashMap<String, String>();
        protectedSettings.put("StorageAccountKey", "jclouds-accountkey");

        return ExtensionProfile.create("extensionName", "Microsoft.compute", "CustomScriptExtension",
                "1.1", false, extensionProfileSettings,
                protectedSettings);
    }

    private VirtualMachineScaleSetVirtualMachineProfile getVirtualMachineProfile() {
        return VirtualMachineScaleSetVirtualMachineProfile.create(getStorageProfile_Default(), getOSProfile(), getNetworkProfile(), getExtensionProfile());
    }

    public VirtualMachineScaleSetProperties getProperties() {

        return VirtualMachineScaleSetProperties.create(null, null, getUpgradePolicy(), getVirtualMachineProfile());
    }

    public String getSubscriptionid() {
        return subscriptionid;
    }
}

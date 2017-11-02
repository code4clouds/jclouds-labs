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

import org.jclouds.azurecompute.arm.internal.BaseAzureComputeApiMockTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


@Test(groups = "unit", testName = "VirtualMachineScaleSetAPIMockTest", singleThreaded = true)
public class VirtualMachineScaleSetApiMockTest extends BaseAzureComputeApiMockTest {

   private final String resourcegroup = "myresourcegroup";

   public void testVMSSGet() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").name(), "jclouds-vmssname");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
            + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSGet_BaseProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").name(), "jclouds-vmssname");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSGet_SKUProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").sku().name(), "Standard_A1");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSGet_Property() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().singlePlacementGroup().booleanValue(), true);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSGet_StorageProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .storageProfile()
         .osDisk()
         .managedDiskParameters()
         .storageAccountType().toString(), "Standard_LRS");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSGet_NetworkProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .networkProfile()
         .networkInterfaceConfigurations()
         .size(), 1);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSCreate_BaseProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").location(), "eastus");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSCreate_SKUProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").sku().name(), "Standard_A1");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSCreate_Property() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().singlePlacementGroup().booleanValue(), true);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSCreate_StorageProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .storageProfile()
         .osDisk()
         .managedDiskParameters()
         .storageAccountType().toString(), "Standard_LRS");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSCreate_PropertyUpgradePolicy() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().upgradePolicy().mode(), "Manual");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testVMSSCreate_NetworkProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .networkProfile()
         .networkInterfaceConfigurations()
         .size(), 1);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

}

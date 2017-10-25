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
   }

   public void testVMSSGet_BaseProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").name(), "jclouds-vmssname");
   }

   public void testVMSSGet_SKUProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").sku().name(), "Standard_A1");
   }

   public void testVMSSGet_Property() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().singlePlacementGroup().booleanValue(), true);
   }

   public void testVMSSGet_StorageProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .storageProfile()
         .osDisk()
         .managedDiskParameters()
         .storageAccountType().toString(), "Standard_LRS");
   }

   public void testVMSSGet_NetworkProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescaleset.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .networkProfile()
         .networkInterfaceConfigurations()
         .size(), 1);
   }

   public void testVMSSCreate_BaseProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").location(), "eastus");
   }

   public void testVMSSCreate_SKUProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").sku().name(), "Standard_A1");
   }

   public void testVMSSCreate_Property() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().singlePlacementGroup().booleanValue(), true);
   }

   public void testVMSSCreate_StorageProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .storageProfile()
         .osDisk()
         .managedDiskParameters()
         .storageAccountType().toString(), "Standard_LRS");
   }

   public void testVMSSCreate_NetworkProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/createvirtualmachinescalesetresponse.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .networkProfile()
         .networkInterfaceConfigurations()
         .size(), 1);
   }

}

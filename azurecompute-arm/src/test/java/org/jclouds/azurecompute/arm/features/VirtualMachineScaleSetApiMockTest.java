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

import org.jclouds.azurecompute.arm.domain.VirtualMachineScaleSet;
import org.jclouds.azurecompute.arm.internal.BaseAzureComputeApiMockTest;
import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


@Test(groups = "unit", testName = "VirtualMachineScaleSetAPIMockTest", singleThreaded = true)
public class VirtualMachineScaleSetApiMockTest extends BaseAzureComputeApiMockTest {

   private final String resourcegroup = "myresourcegroup";
   private final String vmssname = "jclouds-vmssname";

   public void testGet() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetget.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").name(), "jclouds-vmssname");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
            + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testGetWhen404() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetgetwhen404.json").setResponseCode(404));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      vmssAPI.get(vmssname+1);
      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname1?api-version=2017-03-30");
   }

   public void testGet_BaseProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetget.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").name(), "jclouds-vmssname");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testGet_SKUProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetget.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").sku().name(), "Standard_A1");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testGet_Property() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetget.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().singlePlacementGroup().booleanValue(), true);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testGet_StorageProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetget.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .storageProfile()
         .osDisk()
         .managedDiskParameters()
         .storageAccountType().toString(), "Standard_LRS");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testGet_NetworkProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetget.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .networkProfile()
         .networkInterfaceConfigurations()
         .size(), 1);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testCreateOrUpdate_BaseProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").location(), "eastus");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

//   public void testCreateOrUpdateWhen404() throws InterruptedException {
//      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(404));
//      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup+"1");
//      assertEquals(vmssAPI.get("jclouds-vmssname").location(), "eastus");
//
//      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
//              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
//   }

   public void testCreateOrUpdate_SKUProperty() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").sku().name(), "Standard_A1");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testCreateOrUpdate_Property() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().singlePlacementGroup().booleanValue(), true);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testCreateOrUpdate_StorageProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .storageProfile()
         .osDisk()
         .managedDiskParameters()
         .storageAccountType().toString(), "Standard_LRS");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testCreateOrUpdate_PropertyUpgradePolicy() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().upgradePolicy().mode(), "Manual");

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testCreateOrUpdate_NetworkProfile() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetresponsecreateorupdate.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertEquals(vmssAPI.get("jclouds-vmssname").properties().virtualMachineProfile()
         .networkProfile()
         .networkInterfaceConfigurations()
         .size(), 1);

      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testList() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetlist.json").setResponseCode(200));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets?api-version=2017-03-30");
      assertEquals(vmssAPI.list().size(),1);
   }

   public void testListWhen404() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetlistwhen404.json").setResponseCode(404));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup+"1");
      List<VirtualMachineScaleSet> vmssList = vmssAPI.list();
      assertSent(server, "GET", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup1/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets?api-version=2017-03-30");
      assertTrue(isEmpty(vmssList));
   }


   public void testDeleteWhen404() throws InterruptedException {
      server.enqueue(jsonResponse("/virtualmachinescalesetlist.json").setResponseCode(404));
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      vmssAPI.delete(vmssname);
      assertSent(server, "DELETE", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }

   public void testDelete() throws InterruptedException {
      server.enqueue(response202WithHeader());
      final VirtualMachineScaleSetApi vmssAPI = api.getVirtualMachineScaleSetApi(resourcegroup);
      vmssAPI.delete("jclouds-vmssname");
      assertSent(server, "DELETE", "/subscriptions/SUBSCRIPTIONID/resourceGroups/myresourcegroup/providers/Microsoft.Compute"
              + "/VirtualMachineScaleSets/jclouds-vmssname?api-version=2017-03-30");
   }


}

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
package org.apache.jclouds.profitbricks.rest.binder.nic;

import java.util.HashMap;
import java.util.Map;
import org.apache.jclouds.profitbricks.rest.binder.BinderTestBase;
import org.apache.jclouds.profitbricks.rest.domain.Nic;
import org.jclouds.http.HttpRequest;
import org.jclouds.json.Json;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

@Test(groups = "unit", testName = "CreateNicRequestBinderTest")
public class CreateNicRequestBinderTest extends BinderTestBase {
   
   @Test
   public void testUpdatePayload() {
            
      CreateNicRequestBinder binder = injector.getInstance(CreateNicRequestBinder.class);
      
      Nic.Request.CreatePayload payload = Nic.Request.creatingBuilder()
         .dataCenterId("datacenter-id")
         .serverId("server-id")
         .name("jclouds-nic")
         .lan(1)
         .build();

      String actual = binder.createPayload(payload);

      HttpRequest request = binder.createRequest(
              HttpRequest.builder().method("POST").endpoint("http://test.com").build(), 
              actual
      );
      
      assertEquals(request.getEndpoint().getPath(), "/cloudapi/v4/datacenters/datacenter-id/servers/server-id/nics");
      assertNotNull(actual, "Binder returned null payload");
      
      Json json = injector.getInstance(Json.class);
      
      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put("lan", 1);
      properties.put("name", "jclouds-nic");
      
      HashMap<String, Object> expectedPayload = new HashMap<String, Object>();
      expectedPayload.put("properties", properties);
      
      assertEquals(actual, json.toJson(expectedPayload));
   }

}

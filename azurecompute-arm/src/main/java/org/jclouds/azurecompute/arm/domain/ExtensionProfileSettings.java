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
package org.jclouds.azurecompute.arm.domain;

import com.google.auto.value.AutoValue;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class ExtensionProfileSettings {

   /**
    * The fileUris reference of the storage profile
    */
   @Nullable
   public abstract List<String> fileUris();

   /**
    * The image reference of the storage profile
    */
   public abstract String commandToExecute();

    @SerializedNames({"fileUris", "commandToExecute"})
    public static ExtensionProfileSettings create(final List<String> fileUris, final String commandToExecute) {
        return new AutoValue_ExtensionProfileSettings(fileUris, commandToExecute);
    }
}
//      ExtensionProfile.Builder builder = ExtensionProfile.builder()
//              .imageReference(imageReference)
//              .osDisk(osDisk)
//              .dataDisks(dataDisks != null ? ImmutableList.copyOf(dataDisks) : null);
//
//      return builder.build();
//   }

//   public abstract Builder toBuilder();
//
//   public static Builder builder() {
//      return new ExtensionProfile.Builder();
//   }
//
//   @AutoValue.Builder
//   public abstract static class Builder {
//      public abstract Builder imageReference(ImageReference imageReference);
//
//      public abstract Builder osDisk(OSDisk osDisk);
//
//      public abstract Builder dataDisks(List<DataDisk> dataDisks);
//
//      abstract List<DataDisk> dataDisks();
//
//      abstract ExtensionProfile autoBuild();
//
//      public ExtensionProfile build() {
//         dataDisks(dataDisks() != null ? ImmutableList.copyOf(dataDisks()) : null);
//         return autoBuild();
//      }
//   }
//
//}

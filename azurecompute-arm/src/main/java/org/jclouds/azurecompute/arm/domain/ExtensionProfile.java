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
import com.google.common.collect.ImmutableList;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class ExtensionProfile {

   /**
    * The autoUpgradeMinorVersion reference of the extension profile
    */
    public abstract String name();

   /**
    * The autoUpgradeMinorVersion reference of the extension profile
    */
    public abstract String publisher();

    /**
     * The autoUpgradeMinorVersion reference of the extension profile
     */
    public abstract String type();

    /**
    * The autoUpgradeMinorVersion reference of the extension profile
    */
    public abstract String typeHandlerVersion();

   /**
    * The autoUpgradeMinorVersion reference of the extension profile
    */
    public abstract Boolean autoUpgradeMinorVersion();

   /**
    * The ExtensionProfileSettings of the extension profile
    */
   public abstract ExtensionProfileSettings settings();

   /**
    * The list of the protectedSettings of the extension profile
    */
   @Nullable
   public abstract Map<String, String> protectedSettings();

    @SerializedNames({"name", "publisher", "type", "typeHandlerVersion", "autoUpgradeMinorVersion", "settings", "protectedStrings"})
    public static ExtensionProfile create(final String name, final String publisher, String type, final String typeHandlerVersion,
                                          final Boolean autoUpgradeMinorVersion, final ExtensionProfileSettings settings,
                                          final Map<String, String> protectedSettings) {
        return new AutoValue_ExtensionProfile(name, publisher, type, typeHandlerVersion, autoUpgradeMinorVersion, settings, protectedSettings);
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

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
import org.jclouds.azurecompute.arm.util.GetEnumValue;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

/**
 * A virtual machine properties for the virtual machine.
 */
@AutoValue
public abstract class VirtualMachineScaleSetVirtualMachineProfile {

    /**
     * The Storage Profile of the virtual machine .
     */
    public abstract StorageProfile storageProfile();

    /**
     * The OS Profile of the virtual machine .
     */
    public abstract VirtualMachineScaleSetOSProfile osProfile();

    /**
     * The network profile of the VM
     */
    public abstract VirtualMachineScaleSetNetworkProfile networkProfile();

    /**
     * The OS Extension of the virtual machine .
     */
    public abstract ExtensionProfile extensionProfile();



    @SerializedNames({"storageProfile", "osProfile", "networkProfile", "extensionProfile"})
    public static VirtualMachineScaleSetVirtualMachineProfile create(final StorageProfile storageProfile,
                                                                     final VirtualMachineScaleSetOSProfile osProfile,
                                                                     final VirtualMachineScaleSetNetworkProfile networkProfile,
                                                                     final ExtensionProfile extensionProfile) {
        return builder()
                .storageProfile(storageProfile)
                .osProfile(osProfile)
                .networkProfile(networkProfile)
                .extensionProfile(extensionProfile)
                .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_VirtualMachineScaleSetVirtualMachineProfile.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder storageProfile(StorageProfile storageProfile);

        public abstract Builder osProfile(VirtualMachineScaleSetOSProfile osProfile);

        public abstract Builder networkProfile(VirtualMachineScaleSetNetworkProfile networkProfile);

        public abstract Builder extensionProfile(ExtensionProfile extensionProfile);

        public abstract VirtualMachineScaleSetVirtualMachineProfile build();
    }
}

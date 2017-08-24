//package org.jclouds.azurecompute.arm.domain;
//
//public class VirtualMachineScaleSetVirtualMachineProfile {
//}

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

/**
 * SKU
 */
@AutoValue
public abstract class VirtualMachineScaleSetProperties {
    /**
     * The singlePlacementGroup of the VirtualMachineScaleSetProperties
     */
    @Nullable
    public abstract Boolean singlePlacementGroup();

    /**
     *  Specifies the over provision of the virtual machine scale set
     */
    @Nullable
    public abstract Boolean overProvision();

    /**
     *  Specifies the upgrade policy of the virtual machine scale set
     */
    public abstract VirtualMachineScaleSetUpgradePolicy upgradePolicy();

    /**
     *  Specifies the virtual machine profile of the virtual machine scale set
     */
    public abstract VirtualMachineScaleSetVirtualMachineProfile virtualMachineProfile();

    @SerializedNames({"singlePlacementGroup", "overProvision", "upgradePolicy", "virtualMachineProfile"})
    public static VirtualMachineScaleSetProperties create(final Boolean singlePlacementGroup,
                                                          final Boolean overProvision,
                                                          final VirtualMachineScaleSetUpgradePolicy upgradePolicy,
                                                          final VirtualMachineScaleSetVirtualMachineProfile virtualMachineProfile) {

        return new AutoValue_VirtualMachineScaleSetProperties(singlePlacementGroup, overProvision, upgradePolicy, virtualMachineProfile);
    }
}


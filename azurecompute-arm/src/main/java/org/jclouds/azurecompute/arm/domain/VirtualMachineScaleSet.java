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

import java.util.List;
import java.util.Map;

import com.sun.media.jfxmedia.events.BufferListener;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * VirtualMachineScaleSet for subscription
 */
@AutoValue
public abstract class VirtualMachineScaleSet {

    /**
     * The id of the virtual machine scale set
     */
    @Nullable
    public abstract String id();

    /**
     * The name of the virtual machine scale set
     */
    @Nullable
    public abstract String name();

    /**
     * The location of the virtual machine scale set
     */
    @Nullable
    public abstract String location();

    /**
     * Specifies the type of the virtual machine scale set
     */
    @Nullable
    public abstract VirtualMachineScaleSetSKU sku();

    /**
     * Specifies the tags of the virtual machine scale set
     */
    @Nullable
    public abstract Map<String, String> tags();

    @Nullable
    public abstract Boolean singlePlacementGroup();

    @Nullable
    public abstract VirtualMachineScaleSetPlan plan();

    /**
     *  Specifies the virtual machine profile of the virtual machine scale set
     */
    @Nullable
    public abstract VirtualMachineScaleSetVirtualMachineProfile virtualMachineProfile();

    /**
     *  Specifies the upgrade policy of the virtual machine scale set
     */
    @Nullable
    public abstract VirtualMachineScaleSetUpgradePolicy upgradePolicy();

    /**
     *  Specifies the over provision of the virtual machine scale set
     */
    @Nullable
    public abstract Boolean overProvision();

    /**
     * Specifies the properties of the availability set
     */
    @Nullable
    public abstract VirtualMachineScaleSetProperties properties();

    @SerializedNames({  "id", "name", "location","sku", "tags", "plan", "singlePlacementGroup", "plan", "profile",
            "upgradePolicy", "overProvision", "properties"})
    public static VirtualMachineScaleSet create(final String id, final String name, final String location,
                                                VirtualMachineScaleSetSKU sku, final Map<String, String> tags,
                                                Boolean singlePlacementGroup,
                                                VirtualMachineScaleSetPlan plan,
                                                VirtualMachineScaleSetVirtualMachineProfile profile,
                                                VirtualMachineScaleSetUpgradePolicy upgradePolicy,
                                                Boolean overProvision,
                                                VirtualMachineScaleSetProperties properties) {
        return builder().id(id).name(name).location(location).sku(sku).tags(tags)
                .singlePlacementGroup(singlePlacementGroup).plan(plan).virtualMachineProfile(profile)
                .upgradePolicy(upgradePolicy).overProvision(overProvision).properties(properties)
                .build();
    }

    public abstract Builder toBuilder();

    private static Builder builder() {
        return new AutoValue_VirtualMachineScaleSet.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder name(String name);
        public abstract Builder location(String location);
        public abstract Builder sku(VirtualMachineScaleSetSKU sku);
        public abstract Builder tags(Map<String, String> tags);
        public abstract Builder singlePlacementGroup(Boolean singlePlacementGroup);
        public abstract Builder plan(VirtualMachineScaleSetPlan plan);
        public abstract Builder virtualMachineProfile(VirtualMachineScaleSetVirtualMachineProfile virtualMachineProfile);
        public abstract Builder upgradePolicy(VirtualMachineScaleSetUpgradePolicy upgradePolicy);
        public abstract Builder overProvision(Boolean overProvision);
        public abstract Builder properties(VirtualMachineScaleSetProperties properties);

        abstract Map<String, String> tags();
        abstract VirtualMachineScaleSet autoBuild();

        public VirtualMachineScaleSet build() {
            tags(tags() != null ? ImmutableMap.copyOf(tags()) : null);
            return autoBuild();
        }
    }
}

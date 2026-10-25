package yesman.epicfight.registry.deferred.holders;

import net.minecraft.resources.ResourceKey;
import yesman.epicfight.fabric.shim.DeferredHolder;
import yesman.epicfight.api.ex_cap.data.modifier.WeaponModifier;

public final class DeferredModifier extends DeferredHolder<WeaponModifier.Builder, WeaponModifier.Builder>
{
    public DeferredModifier(ResourceKey<WeaponModifier.Builder> key) {
        super(key);
    }
}

package com.ninni.dye_depot.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ninni.dye_depot.registry.DDTags;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerMakeLove.class)
public class VillagerMakeLoveMixin {

    @WrapOperation(
            method = "method_43972",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/resources/ResourceKey;)Z")
    )
    private static boolean isHomePoi(Holder<PoiType> instance, ResourceKey<PoiType> key, Operation<Boolean> original) {
        return instance.is(DDTags.BEDS) || original.call(instance, key);
    }

}

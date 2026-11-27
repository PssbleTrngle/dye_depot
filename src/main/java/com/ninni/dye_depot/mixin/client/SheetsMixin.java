package com.ninni.dye_depot.mixin.client;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.registry.DDDyes;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Stream;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheets.class)
public class SheetsMixin {

    @Shadow
    @Final
    public static Material[] BED_TEXTURES;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void fixBedTextures(CallbackInfo ci) {
        for (var dye : DDDyes.values()) {
            var current = BED_TEXTURES[dye.getId()];
            var location = DyeDepot.modLoc(current.texture().getPath());
            var material = new Material(current.atlasLocation(), location);
            BED_TEXTURES[dye.getId()] = material;
        }
    }

    @WrapOperation(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;", ordinal = 0)
    )
    private static <R> R addShulkerTextures(Stream<Material> instance, Collector<Material, ?, ImmutableList<Material>> collector, Operation<R> original) {
        var modded = Arrays.stream(DDDyes.values()).map(dye -> {
            var location = DyeDepot.modLoc("entity/shulker/shulker_" + dye.getSerializedName());
            return new Material(Sheets.SHULKER_SHEET, location);
        });

        return original.call(Stream.concat(instance, modded), collector);
    }
}

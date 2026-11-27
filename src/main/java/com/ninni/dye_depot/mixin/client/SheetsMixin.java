package com.ninni.dye_depot.mixin.client;

import com.ninni.dye_depot.DyeDepot;

import com.ninni.dye_depot.registry.DDDyes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

@Mixin(Sheets.class)
public class SheetsMixin {

    @Shadow
    @Final
    public static Material[] BED_TEXTURES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void DD$fixBedTextures(CallbackInfo ci) {
        for(DDDyes dye : DDDyes.values()) {
            String name = dye.getSerializedName();
            ResourceLocation location = DyeDepot.modLoc("entity/bed/" + name);
            Material material = new Material(Sheets.BED_SHEET, location);
            BED_TEXTURES[dye.getId()] = material;
        }
    }
}

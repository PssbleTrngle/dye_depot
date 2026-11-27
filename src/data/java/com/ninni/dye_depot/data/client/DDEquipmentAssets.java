package com.ninni.dye_depot.data.client;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.registry.DDDyes;

import java.util.function.BiConsumer;

import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class DDEquipmentAssets extends EquipmentAssetProvider {

    public DDEquipmentAssets(PackOutput output) {
        super(output);
    }

    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        for (var color : DDDyes.values()) {
            output.accept(
                    EquipmentAssets.HARNESSES.get(color.get()),
                    EquipmentClientInfo.builder()
                            .addLayers(
                                    EquipmentClientInfo.LayerType.HAPPY_GHAST_BODY,
                                    new EquipmentClientInfo.Layer(
                                            DyeDepot.modLoc(color.getSerializedName() + "_harness")
                                    )
                            )
                            .build()
            );

            output.accept(
                    EquipmentAssets.CARPETS.get(color.get()),
                    EquipmentClientInfo.builder()
                            .addLayers(
                                    EquipmentClientInfo.LayerType.LLAMA_BODY,
                                    new EquipmentClientInfo.Layer(
                                            DyeDepot.modLoc(color.getSerializedName())
                                    )
                            )
                            .build()
            );
        }
    }
}

package com.ninni.dye_depot.data.client;

import com.ninni.dye_depot.registry.DDItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class DDItemModels {

    private final ItemModelGenerators generator;

    public DDItemModels(ItemModelGenerators generators) {
        this.generator = generators;
    }

    protected void registerModels(HolderLookup.Provider lookup) {
        DDItems.DYES.holders().forEach(this::basicItem);
        DDItems.BUNDLES.holders().forEach(this::bundle);
    }

    private void bundle(Holder<Item> item) {
        generator.generateBundleModels(item.value());
    }

    private void basicItem(Holder<? extends ItemLike> item) {
        generator.generateFlatItem(item.value().asItem(), ModelTemplates.FLAT_ITEM);
    }

}

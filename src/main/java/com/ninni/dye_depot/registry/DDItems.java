package com.ninni.dye_depot.registry;

import com.ninni.dye_depot.DyeDepot;

import java.util.function.Function;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DDItems {

    static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(DyeDepot.MOD_ID);

    public static final DyedHolders<Item, Item> DYES = DyedHolders.createModded(dye ->
            register(dye + "_dye", props -> new DyeItem(props.component(DataComponents.DYE, dye)))
    );

    public static final DyedHolders<Item, Item> SHULKER_BOXES = DyedHolders.createModded(dye ->
            register(dye + "_shulker_box", props -> new BlockItem(DDBlocks.SHULKER_BOXES.getOrThrow(dye), props.stacksTo(1).useBlockDescriptionPrefix()))
    );

    public static final DyedHolders<Item, Item> BANNERS = DyedHolders.createModded(dye ->
            register(dye + "_banner", props -> new BannerItem(DDBlocks.BANNERS.getOrThrow(dye), DDBlocks.WALL_BANNERS.getOrThrow(dye), props
                    .useBlockDescriptionPrefix()
                    .stacksTo(16)
                    .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
            ))
    );

    public static final DyedHolders<Item, Item> BEDS = DyedHolders.createModded(dye ->
            register(dye + "_bed", props -> new BedItem(DDBlocks.BEDS.getOrThrow(dye), props.stacksTo(1).useBlockDescriptionPrefix()))
    );

    public static final DyedHolders<Item, Item> BUNDLES = DyedHolders.createModded(dye ->
            register(dye + "_bundle", props -> new BundleItem(props.stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)))
    );

    @SuppressWarnings("unchecked")
    private static <T extends Item> Holder<T> register(String id, Function<Item.Properties, T> item) {
        return (Holder<T>) REGISTRY.registerItem(id, item);
    }

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }

}

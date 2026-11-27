package com.ninni.dye_depot.registry;

import static com.ninni.dye_depot.registry.DDItems.*;
import static net.minecraft.world.item.Items.*;

import com.ninni.dye_depot.DyeDepot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = DyeDepot.MOD_ID)
public class DDCreativeModeTabs {

    interface Entries {
        void addBefore(ItemLike reference, ItemLike... values);

        void addAfter(ItemLike reference, ItemLike... values);
    }

    @SubscribeEvent
    public static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        var tab = event.getTabKey();
        var entries = new Entries() {
            @Override
            public void addBefore(ItemLike reference, ItemLike... values) {
                for (ItemLike value : values) {
                    event.insertBefore(new ItemStack(reference), new ItemStack(value), TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            }

            @Override
            public void addAfter(ItemLike reference, ItemLike... values) {
                for (ItemLike value : values) {
                    event.insertAfter(new ItemStack(reference), new ItemStack(value), TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            }
        };

        if (tab == CreativeModeTabs.INGREDIENTS) {
            addDyed(entries, DYES, "dye");
        }

        if (tab == CreativeModeTabs.COLORED_BLOCKS) {
            entries.addBefore(WHITE_WOOL,
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.WHITE),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.LIGHT_GRAY),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.GRAY),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.BLACK),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.BROWN),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.MAROON.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.ROSE.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.RED),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.CORAL.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.GINGER.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.ORANGE),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.TAN.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.BEIGE.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.YELLOW),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.AMBER.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.OLIVE.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.LIME),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.FOREST.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.GREEN),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.VERDANT.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.TEAL.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.CYAN),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.MINT.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.AQUA.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.LIGHT_BLUE),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.BLUE),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.SLATE.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.NAVY.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DDDyes.INDIGO.get()),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.PURPLE),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.MAGENTA),
                    DDBlocks.DYE_BASKETS.getOrThrow(DyeColor.PINK)
            );

            addDyed(entries, DDBlocks.WOOL, "wool");
            addDyed(entries, DDBlocks.CARPETS, "carpet");

            addDyed(entries, DDBlocks.TERRACOTTA, "terracotta");
            addDyed(entries, DDBlocks.GLAZED_TERRACOTTA, "glazed_terracotta");

            addDyed(entries, DDBlocks.CONCRETE, "concrete");
            addDyed(entries, DDBlocks.CONCRETE_POWDER, "concrete_powder");

            addDyed(entries, DDBlocks.STAINED_GLASS, "stained_glass");
            addDyed(entries, DDBlocks.STAINED_GLASS_PANES, "stained_glass_pane");

            addDyed(entries, DDBlocks.SHULKER_BOXES, "shulker_box");

            addDyed(entries, DDBlocks.BEDS, "bed");

            addDyed(entries, DDBlocks.CANDLES, "candle");

            addDyed(entries, DDBlocks.BANNERS, "banner");
        }


        if (tab == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            addDyed(entries, DDBlocks.SHULKER_BOXES, "shulker_box");

            addDyed(entries, DDBlocks.BEDS, "bed");

            addDyed(entries, DDBlocks.CANDLES, "candle");

            addDyed(entries, DDBlocks.BANNERS, "banner");
        }

        if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            addDyed(entries, BUNDLES, "bundle");
            addDyed(entries, HARNESSES, "harness");
        }
    }

    private static void addDyed(Entries entries, DyedHolders<?, ? extends ItemLike> values, String baseName) {
        addDyed(entries, values, DyedHolders.fromRegistry(BuiltInRegistries.ITEM, DyedHolders.vanillaColors(), Identifier.withDefaultNamespace(baseName)));
    }

    private static void addDyed(Entries entries, DyedHolders<?, ? extends ItemLike> values, DyedHolders<?, ? extends ItemLike> reference) {
        entries.addBefore(reference.getOrThrow(DyeColor.ORANGE), values.getOrThrow(DDDyes.GINGER.get()));
        entries.addAfter(reference.getOrThrow(DyeColor.ORANGE), values.getOrThrow(DDDyes.TAN.get()));
        entries.addBefore(reference.getOrThrow(DyeColor.YELLOW), values.getOrThrow(DDDyes.BEIGE.get()));
        entries.addAfter(reference.getOrThrow(DyeColor.YELLOW), values.getOrThrow(DDDyes.AMBER.get()), values.getOrThrow(DDDyes.OLIVE.get()));
        entries.addBefore(reference.getOrThrow(DyeColor.GREEN), values.getOrThrow(DDDyes.FOREST.get()));
        entries.addAfter(reference.getOrThrow(DyeColor.GREEN), values.getOrThrow(DDDyes.VERDANT.get()));
        entries.addBefore(reference.getOrThrow(DyeColor.RED), values.getOrThrow(DDDyes.MAROON.get()), values.getOrThrow(DDDyes.ROSE.get()));
        entries.addAfter(reference.getOrThrow(DyeColor.RED), values.getOrThrow(DDDyes.CORAL.get()));
        entries.addBefore(reference.getOrThrow(DyeColor.CYAN), values.getOrThrow(DDDyes.TEAL.get()));
        entries.addAfter(reference.getOrThrow(DyeColor.CYAN), values.getOrThrow(DDDyes.MINT.get()), values.getOrThrow(DDDyes.AQUA.get()));
        entries.addAfter(reference.getOrThrow(DyeColor.BLUE), values.getOrThrow(DDDyes.SLATE.get()), values.getOrThrow(DDDyes.NAVY.get()));
        entries.addBefore(reference.getOrThrow(DyeColor.PURPLE), values.getOrThrow(DDDyes.INDIGO.get()));
    }

}

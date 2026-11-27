package com.ninni.dye_depot.data.server;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.registry.DDBlocks;
import com.ninni.dye_depot.registry.DDDyes;
import com.ninni.dye_depot.registry.DDItems;
import com.ninni.dye_depot.registry.DDTags;
import com.ninni.dye_depot.registry.DyedHolders;

import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NeverCondition;

public class DDRecipes extends RecipeProvider {

    private final DyedHolders<Item, Item> dyes;
    private final HolderLookup.RegistryLookup<Block> blockLookup;
    private final HolderLookup.RegistryLookup<Item> itemLookup;

    public DDRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.dyes = DDItems.DYES.mergeVanilla(registries.lookupOrThrow(Registries.ITEM));
        this.blockLookup = registries.lookupOrThrow(Registries.BLOCK);
        this.itemLookup = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void buildRecipes() {
        var namespace = DyeDepot.MOD_ID + ":";

        DDBlocks.BANNERS.forEach((dye, block) ->
                banner(block.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        dyeing(output, RecipeCategory.DECORATIONS, DDBlocks.BEDS.mergeVanilla(blockLookup), ItemTags.BEDS);
        DDBlocks.BEDS.forEach((dye, block) ->
                bedFromPlanksAndWool(block.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        dyeingTransmute(output, RecipeCategory.DECORATIONS, DDBlocks.SHULKER_BOXES, ItemTags.SHULKER_BOXES);

        DDBlocks.CANDLES.forEach((dye, block) ->
                candle(block.value(), dyes.getOrThrow(dye))
        );

        dyeing(output, RecipeCategory.DECORATIONS, DDBlocks.CARPETS.mergeVanilla(blockLookup), ItemTags.WOOL_CARPETS);
        DDBlocks.CARPETS.forEach((dye, block) ->
                carpet(block.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        DDBlocks.CONCRETE_POWDER.forEach((dye, block) ->
                concretePowder(block.value(), dyes.getOrThrow(dye))
        );

        DDBlocks.GLAZED_TERRACOTTA.forEach((dye, block) ->
                smeltingResultFromBase(block.value(), DDBlocks.TERRACOTTA.getOrThrow(dye))
        );

        DDBlocks.STAINED_GLASS.forEach((dye, block) ->
                stainedGlassFromGlassAndDye(block.value(), dyes.getOrThrow(dye))
        );

        DDBlocks.STAINED_GLASS_PANES.forEach((dye, block) -> {
            var dyeItem = dyes.getOrThrow(dye);
            shaped(RecipeCategory.DECORATIONS, block.value(), 8)
                    .define('#', Blocks.GLASS_PANE).define('$', dyeItem)
                    .pattern("###")
                    .pattern("#$#")
                    .pattern("###")
                    .group("stained_glass_pane")
                    .unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE))
                    .unlockedBy(getHasName(dyeItem), has(dyeItem))
                    .save(output, namespace + getConversionRecipeName(block.value(), Blocks.GLASS_PANE));
            stainedGlassPaneFromStainedGlass(block.value(), DDBlocks.STAINED_GLASS.getOrThrow(dye));
        });

        DDBlocks.TERRACOTTA.forEach((dye, block) ->
                coloredTerracottaFromTerracottaAndDye(block.value(), dyes.getOrThrow(dye))
        );

        DDBlocks.DYE_BASKETS.forEach((dye, block) -> {
            nineBlockStorageRecipes(
                    RecipeCategory.MISC, dyes.getOrThrow(dye), RecipeCategory.DECORATIONS, block.value(),
                    namespace + getItemName(block.value()), null, namespace + dye + "_dye_from_basket", dye + "_dye"
            );
        });

        dyeing(output, RecipeCategory.MISC, DDBlocks.WOOL.mergeVanilla(blockLookup), ItemTags.WOOL);

        dyeingTransmute(output, RecipeCategory.TOOLS, DDItems.BUNDLES, ItemTags.BUNDLES);
        dyeing(output, RecipeCategory.TOOLS, DDItems.HARNESSES.mergeVanilla(itemLookup), ItemTags.HARNESSES);
        DDItems.HARNESSES.forEach((dye, item) ->
                harness(item.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        dyeConversion(output, DDDyes.AMBER.get(), Items.GLOW_BERRIES, 1);
        dyeConversion(output, DDDyes.AMBER.get(), Items.HONEYCOMB, 2);

        dyeConversion(output, DDDyes.AQUA.get(), Items.GLOW_INK_SAC, 1);

        dyeConversion(output, DDDyes.BEIGE.get(), Items.OXEYE_DAISY, 1);

        dyeConversion(output, DDDyes.CORAL.get(), Items.NAUTILUS_SHELL, 2);
        dyeSmelting(output, DDDyes.CORAL.get(), DDTags.SMELTS_INTO_CORAL_DYE);

        dyeConversion(output, DDDyes.FOREST.get(), Items.BAMBOO, 1);

        dyeSmelting(output, DDDyes.GINGER.get(), Items.PUMPKIN);

        dyeConversion(output, DDDyes.MAROON.get(), Items.SPIDER_EYE, 1);

        dyeConversion(output, DDDyes.OLIVE.get(), Items.POISONOUS_POTATO, 1);

        dyeConversion(output, DDDyes.TAN.get(), Items.GLOWSTONE_DUST, 1);

        dyeConversion(output, DDDyes.VERDANT.get(), Items.DRIED_KELP, 1);
        dyeSmelting(output, DDDyes.VERDANT.get(), Items.VINE);

        dyeConversion(output, DDDyes.ROSE.get(), Items.ROSE_BUSH, 2);

        dyeSmelting(output, DDDyes.TEAL.get(), Items.PRISMARINE_SHARD);

        // Gray hues
        dyeMixing(output, DyeColor.BLACK, DyeColor.GRAY, DyeColor.BLACK);
        dyeMixing(output, DyeColor.BLACK, DyeColor.LIGHT_GRAY, DyeColor.BLACK, DyeColor.BLACK);
        dyeMixing(output, DyeColor.BLACK, DyeColor.WHITE, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK);

        dyeMixing(output, DyeColor.GRAY, DyeColor.LIGHT_GRAY, DyeColor.BLACK);
        dyeMixing(output, DyeColor.GRAY, DyeColor.WHITE, DyeColor.BLACK, DyeColor.BLACK);

        dyeMixing(output, DyeColor.WHITE, DyeColor.BLACK, DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE);
        dyeMixing(output, DyeColor.WHITE, DyeColor.GRAY, DyeColor.WHITE, DyeColor.WHITE);
        dyeMixing(output, DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.WHITE);

        // Light, Dark, Tinted variants
        dyeColorSpectrum(output, DDDyes.CORAL.get(), DyeColor.RED, DyeColor.MAGENTA, DDDyes.ROSE.get(), DDDyes.MAROON.get());
        dyeColorSpectrum(output, DDDyes.AQUA.get(), DyeColor.CYAN, DyeColor.GREEN, DDDyes.MINT.get(), DDDyes.TEAL.get());
        dyeColorSpectrum(output, DyeColor.LIME, DyeColor.GREEN, DyeColor.YELLOW, DDDyes.FOREST.get(), DDDyes.VERDANT.get());
        dyeColorSpectrum(output, DyeColor.PINK, DyeColor.MAGENTA, DyeColor.BLUE, DyeColor.PURPLE, DDDyes.INDIGO.get());
        dyeColorSpectrum(output, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.CYAN, DDDyes.SLATE.get(), DDDyes.NAVY.get());
        dyeColorSpectrum(output, DDDyes.TAN.get(), DyeColor.ORANGE, DyeColor.RED, DDDyes.GINGER.get(), DyeColor.BROWN);
        dyeColorSpectrum(output, DDDyes.BEIGE.get(), DyeColor.YELLOW, DyeColor.ORANGE, DDDyes.AMBER.get(), DDDyes.OLIVE.get());

        // Additional mixes
        dyeMixing(output, DyeColor.GREEN, DyeColor.BLUE, DyeColor.YELLOW);
        dyeMixing(output, DyeColor.MAGENTA, DDDyes.INDIGO.get(), DyeColor.LIGHT_BLUE);

        // Disabled vanilla recipes
        disable(output, Identifier.withDefaultNamespace("gray_dye"));
        disable(output, Identifier.withDefaultNamespace("lime_dye"));
        disable(output, Identifier.withDefaultNamespace("light_blue_dye_from_blue_white_dye"));
        disable(output, Identifier.withDefaultNamespace("light_gray_dye_from_oxeye_daisy"));
        disable(output, Identifier.withDefaultNamespace("magenta_dye_from_blue_red_pink"));
        disable(output, Identifier.withDefaultNamespace("magenta_dye_from_blue_red_white_dye"));
        disable(output, Identifier.withDefaultNamespace("magenta_dye_from_purple_and_pink"));
        disable(output, Identifier.withDefaultNamespace("pink_dye_from_red_white_dye"));
        disable(output, Identifier.withDefaultNamespace("purple_dye"));
        disable(output, Identifier.withDefaultNamespace("red_dye_from_rose_bush"));

        // Supplementaries compat
        // TODO waiting for supplementaries
        /*
        withSupplementariesFlag(this, ModCompat.SUPPLEMENTARIES, "candle_holder", () -> {
            var holders = ModCompat.supplementariesHolders(itemLookup, "candle_holder");
            holders.forEach((dye, block) -> {
                var candle = DDBlocks.CANDLES.getOrThrow(dye);
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block.value())
                        .pattern("C")
                        .pattern("N")
                        .define('C', candle)
                        .define('N', Items.IRON_INGOT)
                        .group("candle_holder")
                        .unlockedBy("has_candle", has(candle))
                        .save(output);
            });

            dyeing(output, RecipeCategory.DECORATIONS, holders, ModRegistry.CANDLE_HOLDERS.get(null).get());
        });

        withSupplementariesFlag(this, ModCompat.SUPPLEMENTARIES_SQUARED, "candle_holder", () -> {
            var holders = ModCompat.supplementariesSquaredHolders(itemLookup, "gold_candle_holder");
            holders.forEach((dye, block) -> {
                var candle = DDBlocks.CANDLES.getOrThrow(dye);
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block.value())
                        .pattern("C")
                        .pattern("N")
                        .define('C', candle)
                        .define('N', Items.GOLD_INGOT)
                        .group("gold_candle_holder")
                        .unlockedBy("has_candle", has(candle))
                        .save(output);
            });

            dyeing(output, RecipeCategory.DECORATIONS, holders, SuppSquared.GOLDEN_CANDLE_HOLDERS.get(null).get());
        });
         */
    }

    private void dyeConversion(RecipeOutput output, DyeColor dye, ItemLike from, int amount) {
        var to = dyes.getOrThrow(dye);
        shapeless(RecipeCategory.MISC, to, amount)
                .requires(from)
                .group(dye + "_dye")
                .unlockedBy(getHasName(from), has(from))
                .save(output, DyeDepot.key(Registries.RECIPE, getConversionRecipeName(to, from)));
    }

    private void dyeSmelting(RecipeOutput output, DyeColor dye, TagKey<Item> ingredient) {
        dyeSmelting(output, dye, Ingredient.of(items.getOrThrow(ingredient)), it -> it.unlockedBy(getHasName(ingredient), has(ingredient)));
    }

    private void dyeSmelting(RecipeOutput output, DyeColor dye, ItemLike ingredient) {
        dyeSmelting(output, dye, Ingredient.of(ingredient), it -> it.unlockedBy(getHasName(ingredient), has(ingredient)));
    }

    private void dyeSmelting(RecipeOutput output, DyeColor dye, Ingredient ingredient, UnaryOperator<SimpleCookingRecipeBuilder> factory) {
        factory.apply(SimpleCookingRecipeBuilder.smelting(ingredient, RecipeCategory.MISC, CookingBookCategory.MISC, dyes.getOrThrow(dye), 0.1F, 200))
                .group(dye + "_dye")
                .save(output, DyeDepot.key(Registries.RECIPE, (dye + "_dye_from_smelting")));
    }

    private void disable(RecipeOutput output, Identifier id) {
        shapeless(RecipeCategory.MISC, Items.DEBUG_STICK)
                .requires(Items.DEBUG_STICK)
                .unlockedBy("never", has(Items.DEBUG_STICK))
                .save(withConditions(output, NeverCondition.INSTANCE), ResourceKey.create(Registries.RECIPE, id));
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed) {
        dyeing(output, category, dyed, Ingredient.of(dyed.values().map(ItemLike::asItem)), builder -> builder
                .unlockedBy("has_base", inventoryTrigger(ItemPredicate.Builder.item().of(items, dyed.values().toArray(ItemLike[]::new)).build()))
        );
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed, TagKey<Item> from) {
        dyeing(output, category, dyed, Ingredient.of(items.getOrThrow(from)), builder -> builder
                .unlockedBy(getHasName(from), has(from))
        );
    }

    private void dyeingTransmute(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed, TagKey<Item> from) {
        var group = dyed.detectBaseName();
        dyed.forEach((color, item) -> {
            TransmuteRecipeBuilder.transmute(
                            RecipeCategory.DECORATIONS,
                            Ingredient.of(items.getOrThrow(from)),
                            Ingredient.of(items.getOrThrow(color.getTag())),
                            item.value().asItem()
                    )
                    .unlockedBy(getHasName(from), has(from))
                    .group(group)
                    .save(output);
        });
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed, Ingredient from, UnaryOperator<ShapelessRecipeBuilder> factory) {
        var group = dyed.detectBaseName();
        dyed.forEach((color, item) -> {
            var id = item.unwrapKey().orElseThrow().identifier();
            var dye = dyes.getOrThrow(color);
            factory.apply(shapeless(category, item.value()))
                    .requires(from)
                    .requires(dye)
                    .group(group)
                    .unlockedBy(getHasName(dye), has(dye))
                    .save(output, ResourceKey.create(Registries.RECIPE, id.withPrefix("dye_")));
        });
    }

    private void dyeColorSpectrum(RecipeOutput output, DyeColor light, DyeColor base, DyeColor aspect, DyeColor tinted, DyeColor dark) {
        lightBaseDark(output, light, base, dark);
        dyeHue(output, base, aspect, tinted);
        dyeMixing(output, light, tinted, DyeColor.WHITE);
        dyeMixing(output, dark, tinted, DyeColor.BLACK);
    }

    private void lightBaseDark(RecipeOutput output, DyeColor light, DyeColor base, DyeColor dark) {
        lightenAndDarken(output, light, base);
        lightenAndDarken(output, base, dark);
        dyeMixing(output, light, dark, DyeColor.WHITE, DyeColor.WHITE);
        dyeMixing(output, dark, light, DyeColor.BLACK, DyeColor.BLACK);
        dyeMixing(output, base, dark, light);
    }

    private void lightenAndDarken(RecipeOutput output, DyeColor light, DyeColor dark) {
        dyeMixing(output, light, dark, DyeColor.WHITE);
        dyeMixing(output, dark, light, DyeColor.BLACK);
    }

    private void dyeHue(RecipeOutput output, DyeColor base, DyeColor aspect, DyeColor result) {
        dyeMixing(output, result, base, aspect);
        dyeMixing(output, base, result, DyeColor.GRAY);
        dyeMixing(output, base, result, DyeColor.LIGHT_GRAY);
    }

    private void dyeMixing(RecipeOutput output, DyeColor to, DyeColor... from) {
        var result = dyes.getOrThrow(to);
        var builder = shapeless(RecipeCategory.MISC, result, from.length)
                .group(to + "_dye");

        var unique = new HashSet<DyeColor>();
        for (var color : from) {
            var item = dyes.getOrThrow(color);
            builder.requires(item);
            if (unique.add(color))
                builder.unlockedBy(getHasName(item), has(item));
        }

        var name = getItemName(result) + "_from_" + unique.stream()
                .sorted(Comparator.comparing(DyeColor::getId))
                .map(DyeColor::getSerializedName)
                .collect(Collectors.joining("_and_"));

        builder.save(output, DyeDepot.key(Registries.RECIPE, name));
    }

    public static String getHasName(TagKey<?> tag) {
        return "has_" + tag.location().getPath();
    }

    public static RecipeOutput withConditions(RecipeOutput output, ICondition... conditions) {
        return output.withConditions(conditions);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new DDRecipes(provider, output);
        }

        @Override
        public String getName() {
            return "Recipes - " + DyeDepot.MOD_ID;
        }
    }

}

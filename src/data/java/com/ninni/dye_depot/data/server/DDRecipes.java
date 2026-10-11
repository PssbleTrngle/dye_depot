package com.ninni.dye_depot.data.server;

import static com.ninni.dye_depot.data.ModCompat.withSupplementariesFlag;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.data.ModCompat;
import com.ninni.dye_depot.registry.DDBlocks;
import com.ninni.dye_depot.registry.DDDyes;
import com.ninni.dye_depot.registry.DDItems;
import com.ninni.dye_depot.registry.DDTags;
import com.ninni.dye_depot.registry.DyedHolders;
import com.possible_triangle.multikulti.datagen.conditions.Conditional;
import com.possible_triangle.multikulti.datagen.conditions.False;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class DDRecipes extends RecipeProvider {

    private final DyedHolders<Item, Item> dyes;
    private final HolderLookup.RegistryLookup<Block> blockLookup;
    private final HolderLookup.RegistryLookup<Item> itemLookup;

    public DDRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupFuture) {
        super(output, lookupFuture);
        var lookup = lookupFuture.join();
        this.dyes = DDItems.DYES.mergeVanilla(lookup.lookupOrThrow(Registries.ITEM));
        this.blockLookup = lookup.lookupOrThrow(Registries.BLOCK);
        this.itemLookup = lookup.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        var namespace = DyeDepot.MOD_ID + ":";

        DDBlocks.BANNERS.forEach((dye, block) ->
                banner(output, block.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        dyeing(output, RecipeCategory.DECORATIONS, DDBlocks.BEDS.mergeVanilla(blockLookup), ItemTags.BEDS);
        DDBlocks.BEDS.forEach((dye, block) ->
                bedFromPlanksAndWool(output, block.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        DDBlocks.CANDLES.forEach((dye, block) ->
                candle(output, block.value(), dyes.getOrThrow(dye))
        );

        dyeing(output, RecipeCategory.DECORATIONS, DDBlocks.CARPETS.mergeVanilla(blockLookup), ItemTags.WOOL_CARPETS);
        DDBlocks.CARPETS.forEach((dye, block) ->
                carpet(output, block.value(), DDBlocks.WOOL.getOrThrow(dye))
        );

        DDBlocks.CONCRETE_POWDER.forEach((dye, block) ->
                concretePowder(output, block.value(), dyes.getOrThrow(dye))
        );

        DDBlocks.GLAZED_TERRACOTTA.forEach((dye, block) ->
                smeltingResultFromBase(output, block.value(), DDBlocks.TERRACOTTA.getOrThrow(dye))
        );

        DDBlocks.STAINED_GLASS.forEach((dye, block) ->
                stainedGlassFromGlassAndDye(output, block.value(), dyes.getOrThrow(dye))
        );

        DDBlocks.STAINED_GLASS_PANES.forEach((dye, block) -> {
            var dyeItem = dyes.getOrThrow(dye);
            ShapedRecipeBuilder
                    .shaped(RecipeCategory.DECORATIONS, block.value(), 8)
                    .define('#', Blocks.GLASS_PANE).define('$', dyeItem)
                    .pattern("###")
                    .pattern("#$#")
                    .pattern("###")
                    .group("stained_glass_pane")
                    .unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE))
                    .unlockedBy(getHasName(dyeItem), has(dyeItem))
                    .save(output, namespace + getConversionRecipeName(block.value(), Blocks.GLASS_PANE));
            stainedGlassPaneFromStainedGlass(output, block.value(), DDBlocks.STAINED_GLASS.getOrThrow(dye));
        });

        DDBlocks.TERRACOTTA.forEach((dye, block) ->
                coloredTerracottaFromTerracottaAndDye(output, block.value(), dyes.getOrThrow(dye))
        );

        DDBlocks.DYE_BASKETS.forEach((dye, block) -> {
            nineBlockStorageRecipes(
                    output, RecipeCategory.MISC, dyes.getOrThrow(dye), RecipeCategory.DECORATIONS, block.value(),
                    namespace + getItemName(block.value()), null, namespace + dye + "_dye_from_basket", dye + "_dye"
            );
        });

        dyeing(output, RecipeCategory.MISC, DDBlocks.WOOL.mergeVanilla(blockLookup), ItemTags.WOOL);

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
        disable(output, ResourceLocation.withDefaultNamespace("gray_dye"));
        disable(output, ResourceLocation.withDefaultNamespace("lime_dye"));
        disable(output, ResourceLocation.withDefaultNamespace("light_blue_dye_from_blue_white_dye"));
        disable(output, ResourceLocation.withDefaultNamespace("light_gray_dye_from_oxeye_daisy"));
        disable(output, ResourceLocation.withDefaultNamespace("magenta_dye_from_blue_red_pink"));
        disable(output, ResourceLocation.withDefaultNamespace("magenta_dye_from_blue_red_white_dye"));
        disable(output, ResourceLocation.withDefaultNamespace("magenta_dye_from_purple_and_pink"));
        disable(output, ResourceLocation.withDefaultNamespace("pink_dye_from_red_white_dye"));
        disable(output, ResourceLocation.withDefaultNamespace("purple_dye"));
        disable(output, ResourceLocation.withDefaultNamespace("red_dye_from_rose_bush"));

        // Supplementaries compat
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

            dyeing(output, RecipeCategory.DECORATIONS, holders, ModRegistry.CANDLE_HOLDERS.get(DyeColor.WHITE).get());
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

            dyeing(output, RecipeCategory.DECORATIONS, holders, SuppSquared.GOLDEN_CANDLE_HOLDERS.get(DyeColor.WHITE).get());
        });

        ModCompat.supplementariesHolders(itemLookup, "flag").forEach((dye, block) -> {
            var wool = DDBlocks.WOOL.getOrThrow(dye);
            withSupplementariesFlag(ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block.value()), ModCompat.SUPPLEMENTARIES, "flag")
                    .pattern("###")
                    .pattern("###")
                    .pattern("|  ")
                    .define('#', wool)
                    .define('|', Items.STICK)
                    .group("flag")
                    .unlockedBy("has_wool", has(wool))
                    .save(output);
        });
    }

    private void dyeConversion(RecipeOutput output, DyeColor dye, ItemLike from, int amount) {
        var to = dyes.getOrThrow(dye);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, to, amount)
                .requires(from)
                .group(dye + "_dye")
                .unlockedBy(getHasName(from), has(from))
                .save(output, DyeDepot.modLoc(getConversionRecipeName(to, from)));
    }

    private void dyeSmelting(RecipeOutput output, DyeColor dye, TagKey<Item> ingredient) {
        dyeSmelting(output, dye, Ingredient.of(ingredient), it -> it.unlockedBy(getHasName(ingredient), has(ingredient)));
    }

    private void dyeSmelting(RecipeOutput output, DyeColor dye, ItemLike ingredient) {
        dyeSmelting(output, dye, Ingredient.of(ingredient), it -> it.unlockedBy(getHasName(ingredient), has(ingredient)));
    }

    private void dyeSmelting(RecipeOutput output, DyeColor dye, Ingredient ingredient, UnaryOperator<SimpleCookingRecipeBuilder> factory) {
        factory.apply(SimpleCookingRecipeBuilder.smelting(ingredient, RecipeCategory.MISC, dyes.getOrThrow(dye), 0.1F, 200))
                .group(dye + "_dye")
                .save(output, DyeDepot.modLoc(dye + "_dye_from_smelting"));
    }

    private void disable(RecipeOutput output, ResourceLocation id) {
        Conditional.with(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DEBUG_STICK), False.INSTANCE)
                .requires(Items.DEBUG_STICK)
                .unlockedBy("never", has(Items.DEBUG_STICK))
                .save(output, id);
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed) {
        dyeing(output, category, dyed, Ingredient.of(dyed.values().map(ItemStack::new)), builder -> builder
                .unlockedBy("has_base", inventoryTrigger(ItemPredicate.Builder.item().of(dyed.values().toArray(ItemLike[]::new)).build()))
        );
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed, TagKey<Item> from) {
        dyeing(output, category, dyed, Ingredient.of(from), builder -> builder
                .unlockedBy(getHasName(from), has(from))
        );
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed, ItemLike from) {
        dyeing(output, category, dyed, Ingredient.of(from), builder -> builder
                .unlockedBy(getHasName(from), has(from))
        );
    }

    private void dyeing(RecipeOutput output, RecipeCategory category, DyedHolders<?, ? extends ItemLike> dyed, Ingredient from, UnaryOperator<ShapelessRecipeBuilder> factory) {
        var group = dyed.detectBaseName();
        dyed.forEach((color, item) -> {
            var id = item.unwrapKey().orElseThrow().location();
            var dye = dyes.getOrThrow(color);
            factory.apply(ShapelessRecipeBuilder.shapeless(category, item.value()))
                    .requires(from)
                    .requires(dye)
                    .group(group)
                    .unlockedBy(getHasName(dye), has(dye))
                    .save(output, id.withPrefix("dye_"));
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
        var builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, from.length)
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

        builder.save(output, DyeDepot.modLoc(name));
    }

    public static String getHasName(TagKey<?> tag) {
        return "has_" + tag.location().getPath();
    }

}

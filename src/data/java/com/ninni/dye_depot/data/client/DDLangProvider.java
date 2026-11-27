package com.ninni.dye_depot.data.client;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.data.ModCompat;
import com.ninni.dye_depot.registry.DDBlocks;
import com.ninni.dye_depot.registry.DDItems;
import com.ninni.dye_depot.registry.DyedHolders;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class DDLangProvider extends LanguageProvider implements TranslationBuilder {

    private final CompletableFuture<HolderLookup.Provider> lookup;

    protected DDLangProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, DyeDepot.MOD_ID, "en_us");
        this.lookup = lookup;
    }

    @Override
    public final void addTranslations() {
        var builder = this;

        var lookup = this.lookup.join();
        var itemLookup = lookup.lookupOrThrow(Registries.ITEM);
        var blockLookup = lookup.lookupOrThrow(Registries.BLOCK);

        colors("general").forEach(color -> {
            builder.add("color.minecraft." + color, translate(color));
            builder.add("item.minecraft.firework_star." + color, translate(color));
            builder.add("item.minecraft.shield." + color, translate(color) + " Shield");
        });

        colors("tags").forEach(color -> {
            var translation = translate(color);
            tag(builder, loaderTag(Registries.BLOCK, "dyed/" + color), translation + " Dyed Blocks");
            tag(builder, loaderTag(Registries.ITEM, "dyed/" + color), translation + " Dyed Items");
            tag(builder, loaderTag(Registries.ITEM, "dyes/" + color), translation + " Dyes");
        });

        dyedBannerPattern(builder, "base", color -> "Fully " + color + " Field");
        dyedBannerPattern(builder, "border", color -> color + " Bordure");
        dyedBannerPattern(builder, "bricks", color -> color + " Field Masoned");
        dyedBannerPattern(builder, "circle", color -> color + " Roundel");
        dyedBannerPattern(builder, "creeper", color -> color + " Creeper Charge");
        dyedBannerPattern(builder, "cross", color -> color + " Saltire");
        dyedBannerPattern(builder, "curly_border", color -> color + " Bordure Indented");
        dyedBannerPattern(builder, "diagonal_left", color -> color + " Per Bend Sinister");
        dyedBannerPattern(builder, "diagonal_right", color -> color + " Per Bend");
        dyedBannerPattern(builder, "diagonal_up_left", color -> color + " Per Bend Inverted");
        dyedBannerPattern(builder, "diagonal_up_right", color -> color + " Per Bend Sinister Inverted");
        dyedBannerPattern(builder, "flower", color -> color + " Flower Charge");
        dyedBannerPattern(builder, "globe", color -> color + " Globe");
        dyedBannerPattern(builder, "gradient_up", color -> color + " Base Gradient");
        dyedBannerPattern(builder, "gradient", color -> color + " Gradient");
        dyedBannerPattern(builder, "half_horizontal_bottom", color -> color + " Per Fess Inverted");
        dyedBannerPattern(builder, "half_horizontal", color -> color + " Per Fess");
        dyedBannerPattern(builder, "half_vertical_right", color -> color + " Per Pale Inverted");
        dyedBannerPattern(builder, "half_vertical", color -> color + " Per Pale");
        dyedBannerPattern(builder, "mojang", color -> color + " Thing");
        dyedBannerPattern(builder, "piglin", color -> color + " Snout");
        dyedBannerPattern(builder, "rhombus", color -> color + " Lozenge");
        dyedBannerPattern(builder, "skull", color -> color + " Skull Charge");
        dyedBannerPattern(builder, "small_stripes", color -> color + " Paly");
        dyedBannerPattern(builder, "square_bottom_left", color -> color + " Base Dexter Canton");
        dyedBannerPattern(builder, "square_bottom_right", color -> color + " Base Sinister Canton");
        dyedBannerPattern(builder, "square_top_left", color -> color + " Chief Dexter Canton");
        dyedBannerPattern(builder, "square_top_right", color -> color + " Chief Sinister Canton");
        dyedBannerPattern(builder, "straight_cross", color -> color + " Cross");
        dyedBannerPattern(builder, "stripe_bottom", color -> color + " Base");
        dyedBannerPattern(builder, "stripe_center", color -> color + " Pale");
        dyedBannerPattern(builder, "stripe_downleft", color -> color + " Bend Sinister");
        dyedBannerPattern(builder, "stripe_downright", color -> color + " Bend");
        dyedBannerPattern(builder, "stripe_left", color -> color + " Pale Dexter");
        dyedBannerPattern(builder, "stripe_middle", color -> color + " Fess");
        dyedBannerPattern(builder, "stripe_right", color -> color + " Pale Sinister");
        dyedBannerPattern(builder, "stripe_top", color -> color + " Chief");
        dyedBannerPattern(builder, "triangle_bottom", color -> color + " Chevron");
        dyedBannerPattern(builder, "triangle_top", color -> color + " Inverted Chevron");
        dyedBannerPattern(builder, "triangles_bottom", color -> color + " Base Indented");
        dyedBannerPattern(builder, "triangles_top", color -> color + " Chief Indented");
        dyedBannerPattern(builder, "flow", color -> color + " Flow");
        dyedBannerPattern(builder, "guster", color -> color + " Guster");

        dyed(builder, filterAndMerge(DDItems.DYES, itemLookup), "Dye");
        dyed(builder, filterAndMerge(DDBlocks.BANNERS, blockLookup), "Banner");
        dyed(builder, filterAndMerge(DDBlocks.BEDS, blockLookup), "Bed");
        dyed(builder, filterAndMerge(DDBlocks.CANDLES, blockLookup), "Candle");
        dyed(builder, filterAndMerge(DDBlocks.CANDLE_CAKES, blockLookup), it -> "Cake with " + it + " Candle");
        dyed(builder, filterAndMerge(DDBlocks.CARPETS, blockLookup), "Carpet");
        dyed(builder, filterAndMerge(DDBlocks.CONCRETE, blockLookup), "Concrete");
        dyed(builder, filterAndMerge(DDBlocks.CONCRETE_POWDER, blockLookup), "Concrete Powder");
        dyed(builder, filterAndMerge(DDBlocks.GLAZED_TERRACOTTA, blockLookup), "Glazed Terracotta");
        dyed(builder, filterAndMerge(DDBlocks.SHULKER_BOXES, blockLookup), "Shulker Box");
        dyed(builder, filterAndMerge(DDBlocks.STAINED_GLASS, blockLookup), "Stained Glass");
        dyed(builder, filterAndMerge(DDBlocks.STAINED_GLASS_PANES, blockLookup), "Stained Glass Pane");
        dyed(builder, filterAndMerge(DDBlocks.TERRACOTTA, blockLookup), "Terracotta");
        dyed(builder, filterAndMerge(DDBlocks.WOOL, blockLookup), "Wool");
        dyed(builder, filter(DDBlocks.DYE_BASKETS), "Dye Basket");
        dyed(builder, filterAndMerge(DDItems.BUNDLES, itemLookup), "Bundle");

        // TODO waiting for supplementaries
        //dyed(builder, supplementariesHolders(blockLookup, "present"), "Present");
        //dyed(builder, supplementariesHolders(blockLookup, "trapped_present"), "Trapped Present");
        //dyed(builder, supplementariesHolders(blockLookup, "flag"), "Flag");
        //dyed(builder, supplementariesHolders(blockLookup, "candle_holder"), "Candle Holder");
        //dyed(builder, supplementariesSquaredHolders(blockLookup, "gold_candle_holder"), it -> "Gold " + it + " Candle Holder");

        translateAdditional(builder);
    }

    protected void translateAdditional(TranslationBuilder builder) {
    }

    protected abstract Stream<DyeColor> colors(String group);

    protected abstract String translate(DyeColor dye);

    private <R> DyedHolders<R, R> supplementariesHolders(HolderLookup.RegistryLookup<R> registry, String name) {
        return ModCompat.supplementariesHolders(registry, name, colors(name));
    }

    private <R> DyedHolders<R, R> supplementariesSquaredHolders(HolderLookup.RegistryLookup<R> registry, String name) {
        return ModCompat.supplementariesSquaredHolders(registry, name, colors(name));
    }

    private <T extends R, R> DyedHolders<T, R> filterAndMerge(DyedHolders<T, R> dyed, HolderLookup.RegistryLookup<R> registry) {
        return filter(dyed.mergeVanilla(registry));
    }

    private <T extends R, R> DyedHolders<T, R> filter(DyedHolders<T, R> dyed) {
        var baseName = dyed.detectBaseName();
        return dyed.filter(() -> colors(baseName));
    }

    private void dyed(TranslationBuilder builder, DyedHolders<?, ? extends ItemLike> dyed, String suffix) {
        dyed(builder, dyed, it -> it + " " + suffix);
    }

    private void dyed(TranslationBuilder builder, DyedHolders<?, ? extends ItemLike> dyed, Function<String, String> translation) {
        dyed.forEach((color, item) ->
                add(builder, item.value(), translation.apply(translate(color)))
        );
    }

    private void tag(TranslationBuilder builder, TagKey<?> tag, String translation) {
        builder.add(tag, translation);
    }

    private void add(TranslationBuilder builder, ItemLike item, String translation) {
        if (item instanceof Block block) builder.add(block, translation);
        else builder.add(item.asItem(), translation);
    }

    private void dyedBannerPattern(TranslationBuilder builder, String id, Function<String, String> translation) {
        colors("banner_patterns").forEach(color ->
                builder.add("block.minecraft.banner." + id + "." + color, translation.apply(translate(color)))
        );
    }

    private <T> TagKey<T> loaderTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, Identifier.fromNamespaceAndPath("c", path));
    }

}

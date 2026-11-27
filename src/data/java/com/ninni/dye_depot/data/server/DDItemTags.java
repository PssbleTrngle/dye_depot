package com.ninni.dye_depot.data.server;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.data.ModCompat;
import com.ninni.dye_depot.registry.DDBlocks;
import com.ninni.dye_depot.registry.DDItems;
import com.ninni.dye_depot.registry.DDTags;
import com.ninni.dye_depot.registry.DyedHolders;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

public class DDItemTags extends ItemTagsProvider {

    private final CompletableFuture<HolderLookup.Provider> lookup;

    public DDItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookup, DyeDepot.MOD_ID);
        this.lookup = lookup;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var lookup = this.lookup.join();
        var itemLookup = lookup.lookupOrThrow(Registries.ITEM);

        groupDyedTag("dyed");
        groupDyedTag("dyes");

        tagDyed(DDItems.DYES, "dyes");

        tagDyed(DDBlocks.SHULKER_BOXES, ItemTags.SHULKER_BOXES);
        tagDyed(DDBlocks.BANNERS, ItemTags.BANNERS);
        tagDyed(DDBlocks.CARPETS, ItemTags.WOOL_CARPETS);
        tagDyed(DDBlocks.CANDLES, ItemTags.CANDLES);
        tagDyed(DDBlocks.BEDS, ItemTags.BEDS);
        tagDyed(DDBlocks.WOOL, ItemTags.WOOL);
        tagDyed(DDBlocks.TERRACOTTA, ItemTags.TERRACOTTA);
        tagDyed(DDBlocks.GLAZED_TERRACOTTA);
        tagDyed(DDBlocks.CONCRETE, loaderTag("concretes"));
        tagDyed(DDBlocks.CONCRETE_POWDER, loaderTag("concrete_powders"));
        tagDyed(DDBlocks.STAINED_GLASS, loaderTag("glass_blocks"));
        tagDyed(DDBlocks.STAINED_GLASS_PANES, loaderTag("glass_panes"));

        // TODO waiting for supplementaries
        tagDyed(DDBlocks.DYE_BASKETS /*, ModTags.SOAP_BLACKLIST_ITEM */);

        // tagDyed(ModCompat.supplementariesHolders(itemLookup, "candle_holder"), supplementariesTag("candle_holders"));
        // tagDyed(ModCompat.supplementariesSquaredHolders(itemLookup, "gold_candle_holder"), supplementariesTag("candle_holders"), ItemTags.PIGLIN_LOVED);
        // tagDyed(ModCompat.supplementariesHolders(itemLookup, "flag"), supplementariesTag("flags"));
        // tagDyed(ModCompat.supplementariesHolders(itemLookup, "present"), supplementariesTag("presents"));
        // tagDyed(ModCompat.supplementariesHolders(itemLookup, "trapped_present"), supplementariesTag("trapped_presents"));

        tag(DDTags.SMELTS_INTO_CORAL_DYE).add(
                Items.TUBE_CORAL,
                Items.BRAIN_CORAL,
                Items.BUBBLE_CORAL,
                Items.FIRE_CORAL,
                Items.HORN_CORAL,
                Items.TUBE_CORAL_FAN,
                Items.BRAIN_CORAL_FAN,
                Items.BUBBLE_CORAL_FAN,
                Items.FIRE_CORAL_FAN,
                Items.HORN_CORAL_FAN,
                Items.TUBE_CORAL_BLOCK,
                Items.BRAIN_CORAL_BLOCK,
                Items.BUBBLE_CORAL_BLOCK,
                Items.FIRE_CORAL_BLOCK,
                Items.HORN_CORAL_BLOCK
        );
    }

    private void tag(DyedHolders<?, ? extends ItemLike> values, TagKey<Item> tag) {
        values.holders()
                .map(it -> it.value().asItem())
                .forEach(it -> tag(tag).addOptional(it));
    }

    private void groupDyedTag(String base) {
        Stream.concat(DyedHolders.vanillaColors(), DyedHolders.modColors()).forEach(color ->
            tag(loaderTag(base)).addOptionalTag(loaderTag(base + "/" + color))
        );
    }

    @SafeVarargs
    private void tagDyed(DyedHolders<?, ? extends ItemLike> values, TagKey<Item>... additionalTags) {
        tagDyed(values, "dyed", additionalTags);
    }

    @SafeVarargs
    private void tagDyed(DyedHolders<?, ? extends ItemLike> values, String base, TagKey<Item>... additionalTags) {
        values.forEach((dye, item) -> {
            var tag = loaderTag(base + "/" + dye);
            tag(tag).addOptional(item.value().asItem());
        });

        for (var tag : additionalTags) {
            tag(values, tag);
        }
    }

    private TagKey<Item> loaderTag(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }

    private TagKey<Item> supplementariesTag(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ModCompat.SUPPLEMENTARIES, path));
    }

}

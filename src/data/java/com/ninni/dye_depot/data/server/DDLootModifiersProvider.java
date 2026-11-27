package com.ninni.dye_depot.data.server;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.loot.InjectTableModifier;
import com.ninni.dye_depot.loot.ReplaceDropsModifier;
import com.ninni.dye_depot.registry.DDBlocks;
import com.ninni.dye_depot.registry.DDDyes;
import com.ninni.dye_depot.registry.DDItems;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

public class DDLootModifiersProvider extends GlobalLootModifierProvider {

    public DDLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, DyeDepot.MOD_ID);
    }

    @Override
    protected void start() {
        add("modify_shepherd_gift", ReplaceDropsModifier.forTable(
                BuiltInLootTables.SHEPHERD_GIFT,
                0.1F,
                createWoolList()
        ));

        add("modify_desert_pyramid", ReplaceDropsModifier.forTable(
                BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
                0.1F,
                WeightedList.of(new ItemStackTemplate(DDItems.DYES.getOrThrow(DDDyes.BEIGE.get())))
        ));

        add("modify_ocean_ruin_cold", ReplaceDropsModifier.forTable(
                BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY,
                0.1F,
                WeightedList.of(new ItemStackTemplate(DDItems.DYES.getOrThrow(DDDyes.VERDANT.get())))
        ));

        for (var dye : DDDyes.values()) {
            add("modify_sheep_shearing/" + dye.getSerializedName(), InjectTableModifier.forSheep(dye.get()));
        }
    }

    private WeightedList<ItemStackTemplate> createWoolList() {
        var builder = WeightedList.<ItemStackTemplate>builder();
        DDBlocks.WOOL.values()
                .map(Block::asItem)
                .map(ItemStackTemplate::new)
                .forEach(it -> builder.add(it, 1));
        return builder.build();
    }

}

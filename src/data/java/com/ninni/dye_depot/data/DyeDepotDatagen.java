package com.ninni.dye_depot.data;

import com.ninni.dye_depot.DyeDepot;
import com.ninni.dye_depot.data.client.*;
import com.ninni.dye_depot.data.server.*;
import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = DyeDepot.MOD_ID)
public class DyeDepotDatagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var lookup = event.getLookupProvider();

        generator.addProvider(true, new DDPackMetadata(output, Component.literal(DyeDepot.MOD_ID + " resources")));

        var blockTags = generator.addProvider(true, new DDBlockTags(output, lookup));
        generator.addProvider(true, new DDItemTags(output, lookup, blockTags.contentsGetter()));
        generator.addProvider(true, new DDPoiTags(output, lookup));
        generator.addProvider(true, new DDLoot(output, lookup));
        generator.addProvider(true, new DDRecipes.Runner(output, lookup));
        generator.addProvider(true, new DDLootModifiersProvider(output, lookup));

        generator.addProvider(true, new DDModels(output, lookup));
        generator.addProvider(true, new DDLang(output, lookup));
        generator.addProvider(true, new DDTrades(output, lookup));
        generator.addProvider(true, new DDEquipmentAssets(output));

        var supplementariesPack = generator.getBuiltinDatapack(true, "dye_override");
        var supplementariesOutput = new PackOutput(output.getOutputFolder().resolve("resourcepacks/dye_override"));
        supplementariesPack.addProvider($ -> new DDPackMetadata(supplementariesOutput, Component.literal("Slight dye adjustments").withStyle(ChatFormatting.GRAY)));
        supplementariesPack.addProvider($ -> new DDLangOverrides(supplementariesOutput, lookup));
    }

}

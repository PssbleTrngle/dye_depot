package com.ninni.dye_depot;

import com.ninni.dye_depot.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(DyeDepot.MOD_ID)
public class DyeDepot {
    public static final String MOD_ID = "dye_depot";

    public static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static <T> ResourceKey<T> key(ResourceKey<Registry<T>> registry, String path) {
        return ResourceKey.create(registry, modLoc(path));
    }

    public DyeDepot(IEventBus modBus) {
        DDItems.register(modBus);
        DDBlocks.register(modBus);
        DDBlockEntityType.register(modBus);
        DDParticles.register(modBus);
        DDSoundEvents.register(modBus);
        DDMapDecorationType.register(modBus);
        DDLootModifiers.register(modBus);
        DDPoiTypes.register(modBus);

        modBus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        DDVanillaIntegration.commonInit();
        DDFlammables.register();
        DDBlocks.SHULKER_BOXES.forEach((dye, shulkerBox) ->
                DispenserBlock.registerBehavior(shulkerBox.value(), new ShulkerBoxDispenseBehavior())
        );
    }

}

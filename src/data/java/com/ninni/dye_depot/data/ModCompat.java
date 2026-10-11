package com.ninni.dye_depot.data;

import com.google.gson.JsonObject;
import com.ninni.dye_depot.registry.DyedHolders;
import com.possible_triangle.multikulti.datagen.conditions.Condition;
import com.possible_triangle.multikulti.datagen.conditions.Conditional;
import com.possible_triangle.multikulti.datagen.conditions.ModLoaded;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class ModCompat {

    public static final String SUPPLEMENTARIES = "supplementaries";
    public static final String SUPPLEMENTARIES_SQUARED = "suppsquared";

    public static <T> DyedHolders<T, T> supplementariesHolders(HolderLookup.RegistryLookup<T> registry, String name) {
        return supplementariesHolders(registry, name, DyedHolders.modColors());
    }

    public static <T> DyedHolders<T, T> supplementariesHolders(HolderLookup.RegistryLookup<T> registry, String name, Stream<DyeColor> colors) {
        return DyedHolders.fromRegistry(registry, colors, color -> ResourceLocation.fromNamespaceAndPath(SUPPLEMENTARIES, name + "_" + color));
    }

    public static <T> DyedHolders<T, T> supplementariesSquaredHolders(HolderLookup.RegistryLookup<T> registry, String name) {
        return supplementariesSquaredHolders(registry, name, DyedHolders.modColors());
    }

    public static <T> DyedHolders<T, T> supplementariesSquaredHolders(HolderLookup.RegistryLookup<T> registry, String name, Stream<DyeColor> colors) {
        return DyedHolders.fromRegistry(registry, colors, color -> ResourceLocation.fromNamespaceAndPath(SUPPLEMENTARIES_SQUARED, name + "_" + color));
    }

    public static <T> T withSupplementariesFlag(T value, String modId, String flag) {
        return Conditional.with(value, supplementariesFlag(modId, flag));
    }

    public static <T> T withSupplementariesFlag(T value, String modId, String flag, Runnable runnable) {
        return Conditional.with(value, supplementariesFlag(modId, flag), runnable);
    }

    public static Condition[] supplementariesFlag(String modId, String flag) {
        return new Condition[]{
                new ModLoaded(modId),
                new SupplementariesFlag(flag)
        };
    }

    private record SupplementariesFlag(String flag) implements Condition {
        private static final ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath(SUPPLEMENTARIES, "flag");

        @Override
        public void toForge(JsonObject json) {
            throw new RuntimeException("not implemented to forge");
        }

        @Override
        public void toFabric(JsonObject json) {
            json.addProperty("condition", TYPE.toString());
            json.addProperty("flag", flag);
        }
    }

}

package com.ninni.dye_depot.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ninni.dye_depot.DyeDepot;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Optional;

import net.minecraft.advancements.criterion.*;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

public class InjectTableModifier extends LootModifier {

    public static MapCodec<InjectTableModifier> CODEC = RecordCodecBuilder.mapCodec(builder ->
            codecStart(builder)
                    .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table_id").forGetter(it -> it.table))
                    .apply(builder, InjectTableModifier::new)
    );

    private final ResourceKey<LootTable> table;

    public InjectTableModifier(LootItemCondition[] conditions, int priority, ResourceKey<LootTable> table) {
        super(conditions, priority);
        this.table = table;
    }

    public static InjectTableModifier forSheep(DyeColor dye) {
        var table = ResourceKey.create(Registries.LOOT_TABLE, DyeDepot.modLoc("shearing/sheep/" + dye));
        return new InjectTableModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(BuiltInLootTables.SHEAR_SHEEP.identifier()).build(),
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                                .components(DataComponentMatchers.Builder.components()
                                        .exact(DataComponentExactPredicate.builder()
                                                .expect(DataComponents.SHEEP_COLOR, dye)
                                                .build())
                                        .build()
                                )
                                .subPredicate(new SheepPredicate(Optional.of(false)))
                ).build()
        }, 0, table);
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> stacks, LootContext context) {
        context.getResolver().get(table).ifPresent(it -> {
            it.value().getRandomItemsRaw(context, stacks::add);
        });

        return stacks;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}

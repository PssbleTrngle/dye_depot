package com.ninni.dye_depot.client.particles;

import com.ninni.dye_depot.block.DyeBasketBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PoofParticleProvider implements ParticleProvider<BlockParticleOption> {
    private final SpriteSet sprite;

    public PoofParticleProvider(SpriteSet spriteSet) {
        this.sprite = spriteSet;
    }

    @Override
    @Nullable
    public Particle createParticle(BlockParticleOption particleOptions, ClientLevel level, double x, double y, double z, double g, double h, double i, RandomSource random) {
        BlockState blockState = particleOptions.getState();
        if (!(blockState.getBlock() instanceof DyeBasketBlock block)) return null;
        var color = getColor(block);
        return new PoofParticle(level, x, y, z, ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), this.sprite);
    }

    private static int getColor(DyeBasketBlock block) {
        var dye = block.getDyeColor();

        if (dye == DyeColor.BLUE) return 0x345eb6;
        if (dye == DyeColor.GREEN) return 0x5b8221;
        if (dye == DyeColor.MAGENTA) return 0xcb69c5;
        if (dye == DyeColor.CYAN) return 0x29a9b0;
        if (dye == DyeColor.RED) return 0xb53c39;
        if (dye == DyeColor.ORANGE) return 0xd7710a;

        return dye.getTextColor();
    }
}

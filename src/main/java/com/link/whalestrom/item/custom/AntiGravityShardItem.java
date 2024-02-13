package com.link.whalestrom.item.custom;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class AntiGravityShardItem extends Item {
    public AntiGravityShardItem(Settings settings) {
        super(new FabricItemSettings().maxCount(1).fireproof().rarity(Rarity.UNCOMMON).food(new FoodComponent.Builder().alwaysEdible()
                .statusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20, 0), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 120, 12), 1f)
                .build()));
    }
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_SHULKER_SHOOT;
    }
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.whalestrom.antigravity_shard.tooltip").formatted(Formatting.DARK_PURPLE));
    }
}

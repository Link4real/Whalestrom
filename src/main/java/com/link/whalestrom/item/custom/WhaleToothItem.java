package com.link.whalestrom.item.custom;

import com.link.whalestrom.entity.custom.ToothProjectileEntity;
import com.link.whalestrom.sounds.ModSounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WhaleToothItem extends Item {
    public WhaleToothItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.WHALE_TOOTH_THROWN, SoundCategory.NEUTRAL, 0.3f, 1f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 10);
        if (!world.isClient) {
            ToothProjectileEntity toothProjectile = new ToothProjectileEntity(user, world);
            toothProjectile.setItem(itemStack);
            toothProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2f, 1.5f);
            world.spawnEntity(toothProjectile);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.whalestrom.whale_tooth.tooltip").formatted(Formatting.ITALIC));
    }
}

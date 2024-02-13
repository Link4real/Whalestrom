package com.link.whalestrom.item.custom;

import com.link.whalestrom.sounds.ModSounds;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CetaceanStaffItem extends Item {
    public CetaceanStaffItem(Settings settings) {
        super(new FabricItemSettings().maxCount(1).fireproof().rarity(Rarity.UNCOMMON));
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemstack = user.getStackInHand(hand);
     //   world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.CETACEAN_STAFF_SHOOT, SoundCategory.NEUTRAL,1.5F, 1F);
        user.getItemCooldownManager().set(this, 40);

        if (!world.isClient()) {
            ShulkerBulletEntity bullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, world);
            bullet.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0.25F);
            world.spawnEntity(bullet);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemstack.damage(1, user, p -> p.sendToolBreakStatus(hand));

        }
        return TypedActionResult.success(itemstack, world.isClient());
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.whalestrom.cetacean_staff.tooltip").formatted(Formatting.GOLD));
    }
}
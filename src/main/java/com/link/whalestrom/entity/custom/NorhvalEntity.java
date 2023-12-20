package com.link.whalestrom.entity.custom;

import com.link.whalestrom.Whalestrom;
import com.link.whalestrom.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;

public class NorhvalEntity extends FlyingEntity {
    public NorhvalEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 30;
        this.moveControl = new NorhvalMoveControl(this);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FlyRandomlyGoal(this));
        //this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.COD), false));
        this.goalSelector.add(1, new LookAtDirectionGoal(this));
    }

    public static DefaultAttributeContainer.Builder createNorhvalAttributes() {
     return MobEntity.createMobAttributes()
             .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
             .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.2f)
             .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0)
             .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);

    }
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.COD);
    }

    // Norhval flies randomly through the air:
    static class FlyRandomlyGoal  extends Goal {
        private final NorhvalEntity norhval;
        public FlyRandomlyGoal(NorhvalEntity norhval) {
            this.norhval = norhval;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }
        @Override
        public boolean canStart() {
            double f;
            double e;
            MoveControl moveControl = this.norhval.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            }
            double d = moveControl.getTargetX() - this.norhval.getX();
            double g = d * d + (e = moveControl.getTargetY() - this.norhval.getY()) * e + (f = moveControl.getTargetZ() - this.norhval.getZ()) * f;
            return g < 1.0 || g > 3600.0;
        }
        @Override
        public boolean shouldContinue() {
            return false;
        }
            @Override
        public void start() {
            Random random = this.norhval.getRandom();
            double d = this.norhval.getX() + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double e = this.norhval.getY() + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double f = this.norhval.getZ() + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            this.norhval.getMoveControl().moveTo(d, e, f, 1.0);
        }
    }


    static class NorhvalMoveControl extends MoveControl {
        private final NorhvalEntity norhval;
        private int collisionCheckCooldown;
        public NorhvalMoveControl(NorhvalEntity norhval) {
            super(norhval);
            this.norhval = norhval;
        }
        @Override
        public void tick() {
            if (this.state != MoveControl.State.MOVE_TO) {
                return;
            }
            if (this.collisionCheckCooldown-- <= 0) {
                this.collisionCheckCooldown += this.norhval.getRandom().nextInt(5) + 2;
                Vec3d vec3d = new Vec3d(this.targetX - this.norhval.getX(), this.targetY - this.norhval.getY(), this.targetZ - this.norhval.getZ());
                double d = vec3d.length();
                if (this.willCollide(vec3d = vec3d.normalize(), MathHelper.ceil(d))) {
                    this.norhval.setVelocity(this.norhval.getVelocity().add(vec3d.multiply(0.1)));
                } else {
                    this.state = MoveControl.State.WAIT;
                }
            }
        }
        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.norhval.getBoundingBox();
            for (int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (this.norhval.getWorld().isSpaceEmpty(this.norhval, box)) continue;
                return false;
            }
            return true;
        }
    }


    private static class LookAtDirectionGoal extends Goal {
        private final NorhvalEntity norhval;
        public LookAtDirectionGoal(NorhvalEntity norhval) {
            this.norhval = norhval;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }
        @Override
        public boolean canStart() {
            return true;
        }
        @Override
        public void tick() {
            Vec3d vec3d = this.norhval.getVelocity();
            this.norhval.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F);
            this.norhval.bodyYaw = this.norhval.getYaw();
        }
    }


    public NorhvalEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.NORHVAL.create(world);
    }
    public static void registerModEntities() {
        Whalestrom.LOGGER.info("Registering Entities for " + Whalestrom.MOD_ID);
    }
}

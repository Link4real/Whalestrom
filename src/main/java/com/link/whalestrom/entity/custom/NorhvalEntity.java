package com.link.whalestrom.entity.custom;

import com.link.whalestrom.Whalestrom;
import com.link.whalestrom.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class NorhvalEntity extends FlyingEntity {
    public static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.COD);
    public NorhvalEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
        this.setStepHeight(1.0f);
        this.experiencePoints = 30;
        this.moveControl = new NorhvalMoveControl(this);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FlyRandomlyGoal(this));
        //this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.COD), false));
        this.goalSelector.add(1, new LookAtDirectionGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
    }

    public static DefaultAttributeContainer.Builder createNorhvalAttributes() {
     return MobEntity.createMobAttributes()
             .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
             .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1f)
             .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0)
             //.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
        ;

    }

    public static boolean canSpawn(EntityType<NorhvalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockState blockState = world.getBlockState(pos);
        return random.nextInt(6) == 0 && pos.getY() > 40 && pos.getY() - world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos).getY() > 20 && blockState.isAir()
                && world.getEntitiesByClass(PhantomEntity.class, new Box(pos).expand(80D), EntityPredicates.EXCEPT_SPECTATOR).isEmpty()
                && SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), ModEntities.NORHVAL);
    }
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }
    @Override
    public void tick() {
        super.tick();
        if(this.getWorld().isClient()) {
            updateAnimations();
        }
    }

    //Animations:
    @Override
    protected void updateLimbs(float posDelta) {
        float f = this.getPose() == EntityPose.STANDING ? Math.min(posDelta * 6.0f, 1.0f): 0.0f;
        this.limbAnimator.updateLimbs(f, 0.2f);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private void updateAnimations() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
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
                MoveControl moveControl = this.norhval.getMoveControl();
                if (!moveControl.isMoving()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean shouldContinue() {
                return false;
            }

            @Override
            public void start() {
                Random random = this.norhval.getRandom();
                double randomX = 48D + (random.nextDouble() * 2.0D - 1.0D) * 128.0D;
                double randomZ = 48D + (random.nextDouble() * 2.0D - 1.0D) * 128.0D;

                double d = this.norhval.getX() + randomX;
                double e = this.norhval.getY() + (double) ((random.nextDouble() * 2.0F - 1.0F) * 16.0F);
                double f = this.norhval.getZ() + randomZ;

                if (this.norhval.getY() < 40D)
                    e = this.norhval.getY() + 16D * random.nextDouble();
                else if (this.norhval.getY() < 65D)
                    e = this.norhval.getY() + 40D * random.nextDouble();
                else if (this.norhval.getY() > 300D)
                    e = this.norhval.getY() - 26D * random.nextDouble();

                this.norhval.getMoveControl().moveTo(d, e, f, 1.0D);
            }
        }


    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }
    private static class NorhvalMoveControl extends MoveControl {
        private final NorhvalEntity norhval;
        private int collisionCheckCooldown;
        private int waitCooldown;

        public NorhvalMoveControl(NorhvalEntity norhval) {
            super(norhval);
            this.norhval = norhval;
        }

        @Override
        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += 3;
                    Vec3d vec3d = new Vec3d(this.targetX - this.norhval.getX(), this.targetY - this.norhval.getY(), this.targetZ - this.norhval.getZ());
                    double d = Math.sqrt(vec3d.x * vec3d.x + vec3d.y * vec3d.y + vec3d.z * vec3d.z);
                    vec3d = new Vec3d(vec3d.x / d, vec3d.y / d, vec3d.z / d);
                    if ((!this.willCollide() && d > 1) || (this.waitCooldown++ < 0 && d > 1)) {
                        this.norhval.setVelocity(vec3d.multiply(0.12));
                    } else {
                        this.state = MoveControl.State.WAIT;
                        if (this.waitCooldown++ >= 200) {
                            this.waitCooldown = -40;
                        }
                    }
                }
            }
        }

        private boolean willCollide() {
            Box box = this.norhval.getBoundingBox();
            box = box.expand(0.2D);
            if (this.norhval.getWorld().isSpaceEmpty(this.norhval, box)) {
                return false;
            }
            return true;
        }
    }
    @Override
    public boolean isPushable() {
        return true;
    }
    //Norhval looks at direction, it is flying towards
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

    //Sounds
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_DOLPHIN_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_DOLPHIN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_DOLPHIN_DEATH;
    }

    public NorhvalEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.NORHVAL.create(world);
    }
    public static void registerModEntities() {
        Whalestrom.LOGGER.info("Registering Entities for " + Whalestrom.MOD_ID);
    }
}

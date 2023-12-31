package com.link.whalestrom.entity.custom;

import com.link.whalestrom.Whalestrom;
import com.link.whalestrom.entity.ModEntities;
import com.link.whalestrom.init.KeybindsInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;


public class NorhvalEntity extends TameableEntity implements Mount{
    public int keyBind = 342;
    public static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.COD);
    public NorhvalEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setStepHeight(1.0f);
        this.experiencePoints = 30;
        this.moveControl = new NorhvalMoveControl(this);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new SitGoal(this));
        this.goalSelector.add(1, new FlyRandomlyGoal(this));
        this.goalSelector.add(1, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.COD), false));
        this.goalSelector.add(3, new LookAtDirectionGoal(this));
        this.goalSelector.add(2, new FollowParentGoal(this, 0.1D));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.1D, 20f, 5f, false));
    }


    public static DefaultAttributeContainer.Builder createNorhvalAttributes() {
     return MobEntity.createMobAttributes()
             .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
             .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1f)
             .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0)
             .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
             .add(EntityAttributes.HORSE_JUMP_STRENGTH, 0.3f);
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

    public final AnimationState sitAnimationState = new AnimationState();
    private void updateAnimations() { //setupAnimationStates
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 60;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
        if (isInSittingPose()) {
            sitAnimationState.startIfNotRunning(this.age);
        } else {
            sitAnimationState.stop();
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
                return !moveControl.isMoving();
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

                if (this.norhval.getY() < 68D)
                    e = this.norhval.getY() + 16D * random.nextDouble();
                else if (this.norhval.getY() < 85D)
                    e = this.norhval.getY() + 40D * random.nextDouble();
                else if (this.norhval.getY() > 300)
                    e = this.norhval.getY() - 26D * random.nextDouble();

                this.norhval.getMoveControl().moveTo(d, e, f, 1.0D);
            }
        }
    @Override
    public boolean shouldDismountUnderwater() {
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
            return !this.norhval.getWorld().isSpaceEmpty(this.norhval, box);
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

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    //Tameable Entity
    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.COD;

        if(item == itemForTaming && !isTamed()) {
            if(this.getWorld().isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }
                super.setOwner(player);
                this.navigation.recalculatePath();
                this.setTarget(null);
                this.getWorld().sendEntityStatus(this, (byte)7);
                setSitting(true);
                setInSittingPose(true);

                return ActionResult.SUCCESS;
            }

        }
        if(isTamed() && hand == Hand.MAIN_HAND && item != itemForTaming) {
            if (!player.isSneaking()  && !this.isBaby()) {
                setRiding(player);
            }
            if (player.isSneaking()) {
                boolean sitting = !isSitting();
                setSitting(sitting);
                setInSittingPose(sitting);
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }
    @Override
    public boolean hasNoGravity() {
        return true;
    }
    @Environment(EnvType.CLIENT)
    public static void flyWhaleDown(ClientPlayerEntity player, String keyString) {
        if (player.getVehicle() != null && player.getVehicle() instanceof NorhvalEntity)
            ((NorhvalEntity) player.getVehicle()).setKeyBind(keyString);
    }
    public void setKeyBind(String key) {
        this.keyBind = InputUtil.fromTranslationKey(key).getCode();
    }
    //Flying Entity and Rideable Entity
    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }
    private boolean canBeControlledByRider() {
        return this.getControllingPassenger() != null;
    }
    @Override
    public void travel(Vec3d movementInput) {
        if(this.hasPassengers() && getControllingPassenger() instanceof PlayerEntity) {
            LivingEntity livingentity = this.getControllingPassenger();
            this.setYaw(livingentity.getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(livingentity.getPitch() * 0.5F);
            this.setRotation(this.getYaw(), this.getPitch());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;
            float f = livingentity.sidewaysSpeed * 0.75F;
            float f1 = livingentity.forwardSpeed;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }
            if (this.isLogicalSideForUpdatingMovement()) {
                float newSpeed = (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                // Sprint Key -> Faster Speed; If Left, Right, Down is pressed -> slower speed
                if (MinecraftClient.getInstance().options.sprintKey.isPressed()) {
                    if (!MinecraftClient.getInstance().options.leftKey.isPressed() ^
                            !MinecraftClient.getInstance().options.rightKey.isPressed() ^
                            !KeybindsInit.flyDown.isPressed()) {
                        newSpeed *= 1.75;
                    } else {
                        newSpeed *= 0.5;
                    }
                }
                if (MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                    if(!KeybindsInit.flyDown.isPressed()) {
                        this.jump();
                    } else {
                        this.setVelocity(this.getVelocity().multiply(0,0.85,0));
                    }
                }
                if (MinecraftClient.getInstance().options.forwardKey.isPressed()) {
                    newSpeed *= 1.75F;
                    if (KeybindsInit.flyDown.isPressed()) {
                        newSpeed *= 1.35F;
                    }
                }
                if (MinecraftClient.getInstance().options.leftKey.isPressed()) {

                    this.sidewaysSpeed = 2;
                }
                if (MinecraftClient.getInstance().options.rightKey.isPressed()) {
                    this.sidewaysSpeed = 2;
                }
                if (KeybindsInit.flyDown.isPressed()) {
                        this.updateVelocity(0.0f, movementInput);
                        this.move(MovementType.SELF, this.getVelocity());
                        this.setVelocity(this.getVelocity().add(0, -0.008f, 0));
                }
                if (this.isTouchingWater()) {
                    this.updateVelocity(0.02f, movementInput);
                    this.move(MovementType.SELF, this.getVelocity());
                    this.setVelocity(this.getVelocity().multiply(0.8f));
                } else if (this.isInLava()) {
                    this.updateVelocity(0.02f, movementInput);
                    this.move(MovementType.SELF, this.getVelocity());
                    this.setVelocity(this.getVelocity().multiply(0.5));
                }
                this.setMovementSpeed(newSpeed);
                super.travel(new Vec3d(f, movementInput.y, f1));
            }
        } else {
            super.travel(movementInput);
        }
        this.updateLimbs(false);
    }
    @Override
    public boolean isClimbing() {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return (LivingEntity) this.getFirstPassenger();
    }
    private void setRiding(PlayerEntity pPlayer) {
        this.setInSittingPose(false);

        pPlayer.setYaw(this.getYaw());
        pPlayer.setPitch(this.getPitch());
        pPlayer.startRiding(this);
    }
    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(direction);
        BlockPos blockPos = this.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (EntityPose entityPose : passenger.getPoses()) {
            Box box = passenger.getBoundingBox(entityPose);
            for (int[] js : is) {
                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                double d = this.getWorld().getDismountHeight(mutable);
                if (!Dismounting.canDismountInBlock(d)) continue;
                Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                if (!Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) continue;
                passenger.setPose(entityPose);
                return vec3d;
            }
        }
        return super.updatePassengerForDismount(passenger);
    }
    /*   @Override
      protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
          if (!this.hasPassenger(passenger)) {
              return;
          }
          float offSet = 12F;
          if (passenger.equals(this.getFirstPassenger())) {
              offSet = 1F;
          }
          float f = MathHelper.sin(this.bodyYaw * 0.0027453292F) * offSet;
          float g = MathHelper.cos(this.bodyYaw * 0.0027453292F) * offSet;

          positionUpdater.accept(passenger, this.getX() + (double) (0.1F * f), this.getBodyY(1F), this.getZ() - (double) (0.1F * g));
      }
  */


    public static boolean canSpawn(EntityType<NorhvalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockState blockState = world.getBlockState(pos);
        return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY() > 65 && blockState.isAir()
                && SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), ModEntities.NORHVAL);
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

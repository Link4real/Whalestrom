package com.link.whalestrom.entity.ai;

import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;

import java.util.EnumSet;

public class WhaleSitGoal extends Goal {
    private final TameableEntity whale;

    public WhaleSitGoal(NorhvalEntity whale) {
        this.whale = whale;
        this.setControls(EnumSet.of(Control.JUMP, Goal.Control.MOVE));
    }

    @Override
    public boolean shouldContinue() {
        return this.whale.isInSittingPose();
    }

    @Override
    public boolean canStart() {
        if (!this.whale.isTamed()) {
            return false;
        }
        if (this.whale.isInsideWaterOrBubbleColumn()) {
            return false;
        }
        LivingEntity livingEntity = this.whale.getOwner();
        if (livingEntity == null && !this.whale.isOnGround() || this.whale.isOnGround()) {
            return true;
        }
        if (this.whale.squaredDistanceTo(livingEntity) < 144.0) {
            assert livingEntity != null;
            if (livingEntity.getAttacker() != null) {
                return false;
            }
        }
        return this.whale.isSitting();
    }

    @Override
    public void start() {
        this.whale.getNavigation().stop();
        this.whale.setSitting(true);
    }

    @Override
    public void stop() {
        this.whale.getNavigation();
        this.whale.setSitting(false);
    }
}

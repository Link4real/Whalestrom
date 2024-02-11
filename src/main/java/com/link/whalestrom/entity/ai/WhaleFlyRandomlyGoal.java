package com.link.whalestrom.entity.ai;

import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;

public class WhaleFlyRandomlyGoal extends Goal {
    private final NorhvalEntity whale;
    public WhaleFlyRandomlyGoal(NorhvalEntity whale) {
        this.whale = whale;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        MoveControl moveControl = this.whale.getMoveControl();
        return !moveControl.isMoving();
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        Random random = this.whale.getRandom();
        double randomX = 48D + (random.nextDouble() * 2.0D - 1.0D) * 128.0D;
        double randomZ = 48D + (random.nextDouble() * 2.0D - 1.0D) * 128.0D;

        double d = this.whale.getX() + randomX;
        double e = this.whale.getY() + (double) ((random.nextDouble() * 2.0F - 1.0F) * 16.0F);
        double f = this.whale.getZ() + randomZ;

        if (this.whale.getY() < 68D)
            e = this.whale.getY() + 16D * random.nextDouble();
        else if (this.whale.getY() < 85D)
            e = this.whale.getY() + 40D * random.nextDouble();
        else if (this.whale.getY() > 300)
            e = this.whale.getY() - 26D * random.nextDouble();

        this.whale.getMoveControl().moveTo(d, e, f, 1.0D);
    }
}

package com.veyhunk.battle_of_balls.model;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.db.GameParams.BALL_GROW_SPEED;

/**
 * 定义食物球球
 */
public class FoodBall {
    public final float radius = 15;
    public boolean state;
    public double positionX;
    public double positionY;
    public int colorDraw;

    public FoodBall(double positionX, double positionY, int colorDraw) {
        this.state = BALL_STATE_ALIVE;// 未被吃
        this.positionX = positionX;
        this.positionY = positionY;
        this.colorDraw = colorDraw;
    }

    public void reSetBall(double positionX, double positionY, int colorDraw) {
        this.state = BALL_STATE_ALIVE;// 未被吃
        this.positionX = positionX;
        this.positionY = positionY;
        this.colorDraw = colorDraw;
    }

    /**
     * basic action : be Eat
     */
    public float die() {
        state = BALL_STATE_DEAD;
        return BALL_GROW_SPEED * 3;
    }
}

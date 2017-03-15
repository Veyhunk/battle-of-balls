package veyhunk.battle_of_balls.model;

import static veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;

/**
 * 定义食物球球
 */
public class FoodBall {
    public final float radius = 6;
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
     * basic action : beEat
     */
    public float beEat() {
        state = BALL_STATE_DEAD;
        return radius;
    }
}

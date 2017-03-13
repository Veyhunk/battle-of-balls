package veyhunk.battle_of_balls.model;

/**
 * 定义食物球球的基类
 */
public class FoodBall {
    public final float radius = 6;
    public int state;
    public double positionX;
    public double positionY;
    public int colorDraw;

    public FoodBall(double positionX, double positionY, int colorDraw) {
        this.state = 1;// 未被吃
        this.positionX = positionX;
        this.positionY = positionY;
        this.colorDraw = colorDraw;
    }

    public void reSetBall(double positionX, double positionY, int colorDraw) {
        this.state = 1;// 未被吃
        this.positionX = positionX;
        this.positionY = positionY;
        this.colorDraw = colorDraw;
    }
}

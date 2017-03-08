package veyhunk.battle_of_balls.ball;

/**
 * 定义食物球球的基类
 */
public class FoodBall {
	public int state;
	public double positionX;
	public double positionY;
	public float radius = 6;
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

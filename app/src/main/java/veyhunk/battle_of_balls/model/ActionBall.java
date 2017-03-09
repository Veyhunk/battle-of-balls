package veyhunk.battle_of_balls.model;


/**
 * 定义活动球球的类，即角色球
 */
class ActionBall {
//	public int ID = 0;
//	int eatByID;
//	int life;
//	int state;
//	int weight;
//	int eatCount;
//	int colorDraw;
//	public int timeRandomActtionBegin;
//	public int timeRandomActtionRang;
//	public int timeBallSafeRange = timeBallSafeRangeConstants;
//	public int timeBallSafeBegin;
//	float moveSpeed;
//	float moveSpeedRandom;
//	float radius;
//	double positionX;
//	double positionY;
//	double targetX;
//	double targetY;
//	double dection = 0;
//	double dectionTarget = 0;
//	String name;
//
//	// // positionX, positionY, colorDraw, size, targetX, targetY,
//	// nameString
//	// ActionBall(double positionX, double positionY, int colorDraw,
//	// float weight, String nameString) {
//	// this.state = 1;// 未被吃
//	// this.positionX = positionX;
//	// this.positionY = positionY;
//	// this.colorDraw = colorDraw;
//	// this.eatCount = 0;
//	// this.moveSpeed = ballMoveSpeed;
//	// this.name = nameString;
//	// this.targetX = positionX;
//	// this.targetY = positionY;
//	// this.weight = (int) weight;
//	// }
//
//	// positionX, positionY, colorDraw, size, targetX, targetY,
//	// nameString,life
//	ActionBall(double positionX, double positionY, int colorDraw,
//			float weight, String nameString, int life) {
//		timeBallSafeBegin = getClock();
//		this.state = 1;// 未被吃
//		this.positionX = positionX;
//		this.positionY = positionY;
//		this.colorDraw = colorDraw;
//		this.eatCount = 0;
//		this.life = life;
//		this.moveSpeed = ballMoveSpeed;
//		this.name = nameString;
//		this.targetX = positionX;
//		this.targetY = positionY;
//		this.weight = (int) weight;
//		this.timeRandomActtionBegin = getClock() + 500;
//	}
//
//	// positionX, positionY, colorDraw, size
//	void reSetBall(double positionX, double positionY, int colorDraw,
//			float weight) {
//		timeBallSafeBegin = getClock();
//		// 启动保护罩
//		if (life > 0) {
//			this.state = 1;// 复活
//			this.weight = (int) weight;
//			this.positionX = positionX;
//			this.positionY = positionY;
//			this.colorDraw = colorDraw;
//			this.targetX = positionX;
//			this.targetY = positionY;
//			this.radius = 0;
//		} else {
//			this.weight = 0;
//			this.radius = 0;
//		}
//	}
//
//	public void action() {
//		if (state == 0) {
//			// 死亡判断
//			life--;
//			reSetBall((int) (mapW * Math.random()),
//					(int) (mapH * Math.random()), getColorRandom(),
//					ballDefaultWeight);
//		}
//		if ((int) radius < (int) Math.sqrt(weight)) {
//			// 阻尼增重
//			radius += (Math.sqrt(weight) - radius) / actionDamping;
//		}
//		if ((int) radius > (int) Math.sqrt(weight)) {
//			// 阻尼减重
//			radius -= (radius - Math.sqrt(weight)) / actionDamping;
//		}
//		weight -= (int) radius / 100 * 5;
//		// 损耗减重
//
//		if (radius > 400) {
//			// 角色球尺寸限制，重置尺寸
//			weight = (int) ballDefaultWeight;
//			timeBallSafeBegin = getClock();
//		}
//	}
//
//	public void moveRandom() {
//		// action();
//		if (!getClockIsInRange(timeRandomActtionBegin,
//				timeRandomActtionRang)) {
//			timeRandomActtionBegin = getClock();
//			timeRandomActtionRang = (int) (Math.random() * 12000);
//			// dectionTarget = (Math.random() * Math.PI * 2) - Math.PI;
//			if (myBall.state != 0 && weight > myBall.weight) {
//				dectionTarget = getRadian((float) positionX,
//						(float) myBall.positionX, (float) positionY,
//						(float) myBall.positionY);
//			} else {
//				dectionTarget = (Math.random() * Math.PI * 2) - Math.PI;
//			}
//			moveSpeedRandom = (float) Math.random();
//		} else {
//			dection += Math.abs((dectionTarget - dection)) < Math.PI ? (((dectionTarget - dection) / actionDamping))
//					: ((dectionTarget - dection) > 0 ? -(Math
//							.abs((dectionTarget - dection - 2 * Math.PI)) / actionDamping)
//							: +(Math.abs((dectionTarget - dection + 2 * Math.PI)) / actionDamping));
//			dection += (dection >= Math.PI) ? (-2 * Math.PI)
//					: ((dection <= -Math.PI) ? (+2 * Math.PI) : 0);
//			targetX += moveSpeed * Math.cos(dectionTarget)
//					* (30 / radius * 1 + 0.6) * moveSpeedRandom;
//			targetY += moveSpeed * Math.sin(dectionTarget)
//					* (30 / radius * 1 + 0.6) * moveSpeedRandom;
//			if (targetX < 0) {
//				// 边界判断
//				targetX = 0;
//				// myBall.targetX = 0;
//				// ptRockerPosition.x = ptRockerCtrlPoint.x;
//
//			}
//			if (targetX > mapW) {
//				// 边界判断
//				targetX = mapW;
//				// myBall.targetX = mapW;
//				// ptRockerPosition.x = ptRockerCtrlPoint.x;
//			}
//			if (targetY < 0) {
//				// 边界判断
//				targetY = 0;
//				// myBall.targetY = 0;
//				// ptRockerPosition.y = ptRockerCtrlPoint.y;
//			}
//			if (targetY > mapH) {
//				// 边界判断
//				targetY = mapH;
//				// // myBall.targetY = mapH;
//				// ptRockerPosition.y = ptRockerCtrlPoint.y;
//			}
//			positionX += (targetX - positionX) / actionDamping;
//			positionY += (targetY - positionY) / actionDamping;
//		}
//	}
//
//	public void move(float rocker) {
//		if (dectionTarget == 404) {
//			return;
//		} else {
//			dection += Math.abs((dectionTarget - dection)) < Math.PI ? (((dectionTarget - dection) / actionDamping))
//					: ((dectionTarget - dection) > 0 ? -(Math
//							.abs((dectionTarget - dection - 2 * Math.PI)) / actionDamping)
//							: +(Math.abs((dectionTarget - dection + 2 * Math.PI)) / actionDamping));
//			dection += (dection >= Math.PI) ? (-2 * Math.PI)
//					: ((dection <= -Math.PI) ? (+2 * Math.PI) : 0);
//			targetX += moveSpeed * Math.cos(dectionTarget)
//					* (30 / radius * 1 + 0.6) * rocker;
//			targetY += moveSpeed * Math.sin(dectionTarget)
//					* (30 / radius * 1 + 0.6) * rocker;
//			if (targetX < 0) {
//				// 边界判断
//				targetX = 0;
//				dectionTarget = getRadian(ptRockerCtrlPoint.x,
//						ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
//						ptRockerPosition.y);
//				ptRockerPosition.x = ptRockerCtrlPoint.x;
//				// myBall.targetX = 0;
//				// ptRockerPosition.x = ptRockerCtrlPoint.x;
//
//			}
//			if (targetX > mapW) {
//				// 边界判断
//				targetX = mapW;
//				dectionTarget = getRadian(ptRockerCtrlPoint.x,
//						ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
//						ptRockerPosition.y);
//				ptRockerPosition.x = ptRockerCtrlPoint.x;
//				// myBall.targetX = mapW;
//				// ptRockerPosition.x = ptRockerCtrlPoint.x;
//			}
//			if (targetY < 0) {
//				// 边界判断
//				targetY = 0;
//				dectionTarget = getRadian(ptRockerCtrlPoint.x,
//						ptRockerPosition.x, ptRockerCtrlPoint.y,
//						ptRockerCtrlPoint.y);
//				ptRockerPosition.y = ptRockerCtrlPoint.y;
//				// myBall.targetY = 0;
//				// ptRockerPosition.y = ptRockerCtrlPoint.y;
//			}
//			if (targetY > mapH) {
//				// 边界判断
//				targetY = mapH;
//				dectionTarget = getRadian(ptRockerCtrlPoint.x,
//						ptRockerPosition.x, ptRockerCtrlPoint.y,
//						ptRockerCtrlPoint.y);
//				ptRockerPosition.y = ptRockerCtrlPoint.y;
//				// // myBall.targetY = mapH;
//				// ptRockerPosition.y = ptRockerCtrlPoint.y;
//			}
//			positionX += (targetX - positionX) / actionDamping;
//			positionY += (targetY - positionY) / actionDamping;
//		}
//
//	}
//
//	public float getRadian(float x1, float x2, float y1, float y2) {
//		float lenA = x2 - x1;
//		float lenB = y2 - y1;
//		if (lenA == 0 && lenB == 0) {
//			return 404;
//		}
//		float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
//		float ang = (float) Math.acos(lenA / lenC);
//		ang = ang * (y2 < y1 ? -1 : 1);
//		return ang;
//	}
}

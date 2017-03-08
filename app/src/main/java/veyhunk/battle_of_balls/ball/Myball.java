package veyhunk.battle_of_balls.ball;

import java.util.ArrayList;

public class Myball extends ActionBall {
//	// array
//	ArrayList<ActionBall> myAvatars;
//	int isAvatar;
//
//	Myball(double positionX, double positionY, int colorDraw, float weight,
//			String nameString, int life) {
//		super(positionX, positionY, colorDraw, weight, nameString, life);
//		// TODO Auto-generated constructor stub
//		isAvatar = 0;
//	}
//
//	@Override
//	public void action() {
//		// TODO Auto-generated method stub
//		if (state == 0) {
//			// 死亡判断
//			life--;
//			isAvatar = 0;
//			myAvatars = null;
//			reSetBall((int) (mapW * Math.random()),
//					(int) (mapH * Math.random()), getColorRandom(),
//					ballDefaultWeight);
//		}
//
//		if (isAvatar == 0) {
//			if ((int) radius < (int) Math.sqrt(weight)) {
//				// 阻尼增重
//				radius += (Math.sqrt(weight) - radius) / actionDamping;
//			}
//			if ((int) radius > (int) Math.sqrt(weight)) {
//				// 阻尼减重
//				radius -= (radius - Math.sqrt(weight)) / actionDamping;
//			}
//			weight -= (int) radius / 100 * 5;
//			// 损耗减重
//
//			if (radius > 400) {
//				// 角色球尺寸限制，重置尺寸
//				weight = (int) ballDefaultWeight;
//				timeBallSafeBegin = getClock();
//			}
//		} else {
//			for (ActionBall avatar : myAvatars) {
//				if ((int) avatar.radius < (int) Math.sqrt(avatar.weight)) {
//					// 阻尼增重
//					avatar.radius += (Math.sqrt(avatar.weight) - avatar.radius)
//							/ actionDamping;
//				}
//				if ((int) avatar.radius > (int) Math.sqrt(avatar.weight)) {
//					// 阻尼减重
//					avatar.radius -= (avatar.radius - Math
//							.sqrt(avatar.weight)) / actionDamping;
//				}
//				avatar.weight -= (int) avatar.radius / 100 * 5;
//				// 损耗减重
//			}
//		}
//	}
//
//	@Override
//	public void move(float rocker) {
//		// TODO Auto-generated method stub
//		if (isAvatar == 0) {
//			super.move(rocker);
//		} else {
//			targetX = 0;
//			targetY = 0;
//			for (ActionBall avatar : myAvatars) {
//				if (dectionTarget == 404) {
//					return;
//				} else {
//					avatar.dection += Math
//							.abs((dectionTarget - avatar.dection)) < Math.PI ? (((dectionTarget - avatar.dection) / actionDamping))
//							: ((dectionTarget - avatar.dection) > 0 ? -(Math
//									.abs((dectionTarget - avatar.dection - 2 * Math.PI)) / actionDamping)
//									: +(Math.abs((dectionTarget
//											- avatar.dection + 2 * Math.PI)) / actionDamping));
//					avatar.dection += (avatar.dection >= Math.PI) ? (-2 * Math.PI)
//							: ((avatar.dection <= -Math.PI) ? (+2 * Math.PI)
//									: 0);
//					avatar.targetX += moveSpeed * Math.cos(dectionTarget)
//							* (30 / avatar.radius * 1 + 0.6) * rocker;
//					avatar.targetY += moveSpeed * Math.sin(dectionTarget)
//							* (30 / avatar.radius * 1 + 0.6) * rocker;
//					if (avatar.targetX < 0) {
//						// 边界判断
//						avatar.targetX = 0;
//						// myBall.targetX = 0;
//						// ptRockerPosition.x = ptRockerCtrlPoint.x;
//
//					}
//					if (avatar.targetX > mapW) {
//						// 边界判断
//						avatar.targetX = mapW;
//						// myBall.targetX = mapW;
//						// ptRockerPosition.x = ptRockerCtrlPoint.x;
//					}
//					if (avatar.targetY < 0) {
//						// 边界判断
//						avatar.targetY = 0;
//						// myBall.targetY = 0;
//						// ptRockerPosition.y = ptRockerCtrlPoint.y;
//					}
//					if (avatar.targetY > mapH) {
//						// 边界判断
//						avatar.targetY = mapH;
//						// // myBall.targetY = mapH;
//						// ptRockerPosition.y = ptRockerCtrlPoint.y;
//					}
//					avatar.positionX += (avatar.targetX - avatar.positionX)
//							/ actionDamping;
//					avatar.positionY += (avatar.targetY - avatar.positionY)
//							/ actionDamping;
//					targetX += avatar.positionX;
//					targetY += avatar.positionY;
//				}
//			}
//			positionX += (targetX / myAvatars.size() - positionX)
//					/ actionDamping;
//			positionY += (targetY / myAvatars.size() - positionY)
//					/ actionDamping;
//		}
//	}
//
//	public void avatar() {
//		System.out.println("1" + isAvatar);
//		if (isAvatar > 15) {
//			return;
//		}
//		// TODO Auto-generated method stub
//		if (isAvatar == 0) {
//			myAvatars = new ArrayList<MySurfaceView.ActionBall>();
//			myAvatars.add(new ActionBall(positionX, positionY, colorDraw,
//					weight, name, 1));
//			isAvatar = 1;
//		}
//		for (int i = 0; i < isAvatar; i++) {
//			ActionBall avatar = myAvatars.get(i);
//			avatar.weight /= 2;
//			myAvatars.add(new ActionBall(avatar.positionX + avatar.radius
//					* 2 * Math.cos(dectionTarget), avatar.positionY
//					+ avatar.radius * 2 * Math.sin(dectionTarget),
//					colorDraw, avatar.weight, name, 1));
//		}
//		isAvatar = myAvatars.size();
//		System.out.println(isAvatar);
//		System.out.println("========***********************==========1111");
//		for (ActionBall avatar : myBall.myAvatars) {
//			System.out.println(avatar);
//		}
//		System.out.println("========***********************==========1111");
//	}
}

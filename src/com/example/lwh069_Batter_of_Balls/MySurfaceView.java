package com.example.lwh069_Batter_of_Balls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 * 
 * @author Veyhunk
 * 
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	// constant
	private static final String strPalyerName = "goodnight 逗逗";// 中国辣条协会会长 琪红小傻瓜
	private static final float ballGrowSpeed = 120f;// ballGrowSpeed
	private static final float ballMoveSpeed = 0.8F;// ballMoveSpeed
	private static final float ballDefaultSize = 900;// ballDefaultSize
	private static final float actionDamping = 10;// 活动阻尼
	private static final int frameRate = 50;// 帧率（单位：Hz/s）
	private static final int safeTime = 5000 / (1000 / frameRate);// 开始游戏前的无敌时间（单位：ms）
	private static final int mapW = 3000, mapH = 2000; // Map_size
	private static final int ballColorIndex = 8;// playerColor
	private static final int[] ballColor = new int[] { R.color.color0,
			R.color.color1, R.color.color2, R.color.color3, R.color.color4,
			R.color.color5, R.color.color6 };// 颜色表
	private static final String[] strArrayName = new String[] { "触手TV大白",
			"董大鹏", "关注我带团", "孙红雷", "北丘", "触手TV阿木", "骷髅狙手", "被白菜怼过的猪", "学长",
			"超萌的一天" };
	// flag
	private boolean flagThread;// 线程消亡的标识位
	private boolean flagIsTouchLongMove;// 是否长按的标识位
	private boolean flagRockerDisplay = false;// 是否显示遥感的标识位
	// variable
	private float textLength; // 文本长度
	private int screenW, screenH; // Screen_size
	private int safeTimeClock = 0, score = 0, rank, index, index2;
	private int[] rankList = new int[10];
	private int rockerRudderRadius = 30;// 摇杆半径
	private int rockerWheelRadius = 65;// 摇杆活动范围半径
	private Point ptRockerPosition;// 摇杆位置
	private Point ptRockerCtrlPoint = new Point(0, 0);// 摇杆起始位置
	private SurfaceHolder sfh; // 用于控制SurfaceView
	private Paint paint;// 声明一个画笔
	private Thread th;// 声明一条线程
	private Canvas canvas;// 声明一个画布
	// ball
	private ActionBall myBall; // 声明Ball
	private Ball[] ballListSmall = new Ball[500];
	private ActionBall[] ballListLarge = new ActionBall[10]; // 位图文件
	// bitmap
	private Bitmap bmpRank = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.rank);

	/**
	 * 游戏绘图
	 */
	private void myDraw() {
		try {

			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.save();
				canvas.translate((float) (0 - myBall.positionX + screenW / 2),
						(float) (0 - myBall.positionY + screenH / 2));
				canvas.scale((3 / (myBall.radius / 10) * 0.6F + 0.4F),
						(3 / (myBall.radius / 10) * 0.6F + 0.4F),
						(float) myBall.positionX, (float) myBall.positionY);
				DrawBackground();

				for (Ball ball : ballListSmall) {
					// 绘制小球
					if ((ball.positionX - myBall.positionX)
							* (ball.positionX - myBall.positionX)
							+ (ball.positionY - myBall.positionY)
							* (ball.positionY - myBall.positionY) < (myBall.radius)
							* (myBall.radius)) {
						// 任意一点P（x1,y1） （x-x1）*(x-x1)+(y-y1)*(y-y1)<R*R,则球被吃
						ball.state = 0;
						myBall.weight += ballGrowSpeed;
					}
					for (ActionBall ball2 : ballListLarge) {
						// 判断是否被吃
						if ((ball.positionX - ball2.positionX)
								* (ball.positionX - ball2.positionX)
								+ (ball.positionY - ball2.positionY)
								* (ball.positionY - ball2.positionY) < (ball2.radius)
								* (ball2.radius)) {
							ball.state = 0;
							ball2.weight += ballGrowSpeed * 7;

						}
					}
					paint.setColor(getResources().getColor(ball.colorBall));
					if (ball.state == 1) {
						canvas.drawCircle((float) ball.positionX,
								(float) ball.positionY, ball.radius, paint);
					} else {
						ball.reSetBall((int) (mapW * Math.random()),
								(int) (mapH * Math.random()), randomColor());
					}
				}
				for (ActionBall ball : ballListLarge) {
					if (ball.life < 1) {
						continue;
					}
					// 绘制活动大球
					if ((ball.positionX - myBall.positionX)
							* (ball.positionX - myBall.positionX)
							+ (ball.positionY - myBall.positionY)
							* (ball.positionY - myBall.positionY) < (myBall.radius)
							* (myBall.radius)
							|| (ball.positionX - myBall.positionX)
									* (ball.positionX - myBall.positionX)
									+ (ball.positionY - myBall.positionY)
									* (ball.positionY - myBall.positionY) < (ball.radius)
									* (ball.radius)) {
						// 任意一点P（x1,y1） （x-x1）*(x-x1)+(y-y1)*(y-y1)<R*R,则球被吃
						if (ball.radius > myBall.radius) {
							// 判断是否被AI吃
							if (safeTimeClock >= safeTime) {
								// 判断是否无敌时间
								myBall.state = 0;
								ball.life++;
								ball.weight += myBall.weight;
								ball.eatCount++;
							}

						} else {
							// 吃掉AI
							ball.state = 0;
							myBall.eatCount++;
							myBall.life++;
							myBall.weight += ball.weight;
						}

					}
					paint.setColor(getResources().getColor(ball.colorBall));
					if (ball.state == 1) {
						// 绘制AI大球
						canvas.drawCircle((float) ball.positionX,
								(float) ball.positionY, ball.radius, paint);
						// 绘制名称
						paint.setColor(Color.WHITE);
						paint.setTextSize(ball.radius > 40 ? (20 + (ball.radius > 90 ? 15
								: ball.radius / 6))
								: 23);
						textLength = paint.measureText(ball.name);
						canvas.drawText(
								ball.name,
								(float) ball.positionX - textLength / 2,
								(float) ball.positionY
										+ (myBall.radius > 40 ? (20 + (myBall.radius > 90 ? 15
												: myBall.radius / 6))
												: 23) / 4, paint);
					}
				}
				// 绘制角色球
				if (safeTimeClock + 1 < safeTime) {
					// 保护罩
					if (safeTimeClock % 20 > 10) {
						paint.setColor(getResources().getColor(
								R.color.safeLight));
						canvas.drawCircle((float) myBall.positionX,
								(float) myBall.positionY,
								(myBall.radius * 1.3F), paint);
					}
				}
				paint.setColor(getResources().getColor(myBall.colorBall));
				canvas.drawCircle((float) myBall.positionX,
						(float) myBall.positionY, myBall.radius, paint);// 绘制角色球
				// 绘制角色名称
				paint.setColor(Color.WHITE);
				paint.setTextSize(myBall.radius > 40 ? (20 + (myBall.radius > 90 ? 15
						: myBall.radius / 6))
						: 23);
				textLength = paint.measureText(myBall.name);
				canvas.drawText(myBall.name, (float) myBall.positionX
						- textLength / 2, (float) myBall.positionY
						+ (myBall.radius > 40 ? (20 + (myBall.radius > 90 ? 15
								: myBall.radius / 6)) : 23) / 4, paint);// 绘制角色球
				canvas.restore();

				canvas.save();

				// 摇杆
				if (flagRockerDisplay) {
					paint.setColor(getResources()
							.getColor(R.color.rockerRudder));
					canvas.drawCircle(ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
							rockerWheelRadius, paint);// 绘制范围
					paint.setColor(getResources().getColor(R.color.rocker));
					canvas.drawCircle(ptRockerPosition.x, ptRockerPosition.y,
							rockerRudderRadius, paint);// 绘制摇杆
				}
				// score计分板
				paint.setColor(getResources().getColor(R.color.rockerRudder));
				canvas.drawRect(screenW - (screenW / 6) - 5, 5, screenW - 5,
						screenH / 2 + 5, paint);
				canvas.drawBitmap(bmpRank, screenW - (screenW / 6), 13, paint);
				paint.setTextSize(23);
				paint.setColor(getResources().getColor(R.color.font));
				for (int j = 0; j < ballListLarge.length; j++) {
					ActionBall ball = rankList[j] < 10 ? ballListLarge[rankList[j]]
							: myBall;
					if (ball.life < 1) {
						continue;
					}
					canvas.drawText(
							(String) (ball.name.length() > 5 ? ball.name
									.subSequence(0, 5) : ball.name), screenW
									- (screenW / 6) + 55,
							(float) (50 + j * 32.5), paint);
					canvas.drawText("" + ball.life, screenW - 30,
							(float) (50 + j * 32.5), paint);
				}

				canvas.drawText("score:" + (score + myBall.weight - 30)
						+ "   Weight:" + myBall.weight + "   Size:"
						+ myBall.radius + "   EatCount:" + myBall.eatCount, 40,
						45, paint);
				canvas.restore();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 游戏逻辑
	 */
	private void logic() {
		if (myBall.state == 0) {
			// 死亡判断
			myBall.life--;
			myBall.reSetBall(mapW * Math.random(), mapH * Math.random(),
					randomColor(ballColorIndex), ballDefaultSize);
			score = 0;
			ptRockerPosition.y = ptRockerCtrlPoint.y;
			ptRockerPosition.x = ptRockerCtrlPoint.x;
			flagRockerDisplay = false;
			safeTimeClock = 0;
		}
		if (safeTimeClock < safeTime) {
			// 无敌时间计数
			safeTimeClock++;
		}
		if (myBall.radius * 2 > mapW) {
			// 角色球尺寸限制，重置尺寸
			score += myBall.weight;
			myBall.weight = (int) ballDefaultSize;
		}
		if (myBall.positionX < 0) {
			// 边界判断
			myBall.positionX = 0;
			myBall.targetX = 0;
			ptRockerPosition.x = ptRockerCtrlPoint.x;

		}
		if (myBall.positionX > mapW) {
			// 边界判断
			myBall.positionX = mapW;
			myBall.targetX = mapW;
			ptRockerPosition.x = ptRockerCtrlPoint.x;
		}
		if (myBall.positionY < 0) {
			// 边界判断
			myBall.positionY = 0;
			myBall.targetY = 0;
			ptRockerPosition.y = ptRockerCtrlPoint.y;
		}
		if (myBall.positionY > mapH) {
			// 边界判断
			myBall.positionY = mapH;
			myBall.targetY = mapH;
			ptRockerPosition.y = ptRockerCtrlPoint.y;
		}
		if ((int) myBall.radius < (int) Math.sqrt(myBall.weight)) {
			// 阻尼增重
			myBall.radius += (Math.sqrt(myBall.weight) - myBall.radius)
					/ actionDamping;
		}
		if ((int) myBall.radius > (int) Math.sqrt(myBall.weight)) {
			// 阻尼增重
			myBall.radius -= (myBall.radius - Math.sqrt(myBall.weight))
					/ actionDamping;
		}
		myBall.weight -= (int) myBall.radius / 100 * actionDamping;
		myBall.targetX += ((ptRockerPosition.x - ptRockerCtrlPoint.x)
				/ (double) (myBall.radius < 200 ? myBall.radius / 5 : 40) * (double) myBall.moveSpeed);
		myBall.targetY += ((ptRockerPosition.y - ptRockerCtrlPoint.y)
				/ (double) (myBall.radius < 200 ? myBall.radius / 5 : 40) * (double) myBall.moveSpeed);
		// 角色球目标位置随遥感位移
		if (Math.abs(myBall.targetX - myBall.positionX) > 1
				|| Math.abs(myBall.targetY - myBall.positionY) > 1) {
			// 角色球位置随目标位置位移
			myBall.positionX += Math.abs(myBall.targetX - myBall.positionX) > 10 ? (myBall.targetX - myBall.positionX)
					/ actionDamping
					: ((myBall.targetX - myBall.positionX) < 1 ? 0
							: ((myBall.targetX - myBall.positionX) > 0 ? 1 : -1));
			myBall.positionY += Math.abs(myBall.targetY - myBall.positionY) > 10 ? (myBall.targetY - myBall.positionY)
					/ actionDamping
					: ((myBall.targetY - myBall.positionY) < 1 ? 0
							: ((myBall.targetY - myBall.positionY) > 0 ? 1 : -1));
		}
		for (index = 0; index < ballListLarge.length; index++) {
			// AI大球位移控制
			ActionBall ball = ballListLarge[index];
			// 排名
			rank = 0;
			for (index2 = 0; index2 < ballListLarge.length; index2++) {
				if (ballListLarge[index].radius < ballListLarge[index2].radius) {
					rank++;
				}
			}
			if (rank < 10) {
				rankList[rank] = index;
			}
			if (ball.life < 1) {
				// 生命值为空
				ball.radius = 0;
				continue;
			}
			if (ball.state == 0) {
				// 死亡判断
				ball.life--;
				ball.reSetBall(
						(int) (mapW * Math.random()),
						(int) (mapH * Math.random()),
						randomColor(),
						(float) (ballDefaultSize * Math.random() + ballDefaultSize));
				continue;
			}
			if (ball.radius > 300) {
				// 角色球尺寸限制，重置尺寸
				ball.weight = (int) ballDefaultSize;
			}

			if (ball.radius < Math.sqrt(ball.weight)) {
				// 阻尼增重
				ball.radius += (Math.sqrt(ball.weight) - ball.radius)
						/ actionDamping;
			}
			ball.positionX += Math.abs(ball.targetX - ball.positionX) > 10 ? (ball.targetX - ball.positionX)
					/ ball.radius * ball.moveSpeed / actionDamping
					: (Math.abs(ball.targetX - ball.positionX) < 1 ? 0
							: ((ball.targetX - ball.positionX) > 0 ? 1 : -1));
			ball.positionY += Math.abs(ball.targetY - ball.positionY) > 10 ? (ball.targetY - ball.positionY)
					/ ball.radius * ball.moveSpeed / actionDamping
					: (Math.abs(ball.targetY - ball.positionY) < 1 ? 0
							: ((ball.targetY - ball.positionY) > 0 ? 1 : -1));
			if (Math.abs(ball.positionX - ball.targetX) < 4) {
				// 重新获取运动目标位置
				ball.targetX = (int) (mapW * Math.random());
				ball.targetY = (int) (mapH * Math.random());
			}
		}
		rank = 0;
		for (int j = 0; j < ballListLarge.length; j++) {
			if (myBall.radius < ballListLarge[j].radius) {
				rank++;
			}
		}
		if (rank < 10) {
			rankList[rank] = 10;
		}

	}

	/**
	 * 绘制背景
	 */
	private void DrawBackground() {
		// -----------利用填充画布，刷屏
		canvas.drawColor(getResources().getColor(R.color.Background) + 1);
		// //绘制矩形
		paint.setColor(getResources().getColor(R.color.Background));
		canvas.drawRect(0, 0, mapW, mapH, paint);
		int rowWidth = 3, rowInterval = 40;
		paint.setColor(getResources().getColor(R.color.BackgroundStripe));
		for (index = 1; index <= mapH / (rowWidth + rowInterval); index++) {
			canvas.drawRect(0, index * (rowWidth + rowInterval), mapW, index
					* (rowWidth + rowInterval) + rowWidth, paint);
		}
		for (index = 1; index <= mapW / (rowWidth + rowInterval); index++) {
			canvas.drawRect(index * (rowWidth + rowInterval), 0, index
					* (rowWidth + rowInterval) + rowWidth, mapH, paint);
		}

	}

	/**
	 * 随机生成颜色
	 */
	private int randomColor() {
		return ballColor[(int) (Math.random() * ballColor.length)];
	}

	private int randomColor(int colorIndex) {
		return ballColor[colorIndex > 7 ? ((int) (Math.random() * ballColor.length))
				: (colorIndex)];
	}

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context) {
		super(context);
		// 实例SurfaceHolder
		sfh = this.getHolder();
		// 为SurfaceView添加状态监听
		sfh.addCallback(this);
		// 实例一个画笔
		paint = new Paint();
		ptRockerPosition = new Point(ptRockerCtrlPoint);
		// 设置消除锯齿
		paint.setAntiAlias(true);
		// 设置焦点
		setFocusable(true);
	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		// init player ball
		myBall = new ActionBall(mapW / 2, mapH / 2,
				randomColor(ballColorIndex), ballDefaultSize, mapW / 2,
				mapH / 2, strPalyerName, 3);
		// init food ball
		for (index = 0; index < ballListSmall.length; index++) {
			ballListSmall[index] = new Ball((int) (mapW * Math.random()),
					(int) (mapH * Math.random()), randomColor());
		}
		// init large ball
		for (index = 0; index < ballListLarge.length; index++) {
			ballListLarge[index] = new ActionBall(
					0,
					0,
					randomColor(),
					(float) (ballDefaultSize * Math.random() + ballDefaultSize),
					0, 0, strArrayName[index], 3);
		}
		// 启动线程flag
		flagThread = true;
		// 实例线程
		th = new Thread(this);
		// 启动线程
		th.start();
	}

	/**
	 * 按键事件监听
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {// 画面转换位置移动动画效果
	// myBall.targetY -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// 画面转移旋转动画效果
	// myBall.targetY += myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// 画面转移旋转动画效果
	// myBall.targetX -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// 画面转移旋转动画效果
	// myBall.targetX += myBall.moveSpeed * 20;
	// }
	// return super.onKeyDown(keyCode, event);
	//
	// }

	/**
	 * SurfaceView视图状态发生改变，响应此函数
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * SurfaceView视图消亡时，响应此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flagThread = false;
	}

	/**
	 * 触屏事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// System.out.println("---按下----");
			ptRockerCtrlPoint.set((int) event.getRawX(), (int) event.getRawY());
			ptRockerPosition.set((int) event.getX(), (int) event.getY());
			// Xpoint = (int) event.getRawX();
			// Ypoint = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			// System.out.println("----放开----");
			flagIsTouchLongMove = true;
			// myBall.targetX += (int) event.getRawX() - Xpoint;
			// myBall.targetY += (int) event.getRawY() - Ypoint;
			break;
		case MotionEvent.ACTION_MOVE:
			flagRockerDisplay = true;
			// System.out.println("----移动----");
			if (event.getPointerCount() == 1 && safeTimeClock > 15) {
				int len = MathUtils.getLength(ptRockerCtrlPoint.x,
						ptRockerCtrlPoint.y, event.getX(), event.getY());
				if (len < 20 && flagIsTouchLongMove) {
					// 如果屏幕接触点不在摇杆挥动范围内,则不处理
					break;
				}
				if (len <= rockerWheelRadius) {
					// 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置

					flagIsTouchLongMove = false;
					ptRockerPosition
							.set((int) event.getX(), (int) event.getY());

				} else {
					// 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘

					flagIsTouchLongMove = false;
					ptRockerPosition = MathUtils.getBorderPoint(
							ptRockerCtrlPoint, new Point((int) event.getX(),
									(int) event.getY()), rockerWheelRadius);
				}

			}
			break;

		default:
			break;
		}
		return true;

	}

	/**
	 * 进程控制
	 */
	@Override
	public void run() {
		while (flagThread) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < (1000 / frameRate)) {
					Thread.sleep((1000 / frameRate) - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 按键事件监听
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {// 画面转换位置移动动画效果
	// myBall.targetY -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// 画面转移旋转动画效果
	// myBall.targetY += myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// 画面转移旋转动画效果
	// myBall.targetX -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// 画面转移旋转动画效果
	// myBall.targetX += myBall.moveSpeed * 20;
	// }
	// return super.onKeyDown(keyCode, event);
	//
	// }

	/**
	 * 定义食物球球的基类
	 */
	private class Ball {
		int state;
		double positionX;
		double positionY;
		float radius = 6;
		int colorBall;

		Ball(double positionX, double positionY, int colorBall) {
			this.state = 1;// 未被吃
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorBall = colorBall;
		}

		void reSetBall(double positionX, double positionY, int colorBall) {
			this.state = 1;// 未被吃
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorBall = colorBall;
		}
	}

	/**
	 * 定义活动球球的类，即角色球
	 */
	private class ActionBall extends Ball {

		int eatCount;
		int life;
		float moveSpeed;
		String name;
		double targetX;
		double targetY;
		double dection;
		int weight;

		// positionX, positionY, colorBall, size, targetX, targetY, nameString
		ActionBall(double positionX, double positionY, int colorBall,
				float weight, double targetX, double targetY, String nameString) {
			super(positionX, positionY, colorBall);
			this.eatCount = 0;
			this.moveSpeed = ballMoveSpeed;
			this.name = nameString;
			this.targetX = targetX;
			this.targetY = targetY;
			this.weight = (int) weight;
		}

		// positionX, positionY, colorBall, size, targetX, targetY,
		// nameString,life
		ActionBall(double positionX, double positionY, int colorBall,
				float weight, double targetX, double targetY,
				String nameString, int life) {
			super(positionX, positionY, colorBall);
			this.eatCount = 0;
			this.life = life;
			this.moveSpeed = ballMoveSpeed;
			this.name = nameString;
			this.targetX = targetX;
			this.targetY = targetY;
			this.weight = (int) weight;
		}

		// positionX, positionY, colorBall, size
		void reSetBall(double positionX, double positionY, int colorBall,
				float weight) {
			super.reSetBall(positionX, positionY, colorBall);
			this.targetX = positionX;
			this.targetY = positionY;
			this.radius = 0;
			this.weight = (int) weight;
		}

	}

}

package veyhunk.battle_of_balls.surface_view;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.utils.MathUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
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
	// user customer
	public static String ballName = "goodnight 逗逗";// 中国辣条协会会长
	public static int ballColorIndex = 8;// playerColo
	public static int score = 0;// score
	public static float ballGrowSpeed = 20f;// ballGrowSpeed
	public static float ballMoveSpeed = 4F;// ballMoveSpeed
	public static float aiDifficult = 6;// AiDifficult
	// constant
	// private static final String strPalyerName = "goodnight 逗逗";// 中国辣条协会会长
	// 琪红小傻瓜
	// private static final float ballGrowSpeed = 20f;// ballGrowSpeed
	// private static final float ballMoveSpeed = 4F;// ballMoveSpeed
	private static final int frameRate = 50;// 帧率（单位：Hz/s）
	private static final float ballDefaultSize = 2000;// ballDefaultSize
	private static final float actionDamping = 10;// 活动阻尼
	private static final int mapW = 2500, mapH = 2000; // Map_size
	// private static final int ballColorIndex = 8;// playerColor
	private static final int[] ballColor = new int[] { R.color.color0,
			R.color.color1, R.color.color2, R.color.color3, R.color.color4,
			R.color.color5, R.color.color6 };// 颜色表
	private static final String[] strArrayName = new String[] { "触手TV大白",
			"董大鹏", "关注我带团", "孙红雷", "北丘", "触手TV阿木", "骷髅狙手", "被白菜怼过的猪", "冷瞳 炸弹",
			"超萌的一天" };
	// flag
	private boolean flagThread;// 线程消亡的标识位
	private boolean flagGameOver_Win;// 线程消亡的标识位
	private boolean flagIsTouchLongMove;// 是否长按的标识位
	private boolean flagRockerDisplay = false;// 是否显示遥感的标识位
	// time
	// private static Long CLOCK = (long) 0;
	private static Long timeBegin;
	// private static int timeBallSafeBegin = 0;
	private static int timeBallSafeRangeConstants = 5000;// 开始游戏前的无敌时间（单位：ms）
	private static int timeNewRaceBegin = 0;
	private static int timeNewRaceRange = 3000;// 开始游戏前的无敌时间（单位：ms）
	// variable
	// private static float AiDifficult = 6;// AiDifficult
	private float textLength; // 文本长度
	private int screenW, screenH; // Screen_size
	private int rank, index, index2;
	private int[] rankList = new int[10];
	private int rockerRudderRadius = 30;// 摇杆半径
	private int rockerWheelRadius = 65;// 摇杆活动范围半径
	private Point ptRockerPosition;// 摇杆位置
	private Point ptRockerCtrlPoint = new Point(0, 0);// 摇杆起始位置
	private SurfaceHolder sfh; // 用于控制SurfaceView
	private Paint paint;// 声明一个画笔
	private Thread th;// 声明一条线程
	private Canvas canvas;// 声明一个画布
	// 声明Ball
	private ActionBall myBall;
	private FoodBall[] FoodBallList = new FoodBall[500];
	private ActionBall[] AiBallList = new ActionBall[10];
	// 位图文件 bitmap
	private Bitmap bmpRank = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.rank);// 排行榜素材
	private Bitmap bmpDir = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.dir);// 小球指针素材
	// 回调
	protected OnEndOfGameInterface mOnEndOfGame; // callback interface

	public void setOnEndOfGame(OnEndOfGameInterface xOnEndOfGame) {
		mOnEndOfGame = xOnEndOfGame;
	}

	public interface OnEndOfGameInterface {
		public void onEndOfGame();
	}

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
	 * 【Name: "goodnight 逗逗"】 【 grow =20f】【speed = * 4F】 【color =
	 * 8】[【AiDifficult = 6】
	 * 
	 * @param color2
	 * @return
	 */
	public static void Setting(String Name, float speed, float grow,
			float Difficule, int color) {
		// user customer
		if (Name.length() == 0) {
			Name = "你个傻瓜没写名字";
		}
		if (color > 6 || color < 0) {
			color = 10;
		}
		ballName = Name;// 中国辣条协会会长
		ballMoveSpeed = speed / 10F;// ballGrowSpeed
		ballGrowSpeed = grow / 10F;// ballMoveSpeed
		ballColorIndex = color;// playerColor
		aiDifficult = Difficule;// playerColor
	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Clock Record
		timeBegin = System.currentTimeMillis();
		timeNewRaceBegin = getClock() - timeNewRaceRange;
		// screen size
		screenW = this.getWidth();
		screenH = this.getHeight();
		// initialization player aiBall
		myBall = new ActionBall(mapW / 2, mapH / 2,
				randomColor(ballColorIndex), ballDefaultSize, ballName, 3);
		// initialization food aiBall
		for (index = 0; index < FoodBallList.length; index++) {
			FoodBallList[index] = new FoodBall((int) (mapW * Math.random()),
					(int) (mapH * Math.random()), randomColor());
		}
		// initialization large aiBall
		for (index = 0; index < AiBallList.length; index++) {
			AiBallList[index] = new ActionBall(
					(int) (mapW * Math.random()),
					(int) (mapH * Math.random()),
					randomColor(),
					(float) (ballDefaultSize * Math.random() + ballDefaultSize),
					strArrayName[index], 3);
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
			break;
		case MotionEvent.ACTION_UP:
			// System.out.println("----放开----");
			flagIsTouchLongMove = true;
			break;
		case MotionEvent.ACTION_MOVE:
			flagRockerDisplay = true;
			// System.out.println("----移动----");
			if (event.getPointerCount() == 1
					&& !getClockIsInRange(myBall.timeBallSafeBegin, 15)) {
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

				myBall.dectionTarget = MathUtils.getRadian(ptRockerCtrlPoint,
						ptRockerPosition);

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
			// long start = System.currentTimeMillis();
			if (myBall.life > 32 || myBall.life < 1) {
				flagThread = false;
				break;
			}
			logic();
			myDraw();

			// try {
			// if ((System.currentTimeMillis() - start) < (1000 / frameRate)) {
			// Thread.sleep((1000 / frameRate)
			// - (System.currentTimeMillis() - start));
			// } else {
			//
			// }
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		if (!flagThread) {
			if (myBall.life > 32) {
				System.out.println("Win");
				flagGameOver_Win = true;
				// mOnEndOfGame.onEndOfGame();
			} else if (myBall.life < 1) {
				System.out.println("False");
				flagGameOver_Win = false;
				// mOnEndOfGame.onEndOfGame();
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
	 * 游戏绘图
	 */
	private void myDraw() {
		try {

			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.save();
				// 视野调整
				canvas.translate((float) (0 - myBall.positionX + screenW / 2),
						(float) (0 - myBall.positionY + screenH / 2));
				// 以玩家为中心
				canvas.scale((3 / (myBall.radius / 15) + 0.3F),
						(3 / (myBall.radius / 15) + 0.3F),
						(float) myBall.positionX, (float) myBall.positionY);
				// 适应性的缩放
				DrawBackground();
				// 绘制背景

				for (FoodBall foodBall : FoodBallList) {
					// 绘制食物小球
					if ((foodBall.positionX - myBall.positionX)
							* (foodBall.positionX - myBall.positionX)
							+ (foodBall.positionY - myBall.positionY)
							* (foodBall.positionY - myBall.positionY) < (myBall.radius)
							* (myBall.radius)) {
						// 判断是否被吃
						foodBall.state = 0;
						myBall.weight += ballGrowSpeed;
					}
					for (ActionBall aiBall2 : AiBallList) {
						// 判断是否被吃
						if ((foodBall.positionX - aiBall2.positionX)
								* (foodBall.positionX - aiBall2.positionX)
								+ (foodBall.positionY - aiBall2.positionY)
								* (foodBall.positionY - aiBall2.positionY) < (aiBall2.radius)
								* (aiBall2.radius)) {
							foodBall.state = 0;
							aiBall2.weight += ballGrowSpeed * aiDifficult;

						}
					}
					paint.setColor(getResources().getColor(foodBall.colorBall));
					if (foodBall.state == 1) {
						// 绘制
						canvas.drawCircle((float) foodBall.positionX,
								(float) foodBall.positionY, foodBall.radius,
								paint);
					} else {
						// 重置
						foodBall.reSetBall((int) (mapW * Math.random()),
								(int) (mapH * Math.random()), randomColor());
					}
				}
				for (ActionBall aiBall : AiBallList) {
					// 绘制AI大球
					if (aiBall.life < 1 || aiBall.state == 0) {
						// 是否还有生命值
						continue;
					}
					if ((aiBall.positionX - myBall.positionX)
							* (aiBall.positionX - myBall.positionX)
							+ (aiBall.positionY - myBall.positionY)
							* (aiBall.positionY - myBall.positionY) < (myBall.radius)
							* (myBall.radius)
							|| (aiBall.positionX - myBall.positionX)
									* (aiBall.positionX - myBall.positionX)
									+ (aiBall.positionY - myBall.positionY)
									* (aiBall.positionY - myBall.positionY) < (aiBall.radius)
									* (aiBall.radius)) {
						// 判断是否被吃
						if (aiBall.radius > (myBall.radius + myBall.radius / 10)
								&& !getClockIsInRange(myBall)
								&& !getClockIsInRange(timeNewRaceBegin,
										timeNewRaceRange)) {
							// 判断是否被AI吃
							// 判断是否无敌时间
							// 主角死亡标记
							myBall.state = 0;
							timeNewRaceBegin = getClock();
							myBall.timeBallSafeBegin = getClock()
									+ timeNewRaceRange;
							// 启动保护罩
							aiBall.life++;
							aiBall.weight += myBall.weight;
							aiBall.eatCount++;

						} else if (myBall.radius > (aiBall.radius + aiBall.radius / 10)
								&& !getClockIsInRange(aiBall)) {
							// 吃掉AI
							aiBall.state = 0;
							aiBall.timeBallSafeBegin = getClock();
							// 启动保护罩
							myBall.eatCount++;
							myBall.life++;
							myBall.weight += aiBall.weight;
						}

					}
					if (aiBall.state == 1) {
						// AI是否还活着
						for (ActionBall aiBall2 : AiBallList) {
							if (aiBall2.equals(aiBall)) {
								// 是否同一个AI
								continue;
							}
							// 判断是否被吃
							if ((aiBall.positionX - aiBall2.positionX)
									* (aiBall.positionX - aiBall2.positionX)
									+ (aiBall.positionY - aiBall2.positionY)
									* (aiBall.positionY - aiBall2.positionY) < (aiBall.radius)
									* (aiBall.radius)
									|| (aiBall.positionX - aiBall2.positionX)
											* (aiBall.positionX - aiBall2.positionX)
											+ (aiBall.positionY - aiBall2.positionY)
											* (aiBall.positionY - aiBall2.positionY) < (aiBall2.radius)
											* (aiBall2.radius)) {
								// 是否满足吃球的距离
								if (aiBall.radius > (aiBall2.radius + aiBall.radius / 10)
										&& !getClockIsInRange(aiBall2)) {
									// 判断是否被吃
									aiBall2.state = 0;
									aiBall2.timeBallSafeBegin = getClock();
									// 启动保护罩
									aiBall.life++;
									aiBall.weight += aiBall2.weight;
									aiBall.eatCount++;

								} else if ((aiBall.radius + aiBall.radius / 10) < aiBall2.radius
										&& !getClockIsInRange(aiBall)) {
									// 判断是否被吃
									aiBall.state = 0;
									aiBall.timeBallSafeBegin = getClock();
									// 启动保护罩
									aiBall2.eatCount++;
									aiBall2.life++;
									aiBall2.weight += aiBall.weight;
								}
							}
						}
						if (getClockIsInRange(aiBall) && aiBall.state != 0) {
							if (getClock() % (frameRate * 12) > frameRate * 6) {

								paint.setColor(getResources().getColor(
										R.color.safeLight));
								canvas.drawCircle((float) aiBall.positionX,
										(float) aiBall.positionY,
										(aiBall.radius * 1.23F), paint);
							}
						}
						paint.setColor(getResources()
								.getColor(aiBall.colorBall));
						canvas.drawCircle((float) aiBall.positionX,
								(float) aiBall.positionY, aiBall.radius, paint);
						// 绘制AI大球
						paint.setColor(Color.WHITE);
						paint.setTextSize(aiBall.radius > 40 ? (20 + ((int) aiBall.radius > 90 ? 15
								: aiBall.radius / 6))
								: 23);
						// 绘制名称
						textLength = paint.measureText(aiBall.name);
						canvas.drawText(
								aiBall.name,
								(float) aiBall.positionX - textLength / 2,
								(float) aiBall.positionY
										+ (aiBall.radius > 40 ? (20 + ((int) aiBall.radius > 90 ? 15
												: aiBall.radius / 6))
												: 23) / 4, paint);
					}
				}
				// 绘制角色球
				if (myBall.state != 0) {
					if (getClockIsInRange(myBall)) {
						// 保护罩
						if (getClock() % (frameRate * 12) > frameRate * 6) {
							// Shader mShader = new RadialGradient(
							// (float) myBall.positionX,
							// (float) myBall.positionY, myBall.radius * 1.5F,
							// Color.WHITE, getResources().getColor(
							// R.color.safeLight),
							// Shader.TileMode.CLAMP);
							// paint.setShader(mShader);
							paint.setColor(getResources().getColor(
									R.color.safeLight));
							canvas.drawCircle((float) myBall.positionX,
									(float) myBall.positionY,
									(myBall.radius * 1.23F), paint);
						}
					}
					paint.setColor(getResources().getColor(myBall.colorBall));
					canvas.drawCircle((float) myBall.positionX,
							(float) myBall.positionY, myBall.radius, paint);// 绘制角色球
					// dir
					Matrix matrix = new Matrix();
					// 设置旋转角度
					matrix.postScale(myBall.radius / 230, myBall.radius / 230,
							bmpDir.getWidth() / 2, bmpDir.getHeight() / 2);
					matrix.postRotate(
							(float) (myBall.dection * (180 / Math.PI)) + 90,
							bmpDir.getWidth() / 2, bmpDir.getHeight() / 2);
					matrix.postTranslate(
							(float) myBall.positionX
									- bmpDir.getWidth()
									/ 2
									+ (int) (myBall.radius * 1.25 * Math
											.cos(myBall.dection)),
							(float) myBall.positionY
									- bmpDir.getHeight()
									/ 2
									+ (int) (myBall.radius * 1.25 * Math
											.sin(myBall.dection)));
					// 绘制旋转图片
					canvas.drawBitmap(bmpDir, matrix, paint);
					// 绘制角色名称
					paint.setColor(Color.WHITE);
					paint.setTextSize(myBall.radius > 40 ? (20 + (myBall.radius > 90 ? 15
							: myBall.radius / 6))
							: 23);
					textLength = paint.measureText(myBall.name);
					canvas.drawText(
							myBall.name,
							(float) myBall.positionX - textLength / 2,
							(float) myBall.positionY
									+ (myBall.radius > 40 ? (20 + (myBall.radius > 90 ? 15
											: myBall.radius / 6))
											: 23) / 4, paint);// 绘制角色球
				}
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
				for (int j = 0; j < AiBallList.length; j++) {
					ActionBall aiBall = rankList[j] < 10 ? AiBallList[rankList[j]]
							: myBall;
					if (aiBall.life < 1) {
						continue;
					}
					canvas.drawText(
							(String) (aiBall.name.length() > 5 ? aiBall.name
									.subSequence(0, 5) : aiBall.name), screenW
									- (screenW / 6) + 55,
							(float) (50 + j * 32.5), paint);
					canvas.drawText("" + aiBall.life, screenW - 30,
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
	private final void logic() {
		if (!getClockIsInRange(timeNewRaceBegin, timeNewRaceRange)) {
			myBall.action();
			myBall.move((float) Math
					.sqrt((ptRockerPosition.x - ptRockerCtrlPoint.x)
							* (ptRockerPosition.x - ptRockerCtrlPoint.x)
							+ (ptRockerPosition.y - ptRockerCtrlPoint.y)
							* (ptRockerPosition.y - ptRockerCtrlPoint.y))
					/ rockerWheelRadius);
		} else {
			// 锁定遥感
			ptRockerPosition.y = ptRockerCtrlPoint.y;
			ptRockerPosition.x = ptRockerCtrlPoint.x;
			flagRockerDisplay = false;

		}

		for (index = 0; index < AiBallList.length; index++) {
			// AI大球位移控制
			ActionBall aiBall = AiBallList[index];
			// 排名
			rank = 0;
			for (index2 = 0; index2 < AiBallList.length; index2++) {
				if (AiBallList[index].radius < AiBallList[index2].radius) {
					rank++;
				}
			}
			if (rank < 10) {
				rankList[rank] = index;
			}
			if (aiBall.life < 1) {
				// 生命值为空
				aiBall.radius = 0;
				continue;
			}
			aiBall.action();
			aiBall.moveRandom();
		}
		rank = 0;
		for (int j = 0; j < AiBallList.length; j++) {
			if (myBall.radius < AiBallList[j].radius) {
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
	private final void DrawBackground() {
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
	private static final int randomColor() {
		return ballColor[(int) (Math.random() * ballColor.length)];
	}

	private static final int randomColor(int colorIndex) {
		return ballColor[colorIndex > 7 ? ((int) (Math.random() * ballColor.length))
				: (colorIndex)];
	}

	private static final int getClock() {
		return (int) (System.currentTimeMillis() - timeBegin);
	}

	private static final boolean getClockIsInRange(int begin, int range) {
		return (int) (System.currentTimeMillis() - timeBegin) - begin > range ? false
				: true;
	}

	private static final boolean getClockIsInRange(ActionBall ball) {
		return (int) (System.currentTimeMillis() - timeBegin)
				- ball.timeBallSafeBegin > ball.timeBallSafeRange ? false
				: true;
	}

	/**
	 * 定义食物球球的基类
	 */
	private class FoodBall {
		int state;
		double positionX;
		double positionY;
		float radius = 6;
		int colorBall;

		FoodBall(double positionX, double positionY, int colorBall) {
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
	private class ActionBall {
		int life;
		int state;
		int weight;
		int eatCount;
		int colorBall;
		public int timeRandomActtionBegin;
		public int timeRandomActtionRang;
		public int timeBallSafeRange = timeBallSafeRangeConstants;
		public int timeBallSafeBegin;
		float moveSpeed;
		float moveSpeedRandom;
		float radius;
		double positionX;
		double positionY;
		double targetX;
		double targetY;
		double dection = 0;
		double dectionTarget = 0;
		String name;

		// // positionX, positionY, colorBall, size, targetX, targetY,
		// nameString
		// ActionBall(double positionX, double positionY, int colorBall,
		// float weight, String nameString) {
		// this.state = 1;// 未被吃
		// this.positionX = positionX;
		// this.positionY = positionY;
		// this.colorBall = colorBall;
		// this.eatCount = 0;
		// this.moveSpeed = ballMoveSpeed;
		// this.name = nameString;
		// this.targetX = positionX;
		// this.targetY = positionY;
		// this.weight = (int) weight;
		// }

		// positionX, positionY, colorBall, size, targetX, targetY,
		// nameString,life
		ActionBall(double positionX, double positionY, int colorBall,
				float weight, String nameString, int life) {
			timeBallSafeBegin = getClock();
			this.state = 1;// 未被吃
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorBall = colorBall;
			this.eatCount = 0;
			this.life = life;
			this.moveSpeed = ballMoveSpeed;
			this.name = nameString;
			this.targetX = positionX;
			this.targetY = positionY;
			this.weight = (int) weight;
		}

		// positionX, positionY, colorBall, size
		void reSetBall(double positionX, double positionY, int colorBall,
				float weight) {
			timeBallSafeBegin = getClock();
			// 启动保护罩
			this.state = 1;// 未被吃
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorBall = colorBall;
			this.targetX = positionX;
			this.targetY = positionY;
			this.radius = 0;
			this.weight = (int) weight;
		}

		public void action() {
			if (state == 0) {
				// 死亡判断
				life--;
				reSetBall((int) (mapW * Math.random()),
						(int) (mapH * Math.random()), randomColor(),
						ballDefaultSize);
			}
			if ((int) radius < (int) Math.sqrt(weight)) {
				// 阻尼增重
				radius += (Math.sqrt(weight) - radius) / actionDamping;
			}
			if ((int) radius > (int) Math.sqrt(weight)) {
				// 阻尼减重
				radius -= (radius - Math.sqrt(weight)) / actionDamping;
			}
			weight -= (int) radius / 100 * actionDamping;
			// 损耗减重
			if (radius > mapW || radius > mapH) {
				// 角色球尺寸限制，重置尺寸
				weight = (int) ballDefaultSize;
			}

			if (radius > 300) {
				// 角色球尺寸限制，重置尺寸
				weight = (int) ballDefaultSize;
			}
		}

		public void moveRandom() {
			// action();
			if (!getClockIsInRange(timeRandomActtionBegin,
					timeRandomActtionRang)) {
				timeRandomActtionBegin = getClock();
				timeRandomActtionRang = (int) (Math.random() * 12000);
				dectionTarget = (Math.random() * Math.PI * 2) - Math.PI;
				moveSpeedRandom = (float) Math.random();
			} else {
				dection += Math.abs((dectionTarget - dection)) < Math.PI ? (((dectionTarget - dection) / actionDamping))
						: ((dectionTarget - dection) > 0 ? -(Math
								.abs((dectionTarget - dection - 2 * Math.PI)) / actionDamping)
								: +(Math.abs((dectionTarget - dection + 2 * Math.PI)) / actionDamping));
				dection += (dection >= Math.PI) ? (-2 * Math.PI)
						: ((dection <= -Math.PI) ? (+2 * Math.PI) : 0);
				targetX += moveSpeed * Math.cos(dectionTarget)
						* (30 / radius * 1 + 0.6) * moveSpeedRandom;
				targetY += moveSpeed * Math.sin(dectionTarget)
						* (30 / radius * 1 + 0.6) * moveSpeedRandom;
				if (targetX < 0) {
					// 边界判断
					targetX = 0;
					// myBall.targetX = 0;
					// ptRockerPosition.x = ptRockerCtrlPoint.x;

				}
				if (targetX > mapW) {
					// 边界判断
					targetX = mapW;
					// myBall.targetX = mapW;
					// ptRockerPosition.x = ptRockerCtrlPoint.x;
				}
				if (targetY < 0) {
					// 边界判断
					targetY = 0;
					// myBall.targetY = 0;
					// ptRockerPosition.y = ptRockerCtrlPoint.y;
				}
				if (targetY > mapH) {
					// 边界判断
					targetY = mapH;
					// // myBall.targetY = mapH;
					// ptRockerPosition.y = ptRockerCtrlPoint.y;
				}
				positionX += (targetX - positionX) / actionDamping;
				positionY += (targetY - positionY) / actionDamping;
			}
		}

		public void move(float rocker) {
			dection += Math.abs((dectionTarget - dection)) < Math.PI ? (((dectionTarget - dection) / actionDamping))
					: ((dectionTarget - dection) > 0 ? -(Math
							.abs((dectionTarget - dection - 2 * Math.PI)) / actionDamping)
							: +(Math.abs((dectionTarget - dection + 2 * Math.PI)) / actionDamping));
			dection += (dection >= Math.PI) ? (-2 * Math.PI)
					: ((dection <= -Math.PI) ? (+2 * Math.PI) : 0);
			targetX += moveSpeed * Math.cos(dectionTarget)
					* (30 / radius * 1 + 0.6) * rocker;
			targetY += moveSpeed * Math.sin(dectionTarget)
					* (30 / radius * 1 + 0.6) * rocker;
			if (targetX < 0) {
				// 边界判断
				targetX = 0;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
						ptRockerPosition.y);
				// myBall.targetX = 0;
				// ptRockerPosition.x = ptRockerCtrlPoint.x;

			}
			if (targetX > mapW) {
				// 边界判断
				targetX = mapW;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
						ptRockerPosition.y);
				// myBall.targetX = mapW;
				// ptRockerPosition.x = ptRockerCtrlPoint.x;
			}
			if (targetY < 0) {
				// 边界判断
				targetY = 0;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerPosition.x, ptRockerCtrlPoint.y,
						ptRockerCtrlPoint.y);
				// myBall.targetY = 0;
				// ptRockerPosition.y = ptRockerCtrlPoint.y;
			}
			if (targetY > mapH) {
				// 边界判断
				targetY = mapH;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerPosition.x, ptRockerCtrlPoint.y,
						ptRockerCtrlPoint.y);
				// // myBall.targetY = mapH;
				// ptRockerPosition.y = ptRockerCtrlPoint.y;
			}
			positionX += (targetX - positionX) / actionDamping;
			positionY += (targetY - positionY) / actionDamping;
		}

		public float getRadian(float x1, float x2, float y1, float y2) {
			float lenA = x2 - x1;
			float lenB = y2 - y1;
			float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
			float ang = (float) Math.acos(lenA / lenC);
			ang = ang * (y2 < y1 ? -1 : 1);
			return ang;
		}
	}

}

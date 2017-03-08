
package veyhunk.battle_of_balls.surface_view;

import java.util.ArrayList;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.sound.GameMusic;
import veyhunk.battle_of_balls.ball.FoodBall;
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
import android.view.KeyEvent;
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
	public static String ballName = "感情淡了放盐啊";// 感情淡了放盐啊
	public static int ballColorIndex = 8;// playerColo
	public static int score = 0;// score
	public static int bestScore = 0;// score
	public static float ballGrowSpeed = 20f;// ballGrowSpeed
	public static float ballMoveSpeed = 4F;// ballMoveSpeed
	public static float aiDifficult = 6;// AiDifficult
	// constant
	private static final float actionDamping = 10;// 活动阻尼
	private static final float itemW = 210, itemH = 32.5F; // tab_size
	private static final int frameRate = 50;// 帧率（单位：Hz/s）
	private static final int ballDefaultLife = 3;// ballDefaultLife
	private static final int ballAiCount = 10;// ballAiCount
	private static final int ballFoodCount = 600;// ballFoodCount
	private static final int ballDefaultWeight = 1600;// ballDefaultSize
	private static final int mapW = 3000, mapH = 2000; // Map_size
	private static final int[] ballColor = new int[] { R.color.color0,
			R.color.color1, R.color.color2, R.color.color3, R.color.color4,
			R.color.color5, R.color.color6 };// 颜色表
	private static final String[] strArrayName = new String[] { "触手TV大白",
			"董大鹏", "关注我带团", "孙红雷", "北丘", "触手TV阿木", "二狗子", "被白菜怼过的猪", "冷瞳 炸弹",
			"超萌的一天" };
	// flag
	private boolean flagGameThread;// 线程消亡的标识位
	private int flagButtonIndex;// 线程消亡的标识位
	private boolean flagGameOver;// 线程消亡的标识位
	private boolean flagIsTouchLongMove;// 是否长按的标识位
	private boolean flagRockerDisplay = false;// 是否显示遥感的标识位
	// time
	private static Long timeBegin;
	private static int timeBallSafeRangeConstants = 3000;// 开始游戏前的无敌时间（单位：ms）
	private static int timeNewRaceBegin = 0;
	private static int timeMinute;// 开始游戏前的无敌时间（单位：ms）
	private static int timeSecond;// 开始游戏前的无敌时间（单位：ms）
	private static int timeGame;// 开始游戏前的无敌时间（单位：ms）
	private static int timeNewRaceRange = 2000;// 开始游戏前的无敌时间（单位：ms）
	// variable
	private int screenW, screenH; // Screen_size
	private int rank, index, index2;
	private int[] rankList = new int[11];
	private int rockerRudderRadius = 30;// 摇杆半径
	private int rockerActionRadius = 75;// 摇杆活动范围半径
	private int rockerWheelRadius = 60;// 摇杆活动范围半径
	private Point ptRockerPosition;// 摇杆位置
	private Point ptRockerCtrlPoint = new Point(0, 0);// 摇杆起始位置
	private SurfaceHolder sfh; // 用于控制SurfaceView
	private Paint paint;// 声明一个画笔
	private Paint paintFont;// 声明一个画笔
	private Thread th;// 声明一条线程
	private Canvas canvas;// 声明一个画布
	// array
	ArrayList<Bubble> BubbleList = new ArrayList<MySurfaceView.Bubble>();

	// 声明Ball
	private Myball myBall;
	private FoodBall[] FoodBallList = new FoodBall[ballFoodCount];
	private ActionBall[] AiBallList = new ActionBall[ballAiCount];
	// 位图文件 bitmap
	private Bitmap bmpRank = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.rank);// 排行榜素材
	private Bitmap bmpDir = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.dir);// 小球指针素材
	private Bitmap bmpInfo = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.infowindows);// 球球通知框素材
	private Bitmap bmpBadgesWin = BitmapFactory.decodeResource(
			this.getResources(), R.drawable.badges_win);// 球球胜利徽章素材
	private Bitmap bmpBadgesFaile = BitmapFactory.decodeResource(
			this.getResources(), R.drawable.badges_faile);// 球球失败徽章素材
	// button
	private Bitmap bmpBtnAveta = BitmapFactory.decodeResource(
			this.getResources(), R.drawable.button_aveta);// 球球胜利徽章素材
	private Bitmap bmpBtnLaunch = BitmapFactory.decodeResource(
			this.getResources(), R.drawable.button_launch);// 球球失败徽章素材
	// Music
	private GameMusic gameMusic;
	// callback
	protected OnEndOfGameInterface mOnEndOfGame; // callback interface

	/**
	 * callback interface
	 */
	public interface OnEndOfGameInterface {
		public void onEndOfGame();
	}

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mOnEndOfGame = (OnEndOfGameInterface) context;
		gameMusic = new GameMusic(context);
		gameMusic.starMusic(GameMusic.BGM);
		// 实例SurfaceHolder
		sfh = this.getHolder();
		// 为SurfaceView添加状态监听
		sfh.addCallback(this);
		// 实例一个画笔
		paint = new Paint();
		ptRockerPosition = new Point(ptRockerCtrlPoint);
		// 设置消除锯齿
		paint.setAntiAlias(true);
		// 实例一个画笔
		paintFont = new Paint();
		paintFont.setAntiAlias(true);
		// 设置消除锯齿
		setFocusable(true);
		// 设置焦点
	}

	/**
	 * 【Name: "感情淡了放盐啊"】 【speed = * 4F】 【 grow =20f】【AiDifficult = 6】【color
	 * = 8】【oldBestScore】
	 * 
	 * @param Name speed grow Difficule color oldBestScore
	 * @return
	 */
	public static void Setting(String Name, float speed, float grow,
			float Difficule, int color, String oldBestScore) {
		// user customer
		if (Name.length() == 0) {
			Name = "你个傻瓜没写名字";
		}
		if (color > 6 || color < 0) {
			color = 10;
		}
		bestScore = Integer.parseInt(oldBestScore);
		ballName = Name;// 感情淡了放盐啊
		ballMoveSpeed = speed / 10F;// ballGrowSpeed
		ballGrowSpeed = grow / 10F;// ballMoveSpeed
		ballColorIndex = color;// playerColor
		aiDifficult = Difficule;// playerColor
		timeSecond = 0;
		timeMinute = 1;
		timeGame = 320;
	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Clock Record
		timeBegin = System.currentTimeMillis();
		timeNewRaceBegin = getClock() - timeNewRaceRange;
		score = 0;
		// screen size
		screenW = this.getWidth();
		screenH = this.getHeight();
		// initialization player aiBall
		myBall = new Myball(mapW / 2, mapH / 2,
				getColorbyIndex(ballColorIndex), ballDefaultWeight * 8,
				ballName, ballDefaultLife);
		myBall.ID = 1;
		// initialization food Ball
		for (index = 0; index < FoodBallList.length; index++) {
			FoodBallList[index] = new FoodBall((int) (mapW * Math.random()),
					(int) (mapH * Math.random()), getColorRandom());
		}
		// initialization AI Ball
		for (index = 0; index < AiBallList.length; index++) {
			AiBallList[index] = new ActionBall(
					(int) (mapW * Math.random()),
					(int) (mapH * Math.random()),
					getColorRandom(),
					(float) (ballDefaultWeight * Math.random() + ballDefaultWeight),
					strArrayName[index], ballDefaultLife);
		}
		// 启动线程flag
		flagGameThread = true;
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
		flagGameThread = false;
		gameMusic.stopMusic();
		gameMusic.recycle();
		// 销毁画图线程
		// if (bestScore < score) {
		// bestScore = score;
		// }

		// System.out.println("surf de" + bestScore + "surf run" + score);
		// MainActivity.Setting(ballName, ballMoveSpeed * 10, ballGrowSpeed *
		// 10,
		// aiDifficult, ballColorIndex, bestScore);
	}

	/**
	 * 触屏事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		index2 = event.getPointerCount();
		if (index2 > 1) {
			for (index = 0; index < index2; index++) {
				if (flagButtonIndex == index
						&& event.getX(index) > (screenW - bmpBtnLaunch
								.getWidth() * 2)
						&& event.getX(index) < (screenW - bmpBtnLaunch
								.getWidth())
						&& event.getY(index) > (screenH - bmpBtnLaunch
								.getHeight())) {
					launchBubble(myBall);
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// System.out.println("---按下----");

					if (event.getX(index) > (screenW - bmpBtnLaunch.getWidth() * 2)
							&& event.getY(index) > (screenH - bmpBtnLaunch
									.getHeight())) {
						if (event.getX(index) < (screenW - bmpBtnLaunch
								.getWidth())) {
							gameMusic.starMusic(GameMusic.BUBBLE);
							launchBubble(myBall);
						} else {
							myBall.avatar();
							gameMusic.starMusic(GameMusic.AVATAR);
						}
						flagButtonIndex = index;
						break;
					} else {
						flagRockerDisplay = true;
						ptRockerCtrlPoint.set((int) event.getX(index),
								(int) event.getY(index));
						ptRockerPosition.set((int) event.getX(index) + 1,
								(int) event.getY(index));
					}
				case MotionEvent.ACTION_UP:
					// System.out.println("----放开----");
					flagIsTouchLongMove = true;
					flagRockerDisplay = false;
					flagButtonIndex = -1;
					if (flagGameOver) {
						System.out.println("over");
						timeNewRaceBegin -= 1000;
					}
					if (Math.abs(event.getX(index) - screenW / 2) < 150
							&& (screenH / 2 - event.getY(index)) < 50) {
						System.out.println("overxxx");
						timeNewRaceBegin -= 1000;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (flagButtonIndex == index) {
						break;
					}
					flagRockerDisplay = true;
					// System.out.println("----移动----");
					if (event.getPointerCount() == 1
							&& !getClockIsInRange(timeNewRaceBegin,
									timeNewRaceRange)) {
						int len = MathUtils.getLength(ptRockerCtrlPoint.x,
								ptRockerCtrlPoint.y, event.getX(index),
								event.getY(index));
						if (len < 20 && flagIsTouchLongMove) {
							// 如果屏幕接触点不在摇杆挥动范围内,则不处理
							break;
						}
						if (len <= rockerActionRadius) {
							// 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
							flagIsTouchLongMove = false;
							ptRockerPosition.set((int) event.getX(index),
									(int) event.getY(index));

						} else {
							// 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
							flagIsTouchLongMove = false;
							ptRockerPosition = MathUtils.getBorderPoint(
									ptRockerCtrlPoint,
									new Point((int) event.getX(index),
											(int) event.getY(index)),
									rockerActionRadius);
						}

						myBall.dectionTarget = MathUtils.getRadian(
								ptRockerCtrlPoint, ptRockerPosition);

					}
					break;

				default:
					break;
				}
			}

		} else {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// System.out.println("---按下----");
				if (event.getX() > (screenW - bmpBtnLaunch.getWidth() * 2)
						&& event.getY() > (screenH - bmpBtnLaunch.getHeight())) {
					if (event.getX() < (screenW - bmpBtnLaunch.getWidth())) {
						gameMusic.starMusic(GameMusic.BUBBLE);
						if (myBall.isAvatar != 0) {
							for (ActionBall avata : myBall.myAvatars) {
								launchBubble(avata);
							}
						} else {
							launchBubble(myBall);
						}
					} else {
						myBall.avatar();
						gameMusic.starMusic(GameMusic.AVATAR);
					}
					flagButtonIndex = 1;
					break;
				} else {
					flagRockerDisplay = true;
					ptRockerCtrlPoint.set((int) event.getX(),
							(int) event.getY());
					ptRockerPosition.set((int) event.getX() + 1,
							(int) event.getY());
				}
			case MotionEvent.ACTION_UP:
				// System.out.println("----放开----");
				flagIsTouchLongMove = true;
				flagRockerDisplay = false;
				flagButtonIndex = -1;
				if (flagGameOver) {
					System.out.println("over");
					timeNewRaceBegin -= 1000;
				}
				if (Math.abs(event.getX() - screenW / 2) < 150
						&& (screenH / 2 - event.getY()) < 50) {
					System.out.println("overxxx");
					timeNewRaceBegin -= 1000;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (flagButtonIndex == 1) {
					break;
				}
				flagRockerDisplay = true;
				// System.out.println("----移动----");
				if (event.getPointerCount() == 1
						&& !getClockIsInRange(timeNewRaceBegin,
								timeNewRaceRange)) {
					int len = MathUtils.getLength(ptRockerCtrlPoint.x,
							ptRockerCtrlPoint.y, event.getX(), event.getY());
					if (len < 20 && flagIsTouchLongMove) {
						// 如果屏幕接触点不在摇杆挥动范围内,则不处理
						break;
					}
					if (len <= rockerActionRadius) {
						// 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
						flagIsTouchLongMove = false;
						ptRockerPosition.set((int) event.getX(),
								(int) event.getY());

					} else {
						// 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
						flagIsTouchLongMove = false;
						ptRockerPosition = MathUtils.getBorderPoint(
								ptRockerCtrlPoint,
								new Point((int) event.getX(), (int) event
										.getY()), rockerActionRadius);
					}

					myBall.dectionTarget = MathUtils.getRadian(
							ptRockerCtrlPoint, ptRockerPosition);

				}
				break;

			default:
				break;
			}
		}
		return true;

	}

	/**
	 * 按键事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {// 画面转换位置移动动画效果
			ptRockerCtrlPoint.x = 100;
			ptRockerCtrlPoint.y = screenH - 100;
			ptRockerPosition.x = 100 + 65;
			ptRockerPosition.y = screenH - 100;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// 画面转移旋转动画效果
			ptRockerCtrlPoint.x = 100;
			ptRockerCtrlPoint.y = screenH - 100;
			ptRockerPosition.x = 100;
			ptRockerPosition.y = screenH - 100 + 65;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// 画面转移旋转动画效果
			ptRockerCtrlPoint.x = 100;
			ptRockerCtrlPoint.y = screenH - 100;
			ptRockerPosition.x = 100;
			ptRockerPosition.y = screenH - 100 - 65;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// 画面转移旋转动画效果
			ptRockerCtrlPoint.x = 100;
			ptRockerCtrlPoint.y = screenH - 100;
			ptRockerPosition.x = 100 - 65;
			ptRockerPosition.y = screenH - 100;
		}
		myBall.dectionTarget = MathUtils.getRadian(ptRockerCtrlPoint,
				ptRockerPosition);

		return super.onKeyDown(keyCode, event);

	}

	public boolean launchBubble(ActionBall launchBall) {
		if (launchBall.state != 0 && launchBall.weight > 1300) {
			BubbleList.add(new Bubble(launchBall));
			return true;
		}
		return false;
	}

	/**
	 * 进程控制
	 */
	@Override
	public void run() {
		while (flagGameThread) {
			// long start = System.currentTimeMillis();
			// if (myBall.life < 1 || myBall.life
			// ==(ballAiCount+1)*ballDefaultLife) {
			// if (myBall.life ==(ballAiCount+1)*ballDefaultLife) {
			// // System.out.println("GAME WIN");
			// flagGameOver_Win = true;
			// timeNewRaceBegin = getClock();
			// } else if (myBall.life < 1) {
			// // System.out.println("GAME OVER");
			// flagGameOver_Win = false;
			// }
			//
			// }
			if (flagGameOver
					&& !getClockIsInRange(timeNewRaceBegin, timeNewRaceRange)) {
				// 满足死亡的生命数，且不在等待时间内

				// 游戏结束
				if (bestScore < score) {
					// bestScore
					bestScore = score;
				}

				// System.out.println("surf run" + bestScore + "surf run" +
				// score);
				// MainActivity.Setting(ballName, ballMoveSpeed * 10,
				// ballGrowSpeed * 10, aiDifficult, ballColorIndex,
				// bestScore);

				flagGameThread = false;
				mOnEndOfGame.onEndOfGame();
				// 结束进程
				break;
			} else {
				logic();
				myDraw();
				timeSecond = (timeGame - (int) getClock() / 1000) % 60;
				timeMinute = (timeGame - (int) getClock() / 1000) / 60;
				if (timeSecond < 0) {
					timeSecond = 0;
					if (!flagGameOver) {
						timeNewRaceBegin = getClock();
					}
					flagGameOver = true;
				}
			}

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
	}

	/**
	 * 游戏绘图
	 */
	private void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			// 初始化canvas
			paintFont.setColor(Color.WHITE);
			// 名称颜色
			if (canvas != null) {
				canvas.save();
				// 视野调整
				canvas.translate((float) (0 - myBall.positionX + screenW / 2),
						(float) (0 - myBall.positionY + screenH / 2));
				// 以玩家为中心
				canvas.scale((3 / (myBall.radius / 15) + 0.4F),
						(3 / (myBall.radius / 15) + 0.4F),
						(float) myBall.positionX, (float) myBall.positionY);
				// 适应性的缩放
				DrawBackground();
				// 绘制背景
				for (FoodBall foodBall : FoodBallList) {
					// 绘制食物小球
					paint.setColor(getResources().getColor(foodBall.colorDraw));
					canvas.drawCircle((float) foodBall.positionX,
							(float) foodBall.positionY, foodBall.radius, paint);
				}
				try {
					// 泡泡
					for (Bubble bubble : BubbleList) {
						if (bubble.equals(null)) {
							break;
						}
						bubble.move();
						paint.setColor(getResources()
								.getColor(bubble.colorDraw));
						canvas.drawCircle((float) bubble.positionX,
								(float) bubble.positionY, bubble.radius, paint);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(BubbleList);
					// TODO: handle exception
				}

				for (ActionBall aiBall : AiBallList) {
					// 绘制AI大球
					if (aiBall.life == 0) {
						// 是否还有生命值
						continue;
					}
					if (aiBall.state == 1) {
						// AI是否还活着
						if (getClockIsInRange(aiBall) && aiBall.state != 0) {
							// 绘制AI保护罩
							if (getClock() % (frameRate * 12) > frameRate * 6) {

								paint.setColor(getResources().getColor(
										R.color.safeLight));
								// 保护罩颜色
								canvas.drawCircle((float) aiBall.positionX,
										(float) aiBall.positionY,
										(aiBall.radius * 1.23F), paint);
								// 绘制保护罩形状
							}
						}
						paint.setColor(getResources()
								.getColor(aiBall.colorDraw));
						// AI皮肤颜色
						canvas.drawCircle((float) aiBall.positionX,
								(float) aiBall.positionY, aiBall.radius, paint);
						// 绘制AI大球
						paintFont
								.setTextSize(aiBall.radius > 40 ? (20 + ((int) aiBall.radius > 90 ? 15
										: aiBall.radius / 6))
										: 23);
						// 名称字体大小
						canvas.drawText(
								aiBall.name,
								(float) aiBall.positionX
										- paintFont.measureText(aiBall.name)
										/ 2,
								(float) aiBall.positionY
										+ (aiBall.radius > 40 ? (20 + ((int) aiBall.radius > 90 ? 15
												: aiBall.radius / 6))
												: 23) / 4, paintFont);
						// 绘制名称
					}
				}
				if (myBall.state != 0) {
					// 绘制角色球
					if (myBall.isAvatar == 0) {
						drawBall(myBall);
					} else {
						for (ActionBall avatar : myBall.myAvatars) {
							drawBall(avatar);
						}
					}
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
				// button
				canvas.drawBitmap(bmpBtnAveta,
						screenW - bmpBtnAveta.getWidth(),
						screenH - bmpBtnAveta.getHeight(), paint);
				canvas.drawBitmap(
						bmpBtnLaunch,
						screenW - bmpBtnAveta.getWidth()
								- bmpBtnAveta.getWidth(),
						screenH - bmpBtnAveta.getHeight(), paint);

				// score计分板
				paint.setColor(getResources().getColor(R.color.rockerRudder));
				paintFont.setTextSize((float) (0.7 * itemH));
				paintFont.setColor(getResources().getColor(
						R.color.white_transparent));
				canvas.drawRect(5, 5, itemW, itemH + 5, paint);
				// canvas.drawText("score:" + (score + myBall.weight - 30)
				// + "   Weight:" + myBall.weight + "   Size:"
				// + myBall.radius + "   EatCount:" + myBall.eatCount, 40,
				// 45, paintFont);
				canvas.drawText("score:" + score, 30, 28, paintFont);
				canvas.drawRect(5, itemH + 5, itemW, itemH + itemH + 5, paint);
				canvas.drawText("Weight:" + myBall.weight, 30, 28 + itemH,
						paintFont);
				// 倒计时
				// System.out.println(timeMinute + "ssss" + timeSecond);
				if (timeMinute > 9 && timeSecond > 9) {
					canvas.drawText(timeMinute + ":" + timeSecond,
							screenW / 2 - 25, 28, paintFont);
				} else if (timeMinute > 9) {
					canvas.drawText(timeMinute + ":0" + timeSecond,
							screenW / 2 - 25, 28, paintFont);
				} else if (timeSecond > 9) {
					canvas.drawText("0" + timeMinute + ":" + timeSecond,
							screenW / 2 - 25, 28, paintFont);
				} else {
					canvas.drawText("0" + timeMinute + ":0" + timeSecond,
							screenW / 2 - 25, 28, paintFont);
				}
				// rank排行榜
				canvas.drawRect(screenW - itemW - 5, 5, screenW - 5, 26, paint);
				for (index = 0, index2 = 0; index2 < 10; index2++) {
					if (rankList[index] < rankList.length - 1) {
						ActionBall ball = AiBallList[rankList[index]];
						if (ball.life < 1) {
							continue;
						}
						// rank bg Rect
						canvas.drawRect(screenW - itemW - 5,
								index * itemH + 26, screenW - 5, (index + 1)
										* itemH + 26, paint);
						// rank text
						canvas.drawText(
								(String) (ball.name.length() > 5 ? ball.name
										.subSequence(0, 5) : ball.name),
								screenW - itemW + 50, (float) (50 + index
										* itemH), paintFont);
						canvas.drawText("" + ball.life, screenW - 35,
								(float) (50 + index * itemH), paintFont);

						index++;
					} else {
						ActionBall ball = myBall;
						// rank bg Rect
						canvas.drawRect(screenW - itemW - 5,
								index * itemH + 26, screenW - 5, (index + 1)
										* itemH + 26, paint);
						// rank text
						paintFont.setColor(getResources().getColor(
								R.color.color1));
						canvas.drawText(
								(String) (ball.name.length() > 5 ? ball.name
										.subSequence(0, 5) : ball.name),
								screenW - itemW + 50, (float) (50 + index
										* itemH), paintFont);
						canvas.drawText("" + ball.life, screenW - 35,
								(float) (50 + index * itemH), paintFont);
						paintFont.setColor(getResources().getColor(
								R.color.white_transparent));

						index++;
					}

				}
				// rank Bottom Rect
				canvas.drawRect(screenW - itemW - 5, index * itemH + 26,
						screenW - 5, (index + 1) * itemH, paint);
				// bmp Rank ICO
				canvas.clipRect(screenW - itemW - 5, 5, screenW - 5,
						(index + 1) * itemH - 7);
				canvas.drawBitmap(bmpRank, screenW - itemW, 13, paint);
				canvas.restore();

				canvas.save();
				// bmpBadges & bmpInfo
				if (getClockIsInRange(timeNewRaceBegin, timeNewRaceRange)) {
					if (flagGameOver) {
						// 游戏结束
						if (myBall.life < 2) {
							paint.setColor(getResources().getColor(
									R.color.rockerRudder));
							canvas.drawRect(0, 0, screenW, screenH, paint);
							paint.setColor(0xffffffff);
							canvas.drawBitmap(bmpBadgesFaile, 0, 0, paint);
						} else if (myBall.life == (ballAiCount + 1)
								* ballDefaultLife) {
							paint.setColor(getResources().getColor(
									R.color.black_win));
							canvas.drawRect(0, 0, screenW, screenH, paint);
							paint.setColor(0xffffffff);
							canvas.drawBitmap(bmpBadgesWin, 0, 0, paint);
						} else {
							paint.setColor(getResources().getColor(
									R.color.rockerRudder));
							canvas.drawRect(0, 0, screenW, screenH, paint);
							paint.setColor(0xffffffff);
							canvas.drawBitmap(bmpBadgesFaile, 0, 0, paint);
						}
					} else {
						// 被吃掉
						paintFont.setTextSize(40);
						float len = paintFont.measureText("你被")
								+ paintFont
										.measureText(AiBallList[myBall.eatByID].name)
								+ paintFont.measureText("吃掉了");
						paint.setColor(0xffffffff);
						canvas.drawBitmap(bmpInfo,
								screenW / 2 - bmpInfo.getWidth() / 2, screenH
										/ 2 - bmpInfo.getHeight(), paint);
						paintFont.setColor(getResources().getColor(
								R.color.font_deep));
						canvas.drawText("你被", screenW / 2 - len / 2, screenH
								/ 2 - bmpInfo.getHeight() + 70, paintFont);
						canvas.drawText(
								"吃掉了",
								(screenW / 2)
										- (len / 2)
										+ paintFont.measureText("你被")
										+ paintFont
												.measureText(AiBallList[myBall.eatByID].name),
								screenH / 2 - bmpInfo.getHeight() + 70,
								paintFont);
						paintFont.setColor(0xff6b543a);
						canvas.drawText(
								AiBallList[myBall.eatByID].name,
								screenW / 2 - len / 2
										+ paintFont.measureText("你被"), screenH
										/ 2 - bmpInfo.getHeight() + 70,
								paintFont);

					}

				}
				canvas.restore();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	private void drawBall(ActionBall drawRoleBall) {
		if (getClockIsInRange(drawRoleBall)) {
			// 保护罩
			if (getClock() % (frameRate * 12) > frameRate * 6) {
				// Shader mShader = new RadialGradient(
				// (float) myBall.positionX,
				// (float) myBall.positionY, myBall.radius * 1.5F,
				// Color.WHITE, getResources().getColor(
				// R.color.safeLight),
				// Shader.TileMode.CLAMP);
				// paint.setShader(mShader);
				paint.setColor(getResources().getColor(R.color.safeLight));
				canvas.drawCircle((float) drawRoleBall.positionX,
						(float) drawRoleBall.positionY,
						(drawRoleBall.radius * 1.23F), paint);
			}
		}
		paint.setColor(getResources().getColor(drawRoleBall.colorDraw));
		canvas.drawCircle((float) drawRoleBall.positionX,
				(float) drawRoleBall.positionY, drawRoleBall.radius, paint);// 绘制角色球
		// dir
		if (drawRoleBall.dectionTarget != 404) {
			Matrix matrix = new Matrix();
			// 设置旋转角度
			matrix.postScale(drawRoleBall.radius / 230,
					drawRoleBall.radius / 230, bmpDir.getWidth() / 2,
					bmpDir.getHeight() / 2);
			matrix.postRotate(
					(float) (drawRoleBall.dection * (180 / Math.PI)) + 90,
					bmpDir.getWidth() / 2, bmpDir.getHeight() / 2);
			matrix.postTranslate(
					(float) drawRoleBall.positionX
							- bmpDir.getWidth()
							/ 2
							+ (int) (drawRoleBall.radius * 1.25 * Math
									.cos(drawRoleBall.dection)),
					(float) drawRoleBall.positionY
							- bmpDir.getHeight()
							/ 2
							+ (int) (drawRoleBall.radius * 1.25 * Math
									.sin(drawRoleBall.dection)));
			// 绘制旋转图片
			canvas.drawBitmap(bmpDir, matrix, paint);
		}
		paintFont
				.setTextSize(drawRoleBall.radius > 40 ? (20 + (drawRoleBall.radius > 90 ? 15
						: drawRoleBall.radius / 6))
						: 23);
		// 绘制角色名称字体大小
		canvas.drawText(
				drawRoleBall.name,
				(float) drawRoleBall.positionX
						- paintFont.measureText(drawRoleBall.name) / 2,
				(float) drawRoleBall.positionY
						+ (drawRoleBall.radius > 40 ? (20 + (drawRoleBall.radius > 90 ? 15
								: drawRoleBall.radius / 6))
								: 23) / 4, paintFont);
		// 绘制角色名称
	}

	/**
	 * 游戏逻辑
	 */
	private final void logic() {
		// System.out.println("logic=====================================================");
		if (ptRockerCtrlPoint.equals(ptRockerPosition)) {
			flagRockerDisplay = false;
		}
		if (!getClockIsInRange(timeNewRaceBegin, timeNewRaceRange)) {
			if (myBall.state == 0) {
				gameMusic.stopBGM();
				gameMusic.restarBGM();
			}
			// 角色球
			myBall.action();
			myBall.move((float) Math
					.sqrt((ptRockerPosition.x - ptRockerCtrlPoint.x)
							* (ptRockerPosition.x - ptRockerCtrlPoint.x)
							+ (ptRockerPosition.y - ptRockerCtrlPoint.y)
							* (ptRockerPosition.y - ptRockerCtrlPoint.y))
					/ rockerActionRadius);
			if (myBall.life == 0
					|| myBall.life == (ballAiCount + 1) * ballDefaultLife) {
				flagGameOver = true;
				timeNewRaceBegin = getClock();
			}
			// System.out.println("myBall.life"+myBall.life);

		} else {
			// 锁定遥感
			ptRockerPosition.y = ptRockerCtrlPoint.y;
			ptRockerPosition.x = ptRockerCtrlPoint.x;
			flagRockerDisplay = false;
		}
		try {
			// 泡泡
			for (int index = 0; index < BubbleList.size(); index++) {
				Bubble bubble = BubbleList.get(index);
				if (bubble.equals(null)) {
					break;
				}
				if (myBall.isAvatar == 0) {
					if ((bubble.positionX - myBall.positionX)
							* (bubble.positionX - myBall.positionX)
							+ (bubble.positionY - myBall.positionY)
							* (bubble.positionY - myBall.positionY) < (myBall.radius)
							* (myBall.radius)) {
						// 判断是否被吃
						myBall.weight += bubble.weight;
						BubbleList.remove(index--);
						continue;
					}
				} else {
					for (ActionBall avata : myBall.myAvatars) {
						if ((bubble.positionX - avata.positionX)
								* (bubble.positionX - avata.positionX)
								+ (bubble.positionY - avata.positionY)
								* (bubble.positionY - avata.positionY) < (avata.radius)
								* (avata.radius)) {
							// 判断是否被吃
							avata.weight += bubble.weight;
							BubbleList.remove(index--);
							continue;
						}
					}
				}

				for (ActionBall aiBall2 : AiBallList) {
					// 判断是否被吃
					if ((bubble.positionX - aiBall2.positionX)
							* (bubble.positionX - aiBall2.positionX)
							+ (bubble.positionY - aiBall2.positionY)
							* (bubble.positionY - aiBall2.positionY) < (aiBall2.radius)
							* (aiBall2.radius)) {
						aiBall2.weight += bubble.weight;
						BubbleList.remove(index--);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(BubbleList);
			// TODO: handle exception
		}
		for (FoodBall foodBall : FoodBallList) {
			// 食物小球
			if (myBall.isAvatar == 0) {
				if ((foodBall.positionX - myBall.positionX)
						* (foodBall.positionX - myBall.positionX)
						+ (foodBall.positionY - myBall.positionY)
						* (foodBall.positionY - myBall.positionY) < (myBall.radius)
						* (myBall.radius)) {
					// 判断是否被吃
					foodBall.state = 0;
					myBall.weight += ballGrowSpeed;
					score += ballGrowSpeed / 10;
				}
			} else {
				for (ActionBall avata : myBall.myAvatars) {
					if ((foodBall.positionX - avata.positionX)
							* (foodBall.positionX - avata.positionX)
							+ (foodBall.positionY - avata.positionY)
							* (foodBall.positionY - avata.positionY) < (avata.radius)
							* (avata.radius)) {
						// 判断是否被吃
						foodBall.state = 0;
						avata.weight += ballGrowSpeed;
						score += ballGrowSpeed / 10;
					}
				}
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
			if (foodBall.state == 0) {
				// 重置
				foodBall.reSetBall((int) (mapW * Math.random()),
						(int) (mapH * Math.random()), getColorRandom());
			}
		}
		for (index = 0; index < AiBallList.length; index++) {
			// AI大球
			ActionBall aiBall = AiBallList[index];
			// System.out.println(aiBall.name+"life:"+aiBall.life);
			if (myBall.isAvatar == 0) {
				if (aiBall.life > 0
						&& ((aiBall.positionX - myBall.positionX)
								* (aiBall.positionX - myBall.positionX)
								+ (aiBall.positionY - myBall.positionY)
								* (aiBall.positionY - myBall.positionY) < (myBall.radius)
								* (myBall.radius) || (aiBall.positionX - myBall.positionX)
								* (aiBall.positionX - myBall.positionX)
								+ (aiBall.positionY - myBall.positionY)
								* (aiBall.positionY - myBall.positionY) < (aiBall.radius)
								* (aiBall.radius))) {
					// AI吃主角
					if (aiBall.radius > (myBall.radius + myBall.radius / 10)
							&& !getClockIsInRange(myBall)
							&& !getClockIsInRange(timeNewRaceBegin,
									timeNewRaceRange)) {
						if (aiBall.name == "二狗子") {
							continue;
						}
						// AI吃主角
						// 判断是否无敌时间
						// 主角死亡标记
						myBall.state = 0;
						myBall.eatByID = index;
						if (myBall.life > 1) {
							timeNewRaceBegin = getClock();
						}
						myBall.timeBallSafeBegin = getClock()
								+ timeNewRaceRange;
						// 启动保护罩
						aiBall.life++;
						aiBall.weight += myBall.weight;
						myBall.weight = 0;
						aiBall.eatCount++;
						gameMusic.starMusic(GameMusic.EAT_DEFAULT);

					} else if (myBall.radius > (aiBall.radius + aiBall.radius / 10)
							&& !getClockIsInRange(aiBall)) {
						// 主角吃AI
						aiBall.state = 0;
						aiBall.timeBallSafeBegin = getClock();
						// 启动保护罩
						myBall.eatCount++;
						myBall.life++;
						myBall.weight += aiBall.weight;
						aiBall.weight = 0;
						score += aiBall.weight / 10;
						gameMusic.starMusic(GameMusic.EAT_ZBZG);
					}

				}
			} else {
				for (int index = 0; index < myBall.myAvatars.size(); index++) {
					ActionBall avata = myBall.myAvatars.get(index);
					if (aiBall.life > 0
							&& ((aiBall.positionX - avata.positionX)
									* (aiBall.positionX - avata.positionX)
									+ (aiBall.positionY - avata.positionY)
									* (aiBall.positionY - avata.positionY) < (avata.radius)
									* (avata.radius) || (aiBall.positionX - avata.positionX)
									* (aiBall.positionX - avata.positionX)
									+ (aiBall.positionY - avata.positionY)
									* (aiBall.positionY - avata.positionY) < (aiBall.radius)
									* (aiBall.radius))) {
						// AI吃主角
						if (aiBall.radius > (avata.radius + avata.radius / 10)
								&& !getClockIsInRange(avata)
								&& !getClockIsInRange(timeNewRaceBegin,
										timeNewRaceRange)) {
							if (aiBall.name == "二狗子"
									&& myBall.myAvatars.size() < 2) {
								continue;
							}
							// AI吃主角
							// 判断是否无敌时间
							// 主角死亡标记
							if (avata.life > 1) {
								timeNewRaceBegin = getClock();
							}
							avata.timeBallSafeBegin = getClock()
									+ timeNewRaceRange;
							// 启动保护罩
							aiBall.life++;
							aiBall.weight += avata.weight;
							aiBall.eatCount++;
							gameMusic.starMusic(GameMusic.EAT_DEFAULT);
							myBall.myAvatars.remove(index--);
							myBall.isAvatar--;
							if (myBall.myAvatars.size() == 0) {
								// 主角死亡标记
								myBall.state = 0;
								myBall.eatByID = index;
								if (myBall.life > 1) {
									timeNewRaceBegin = getClock();
								}
								myBall.timeBallSafeBegin = getClock()
										+ timeNewRaceRange;
								// 启动保护罩
							}
							continue;
						} else if (avata.radius > (aiBall.radius + aiBall.radius / 10)
								&& !getClockIsInRange(aiBall)) {
							// 主角吃AI
							aiBall.state = 0;
							aiBall.timeBallSafeBegin = getClock();
							// 启动保护罩
							avata.eatCount++;
							avata.life++;
							avata.weight += aiBall.weight;
							aiBall.weight = 0;
							score += aiBall.weight / 10;
							gameMusic.starMusic(GameMusic.EAT_ZBZG);
						}

					}
				}
			}

			// 排名
			rank = 0;
			for (index2 = 0; index2 < AiBallList.length; index2++) {
				if (AiBallList[index].weight < AiBallList[index2].weight) {
					rank++;
				}
			}
			rankList[rank] = index;
			if (aiBall.life == 0) {
				// 生命值为空
				continue;
			}
			if (aiBall.state != 0) {
				for (ActionBall aiBall2 : AiBallList) {
					if (aiBall2.equals(aiBall)) {
						// 是否同一个AI
						continue;
					}
					// 判断是否被吃
					if (aiBall.life > 0
							&& aiBall2.life > 0
							&& ((aiBall.positionX - aiBall2.positionX)
									* (aiBall.positionX - aiBall2.positionX)
									+ (aiBall.positionY - aiBall2.positionY)
									* (aiBall.positionY - aiBall2.positionY) < (aiBall.radius)
									* (aiBall.radius) || (aiBall.positionX - aiBall2.positionX)
									* (aiBall.positionX - aiBall2.positionX)
									+ (aiBall.positionY - aiBall2.positionY)
									* (aiBall.positionY - aiBall2.positionY) < (aiBall2.radius)
									* (aiBall2.radius))) {
						// 是否满足吃球的距离
						if (aiBall.radius > (aiBall2.radius + aiBall2.radius / 10)
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
			}
			aiBall.action();
			aiBall.moveRandom();
			if (aiBall.eatCount != 0) {
				aiBall.eatCount--;
				if (aiBall.eatCount % 15 == 0) {
					if (!launchBubble(aiBall)) {
						aiBall.eatCount = 0;
					}
				}
			} else {
				aiBall.eatCount = (int) (Math.random() * 8 * (1 / aiDifficult));
			}
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
		canvas.drawColor(getResources().getColor(R.color.background) + 5);
		// //绘制矩形
		paint.setColor(getResources().getColor(R.color.background));
		canvas.drawRect(0, 0, mapW, mapH, paint);
		int rowWidth = 5, rowInterval = 40;
		paint.setColor(getResources().getColor(R.color.backgroundStripe));
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
	private static final int getColorRandom() {
		return ballColor[(int) (Math.random() * ballColor.length)];
	}

	private static final int getColorbyIndex(int colorIndex) {
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
	 * 定义活动球球的类，即角色球
	 */
	private class ActionBall {
		public int ID = 0;
		int eatByID;
		int life;
		int state;
		int weight;
		int eatCount;
		int colorDraw;
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

		// // positionX, positionY, colorDraw, size, targetX, targetY,
		// nameString
		// ActionBall(double positionX, double positionY, int colorDraw,
		// float weight, String nameString) {
		// this.state = 1;// 未被吃
		// this.positionX = positionX;
		// this.positionY = positionY;
		// this.colorDraw = colorDraw;
		// this.eatCount = 0;
		// this.moveSpeed = ballMoveSpeed;
		// this.name = nameString;
		// this.targetX = positionX;
		// this.targetY = positionY;
		// this.weight = (int) weight;
		// }

		// positionX, positionY, colorDraw, size, targetX, targetY,
		// nameString,life
		ActionBall(double positionX, double positionY, int colorDraw,
				float weight, String nameString, int life) {
			timeBallSafeBegin = getClock();
			this.state = 1;// 未被吃
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorDraw = colorDraw;
			this.eatCount = 0;
			this.life = life;
			this.moveSpeed = ballMoveSpeed;
			this.name = nameString;
			this.targetX = positionX;
			this.targetY = positionY;
			this.weight = (int) weight;
			this.timeRandomActtionBegin = getClock() + 500;
		}

		// positionX, positionY, colorDraw, size
		void reSetBall(double positionX, double positionY, int colorDraw,
				float weight) {
			timeBallSafeBegin = getClock();
			// 启动保护罩
			if (life > 0) {
				this.state = 1;// 复活
				this.weight = (int) weight;
				this.positionX = positionX;
				this.positionY = positionY;
				this.colorDraw = colorDraw;
				this.targetX = positionX;
				this.targetY = positionY;
				this.radius = 0;
			} else {
				this.weight = 0;
				this.radius = 0;
			}
		}

		public void action() {
			if (state == 0) {
				// 死亡判断
				life--;
				reSetBall((int) (mapW * Math.random()),
						(int) (mapH * Math.random()), getColorRandom(),
						ballDefaultWeight);
			}
			if ((int) radius < (int) Math.sqrt(weight)) {
				// 阻尼增重
				radius += (Math.sqrt(weight) - radius) / actionDamping;
			}
			if ((int) radius > (int) Math.sqrt(weight)) {
				// 阻尼减重
				radius -= (radius - Math.sqrt(weight)) / actionDamping;
			}
			weight -= (int) radius / 100 * 5;
			// 损耗减重

			if (radius > 400) {
				// 角色球尺寸限制，重置尺寸
				weight = (int) ballDefaultWeight;
				timeBallSafeBegin = getClock();
				if (name == "二狗子") {
					weight = 120000;
				}
			}
		}

		public void moveRandom() {
			// action();
			if (!getClockIsInRange(timeRandomActtionBegin,
					timeRandomActtionRang)) {
				timeRandomActtionBegin = getClock();
				timeRandomActtionRang = (int) (Math.random() * 12000);
				// dectionTarget = (Math.random() * Math.PI * 2) - Math.PI;
				if (myBall.state != 0 && weight > myBall.weight) {
					dectionTarget = getRadian((float) positionX,
							(float) myBall.positionX, (float) positionY,
							(float) myBall.positionY);
				} else {
					dectionTarget = (Math.random() * Math.PI * 2) - Math.PI;
				}
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
			if (dectionTarget == 404) {
				return;
			} else {
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
					ptRockerPosition.x = ptRockerCtrlPoint.x;
					// myBall.targetX = 0;
					// ptRockerPosition.x = ptRockerCtrlPoint.x;

				}
				if (targetX > mapW) {
					// 边界判断
					targetX = mapW;
					dectionTarget = getRadian(ptRockerCtrlPoint.x,
							ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
							ptRockerPosition.y);
					ptRockerPosition.x = ptRockerCtrlPoint.x;
					// myBall.targetX = mapW;
					// ptRockerPosition.x = ptRockerCtrlPoint.x;
				}
				if (targetY < 0) {
					// 边界判断
					targetY = 0;
					dectionTarget = getRadian(ptRockerCtrlPoint.x,
							ptRockerPosition.x, ptRockerCtrlPoint.y,
							ptRockerCtrlPoint.y);
					ptRockerPosition.y = ptRockerCtrlPoint.y;
					// myBall.targetY = 0;
					// ptRockerPosition.y = ptRockerCtrlPoint.y;
				}
				if (targetY > mapH) {
					// 边界判断
					targetY = mapH;
					dectionTarget = getRadian(ptRockerCtrlPoint.x,
							ptRockerPosition.x, ptRockerCtrlPoint.y,
							ptRockerCtrlPoint.y);
					ptRockerPosition.y = ptRockerCtrlPoint.y;
					// // myBall.targetY = mapH;
					// ptRockerPosition.y = ptRockerCtrlPoint.y;
				}
				positionX += (targetX - positionX) / actionDamping;
				positionY += (targetY - positionY) / actionDamping;
			}

		}

		public float getRadian(float x1, float x2, float y1, float y2) {
			float lenA = x2 - x1;
			float lenB = y2 - y1;
			if (lenA == 0 && lenB == 0) {
				return 404;
			}
			float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
			float ang = (float) Math.acos(lenA / lenC);
			ang = ang * (y2 < y1 ? -1 : 1);
			return ang;
		}
	}

	/**
	 * 定义角色球球的类，即角色球
	 */
	private class Myball extends ActionBall {
		// array
		ArrayList<ActionBall> myAvatars;
		int isAvatar;

		Myball(double positionX, double positionY, int colorDraw, float weight,
				String nameString, int life) {
			super(positionX, positionY, colorDraw, weight, nameString, life);
			// TODO Auto-generated constructor stub
			isAvatar = 0;
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			if (state == 0) {
				// 死亡判断
				life--;
				isAvatar = 0;
				myAvatars = null;
				reSetBall((int) (mapW * Math.random()),
						(int) (mapH * Math.random()), getColorRandom(),
						ballDefaultWeight);
			}

			if (isAvatar == 0) {
				if ((int) radius < (int) Math.sqrt(weight)) {
					// 阻尼增重
					radius += (Math.sqrt(weight) - radius) / actionDamping;
				}
				if ((int) radius > (int) Math.sqrt(weight)) {
					// 阻尼减重
					radius -= (radius - Math.sqrt(weight)) / actionDamping;
				}
				weight -= (int) radius / 100 * 5;
				// 损耗减重

				if (radius > 400) {
					// 角色球尺寸限制，重置尺寸
					weight = (int) ballDefaultWeight;
					timeBallSafeBegin = getClock();
				}
			} else {
				for (ActionBall avatar : myAvatars) {
					if ((int) avatar.radius < (int) Math.sqrt(avatar.weight)) {
						// 阻尼增重
						avatar.radius += (Math.sqrt(avatar.weight) - avatar.radius)
								/ actionDamping;
					}
					if ((int) avatar.radius > (int) Math.sqrt(avatar.weight)) {
						// 阻尼减重
						avatar.radius -= (avatar.radius - Math
								.sqrt(avatar.weight)) / actionDamping;
					}
					avatar.weight -= (int) avatar.radius / 100 * 5;
					// 损耗减重
				}
			}
		}

		@Override
		public void move(float rocker) {
			// TODO Auto-generated method stub
			if (isAvatar == 0) {
				super.move(rocker);
			} else {
				targetX = 0;
				targetY = 0;
				for (ActionBall avatar : myAvatars) {
					avatar.dectionTarget = dectionTarget;
					if (dectionTarget == 404) {
						return;
					} else {
						avatar.dection += Math
								.abs((dectionTarget - avatar.dection)) < Math.PI ? (((dectionTarget - avatar.dection) / actionDamping))
								: ((dectionTarget - avatar.dection) > 0 ? -(Math
										.abs((dectionTarget - avatar.dection - 2 * Math.PI)) / actionDamping)
										: +(Math.abs((dectionTarget
												- avatar.dection + 2 * Math.PI)) / actionDamping));
						avatar.dection += (avatar.dection >= Math.PI) ? (-2 * Math.PI)
								: ((avatar.dection <= -Math.PI) ? (+2 * Math.PI)
										: 0);
						avatar.targetX += moveSpeed * Math.cos(dectionTarget)
								* (30 / avatar.radius * 1 + 0.6) * rocker;
						avatar.targetY += moveSpeed * Math.sin(dectionTarget)
								* (30 / avatar.radius * 1 + 0.6) * rocker;
						if (avatar.targetX < 0) {
							// 边界判断
							avatar.targetX = 0;
							// myBall.targetX = 0;
							// ptRockerPosition.x = ptRockerCtrlPoint.x;

						}
						if (avatar.targetX > mapW) {
							// 边界判断
							avatar.targetX = mapW;
							// myBall.targetX = mapW;
							// ptRockerPosition.x = ptRockerCtrlPoint.x;
						}
						if (avatar.targetY < 0) {
							// 边界判断
							avatar.targetY = 0;
							// myBall.targetY = 0;
							// ptRockerPosition.y = ptRockerCtrlPoint.y;
						}
						if (avatar.targetY > mapH) {
							// 边界判断
							avatar.targetY = mapH;
							// // myBall.targetY = mapH;
							// ptRockerPosition.y = ptRockerCtrlPoint.y;
						}
						avatar.positionX += (avatar.targetX - avatar.positionX)
								/ actionDamping;
						avatar.positionY += (avatar.targetY - avatar.positionY)
								/ actionDamping;
						targetX += avatar.positionX;
						targetY += avatar.positionY;
					}
				}
				positionX += (targetX / myAvatars.size() - positionX)
						/ actionDamping;
				positionY += (targetY / myAvatars.size() - positionY)
						/ actionDamping;
			}
		}

		public void avatar() {
			if (isAvatar > 15) {
				return;
			}
			// TODO Auto-generated method stub
			if (isAvatar == 0) {
				myAvatars = new ArrayList<MySurfaceView.ActionBall>();
				myAvatars.add(new ActionBall(positionX, positionY, colorDraw,
						weight, name, 1));
				isAvatar = 1;
			}
			for (int i = 0; i < isAvatar; i++) {
				ActionBall avatar = myAvatars.get(i);
				avatar.weight /= 2;
				myAvatars.add(new ActionBall(avatar.positionX
						+ Math.sqrt(weight) * 2 * Math.cos(dectionTarget),
						avatar.positionY + Math.sqrt(weight) * 2
								* Math.sin(dectionTarget), colorDraw,
						avatar.weight, name, 1));
			}
			isAvatar = myAvatars.size();
			// System.out.println("========***********************==========1111");
			for (ActionBall avatar : myBall.myAvatars) {
				avatar.timeBallSafeBegin = timeBallSafeBegin;
			}
			// System.out.println("========***********************==========1111");
		}
	}

	/**
	 * 定义泡泡的类
	 */
	private class Bubble {

		Bubble(ActionBall launchBall) {
			weight = (int) (radius * radius);
			launchBall.weight -= weight;
			this.colorDraw = launchBall.colorDraw;
			this.positionX = launchBall.positionX;
			this.positionY = launchBall.positionY;
			this.dection = launchBall.dectionTarget;
			this.positionX += (launchBall.radius + 12) * Math.cos(dection);
			this.positionY += (launchBall.radius + 12) * Math.sin(dection);
			this.moveSpeed = ballMoveSpeed;
			this.targetX = positionX;
			this.targetY = positionY;
			this.launchByID = myBall.ID;
		}

		public void move() {
			if (distance-- > 0) {
				targetX += moveSpeed * Math.cos(dection) * 4;
				targetY += moveSpeed * Math.sin(dection) * 4;
				if (targetX < 0) {
					// 边界判断
					targetX = 0;
				}
				if (targetX > mapW) {
					// 边界判断
					targetX = mapW;
				}
				if (targetY < 0) {
					// 边界判断
					targetY = 0;
				}
				if (targetY > mapH) {
					// 边界判断
					targetY = mapH;
				}
				positionX += (targetX - positionX) / (actionDamping / 2);
				positionY += (targetY - positionY) / (actionDamping / 2);
				state++;
			}
		}

		// int ID;
		int launchByID;
		int state = 0;
		int weight;
		int colorDraw;
		float moveSpeed;
		float radius = 22;
		double positionX;
		double positionY;
		double targetX;
		double targetY;
		double dection = 0;
		int distance = 20;

	}

}


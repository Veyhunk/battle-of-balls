package com.Veyhunk.Batter_of_Balls.SurfaceView;

import com.Veyhunk.Batter_of_Balls.R;
import com.Veyhunk.Batter_of_Balls.Utils.MathUtils;

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
	public static String ballName = "goodnight ����";// �й�����Э��᳤
	public static int ballColorIndex = 8;// playerColo
	public static int score = 0;// score
	public static float ballGrowSpeed = 20f;// ballGrowSpeed
	public static float ballMoveSpeed = 4F;// ballMoveSpeed
	public static float aiDifficult = 6;// AiDifficult
	// constant
	// private static final String strPalyerName = "goodnight ����";// �й�����Э��᳤
	// ����Сɵ��
	// private static final float ballGrowSpeed = 20f;// ballGrowSpeed
	// private static final float ballMoveSpeed = 4F;// ballMoveSpeed
	private static final int frameRate = 50;// ֡�ʣ���λ��Hz/s��
	private static final float ballDefaultSize = 2000;// ballDefaultSize
	private static final float actionDamping = 10;// �����
	private static final int mapW = 2500, mapH = 2000; // Map_size
	// private static final int ballColorIndex = 8;// playerColor
	private static final int[] ballColor = new int[] { R.color.color0,
			R.color.color1, R.color.color2, R.color.color3, R.color.color4,
			R.color.color5, R.color.color6 };// ��ɫ��
	private static final String[] strArrayName = new String[] { "����TV���",
			"������", "��ע�Ҵ���", "�����", "����", "����TV��ľ", "���þ���", "���ײ�������", "��ͫ ը��",
			"���ȵ�һ��" };
	// flag
	private boolean flagThread;// �߳������ı�ʶλ
	private boolean flagGameOver_Win;// �߳������ı�ʶλ
	private boolean flagIsTouchLongMove;// �Ƿ񳤰��ı�ʶλ
	private boolean flagRockerDisplay = false;// �Ƿ���ʾң�еı�ʶλ
	// time
	// private static Long CLOCK = (long) 0;
	private static Long timeBegin;
	// private static int timeBallSafeBegin = 0;
	private static int timeBallSafeRangeConstants = 5000;// ��ʼ��Ϸǰ���޵�ʱ�䣨��λ��ms��
	private static int timeNewRaceBegin = 0;
	private static int timeNewRaceRange = 3000;// ��ʼ��Ϸǰ���޵�ʱ�䣨��λ��ms��
	// variable
	// private static float AiDifficult = 6;// AiDifficult
	private float textLength; // �ı�����
	private int screenW, screenH; // Screen_size
	private int rank, index, index2;
	private int[] rankList = new int[10];
	private int rockerRudderRadius = 30;// ҡ�˰뾶
	private int rockerWheelRadius = 65;// ҡ�˻��Χ�뾶
	private Point ptRockerPosition;// ҡ��λ��
	private Point ptRockerCtrlPoint = new Point(0, 0);// ҡ����ʼλ��
	private SurfaceHolder sfh; // ���ڿ���SurfaceView
	private Paint paint;// ����һ������
	private Thread th;// ����һ���߳�
	private Canvas canvas;// ����һ������
	// ����Ball
	private ActionBall myBall;
	private FoodBall[] FoodBallList = new FoodBall[500];
	private ActionBall[] AiBallList = new ActionBall[10];
	// λͼ�ļ� bitmap
	private Bitmap bmpRank = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.rank);// ���а��ز�
	private Bitmap bmpDir = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.dir);// С��ָ���ز�
	// �ص�
	protected OnEndOfGameInterface mOnEndOfGame; // callback interface

	public void setOnEndOfGame(OnEndOfGameInterface xOnEndOfGame) {
		mOnEndOfGame = xOnEndOfGame;
	}

	public interface OnEndOfGameInterface {
		public void onEndOfGame();
	}

	/**
	 * SurfaceView��ʼ������
	 */
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ʵ��SurfaceHolder
		sfh = this.getHolder();
		// ΪSurfaceView���״̬����
		sfh.addCallback(this);
		// ʵ��һ������
		paint = new Paint();
		ptRockerPosition = new Point(ptRockerCtrlPoint);
		// �����������
		paint.setAntiAlias(true);
		// ���ý���
		setFocusable(true);
	}

	/**
	 * ��Name: "goodnight ����"�� �� grow =20f����speed = * 4F�� ��color =
	 * 8��[��AiDifficult = 6��
	 * 
	 * @param color2
	 * @return
	 */
	public static void Setting(String Name, float speed, float grow,
			float Difficule, int color) {
		// user customer
		if (Name.length() == 0) {
			Name = "���ɵ��ûд����";
		}
		if (color > 6 || color < 0) {
			color = 10;
		}
		ballName = Name;// �й�����Э��᳤
		ballMoveSpeed = speed / 10F;// ballGrowSpeed
		ballGrowSpeed = grow / 10F;// ballMoveSpeed
		ballColorIndex = color;// playerColor
		aiDifficult = Difficule;// playerColor
	}

	/**
	 * SurfaceView��ͼ��������Ӧ�˺���
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
		// �����߳�flag
		flagThread = true;
		// ʵ���߳�
		th = new Thread(this);
		// �����߳�
		th.start();
	}

	/**
	 * �����¼�����
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {// ����ת��λ���ƶ�����Ч��
	// myBall.targetY -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// ����ת����ת����Ч��
	// myBall.targetY += myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// ����ת����ת����Ч��
	// myBall.targetX -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// ����ת����ת����Ч��
	// myBall.targetX += myBall.moveSpeed * 20;
	// }
	// return super.onKeyDown(keyCode, event);
	//
	// }

	/**
	 * SurfaceView��ͼ״̬�����ı䣬��Ӧ�˺���
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * SurfaceView��ͼ����ʱ����Ӧ�˺���
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flagThread = false;
	}

	/**
	 * �����¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// System.out.println("---����----");
			ptRockerCtrlPoint.set((int) event.getRawX(), (int) event.getRawY());
			ptRockerPosition.set((int) event.getX(), (int) event.getY());
			break;
		case MotionEvent.ACTION_UP:
			// System.out.println("----�ſ�----");
			flagIsTouchLongMove = true;
			break;
		case MotionEvent.ACTION_MOVE:
			flagRockerDisplay = true;
			// System.out.println("----�ƶ�----");
			if (event.getPointerCount() == 1
					&& !getClockIsInRange(myBall.timeBallSafeBegin, 15)) {
				int len = MathUtils.getLength(ptRockerCtrlPoint.x,
						ptRockerCtrlPoint.y, event.getX(), event.getY());
				if (len < 20 && flagIsTouchLongMove) {
					// �����Ļ�Ӵ��㲻��ҡ�˻Ӷ���Χ��,�򲻴���
					break;
				}
				if (len <= rockerWheelRadius) {
					// �����ָ��ҡ�˻��Χ�ڣ���ҡ�˴�����ָ����λ��
					flagIsTouchLongMove = false;
					ptRockerPosition
							.set((int) event.getX(), (int) event.getY());

				} else {
					// ����ҡ��λ�ã�ʹ�䴦����ָ��������� ҡ�˻��Χ��Ե
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
	 * ���̿���
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
	 * �����¼�����
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {// ����ת��λ���ƶ�����Ч��
	// myBall.targetY -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// ����ת����ת����Ч��
	// myBall.targetY += myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// ����ת����ת����Ч��
	// myBall.targetX -= myBall.moveSpeed * 20;
	// } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// ����ת����ת����Ч��
	// myBall.targetX += myBall.moveSpeed * 20;
	// }
	// return super.onKeyDown(keyCode, event);
	//
	// }

	/**
	 * ��Ϸ��ͼ
	 */
	private void myDraw() {
		try {

			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.save();
				// ��Ұ����
				canvas.translate((float) (0 - myBall.positionX + screenW / 2),
						(float) (0 - myBall.positionY + screenH / 2));
				// �����Ϊ����
				canvas.scale((3 / (myBall.radius / 15) + 0.3F),
						(3 / (myBall.radius / 15) + 0.3F),
						(float) myBall.positionX, (float) myBall.positionY);
				// ��Ӧ�Ե�����
				DrawBackground();
				// ���Ʊ���

				for (FoodBall foodBall : FoodBallList) {
					// ����ʳ��С��
					if ((foodBall.positionX - myBall.positionX)
							* (foodBall.positionX - myBall.positionX)
							+ (foodBall.positionY - myBall.positionY)
							* (foodBall.positionY - myBall.positionY) < (myBall.radius)
							* (myBall.radius)) {
						// �ж��Ƿ񱻳�
						foodBall.state = 0;
						myBall.weight += ballGrowSpeed;
					}
					for (ActionBall aiBall2 : AiBallList) {
						// �ж��Ƿ񱻳�
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
						// ����
						canvas.drawCircle((float) foodBall.positionX,
								(float) foodBall.positionY, foodBall.radius,
								paint);
					} else {
						// ����
						foodBall.reSetBall((int) (mapW * Math.random()),
								(int) (mapH * Math.random()), randomColor());
					}
				}
				for (ActionBall aiBall : AiBallList) {
					// ����AI����
					if (aiBall.life < 1 || aiBall.state == 0) {
						// �Ƿ�������ֵ
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
						// �ж��Ƿ񱻳�
						if (aiBall.radius > (myBall.radius + myBall.radius / 10)
								&& !getClockIsInRange(myBall)
								&& !getClockIsInRange(timeNewRaceBegin,
										timeNewRaceRange)) {
							// �ж��Ƿ�AI��
							// �ж��Ƿ��޵�ʱ��
							// �����������
							myBall.state = 0;
							timeNewRaceBegin = getClock();
							myBall.timeBallSafeBegin = getClock()
									+ timeNewRaceRange;
							// ����������
							aiBall.life++;
							aiBall.weight += myBall.weight;
							aiBall.eatCount++;

						} else if (myBall.radius > (aiBall.radius + aiBall.radius / 10)
								&& !getClockIsInRange(aiBall)) {
							// �Ե�AI
							aiBall.state = 0;
							aiBall.timeBallSafeBegin = getClock();
							// ����������
							myBall.eatCount++;
							myBall.life++;
							myBall.weight += aiBall.weight;
						}

					}
					if (aiBall.state == 1) {
						// AI�Ƿ񻹻���
						for (ActionBall aiBall2 : AiBallList) {
							if (aiBall2.equals(aiBall)) {
								// �Ƿ�ͬһ��AI
								continue;
							}
							// �ж��Ƿ񱻳�
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
								// �Ƿ��������ľ���
								if (aiBall.radius > (aiBall2.radius + aiBall.radius / 10)
										&& !getClockIsInRange(aiBall2)) {
									// �ж��Ƿ񱻳�
									aiBall2.state = 0;
									aiBall2.timeBallSafeBegin = getClock();
									// ����������
									aiBall.life++;
									aiBall.weight += aiBall2.weight;
									aiBall.eatCount++;

								} else if ((aiBall.radius + aiBall.radius / 10) < aiBall2.radius
										&& !getClockIsInRange(aiBall)) {
									// �ж��Ƿ񱻳�
									aiBall.state = 0;
									aiBall.timeBallSafeBegin = getClock();
									// ����������
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
						// ����AI����
						paint.setColor(Color.WHITE);
						paint.setTextSize(aiBall.radius > 40 ? (20 + ((int) aiBall.radius > 90 ? 15
								: aiBall.radius / 6))
								: 23);
						// ��������
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
				// ���ƽ�ɫ��
				if (myBall.state != 0) {
					if (getClockIsInRange(myBall)) {
						// ������
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
							(float) myBall.positionY, myBall.radius, paint);// ���ƽ�ɫ��
					// dir
					Matrix matrix = new Matrix();
					// ������ת�Ƕ�
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
					// ������תͼƬ
					canvas.drawBitmap(bmpDir, matrix, paint);
					// ���ƽ�ɫ����
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
											: 23) / 4, paint);// ���ƽ�ɫ��
				}
				canvas.restore();

				canvas.save();

				// ҡ��
				if (flagRockerDisplay) {
					paint.setColor(getResources()
							.getColor(R.color.rockerRudder));
					canvas.drawCircle(ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
							rockerWheelRadius, paint);// ���Ʒ�Χ
					paint.setColor(getResources().getColor(R.color.rocker));
					canvas.drawCircle(ptRockerPosition.x, ptRockerPosition.y,
							rockerRudderRadius, paint);// ����ҡ��
				}
				// score�Ʒְ�
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
	 * ��Ϸ�߼�
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
			// ����ң��
			ptRockerPosition.y = ptRockerCtrlPoint.y;
			ptRockerPosition.x = ptRockerCtrlPoint.x;
			flagRockerDisplay = false;

		}

		for (index = 0; index < AiBallList.length; index++) {
			// AI����λ�ƿ���
			ActionBall aiBall = AiBallList[index];
			// ����
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
				// ����ֵΪ��
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
	 * ���Ʊ���
	 */
	private final void DrawBackground() {
		// -----------������仭����ˢ��
		canvas.drawColor(getResources().getColor(R.color.Background) + 1);
		// //���ƾ���
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
	 * ���������ɫ
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
	 * ����ʳ������Ļ���
	 */
	private class FoodBall {
		int state;
		double positionX;
		double positionY;
		float radius = 6;
		int colorBall;

		FoodBall(double positionX, double positionY, int colorBall) {
			this.state = 1;// δ����
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorBall = colorBall;
		}

		void reSetBall(double positionX, double positionY, int colorBall) {
			this.state = 1;// δ����
			this.positionX = positionX;
			this.positionY = positionY;
			this.colorBall = colorBall;
		}
	}

	/**
	 * ����������࣬����ɫ��
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
		// this.state = 1;// δ����
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
			this.state = 1;// δ����
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
			// ����������
			this.state = 1;// δ����
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
				// �����ж�
				life--;
				reSetBall((int) (mapW * Math.random()),
						(int) (mapH * Math.random()), randomColor(),
						ballDefaultSize);
			}
			if ((int) radius < (int) Math.sqrt(weight)) {
				// ��������
				radius += (Math.sqrt(weight) - radius) / actionDamping;
			}
			if ((int) radius > (int) Math.sqrt(weight)) {
				// �������
				radius -= (radius - Math.sqrt(weight)) / actionDamping;
			}
			weight -= (int) radius / 100 * actionDamping;
			// ��ļ���
			if (radius > mapW || radius > mapH) {
				// ��ɫ��ߴ����ƣ����óߴ�
				weight = (int) ballDefaultSize;
			}

			if (radius > 300) {
				// ��ɫ��ߴ����ƣ����óߴ�
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
					// �߽��ж�
					targetX = 0;
					// myBall.targetX = 0;
					// ptRockerPosition.x = ptRockerCtrlPoint.x;

				}
				if (targetX > mapW) {
					// �߽��ж�
					targetX = mapW;
					// myBall.targetX = mapW;
					// ptRockerPosition.x = ptRockerCtrlPoint.x;
				}
				if (targetY < 0) {
					// �߽��ж�
					targetY = 0;
					// myBall.targetY = 0;
					// ptRockerPosition.y = ptRockerCtrlPoint.y;
				}
				if (targetY > mapH) {
					// �߽��ж�
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
				// �߽��ж�
				targetX = 0;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
						ptRockerPosition.y);
				// myBall.targetX = 0;
				// ptRockerPosition.x = ptRockerCtrlPoint.x;

			}
			if (targetX > mapW) {
				// �߽��ж�
				targetX = mapW;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
						ptRockerPosition.y);
				// myBall.targetX = mapW;
				// ptRockerPosition.x = ptRockerCtrlPoint.x;
			}
			if (targetY < 0) {
				// �߽��ж�
				targetY = 0;
				dectionTarget = getRadian(ptRockerCtrlPoint.x,
						ptRockerPosition.x, ptRockerCtrlPoint.y,
						ptRockerCtrlPoint.y);
				// myBall.targetY = 0;
				// ptRockerPosition.y = ptRockerCtrlPoint.y;
			}
			if (targetY > mapH) {
				// �߽��ж�
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

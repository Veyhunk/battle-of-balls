package com.veyhunk.battle_of_balls.surface_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.veyhunk.battle_of_balls.R;
import com.veyhunk.battle_of_balls.model.Ball;
import com.veyhunk.battle_of_balls.model.BallTeam;
import com.veyhunk.battle_of_balls.model.Camera;
import com.veyhunk.battle_of_balls.model.FoodBall;
import com.veyhunk.battle_of_balls.model.MyBall;
import com.veyhunk.battle_of_balls.model.TeamsManager;
import com.veyhunk.battle_of_balls.sounds.GameSounds;
import com.veyhunk.battle_of_balls.utils.Clock;
import com.veyhunk.battle_of_balls.utils.MathUtils;

import static com.veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_DEFAULT_WEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_FOOD_COUNT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.RANK_LIST_ITEM_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.RANK_LIST_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_ACTION_RADIUS;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_RUDDER_RADIUS;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_WHEEL_RADIUS;
import static com.veyhunk.battle_of_balls.constants.Constants.SQRT1_2;
import static com.veyhunk.battle_of_balls.db.GameParams.aiDifficult;
import static com.veyhunk.battle_of_balls.db.GameParams.ballColorIndex;
import static com.veyhunk.battle_of_balls.db.GameParams.ballGrowSpeed;
import static com.veyhunk.battle_of_balls.db.GameParams.ballMoveSpeed;
import static com.veyhunk.battle_of_balls.db.GameParams.ballName;
import static com.veyhunk.battle_of_balls.db.GameParams.bestScore;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.AVATAR;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.BATTLE;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.BGM;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.BUBBLE;
import static com.veyhunk.battle_of_balls.utils.Colors.getColorRandom;

/**
 * @author Veyhunk
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
    public static int score = 0;// score
    private static int index1, index2, index3;
    // user customer
    final Context context;
    // callback
    protected OnEndOfGameInterface mOnEndOfGame; // callback interface
    // flag
    private boolean flagGameThread;// 线程消亡的标识位
    private int flagButtonIndex;// 线程消亡的标识位
    private boolean flagGameOver;// 线程消亡的标识位
    private boolean flagIsTouchLongMove;// 是否长按的标识位
    private boolean flagRockerDisplay = false;// 是否显示遥感的标识位
    private int keyCheck;
    // variable
    private int screenW, screenH; // Screen_size
    private Point ptRockerPosition;// 摇杆位置
    private Point ptRockerCtrlPoint = new Point(0, 0);// 摇杆起始位置
    private SurfaceHolder sfh; // 用于控制SurfaceView
    private Paint paint;// 声明一个画笔
    private Paint paintFont;// 声明一个画笔
    private Canvas canvas;// 声明一个画布
    private Camera camera;
    private Camera cameraGlobal;
    private Camera cameraPlayer;
    // 声明Ball
    private MyBall myBall;
    private FoodBall[] FoodBallList = new FoodBall[BALL_FOOD_COUNT];
    private BallTeam[] teams;
    private TeamsManager teamsManager;
    // 位图文件 bitmap
    private Bitmap bmpRank = BitmapFactory.decodeResource(this.getResources(),
            R.mipmap.rank);// 排行榜素材
    private Bitmap bmpDir = BitmapFactory.decodeResource(this.getResources(),
            R.mipmap.dir);// 小球指针素材
    //    private Bitmap bmpInfo = BitmapFactory.decodeResource(this.getResources(),
//            R.mipmap.toast);// 球球通知框素材
    private Bitmap bmpBadgesVictory = BitmapFactory.decodeResource(
            this.getResources(), R.mipmap.badges_victory);// 球球胜利徽章素材
    private Bitmap bmpBadgesDefeat = BitmapFactory.decodeResource(
            this.getResources(), R.mipmap.badges_defeat);// 球球失败徽章素材
    // button
    private Bitmap bmpBtnAvatar = BitmapFactory.decodeResource(
            this.getResources(), R.mipmap.button_avetar);// 分身按钮
    private Bitmap bmpBtnDanger = BitmapFactory.decodeResource(
            this.getResources(), R.mipmap.button_danger);// 发射按钮
    private Bitmap bmpBtnBattle = BitmapFactory.decodeResource(
            this.getResources(), R.mipmap.button_battle);// 发射按钮
    // Music
    private GameSounds gameSounds;

    /**
     * SurfaceView初始化函数
     */
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Setting();
        mOnEndOfGame = (OnEndOfGameInterface) context;
        gameSounds = new GameSounds(context);
        gameSounds.starMusic(BGM);
        camera=new Camera();
        cameraGlobal=new Camera();
        cameraPlayer=new Camera();
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

    public static void Setting() {
        // user customer
        if (ballName.length() == 0) {
            ballName = "你个傻瓜没写名字";
        }
        if (ballColorIndex > 6 || ballColorIndex < 0) {
            ballColorIndex = 10;
        }
    }

    /**
     * SurfaceView视图创建，响应此函数
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Clock.setTimeBegin();
        score = 0;
        // screen size
        screenW = this.getWidth();
        screenH = this.getHeight();
        cameraGlobal.Focus.x = -MAP_WIDTH / 2 + screenW / 2;
        cameraGlobal.Focus.y = -MAP_HEIGHT / 2 + screenH / 2;
        cameraGlobal.Scale.x = 0.3F;
        cameraGlobal.Scale.y = 0.3F;
        cameraGlobal.ScalePosition.x = MAP_WIDTH / 2;
        cameraGlobal.ScalePosition.y = MAP_HEIGHT / 2;

        // initialization food Ball
        for (index1 = 0; index1 < FoodBallList.length; index1++) {
            FoodBallList[index1] = new FoodBall((int) (MAP_WIDTH * Math.random()),
                    (int) (MAP_HEIGHT * Math.random()), getColorRandom());
        }
        teamsManager = new TeamsManager();
        teams = teamsManager.getTeams();

        // initialization player aiBall
        myBall = new MyBall(teams[0].members.get(0));
        myBall.setGameSounds(gameSounds);
        teams[0].members.remove(0);
        teams[0].addMember(myBall);
        // 启动线程flag
        flagGameThread = true;
        // 实例线程
        Thread th = new Thread(this);
        // 启动线程
        th.start();
    }

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
        gameSounds.stopMusic();
        gameSounds.recycle();

    }

    /**
     * 触屏事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        index2 = event.getPointerCount();
//        if (index2 > 1) {
//            for (index1 = 0; index1 < index2; index1++) {
//                if (flagButtonIndex == index1
//                        && event.getX(index1) > (screenW - bmpBtnAvatar
//                        .getWidth() * 2)
//                        && event.getX(index1) < (screenW - bmpBtnAvatar
//                        .getWidth())
//                        && event.getY(index1) > (screenH - bmpBtnAvatar
//                        .getHeight())) {
//                    //launch
//                }
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // System.out.println("---按下----");
//
//                        if (event.getX(index1) > (screenW - bmpBtnAvatar.getWidth() * 2)
//                                && event.getY(index1) > (screenH - bmpBtnAvatar
//                                .getHeight())) {
//                            if (event.getX(index1) < (screenW - bmpBtnAvatar
//                                    .getWidth())) {
//                                gameSounds.starMusic(GameSounds.BUBBLE);
//                            } else {
////                                myBall.avatar();
//                                gameSounds.starMusic(GameSounds.AVATAR);
//                            }
//                            flagButtonIndex = index1;
//                            break;
//                        } else {
//                            flagRockerDisplay = true;
//                            ptRockerCtrlPoint.set((int) event.getX(index1),
//                                    (int) event.getY(index1));
//                            ptRockerPosition.set((int) event.getX(index1) + 1,
//                                    (int) event.getY(index1));
//                        }
//                    case MotionEvent.ACTION_UP:
//                        // System.out.println("----放开----");
//                        flagIsTouchLongMove = true;
////                        flagRockerDisplay = false;
//                        flagButtonIndex = -1;
//                        if (flagGameOver) {
//                            System.out.println("over");
//                        }
//                        if (Math.abs(event.getX(index1) - screenW / 2) < 150
//                                && (screenH / 2 - event.getY(index1)) < 50) {
//                            System.out.println("over 2");
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (flagButtonIndex == index1) {
//                            break;
//                        }
//                        flagRockerDisplay = true;
//                        // System.out.println("----移动----");
//                        if (event.getPointerCount() == 1) {
//                            int len = MathUtils.getLength(ptRockerCtrlPoint.x,
//                                    ptRockerCtrlPoint.y, event.getX(index1),
//                                    event.getY(index1));
//                            if (len < 20 && flagIsTouchLongMove) {
//                                // 如果屏幕接触点不在摇杆挥动范围内,则不处理
//                                break;
//                            }
//                            if (len <= ROCKER_ACTION_RADIUS) {
//                                // 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
//                                flagIsTouchLongMove = false;
//                                ptRockerPosition.set((int) event.getX(index1),
//                                        (int) event.getY(index1));
//
//                            } else {
//                                // 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
//                                flagIsTouchLongMove = false;
//                                ptRockerPosition = MathUtils.getBorderPoint(
//                                        ptRockerCtrlPoint,
//                                        new Point((int) event.getX(index1),
//                                                (int) event.getY(index1)),
//                                        ROCKER_ACTION_RADIUS);
//                            }
//
//                            myBall.directionTarget = MathUtils.getRadian(
//                                    ptRockerCtrlPoint, ptRockerPosition);
//
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//            }

//        } else {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // System.out.println("---按下----");
                if (event.getX() > (screenW - bmpBtnAvatar.getWidth() * 3) && event.getY() > (screenH - bmpBtnAvatar.getHeight())) {
                    if (event.getX() < (screenW - bmpBtnAvatar.getWidth() * 2)) {
                        //danger
                        System.out.print("danger");
                        gameSounds.starMusic(BUBBLE);
                    } else if (event.getX() < (screenW - bmpBtnAvatar.getWidth())) {
//                        battle
                        System.out.print("battle");
                        gameSounds.starMusic(BATTLE);
                    } else {
//                        avatar
                        System.out.print("avatar");
                        myBall.avatar(myBall.direction);
                        gameSounds.starMusic(AVATAR);
                    }
                    flagButtonIndex = 1;
                    break;
                } else if (event.getX() < 30 && event.getY() < 30) {
                    camera.isPlayerCamera = !camera.isPlayerCamera;
                } else {
                    flagRockerDisplay = true;
                    ptRockerCtrlPoint.set((int) event.getX(), (int) event.getY());
                    ptRockerPosition.set((int) event.getX() + 1, (int) event.getY());
                }
            case MotionEvent.ACTION_UP:
                // System.out.println("----放开----");
                flagIsTouchLongMove = true;
//                    flagRockerDisplay = false;
                flagButtonIndex = -1;
                if (flagGameOver) {
                    System.out.println("over");
                }
                if (Math.abs(event.getX() - screenW / 2) < 150 && (screenH / 2 - event.getY()) < 50) {
                    System.out.println("over 2");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (flagButtonIndex == 1) {
                    break;
                }
                flagRockerDisplay = true;
                // System.out.println("----移动----");
                if (event.getPointerCount() == 1) {
                    int len = MathUtils.getLength(ptRockerCtrlPoint.x, ptRockerCtrlPoint.y, event.getX(), event.getY());
                    if (len < 20 && flagIsTouchLongMove) {
                        // 如果屏幕接触点不在摇杆挥动范围内,则不处理
                        return true;
                    }
                    if (len <= ROCKER_ACTION_RADIUS) {
                        // 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
                        flagIsTouchLongMove = false;
                        ptRockerPosition.set((int) event.getX(), (int) event.getY());

                    } else {
                        // 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
                        flagIsTouchLongMove = false;
                        ptRockerPosition = MathUtils.getBorderPoint(ptRockerCtrlPoint, new Point((int) event.getX(), (int) event.getY()), ROCKER_ACTION_RADIUS);
                    }
                    myBall.setTarget(MathUtils.getRadian(ptRockerCtrlPoint, ptRockerPosition), (float) Math.sqrt((ptRockerPosition.x - ptRockerCtrlPoint.x) * (ptRockerPosition.x - ptRockerCtrlPoint.x) + (ptRockerPosition.y - ptRockerCtrlPoint.y) * (ptRockerPosition.y - ptRockerCtrlPoint.y)) / ROCKER_ACTION_RADIUS);
                }
                break;

            default:
                break;
        }
        return true;

    }

    /**
     * 按键事件监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        flagRockerDisplay = true;
        if (keyCheck != keyCode) {
            keyCheck = keyCode;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {//up >> right
                ptRockerCtrlPoint.x = 100;
                ptRockerCtrlPoint.y = screenH - 100;
                ptRockerPosition.x = 100 + 65;
                ptRockerPosition.y = screenH - 100;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// down >> left
                ptRockerCtrlPoint.x = 100;
                ptRockerCtrlPoint.y = screenH - 100;
                ptRockerPosition.x = 100 - 65;
                ptRockerPosition.y = screenH - 100;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// left >> up
                ptRockerCtrlPoint.x = 100;
                ptRockerCtrlPoint.y = screenH - 100;
                ptRockerPosition.x = 100;
                ptRockerPosition.y = screenH - 100 - 65;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// right >> down
                ptRockerCtrlPoint.x = 100;
                ptRockerCtrlPoint.y = screenH - 100;
                ptRockerPosition.x = 100;
                ptRockerPosition.y = screenH - 100 + 65;
            }
            myBall.directionTarget = MathUtils.getRadian(ptRockerCtrlPoint,
                    ptRockerPosition);
        }

        return super.onKeyDown(keyCode, event);

    }

    /**
     * 进程控制
     */
    @Override
    public void run() {
        while (flagGameThread) {
            if (flagGameOver) {
                //game over: 满足死亡的生命数，且不在等待时间内

                // check bestScore
                if (Integer.parseInt(bestScore) < score) bestScore = score + "";

                flagGameThread = false;
                mOnEndOfGame.onEndOfGame();
                // 结束进程
                break;
            } else {
                logic();
                myDraw();
                Clock.timeRun();
                if (Clock.isGameTimeout()) {
                    flagGameOver = true;
                }
            }
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
                // 摄像头适应性的缩放
                canvas.translate(camera.Focus.x, camera.Focus.y);
                canvas.scale(camera.Scale.x, camera.Scale.y, camera.ScalePosition.x, camera.ScalePosition.y);

                DrawBackground();
                // 绘制背景
                for (FoodBall foodBall : FoodBallList) {
                    // 绘制食物小球
                    paint.setColor(ContextCompat.getColor(context, foodBall.colorDraw));
                    canvas.drawCircle((float) foodBall.positionX,
                            (float) foodBall.positionY, foodBall.radius, paint);
                }

                for (BallTeam team : teams) {
                    // 绘制team
                    paint.setColor(ContextCompat.getColor(context, team.teamColor));
                    for (Ball member : team.members) {
                        if (member.state) {
                            // AI 活着
                            // 绘制AI大球
                            canvas.drawCircle((float) member.position.x,
                                    (float) member.position.y, member.radius, paint);

                            // 名称字体大小
                            paintFont.setTextSize(member.radius > 40 ? (20 + ((int) member.radius > 90 ? 15 : member.radius / 6)) : 23);
                            // 绘制名称
                            canvas.drawText(member.name, (float) member.position.x - paintFont.measureText(member.name) / 2, (float) member.position.y + (member.radius > 40 ? (20 + ((int) member.radius > 90 ? 15 : member.radius / 6)) : 23) / 4, paintFont);
                        }
                    }
                }
                if (myBall.state) {
                    // 绘制角色球
                    drawBall(myBall);
                }
                canvas.restore();

                canvas.save();

                // 摇杆
                if (flagRockerDisplay) {
                    paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                    canvas.drawCircle(ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
                            ROCKER_WHEEL_RADIUS, paint);// 绘制范围
                    paint.setColor(ContextCompat.getColor(context, R.color.rocker));
                    canvas.drawCircle(ptRockerPosition.x, ptRockerPosition.y,
                            ROCKER_RUDDER_RADIUS, paint);// 绘制摇杆
                }
                // button
                canvas.drawBitmap(bmpBtnAvatar, screenW - bmpBtnAvatar.getWidth(), screenH - bmpBtnAvatar.getHeight(), paint);
                canvas.drawBitmap(bmpBtnBattle, screenW - bmpBtnAvatar.getWidth() - bmpBtnBattle.getWidth(), screenH - bmpBtnBattle.getHeight(), paint);
                canvas.drawBitmap(bmpBtnDanger, screenW - bmpBtnAvatar.getWidth() - bmpBtnBattle.getWidth() - bmpBtnDanger.getWidth(), screenH - bmpBtnDanger.getHeight(), paint);

                // score计分板
                paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                paintFont.setTextSize((float) (0.7 * RANK_LIST_ITEM_HEIGHT));
                paintFont.setColor(ContextCompat.getColor(context,
                        R.color.white_transparent));
                canvas.drawRect(5, 5, RANK_LIST_WIDTH, RANK_LIST_ITEM_HEIGHT + 5, paint);
                canvas.drawText("score:" + score, 30, 28, paintFont);
                canvas.drawRect(5, RANK_LIST_ITEM_HEIGHT + 5, RANK_LIST_WIDTH, RANK_LIST_ITEM_HEIGHT + RANK_LIST_ITEM_HEIGHT + 5, paint);
                canvas.drawText("Weight:" + myBall.weight, 30, 28 + RANK_LIST_ITEM_HEIGHT,
                        paintFont);
                // 倒计时
                canvas.drawText(Clock.getTimeStr(),
                        screenW / 2 - 25, 28, paintFont);
                // rank排行榜
                canvas.drawRect(screenW - RANK_LIST_WIDTH - 5, 5, screenW - 5, 26, paint);
                index1 = 0;
                for (BallTeam team : teams) {

                    // rank bg Rect
                    canvas.drawRect(screenW - RANK_LIST_WIDTH - 5,
                            index1 * RANK_LIST_ITEM_HEIGHT + 26, screenW - 5, (index1 + 1)
                                    * RANK_LIST_ITEM_HEIGHT + 26, paint);

                    if (team.teamName != myBall.getTeam().teamName) {
                        // rank text
                        canvas.drawText((String) (team.teamName.length() > 5 ? team.teamName.subSequence(0, 5) : team.teamName), screenW - RANK_LIST_WIDTH + 50, 50 + index1 * RANK_LIST_ITEM_HEIGHT, paintFont);
                        canvas.drawText("" + team.getScore(), screenW - 65, 50 + index1 * RANK_LIST_ITEM_HEIGHT, paintFont);
                    } else {
                        // rank text
                        paintFont.setColor(ContextCompat.getColor(context,
                                R.color.color1));
                        // rank text
                        canvas.drawText((String) (team.teamName.length() > 5 ? team.teamName.subSequence(0, 5) : team.teamName), screenW - RANK_LIST_WIDTH + 50, 50 + index1 * RANK_LIST_ITEM_HEIGHT, paintFont);
                        canvas.drawText("" + team.getScore(), screenW - 65, 50 + index1 * RANK_LIST_ITEM_HEIGHT, paintFont);
                        paintFont.setColor(ContextCompat.getColor(context,
                                R.color.white_transparent));

                    }
                    index1++;

                }
                // rank Bottom Rect
                canvas.drawRect(screenW - RANK_LIST_WIDTH - 5, index1 * RANK_LIST_ITEM_HEIGHT + 26,
                        screenW - 5, (index1 + 1.2F) * RANK_LIST_ITEM_HEIGHT, paint);
                // bmp Rank ICO
                canvas.clipRect(screenW - RANK_LIST_WIDTH - 5, 5, screenW - 5,
                        (index1 + 1) * RANK_LIST_ITEM_HEIGHT - 7);
                canvas.drawBitmap(bmpRank, screenW - RANK_LIST_WIDTH, 13, paint);
                canvas.restore();

                canvas.save();
                // bmpBadges & bmpInfo
                if (flagGameOver) {
                    // 游戏结束
//                    if (myBall.life < 2) {
//                        paint.setColor(ContextCompat.getColor(context,
//                                R.color.rockerRudder));
//                        canvas.drawRect(0, 0, screenW, screenH, paint);
//                        paint.setColor(0xffffffff);
//                        canvas.drawBitmap(bmpBadgesDefeat, 0, 0, paint);
//                    } else if (myBall.life == (BALL_AI_COUNT + 1)
//                            * BALL_DEFAULT_LIFE) {
//                        paint.setColor(ContextCompat.getColor(context,
//                                R.color.black_win));
//                        canvas.drawRect(0, 0, screenW, screenH, paint);
//                        paint.setColor(0xffffffff);
//                        canvas.drawBitmap(bmpBadgesVictory, 0, 0, paint);
//                    } else {
//                        paint.setColor(ContextCompat.getColor(context,
//                                R.color.rockerRudder));
//                        canvas.drawRect(0, 0, screenW, screenH, paint);
//                        paint.setColor(0xffffffff);
//                        canvas.drawBitmap(bmpBadgesDefeat, 0, 0, paint);
//                    }
                } else {
                    // 被吃掉
//                    paintFont.setTextSize(40);
//                    float len = paintFont.measureText("你被")
//                            + paintFont
//                            .measureText(AiBallList[myBall.eatByID].name)
//                            + paintFont.measureText("吃掉了");
//                    paint.setColor(0xffffffff);
//                    canvas.drawBitmap(bmpInfo,
//                            screenW / 2 - bmpInfo.getWidth() / 2, screenH
//                                    / 2 - bmpInfo.getHeight(), paint);
//                    paintFont.setColor(ContextCompat.getColor(context,
//                            R.color.font_deep));
//                    canvas.drawText("你被", screenW / 2 - len / 2, screenH
//                            / 2 - bmpInfo.getHeight() + 70, paintFont);
//                    canvas.drawText(
//                            "吃掉了",
//                            (screenW / 2)
//                                    - (len / 2)
//                                    + paintFont.measureText("你被")
//                                    + paintFont
//                                    .measureText(AiBallList[myBall.eatByID].name),
//                            screenH / 2 - bmpInfo.getHeight() + 70,
//                            paintFont);
//                    paintFont.setColor(0xff6b543a);
//                    canvas.drawText(
//                            AiBallList[myBall.eatByID].name,
//                            screenW / 2 - len / 2
//                                    + paintFont.measureText("你被"), screenH
//                                    / 2 - bmpInfo.getHeight() + 70,
//                            paintFont);

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

    private void drawBall(Ball drawRoleBall) {
        paint.setColor(ContextCompat.getColor(context, drawRoleBall.colorDraw));
        canvas.drawCircle((float) drawRoleBall.position.x,
                (float) drawRoleBall.position.y, drawRoleBall.radius, paint);// 绘制角色球
        // dir
        if (drawRoleBall.directionTarget != 404) {
            Matrix matrix = new Matrix();
            // 设置旋转角度
            matrix.postScale(drawRoleBall.radius / 230,
                    drawRoleBall.radius / 230, bmpDir.getWidth() / 2,
                    bmpDir.getHeight() / 2);
            matrix.postRotate(
                    (float) (drawRoleBall.direction * (180 / Math.PI)) + 90,
                    bmpDir.getWidth() / 2, bmpDir.getHeight() / 2);
            matrix.postTranslate(
                    (float) drawRoleBall.position.x
                            - bmpDir.getWidth()
                            / 2
                            + (int) (drawRoleBall.radius * 1.25 * Math
                            .cos(drawRoleBall.direction)),
                    (float) drawRoleBall.position.y
                            - bmpDir.getHeight()
                            / 2
                            + (int) (drawRoleBall.radius * 1.25 * Math
                            .sin(drawRoleBall.direction)));
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
                (float) drawRoleBall.position.x
                        - paintFont.measureText(drawRoleBall.name) / 2,
                (float) drawRoleBall.position.y
                        + (drawRoleBall.radius > 40 ? (20 + (drawRoleBall.radius > 90 ? 15
                        : drawRoleBall.radius / 6))
                        : 23) / 4, paintFont);
        // 绘制角色名称
    }

    /**
     * 绘制背景
     */

    private void DrawBackground() {
        // -----------利用填充画布，刷屏
        canvas.drawColor(ContextCompat.getColor(context, R.color.background) + 5);
        // //绘制矩形
        paint.setColor(ContextCompat.getColor(context, R.color.background));
        canvas.drawRect(0, 0, MAP_WIDTH, MAP_HEIGHT, paint);
        int rowWidth = 5, rowInterval = 40;
        paint.setColor(ContextCompat.getColor(context, R.color.backgroundStripe));
        for (index1 = 1; index1 <= MAP_HEIGHT / (rowWidth + rowInterval); index1++) {
            canvas.drawRect(0, index1 * (rowWidth + rowInterval), MAP_WIDTH, index1
                    * (rowWidth + rowInterval) + rowWidth, paint);
        }
        for (index1 = 1; index1 <= MAP_WIDTH / (rowWidth + rowInterval); index1++) {
            canvas.drawRect(index1 * (rowWidth + rowInterval), 0, index1
                    * (rowWidth + rowInterval) + rowWidth, MAP_HEIGHT, paint);
        }

    }

    /**
     * 游戏逻辑
     */
    private void logic() {
        if (ptRockerCtrlPoint.equals(ptRockerPosition)) {
//            flagRockerDisplay = false;
        }
        if (!myBall.state) {
            if (myBall.getTeam().members.size() > 1) {
                myBall.getTeam().members.get(myBall.getTeam().members.size() - 1);
            } else {
                flagGameOver = true;
            }
        }
//        myBall.move((float) Math
//                .sqrt((ptRockerPosition.x - ptRockerCtrlPoint.x)
//                        * (ptRockerPosition.x - ptRockerCtrlPoint.x)
//                        + (ptRockerPosition.y - ptRockerCtrlPoint.y)
//                        * (ptRockerPosition.y - ptRockerCtrlPoint.y))
//                / ROCKER_ACTION_RADIUS);
//        if (myBall.life == 0
//                || myBall.life == (BALL_AI_COUNT + 1) * BALL_DEFAULT_LIFE) {
//            flagGameOver = true;
//        }

//            // 锁定遥感
//            ptRockerPosition.y = ptRockerCtrlPoint.y;
//            ptRockerPosition.x = ptRockerCtrlPoint.x;
//            flagRockerDisplay = false;

        try {
            for (BallTeam team : teams) {
                for (Ball member : team.members) {

                    if (!member.state) continue;

                    //基本活动
                    member.action();

//                gameSounds.starMusic(GameSounds.EAT_3);
//                gameSounds.starMusic(GameSounds.EAT_DEFAULT);
                    if (member.state) {
                        for (BallTeam team2 : teams) {
                            if (!member.state) break;
                            if (team2.equals(team)) {
                                // 是否同一个队伍
                                continue;
                            }
                            for (Ball member2 : team2.members) {
                                if (!member2.state) break;
                                // 判断是否被吃
                                member.feeling(member2);
                            }
                        }

                    }
                }
            }

            for (FoodBall foodBall : FoodBallList) {
                if (!foodBall.state) {
                    // 重置
                    foodBall.reSetBall((int) (MAP_WIDTH * Math.random()), (int) (MAP_HEIGHT * Math.random()), getColorRandom());
                    continue;
                }
                for (BallTeam team : teams) {
                    if (!foodBall.state) break;
                    for (Ball member : team.members) {
                        if (!foodBall.state) break;
                        // 判断是否被吃
                        if ((foodBall.positionX - member.position.x)
                                * (foodBall.positionX - member.position.x)
                                + (foodBall.positionY - member.position.y)
                                * (foodBall.positionY - member.position.y) < (member.radius - foodBall.radius)
                                * (member.radius - foodBall.radius)) {
                            member.weight += foodBall.die() * ballGrowSpeed * aiDifficult;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //排序
        teamsManager.sorl();
        //摄像机视野调整


        if (camera.isPlayerCamera) {
            // 以玩家为中心
            camera=cameraPlayer;
            cameraPlayer.Focus.x = -myBall.position.x + screenW / 2;
            cameraPlayer.Focus.y = -myBall.position.y + screenH / 2;
            cameraPlayer.Scale.x = (3 / (myBall.radius / 15) + 0.4F);
            cameraPlayer.Scale.y = (3 / (myBall.radius / 15) + 0.4F);
            cameraPlayer.ScalePosition.x = myBall.position.x;
            cameraPlayer.ScalePosition.y = myBall.position.y;
        } else {
            //全局视野
            camera=cameraGlobal;
        }
    }

    /**
     * callback interface
     */
    public interface OnEndOfGameInterface {
        void onEndOfGame();
    }

    /**
     * 定义活动球球的类，即角色球
     */
    private class ActionBall {
        int ID;
        int eatByID;
        int life;
        int state;
        int weight;
        int eatCount;
        int colorDraw;
        int timeRandomActionBegin;
        int timeRandomActionRang;
        float moveSpeed;
        float moveSpeedRandom;
        float radius;
        double positionX;
        double positionY;
        double targetX;
        double targetY;
        double direction = 0;
        double directionTarget = 0;
        double inscribedSquareLen_1_2;
        String name;

        ActionBall(double positionX, double positionY, int colorDraw,
                   float weight, String nameString, int life) {
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
            this.timeRandomActionBegin = Clock.getClock() + 500;
        }

        // positionX, positionY, colorDraw, size
        void reSetBall(double positionX, double positionY, int colorDraw) {
            if (life > 0) {
                this.state = 1;// 复活
                this.weight = BALL_DEFAULT_WEIGHT;
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
                reSetBall((int) (MAP_WIDTH * Math.random()),
                        (int) (MAP_HEIGHT * Math.random()), getColorRandom());
            }
            if ((int) radius < (int) Math.sqrt(weight)) {
                // 阻尼增重
                radius += (Math.sqrt(weight) - radius) / ACTION_DAMPING;
            }
            if ((int) radius > (int) Math.sqrt(weight)) {
                // 阻尼减重
                radius -= (radius - Math.sqrt(weight)) / ACTION_DAMPING;
            }
            weight -= (int) radius / 100 * 5;
            // 损耗减重

            if (radius > 400) {
                // 角色球尺寸限制，重置尺寸
                weight = BALL_DEFAULT_WEIGHT;
            }
        }

        void moveRandom() {
            // action();
            if (!Clock.getClockIsInRange(timeRandomActionBegin,
                    timeRandomActionRang)) {
                timeRandomActionBegin = Clock.getClock();
                timeRandomActionRang = (int) (Math.random() * 12000);
                // directionTarget = (Math.random() * Math.PI * 2) - Math.PI;
                if (myBall.state && weight > myBall.weight) {
                    directionTarget = getRadian((float) positionX,
                            (float) myBall.position.x, (float) positionY,
                            (float) myBall.position.y);
                } else {
                    directionTarget = (Math.random() * Math.PI * 2) - Math.PI;
                }
                moveSpeedRandom = (float) Math.random();
            } else {
                direction += Math.abs((directionTarget - direction)) < Math.PI ? (((directionTarget - direction) / ACTION_DAMPING))
                        : ((directionTarget - direction) > 0 ? -(Math
                        .abs((directionTarget - direction - 2 * Math.PI)) / ACTION_DAMPING)
                        : +(Math.abs((directionTarget - direction + 2 * Math.PI)) / ACTION_DAMPING));
                direction += (direction >= Math.PI) ? (-2 * Math.PI)
                        : ((direction <= -Math.PI) ? (+2 * Math.PI) : 0);
                targetX += moveSpeed * Math.cos(directionTarget)
                        * (30 / radius * 1 + 0.6) * moveSpeedRandom;
                targetY += moveSpeed * Math.sin(directionTarget)
                        * (30 / radius * 1 + 0.6) * moveSpeedRandom;
                if (targetX < 0) {
                    // 边界判断
                    targetX = 0;
                    // myBall.targetX = 0;
                    // ptRockerPosition.x = ptRockerCtrlPoint.x;

                }
                if (targetX > MAP_WIDTH) {
                    // 边界判断
                    targetX = MAP_WIDTH;
                    // myBall.targetX = MAP_WIDTH;
                    // ptRockerPosition.x = ptRockerCtrlPoint.x;
                }
                if (targetY < 0) {
                    // 边界判断
                    targetY = 0;
                    // myBall.targetY = 0;
                    // ptRockerPosition.y = ptRockerCtrlPoint.y;
                }
                if (targetY > MAP_HEIGHT) {
                    // 边界判断
                    targetY = MAP_HEIGHT;
                    // // myBall.targetY = MAP_HEIGHT;
                    // ptRockerPosition.y = ptRockerCtrlPoint.y;
                }
                positionX += (targetX - positionX) / ACTION_DAMPING;
                positionY += (targetY - positionY) / ACTION_DAMPING;
            }
        }

        public void move(float rocker) {
            if (directionTarget != 404) {
                direction += Math.abs((directionTarget - direction)) < Math.PI ? (((directionTarget - direction) / ACTION_DAMPING))
                        : ((directionTarget - direction) > 0 ? -(Math
                        .abs((directionTarget - direction - 2 * Math.PI)) / ACTION_DAMPING)
                        : +(Math.abs((directionTarget - direction + 2 * Math.PI)) / ACTION_DAMPING));
                direction += (direction >= Math.PI) ? (-2 * Math.PI)
                        : ((direction <= -Math.PI) ? (+2 * Math.PI) : 0);
                targetX += moveSpeed * Math.cos(directionTarget)
                        * (30 / radius * 1 + 0.6) * rocker;
                targetY += moveSpeed * Math.sin(directionTarget)
                        * (30 / radius * 1 + 0.6) * rocker;
                inscribedSquareLen_1_2 = radius * SQRT1_2;
                if (targetX < 0 + inscribedSquareLen_1_2) {
                    // 边界判断
//                    targetX = 0;
                    directionTarget = getRadian(ptRockerCtrlPoint.x,
                            ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
                            ptRockerPosition.y);
                    ptRockerPosition.x = ptRockerCtrlPoint.x;
                    // myBall.targetX = 0;
                    // ptRockerPosition.x = ptRockerCtrlPoint.x;

                }
                if (targetX > MAP_WIDTH - inscribedSquareLen_1_2) {
                    // 边界判断
//                    targetX = MAP_WIDTH;
                    directionTarget = getRadian(ptRockerCtrlPoint.x,
                            ptRockerCtrlPoint.x, ptRockerCtrlPoint.y,
                            ptRockerPosition.y);
                    ptRockerPosition.x = ptRockerCtrlPoint.x;
                    // myBall.targetX = MAP_WIDTH;
                    // ptRockerPosition.x = ptRockerCtrlPoint.x;
                }
                if (targetY < 0 + inscribedSquareLen_1_2) {
                    // 边界判断
//                    targetY = 0;
                    directionTarget = getRadian(ptRockerCtrlPoint.x,
                            ptRockerPosition.x, ptRockerCtrlPoint.y,
                            ptRockerCtrlPoint.y);
                    ptRockerPosition.y = ptRockerCtrlPoint.y;
                    // myBall.targetY = 0;
                    // ptRockerPosition.y = ptRockerCtrlPoint.y;
                }
                if (targetY > MAP_HEIGHT - inscribedSquareLen_1_2) {
                    // 边界判断
//                    targetY = MAP_HEIGHT;
                    directionTarget = getRadian(ptRockerCtrlPoint.x,
                            ptRockerPosition.x, ptRockerCtrlPoint.y,
                            ptRockerCtrlPoint.y);
                    ptRockerPosition.y = ptRockerCtrlPoint.y;
                    // // myBall.targetY = MAP_HEIGHT;
                    // ptRockerPosition.y = ptRockerCtrlPoint.y;
                }

                if (targetX < 0 + inscribedSquareLen_1_2) {
                    // 边界判断
                    targetX = inscribedSquareLen_1_2;
                    directionTarget = directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2;

                }
                if (targetX > MAP_WIDTH - inscribedSquareLen_1_2) {
                    // 边界判断
                    targetX = MAP_WIDTH - inscribedSquareLen_1_2;
                    directionTarget = directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2;
                }
                if (targetY < 0 + inscribedSquareLen_1_2) {
                    // 边界判断
                    targetY = inscribedSquareLen_1_2;
                    directionTarget = (directionTarget > (-Math.PI / 2) && directionTarget < Math.PI / 2) ? 0 : Math.PI;
                }
                if (targetY > MAP_HEIGHT - inscribedSquareLen_1_2) {
                    // 边界判断
                    targetY = MAP_HEIGHT - inscribedSquareLen_1_2;
                    directionTarget = directionTarget > Math.PI / 2 ? Math.PI : 0;
                }
                positionX += (targetX - positionX) / ACTION_DAMPING;
                positionY += (targetY - positionY) / ACTION_DAMPING;
            }

        }

        float getRadian(float x1, float x2, float y1, float y2) {
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


}


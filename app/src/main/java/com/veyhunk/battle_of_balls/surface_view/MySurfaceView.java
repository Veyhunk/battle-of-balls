package com.veyhunk.battle_of_balls.surface_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.veyhunk.battle_of_balls.R;
import com.veyhunk.battle_of_balls.constants.Constants;
import com.veyhunk.battle_of_balls.model.Ball;
import com.veyhunk.battle_of_balls.model.BallTeam;
import com.veyhunk.battle_of_balls.model.FoodBall;
import com.veyhunk.battle_of_balls.model.PlayerBall;
import com.veyhunk.battle_of_balls.model.TeamsManager;
import com.veyhunk.battle_of_balls.sounds.GameSounds;
import com.veyhunk.battle_of_balls.utils.Camera;
import com.veyhunk.battle_of_balls.utils.Clock;
import com.veyhunk.battle_of_balls.utils.GameMath;
import com.veyhunk.battle_of_balls.utils.Rocker;

import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_MARGIN;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.DANGER;
import static com.veyhunk.battle_of_balls.constants.Constants.PADDING;
import static com.veyhunk.battle_of_balls.constants.Constants.RANK_LIST_ITEM_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.RANK_LIST_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_ACTION_RADIUS;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_ACTIVITY_RADIUS;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_RUDDER_RADIUS;
import static com.veyhunk.battle_of_balls.constants.Constants.ROCKER_WHEEL_RADIUS;
import static com.veyhunk.battle_of_balls.db.GameParams.AI_DIFFICULT;
import static com.veyhunk.battle_of_balls.db.GameParams.BALL_FOOD_COUNT;
import static com.veyhunk.battle_of_balls.db.GameParams.BALL_WEIGHT_DEFAULT;
import static com.veyhunk.battle_of_balls.db.GameParams.BEST_SCORE;
import static com.veyhunk.battle_of_balls.db.GameParams.PLAYER_NAME;
import static com.veyhunk.battle_of_balls.db.GameParams.PLAYER_TEAM_COLOR;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.AVATAR;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.BATTLE;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.BGM;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.BUBBLE;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.CLICK;
import static com.veyhunk.battle_of_balls.utils.Colors.getColorRandom;

/**
 * @author Veyhunk
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
    public static int score = 0;// score
    private static int intIndex, len;
    public static float float1, float2, screenSCale;
    // user customer
    final Context context;
    // callback
    protected OnEndOfGameInterface mOnEndOfGame; // callback interface
    // flag
    private boolean flagGameThread;// 线程消亡的标识位
    private boolean flagIsGameOver;// 线程消亡的标识位
    private int flagButtonIndex;// 线程消亡的标识位
    private boolean flagIsTouchLongMove;// 是否长按的标识位
    private int keyCheck;
    // variable
    private int screenW, screenH; // Screen_size
    private SurfaceHolder sfh; // 用于控制SurfaceView
    private Paint paint;// 声明一个画笔
    private Paint paintFont;// 声明一个画笔
    private Canvas canvas;// 声明一个画布
    private Camera camera;
    private Camera cameraGlobal;
    private Camera cameraPlayer;
    // 声明Ball
    private PlayerBall playerBall;
    private FoodBall[] FoodBallList = new FoodBall[BALL_FOOD_COUNT];
    private BallTeam[] teams;
    private BallTeam teamOfPlayer;
    private List<Ball> allBalls;//全部成员
    private TeamsManager teamsManager;
    // 位图文件 bitmap
    private Bitmap bmpDir = BitmapFactory.decodeResource(this.getResources(), R.mipmap.dir);// 小球指针素材
    private Bitmap bmpBadgesVictory = BitmapFactory.decodeResource(this.getResources(), R.mipmap.badges_victory);// 球球胜利徽章素材
    private Bitmap bmpBadgesDefeat = BitmapFactory.decodeResource(this.getResources(), R.mipmap.badges_defeat);// 球球失败徽章素材
    // button
    private Bitmap bmpBtnAvatar = BitmapFactory.decodeResource(this.getResources(), R.mipmap.button_avetar);// 分身按钮
    private Bitmap bmpBtnDanger = BitmapFactory.decodeResource(this.getResources(), R.mipmap.button_danger);// 危险按钮
    private Bitmap bmpBtnBattle = BitmapFactory.decodeResource(this.getResources(), R.mipmap.button_battle);// 召唤按钮
    private Bitmap bmpCamera = BitmapFactory.decodeResource(this.getResources(), R.mipmap.camera);// 摄像机
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
        camera = new Camera();
        cameraGlobal = new Camera();
        cameraPlayer = new Camera();
        // 实例SurfaceHolder
        sfh = this.getHolder();
        // 为SurfaceView添加状态监听
        sfh.addCallback(this);
        // 实例一个画笔
        paint = new Paint();
        Rocker.basePosition = new PointF(Rocker.rockerPosition.x, Rocker.rockerPosition.y);
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
        if (PLAYER_NAME.length() == 0) {
            PLAYER_NAME = "你个傻瓜没写名字";
        }
        if (PLAYER_TEAM_COLOR > 6 || PLAYER_TEAM_COLOR < 0) {
            PLAYER_TEAM_COLOR = 10;
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
//scale
        screenSCale = screenW / 800;
        RANK_LIST_WIDTH = 210 * screenSCale;
        PADDING = 5 * screenSCale;
        RANK_LIST_ITEM_HEIGHT = 32.5F * screenSCale;// 排行榜尺寸
        ROCKER_RUDDER_RADIUS = (int) (30 * screenSCale);// 摇杆半径
        ROCKER_ACTION_RADIUS = (int) (75 * screenSCale);// 摇杆活动范围半径
        ROCKER_WHEEL_RADIUS = (int) (60 * screenSCale);// 摇杆底座范围半径
        ROCKER_ACTIVITY_RADIUS = (int) (30 * screenSCale);// 摇杆活动范围半径

        //camera global
        PointF map = new PointF(MAP_WIDTH + MAP_MARGIN, MAP_HEIGHT + MAP_MARGIN);
        float sx = (float) screenW / map.x;
        float sy = (float) screenH / map.y;
        cameraGlobal.Focus.x = screenW / 2 - MAP_WIDTH / 2;
        cameraGlobal.Focus.y = screenH / 2 - MAP_HEIGHT / 2;
        cameraGlobal.Scale.y = cameraGlobal.Scale.x = sx > sy ? sy : sx;
        cameraGlobal.ScalePosition.x = MAP_WIDTH / 2;
        cameraGlobal.ScalePosition.y = MAP_HEIGHT / 2;


        // initialization food Ball
        for (intIndex = 0; intIndex < FoodBallList.length; intIndex++) {
            FoodBallList[intIndex] = new FoodBall((int) (MAP_WIDTH * Math.random()),
                    (int) (MAP_HEIGHT * Math.random()), getColorRandom());
        }
        teamsManager = new TeamsManager();
        teams = teamsManager.getTeams();

        // initialization player aiBall
        teamOfPlayer = teamsManager.getTeamOfPlayer();
        playerBall = teams[0].initPlayer(gameSounds);

        playerBall.isAuto =true;
        camera.isPlayerCamera=false;

        allBalls = teamsManager.getAllBalls();
        flagIsGameOver = false;

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
        score = (int) playerBall.radius;

        // check BEST_SCORE
        if (Integer.parseInt(BEST_SCORE) < score) BEST_SCORE = score + "";
    }

    /**
     * 触屏事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        intIndex = event.getPointerCount();
//        if (intIndex > 1) {
//            for (intIndex = 0; intIndex < intIndex; intIndex++) {
//                if (flagButtonIndex == intIndex
//                        && event.getX(intIndex) > (screenW - bmpBtnAvatar
//                        .getWidth() * 2)
//                        && event.getX(intIndex) < (screenW - bmpBtnAvatar
//                        .getWidth())
//                        && event.getY(intIndex) > (screenH - bmpBtnAvatar
//                        .getHeight())) {
//                    //launch
//                }
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // System.out.println("---按下----");
//
//                        if (event.getX(intIndex) > (screenW - bmpBtnAvatar.getWidth() * 2)
//                                && event.getY(intIndex) > (screenH - bmpBtnAvatar
//                                .getHeight())) {
//                            if (event.getX(intIndex) < (screenW - bmpBtnAvatar
//                                    .getWidth())) {
//                                gameSounds.starMusic(GameSounds.BUBBLE);
//                            } else {
////                                playerBall.avatar();
//                                gameSounds.starMusic(GameSounds.AVATAR);
//                            }
//                            flagButtonIndex = intIndex;
//                            break;
//                        } else {
//                            Rocker.isShow = true;
//                            Rocker.rockerPosition.set((int) event.getX(intIndex),
//                                    (int) event.getY(intIndex));
//                            Rocker.basePosition.set((int) event.getX(intIndex) + 1,
//                                    (int) event.getY(intIndex));
//                        }
//                    case MotionEvent.ACTION_UP:
//                        // System.out.println("----放开----");
//                        flagIsTouchLongMove = true;
////                        Rocker.isShow = false;
//                        flagButtonIndex = -1;
//                        if (flagGameOver) {
//                            System.out.println("over");
//                        }
//                        if (Math.abs(event.getX(intIndex) - screenW / 2) < 150
//                                && (screenH / 2 - event.getY(intIndex)) < 50) {
//                            System.out.println("over 2");
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (flagButtonIndex == intIndex) {
//                            break;
//                        }
//                        Rocker.isShow = true;
//                        // System.out.println("----移动----");
//                        if (event.getPointerCount() == 1) {
//                            int len = GameMath.getLength(Rocker.rockerPosition.x,
//                                    Rocker.rockerPosition.y, event.getX(intIndex),
//                                    event.getY(intIndex));
//                            if (len < 20 && flagIsTouchLongMove) {
//                                // 如果屏幕接触点不在摇杆挥动范围内,则不处理
//                                break;
//                            }
//                            if (len <= ROCKER_ACTION_RADIUS) {
//                                // 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
//                                flagIsTouchLongMove = false;
//                                Rocker.basePosition.set((int) event.getX(intIndex),
//                                        (int) event.getY(intIndex));
//
//                            } else {
//                                // 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
//                                flagIsTouchLongMove = false;
//                                Rocker.basePosition = GameMath.getBorderPoint(
//                                        Rocker.rockerPosition,
//                                        new PointF((int) event.getX(intIndex),
//                                                (int) event.getY(intIndex)),
//                                        ROCKER_ACTION_RADIUS);
//                            }
//
//                            playerBall.directionTarget = GameMath.getRadian(
//                                    Rocker.rockerPosition, Rocker.basePosition);
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
                        //sendDanger
                        System.out.print("sendDanger");
                        gameSounds.starMusic(BUBBLE);
                        playerBall.setState(DANGER,playerBall);
                    } else if (event.getX() < (screenW - bmpBtnAvatar.getWidth())) {
//                        sendBattle
                        System.out.print("sendBattle");
                        gameSounds.starMusic(BATTLE);
                        playerBall.setState(Constants.MessageType.BATTLE,playerBall);
                    } else {
//                        avatar
                        System.out.print("avatar");
                        gameSounds.starMusic(AVATAR);
                        playerBall.avatar(playerBall.direction);
                    }
                    flagButtonIndex = 1;
                    return true;
                } else if (event.getX() > RANK_LIST_WIDTH && event.getX() < RANK_LIST_WIDTH + bmpCamera.getWidth() && event.getY() < bmpCamera.getHeight() + PADDING) {
                    Camera.isPlayerCamera = !Camera.isPlayerCamera;
                    gameSounds.starMusic(CLICK);
                    flagButtonIndex = 1;
                    return true;
                } else if (event.getX() < RANK_LIST_WIDTH && event.getY() < bmpCamera.getHeight() + PADDING) {
                    playerBall.isAuto = !playerBall.isAuto;
                    gameSounds.starMusic(CLICK);
                    flagButtonIndex = 1;
                    return true;
                } else {
                    Rocker.isShow = true;
                    Rocker.rockerPosition.set((int) event.getX(), (int) event.getY());
                    Rocker.basePosition.set((int) event.getX() + 1, (int) event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                // System.out.println("----放开----");
                flagIsTouchLongMove = true;
//                    Rocker.isShow = false;
                flagButtonIndex = -1;
                len = GameMath.getLength(Rocker.rockerPosition.x, Rocker.rockerPosition.y, event.getX(), event.getY());
                if (len < 1) {
                    playerBall.setVector(playerBall.directionTarget, 0.05F);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (flagButtonIndex == 1 || playerBall.isAuto) {
                    Rocker.isShow = false;
                    break;
                }
                Rocker.isShow = true;
                // System.out.println("----移动----");
                if (event.getPointerCount() == 1) {
                    len = GameMath.getLength(Rocker.rockerPosition.x, Rocker.rockerPosition.y, event.getX(), event.getY());
                    if (len < ROCKER_ACTIVITY_RADIUS && flagIsTouchLongMove) {
                        // 如果屏幕接触点不在摇杆挥动范围内,则不处理
                        return true;
                    }
                    if (len <= ROCKER_ACTION_RADIUS) {
                        // 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
                        flagIsTouchLongMove = false;
                        Rocker.basePosition.set((int) event.getX(), (int) event.getY());

                    } else {
                        // 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
                        flagIsTouchLongMove = false;
                        Rocker.basePosition = GameMath.getBorderPoint(Rocker.rockerPosition, new PointF((int) event.getX(), (int) event.getY()), ROCKER_ACTION_RADIUS);
                    }
                    playerBall.setVector(GameMath.getRadian(Rocker.rockerPosition, Rocker.basePosition), (float) Math.sqrt((Rocker.basePosition.x - Rocker.rockerPosition.x) * (Rocker.basePosition.x - Rocker.rockerPosition.x) + (Rocker.basePosition.y - Rocker.rockerPosition.y) * (Rocker.basePosition.y - Rocker.rockerPosition.y)) / ROCKER_ACTION_RADIUS);
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
        Rocker.isShow = true;
        if (keyCheck != keyCode) {
            keyCheck = keyCode;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {//up >> right
                Rocker.rockerPosition.x = 100;
                Rocker.rockerPosition.y = screenH - 100;
                Rocker.basePosition.x = 100 + 65;
                Rocker.basePosition.y = screenH - 100;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// down >> left
                Rocker.rockerPosition.x = 100;
                Rocker.rockerPosition.y = screenH - 100;
                Rocker.basePosition.x = 100 - 65;
                Rocker.basePosition.y = screenH - 100;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// left >> up
                Rocker.rockerPosition.x = 100;
                Rocker.rockerPosition.y = screenH - 100;
                Rocker.basePosition.x = 100;
                Rocker.basePosition.y = screenH - 100 - 65;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// right >> down
                Rocker.rockerPosition.x = 100;
                Rocker.rockerPosition.y = screenH - 100;
                Rocker.basePosition.x = 100;
                Rocker.basePosition.y = screenH - 100 + 65;
            }
            playerBall.directionTarget = GameMath.getRadian(Rocker.rockerPosition,
                    Rocker.basePosition);
        }

        return super.onKeyDown(keyCode, event);

    }

    /**
     * 进程控制
     */
    @Override
    public void run() {
        while (flagGameThread) {
            if (flagIsGameOver) {
                //game over: 满足死亡的生命数，且不在等待时间内

                // check BEST_SCORE
                if (Integer.parseInt(BEST_SCORE) < score) BEST_SCORE = score + "";

                flagGameThread = false;
                mOnEndOfGame.onEndOfGame();
                // 结束进程
                break;
            } else {
                logic();
                myDraw();
                Clock.timeRun();
                if (Clock.isGameTimeout()) {
                    flagIsGameOver = true;
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

                // 绘制背景
                DrawBackground();
                // 绘制食物小球
                for (FoodBall foodBall : FoodBallList) {
                    paint.setColor(ContextCompat.getColor(context, foodBall.colorDraw));
                    canvas.drawCircle((float) foodBall.positionX,
                            (float) foodBall.positionY, foodBall.radius, paint);
                }

                // 绘制team
                for (Ball member : allBalls) {

                    paint.setColor(ContextCompat.getColor(context, member.colorDraw));
                    paintFont.setColor(ContextCompat.getColor(context, R.color.rocker));
                    if (member.state) {
                        // AI 活着
                        // 绘制AI大球
                        canvas.drawCircle(member.position.x, member.position.y, member.radius + 10, paintFont);
                        canvas.drawCircle(member.position.x, member.position.y, member.radius, paint);

                        // 名称字体大小
//                        paintFont.setTextSize((member.radius > 40 ? (20 + ((int) member.radius > 90 ? 15 : member.radius / 6)) : 23) * screenSCale * 2);
                        // 绘制名称
//                        canvas.drawText(member.name, member.position.x - paintFont.measureText(member.name) / 2, member.position.y + (member.radius > 40 ? (20 + ((int) member.radius > 90 ? 15 : member.radius / 6)) : 23) * screenSCale / 2, paintFont);
                    }
                }
                // 绘制角色球
                if (playerBall.state) {
                    drawArrow();
                }
                canvas.restore();

                canvas.save();

                // 摇杆
                if (Rocker.isShow) {
                    paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                    canvas.drawCircle(Rocker.rockerPosition.x, Rocker.rockerPosition.y,
                            ROCKER_WHEEL_RADIUS, paint);// 绘制范围
                    paint.setColor(ContextCompat.getColor(context, R.color.rocker));
                    canvas.drawCircle(Rocker.basePosition.x, Rocker.basePosition.y,
                            ROCKER_RUDDER_RADIUS, paint);// 绘制摇杆
                }
                // button
                if (!playerBall.isAuto) {
                    canvas.drawBitmap(bmpBtnAvatar, screenW - bmpBtnAvatar.getWidth(), screenH - bmpBtnAvatar.getHeight(), paint);
                    canvas.drawBitmap(bmpBtnBattle, screenW - bmpBtnAvatar.getWidth() - bmpBtnBattle.getWidth(), screenH - bmpBtnBattle.getHeight(), paint);
                    canvas.drawBitmap(bmpBtnDanger, screenW - bmpBtnAvatar.getWidth() - bmpBtnBattle.getWidth() - bmpBtnDanger.getWidth(), screenH - bmpBtnDanger.getHeight(), paint);
                }
                // camera
                if (Camera.isPlayerCamera)
                    paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                else
                    paint.setColor(ContextCompat.getColor(context, R.color.rocker));
                canvas.drawBitmap(bmpCamera, RANK_LIST_WIDTH + PADDING, PADDING, paint);

                // score计分板
                paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                paintFont.setTextSize((float) (0.7 * RANK_LIST_ITEM_HEIGHT));
                paintFont.setColor(ContextCompat.getColor(context, R.color.white_transparent));
                canvas.drawRect(PADDING, PADDING, RANK_LIST_WIDTH, RANK_LIST_ITEM_HEIGHT + PADDING, paint);
                if (playerBall.isAuto) {
                    // auto
                    canvas.drawText("自动操作", 30 * screenSCale, 28 * screenSCale, paintFont);
                } else {
                    canvas.drawText("手动操作", 30 * screenSCale, 28 * screenSCale, paintFont);
                }
                canvas.drawRect(PADDING, RANK_LIST_ITEM_HEIGHT + PADDING, RANK_LIST_WIDTH, RANK_LIST_ITEM_HEIGHT + RANK_LIST_ITEM_HEIGHT + PADDING, paint);
                canvas.drawText("我的分数:" + (int) playerBall.radius, 30 * screenSCale, 28 * screenSCale + RANK_LIST_ITEM_HEIGHT,
                        paintFont);

                // 倒计时
                canvas.drawText(Clock.getTimeStr(),
                        screenW / 2 - 25, 28 * screenSCale, paintFont);
                // rank排行榜
                canvas.drawRect(screenW - RANK_LIST_WIDTH - PADDING, PADDING, screenW - PADDING, 26 * screenSCale, paint);
                intIndex = 0;
                canvas.drawText("排行榜", screenW - RANK_LIST_WIDTH + PADDING * 8, 4 * PADDING, paintFont);
                for (BallTeam team : teams) {

                    // rank bg Rect
                    canvas.drawRect(screenW - RANK_LIST_WIDTH - PADDING,
                            intIndex * RANK_LIST_ITEM_HEIGHT + 26 * screenSCale, screenW - PADDING, (intIndex + 1)
                                    * RANK_LIST_ITEM_HEIGHT + 26 * screenSCale, paint);

                    paintFont.setColor(ContextCompat.getColor(context, team.teamColor));
                    canvas.drawText(intIndex + 1 + "." + (team.teamName.length() > 5 ? team.teamName.subSequence(0, 5) : team.teamName), screenW - RANK_LIST_WIDTH + PADDING * 2, 50 * screenSCale + intIndex * RANK_LIST_ITEM_HEIGHT, paintFont);
                    if (team != playerBall.getTeam()) {
                        paintFont.setColor(ContextCompat.getColor(context,
                                R.color.white_transparent));
                        // rank text
                        canvas.drawText("" + team.getScore(), screenW - 65 * screenSCale, 50 * screenSCale + intIndex * RANK_LIST_ITEM_HEIGHT, paintFont);
                    } else {
                        // rank text
                        paintFont.setColor(ContextCompat.getColor(context,
                                R.color.color1));
                        // rank text
                        canvas.drawText("" + team.getScore(), screenW - 65 * screenSCale, 50 * screenSCale + intIndex * RANK_LIST_ITEM_HEIGHT, paintFont);

                    }
                    intIndex++;
                }
                // rank Bottom Rect
                canvas.drawRect(screenW - RANK_LIST_WIDTH - PADDING, intIndex * RANK_LIST_ITEM_HEIGHT + 26 * screenSCale,
                        screenW - PADDING, (intIndex + 1.2F) * RANK_LIST_ITEM_HEIGHT, paint);
                // bmp Rank ICO
                canvas.clipRect(screenW - RANK_LIST_WIDTH - PADDING, PADDING, screenW - PADDING,
                        (intIndex + 1) * RANK_LIST_ITEM_HEIGHT - 7);
//                canvas.drawBitmap(bmpRank, screenW - RANK_LIST_WIDTH, 13*screenSCale, paint);


                canvas.restore();
                canvas.save();
                // bmpBadges & bmpInfo
                if (teamsManager.isGameOver) {
                    if (teamsManager.isPlayerTeamWin) {
                        paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                        paintFont.setTextSize((float) (0.7 * RANK_LIST_ITEM_HEIGHT));
                        paintFont.setColor(ContextCompat.getColor(context,
                                R.color.white_transparent));
                        canvas.drawRect(PADDING, screenH - PADDING - RANK_LIST_ITEM_HEIGHT, RANK_LIST_WIDTH, screenH - PADDING, paint);
                        canvas.drawText("你的队伍获得胜利", PADDING * 2, screenH - 16 * screenSCale, paintFont);
                    } else {
                        paint.setColor(ContextCompat.getColor(context, R.color.rockerRudder));
                        paintFont.setTextSize((float) (0.7 * RANK_LIST_ITEM_HEIGHT));
                        paintFont.setColor(ContextCompat.getColor(context,
                                R.color.white_transparent));
                        canvas.drawRect(PADDING, screenH - PADDING - RANK_LIST_ITEM_HEIGHT, RANK_LIST_WIDTH, screenH - PADDING, paint);
                        canvas.drawText("你的队伍全军覆灭", PADDING * 2, screenH - 16 * screenSCale, paintFont);
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

    private void drawArrow() {
        // dir
        if (playerBall.directionTarget != 404) {
            if (!playerBall.isAuto) {
                Matrix matrix = new Matrix();
                // 设置旋转角度
                matrix.postScale(playerBall.radius / 230,
                        playerBall.radius / 230, bmpDir.getWidth() / 2,
                        bmpDir.getHeight() / 2);
                matrix.postRotate(
                        (float) (playerBall.direction * (180 / Math.PI)) + 90,
                        bmpDir.getWidth() / 2, bmpDir.getHeight() / 2);
                matrix.postTranslate(playerBall.position.x - bmpDir.getWidth() / 2 + (int) (playerBall.radius * 1.25 * Math.cos(playerBall.direction)), playerBall.position.y - bmpDir.getHeight() / 2 + (int) (playerBall.radius * 1.25 * Math.sin(playerBall.direction)));
                // 绘制旋转图片
                canvas.drawBitmap(bmpDir, matrix, paint);
            }
        }
        if (!camera.isPlayerCamera) {
            // AI 活着
            // 绘制AI大球
            paint.setColor(ContextCompat.getColor(context, R.color.player));
            canvas.drawCircle(playerBall.position.x, playerBall.position.y, playerBall.radius + 200, paint);
        }
    }

    /**
     * 绘制背景
     */

    private void DrawBackground() {
        // -----------利用填充画布，刷屏
        canvas.drawColor(ContextCompat.getColor(context, R.color.background2));
        // //绘制矩形
        paint.setColor(ContextCompat.getColor(context, R.color.background));
        canvas.drawRect(0, 0, MAP_WIDTH, MAP_HEIGHT, paint);
        int rowWidth = (int) (4 * PADDING), rowInterval = (int) (20 * PADDING);
        paint.setColor(ContextCompat.getColor(context, R.color.backgroundStripe));
//        for (intIndex = 1; intIndex <= MAP_HEIGHT / (rowWidth + rowInterval); intIndex++) {
//            canvas.drawRect(0, intIndex * (rowWidth + rowInterval), MAP_WIDTH, intIndex
//                    * (rowWidth + rowInterval) + rowWidth, paint);
//        }
//        for (intIndex = 1; intIndex <= MAP_WIDTH / (rowWidth + rowInterval); intIndex++) {
//            canvas.drawRect(intIndex * (rowWidth + rowInterval), 0, intIndex
//                    * (rowWidth + rowInterval) + rowWidth, MAP_HEIGHT, paint);
//        }

    }

    /**
     * 游戏逻辑
     */
    private void logic() {

        allBalls = teamsManager.getAllBalls();
        if (!playerBall.state) {
//            if (!playerBall.getTeam().resetPlayer(playerBall)) {
//                flagIsGameOver = true;
//            }
            playerBall.getTeam().resetPlayer(playerBall);
        }
//        playerBall.move((float) Math
//                .sqrt((Rocker.basePosition.x - Rocker.rockerPosition.x)
//                        * (Rocker.basePosition.x - Rocker.rockerPosition.x)
//                        + (Rocker.basePosition.y - Rocker.rockerPosition.y)
//                        * (Rocker.basePosition.y - Rocker.rockerPosition.y))
//                / ROCKER_ACTION_RADIUS);
//        if (playerBall.life == 0
//                || playerBall.life == (BALL_AI_COUNT + 1) * BALL_DEFAULT_LIFE) {
//            flagGameOver = true;
//        }

//            // 锁定遥感
//            Rocker.basePosition.y = Rocker.rockerPosition.y;
//            Rocker.basePosition.x = Rocker.rockerPosition.x;
//            Rocker.isShow = false;


        try {
            for (Ball ball_1 : allBalls) {
                if (!ball_1.state) continue;
                float1 = ball_1.radius * 3;
                //基本活动
                ball_1.action();
                for (Ball ball_2 : allBalls) {
                    if (!ball_1.state) break;
                    if (!ball_2.state) break;
                    //获取距离
                    float2 = GameMath.getDistance(ball_1.position, ball_2.position);
                    // 判断是否在范围内
                    if (float2 < float1) {
                        // 判断是否碰撞
                        if (ball_1.radius > ball_2.radius) {
                            // 感知
                            if (float2 < Math.sqrt(ball_1.radius * ball_1.radius - ball_2.radius * ball_2.radius)) {
                                ball_1.eat(ball_2);
                            } else {
                                // 感知
                                ball_1.feeling(ball_2, true);
                            }
                        } else {
                            if (float2 < Math.sqrt(ball_2.radius * ball_2.radius - ball_1.radius * ball_1.radius)) {
                                ball_2.eat(ball_1);
                            } else {
                                // 感知
                                ball_1.feeling(ball_2, false);
                            }
                        }
                    }
                }

            }
            //食物
            for (FoodBall foodBall : FoodBallList) {
                if (!foodBall.state) {
                    // 重置
                    foodBall.reSetBall((int) (MAP_WIDTH * Math.random()), (int) (MAP_HEIGHT * Math.random()), getColorRandom());
                    continue;
                }
                for (Ball member : allBalls) {
                    if (!foodBall.state) break;
                    // 判断是否被吃
                    if ((foodBall.positionX - member.position.x)
                            * (foodBall.positionX - member.position.x)
                            + (foodBall.positionY - member.position.y)
                            * (foodBall.positionY - member.position.y) < (member.radius - foodBall.radius)
                            * (member.radius - foodBall.radius)) {
                        member.weight += foodBall.die() * (member.getTeam().equals(teamOfPlayer) ? 1 : AI_DIFFICULT / 10);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //排序
        if (Clock.isSort()) {
            teamsManager.sort();
        }


        //摄像机视野调整
        if (Camera.isPlayerCamera) {
            // 以玩家为中心
            camera = cameraPlayer;
            cameraPlayer.Focus.x = -playerBall.position.x + screenW / 2;
            cameraPlayer.Focus.y = -playerBall.position.y + screenH / 2;
            float1 = (float) (screenH / (2500 + 400 * ((playerBall.radius / (Math.sqrt(BALL_WEIGHT_DEFAULT)) / 20)) + playerBall.radius * 2));
            cameraPlayer.Scale.x = float1;
            cameraPlayer.Scale.y = float1;
            cameraPlayer.ScalePosition.x = playerBall.position.x;
            cameraPlayer.ScalePosition.y = playerBall.position.y;
        } else {
            //全局视野
            camera = cameraGlobal;
        }
    }

    /**
     * callback interface
     */
    public interface OnEndOfGameInterface {
        void onEndOfGame();
    }


}


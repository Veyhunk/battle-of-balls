package com.veyhunk.battle_of_balls.constants;

/**
 * Created by Veyhunk on 12/March/2017.
 * Game constants
 */

public class Constants {

    //size

    public static int BALL_ID;//ball id
    public static int ROCKER_RUDDER_RADIUS = 30;// 摇杆半径
    public static int ROCKER_ACTION_RADIUS = 75;// 摇杆活动范围半径
    public static int ROCKER_WHEEL_RADIUS = 60;// 摇杆底座范围半径
    public static int ROCKER_ACTIVITY_RADIUS = 30;// 摇杆活动范围半径
    public static float PADDING = 5;// 边距
    public static float RANK_LIST_WIDTH = 210, RANK_LIST_ITEM_HEIGHT = 32.5F; // 排行榜尺寸

    //constant
    public static final int MAP_WIDTH = 8000, MAP_HEIGHT = 6000; // Map_size
    public static final int MAP_MARGIN = 301; // Map_MARGIN
    public static final int BALL_AVATAR_DISTANCE = 400;// BALL_AVATAR_DISTANCE
    public static final int BALL_WEIGHT_MAX = 4000000;// BALL_WEIGHT_MAX
    public static final boolean BALL_STATE_DEAD = false;
    public static final boolean BALL_STATE_ALIVE = true;
    public static final float ACTION_DAMPING = 10;// 活动阻尼
    public static final float MAX_ACCELERATED_SPEED = 0.5F;// 最大加速度
    public static final double SQRT1_2 = 0.7071067812;// 根号1/2
    //    public static final int FRAME_RATE = 50;// 帧率（单位：Hz/s）
    //Message Duration (util:second)
    private static int util = 10;
    public static final int[] MessageDuration = new int[]{1 * util, 1 * util, 1 * util, 5 * util, 5 * util,1 * util};

    private static final String[] BALL_NAMES = new String[]{"猪头","触手TV大白","小云云","熊霸天下","咕噜咕噜",
            "董大鹏", "关注我带团", "孙红雷", "北丘", "触手TV阿木", "二狗子", "被白菜怼过的猪", "冷瞳炸弹",
            "超萌的一天"};
    public static String getName() {
        return BALL_NAMES[(int) (Math.random() * 100 % BALL_NAMES.length)];
    }

    public static final class MessageType {
        public static final short EMPTY = 0;
        public static final short SAFE = 1;
        public static final short AVATAR = 2;
        public static final short BATTLE = 3;
        public static final short DANGER = 4;
        public static final short ESCAPE = 5;
    }

//    public class MoveTarget {
//        public PointF basePosition;
//        public double direction;
//
//        public MoveTarget() {
//        }
//
//        public void setTarget(PointF basePosition, double direction) {
//            this.basePosition = basePosition;
//            this.direction = direction;
//        }
//    }
}

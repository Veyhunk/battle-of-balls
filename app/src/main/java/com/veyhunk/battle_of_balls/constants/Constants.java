package com.veyhunk.battle_of_balls.constants;

import com.veyhunk.battle_of_balls.R;

/**
 * Created by Veyhunk on 12/March/2017.
 * Game constants
 */

public class Constants {
    // constant
    public static final float ACTION_DAMPING = 10;// 活动阻尼
    public static final double SQRT1_2 = 0.7071067812;// SQRT * 1/2
    public static final float MAX_ACCELERATED_SPEED = 0.5F;// MAX_ACCELERATED_SPEED
    public static final float RANK_LIST_WIDTH = 210, RANK_LIST_ITEM_HEIGHT = 32.5F; // tab_size
    public static final int FRAME_RATE = 50;// 帧率（单位：Hz/s）
    public static final int GAME_TIME = 320;// 游戏时长
    public static final int BALL_DEFAULT_LIFE = 3;// BALL_DEFAULT_LIFE
    public static final int BALL_AI_COUNT = 1;// BALL_AI_COUNT
    public static final int BALL_FOOD_COUNT = 600;// BALL_FOOD_COUNT
    public static final int BALL_DEFAULT_WEIGHT = 1600;// ballDefaultSize
    public static final int MAP_WIDTH = 3000, MAP_HEIGHT = 2000; // Map_size
    public static final int ROCKER_RUDDER_RADIUS = 30;// 摇杆半径
    public static final int ROCKER_ACTION_RADIUS = 75;// 摇杆活动范围半径
    public static final int ROCKER_WHEEL_RADIUS = 60;// 摇杆底座范围半径
    public static final int[] BALL_COLORS = new int[]{R.color.color0,
            R.color.color1, R.color.color2, R.color.color3, R.color.color4,
            R.color.color5, R.color.color6};// 颜色表
    public static final String[] BALL_NAMES = new String[]{"触手TV大白",
            "董大鹏", "关注我带团", "孙红雷", "北丘", "触手TV阿木", "二狗子", "被白菜怼过的猪", "冷瞳 炸弹",
            "超萌的一天"};
    public static final boolean BALL_STATE_DEAD = false;
    public static final boolean BALL_STATE_ALIVE = true;
    //Message Duration (util:second)
    private static int util = 1;
    public static final int[] MessageDuration = new int[]{0 * util, 1 * util, 1 * util, 5 * util, 5 * util};

    public static String getName() {
        return BALL_NAMES[(int) (Math.random() * 100 % BALL_NAMES.length)];
    }

    public static final class MessageType {
        public static final short EMPTY = 0;
        public static final short SAFE = 1;
        public static final short AVATAR = 2;
        public static final short BATTLE = 3;
        public static final short DANGED = 4;
    }

    public static final class TEAM_PARAMS {
        public static final int TEAM_AMOUNT = 4;
        public static final String[] TEAM_NAMES = new String[]{"SSS战队", "吞噬军团",
                "START", "人帅手速快", "浪够了回家", "触手TV阿木", "二狗子", "被白菜怼过的猪", "冷瞳 炸弹",
                "超萌的一天"};
        public static final int TEAM_MEMBER_AMOUNT = 5;
    }
//    public static class MoveTarget {
//        public Point position;
//        public double direction;
//
//        public MoveTarget() {
//        }
//
//        public void setTarget(Point position, double direction) {
//            this.position = position;
//            this.direction = direction;
//        }
//    }
}

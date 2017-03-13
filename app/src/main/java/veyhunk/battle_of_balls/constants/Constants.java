package veyhunk.battle_of_balls.constants;

import veyhunk.battle_of_balls.R;

/**
 * Created by Veyhunk on 12/March/2017.
 * Game constants
 */

public class Constants {
    // constant
    public static final float ACTION_DAMPING = 10;// 活动阻尼
    public static final double SQRT1_2 = 0.7071067812;// 活动阻尼
    public static final float RANK_LIST_WIDTH = 210, RANK_LIST_ITEM_HEIGHT = 32.5F; // tab_size
    public static final int FRAME_RATE = 50;// 帧率（单位：Hz/s）
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
    public static String name;
    public static boolean BALL_STATE_DEAD = false;
    public static boolean BALL_STATE_ALIVE = true;

    public static class MessageType {
        public static short DANGED = 0;
        public static short HELP = 1;
        public static short SAFE = 2;
    }

}

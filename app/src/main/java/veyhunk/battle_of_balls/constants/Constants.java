package veyhunk.battle_of_balls.constants;

import veyhunk.battle_of_balls.R;

/**
 * Created by Veyhunk on 12/March/2017.
 * Game constants
 */

public  class Constants {
    public static String name;

    public static boolean BALL_STATE_DEAD=false;
    public static boolean BALL_STATE_ALIVE=true;
    // constant
    public static final float actionDamping = 10;// 活动阻尼
    public static final float itemW = 210, itemH = 32.5F; // tab_size
    public static final int frameRate = 50;// 帧率（单位：Hz/s）
    public static final int ballDefaultLife = 3;// ballDefaultLife
    public static final int ballAiCount = 10;// ballAiCount
    public static final int ballFoodCount = 600;// ballFoodCount
    public static final int ballDefaultWeight = 1600;// ballDefaultSize
    public static final int mapW = 3000, mapH = 2000; // Map_size
    public static final int[] ballColor = new int[] { R.color.color0,
            R.color.color1, R.color.color2, R.color.color3, R.color.color4,
            R.color.color5, R.color.color6 };// 颜色表
    public static final String[] strArrayName = new String[] { "触手TV大白",
            "董大鹏", "关注我带团", "孙红雷", "北丘", "触手TV阿木", "二狗子", "被白菜怼过的猪", "冷瞳 炸弹",
            "超萌的一天" };

}

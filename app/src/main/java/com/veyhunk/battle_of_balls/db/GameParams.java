package com.veyhunk.battle_of_balls.db;

/**
 * Created by Veyhunk on 09/March/2017.
 * Game Params
 */

public class GameParams {

    public static String ballName; //玩家名称
    public static String bestScore; //最佳分数
    public static float ballMoveSpeed;//移动速度
    public static float ballGrowSpeed;//成长速度
    public static float aiDifficult;//敌人难度
    public static int ballColorIndex;//颜色编号

    // variable
    public static int GAME_TIME = 320;// 游戏时长（单位：s）
    public static int BALL_FOOD_COUNT = 400;// 食物数量
    public static final class TEAM_PARAMS {
        public static int TEAM_AMOUNT = 4;//队伍总数
        public static int TEAM_MEMBER_AMOUNT = 3;//队伍成员数
        public static int TEAM_MEMBER_MAX = 16;//最大队友数量
        public static final String[] TEAM_NAMES = new String[]{"SSS战队", "吞噬军团",
                "START", "人帅手速快", "浪够了回家", "触手TV阿木", "二狗子", "被白菜怼过的猪", "冷瞳 炸弹",
                "超萌的一天"};
    }

}

package com.veyhunk.battle_of_balls.db;

/**
 * Created by Veyhunk on 09/March/2017.
 * Game Params
 */

public class GameParams {

    public static String PLAYER_NAME; //玩家名称
    public static String BEST_SCORE; //最佳分数
    public static float BALL_MOVE_SPEED;//移动速度
    public static float BALL_GROW_SPEED;//成长速度
    public static float AI_DIFFICULT;//敌人难度
    public static int PLAYER_TEAM_COLOR;//颜色编号
    public static int BALL_WEIGHT_DEFAULT = 2500;// BALL_WEIGHT_DEFAULT

    // variable
    public static int GAME_TIME = 320;// 游戏时长（单位：s）
    public static int BALL_FOOD_COUNT = 400;// 食物数量

    public static final class TEAM_PARAMS {
        public static int TEAM_AMOUNT = 4;//队伍总数
        public static int TEAM_MEMBER_AMOUNT = 3;//队伍成员数
        public static int TEAM_MEMBER_MAX = 16;//最大队友数量
    }

}

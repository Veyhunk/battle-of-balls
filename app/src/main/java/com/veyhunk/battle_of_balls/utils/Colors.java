package com.veyhunk.battle_of_balls.utils;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_COLORS;

/**
 * Created by Veyhunk on 12/March/2017.
 * Colors
 */

public class Colors {

    /**
     * 随机生成颜色
     */
    public static int getColorRandom() {
        return BALL_COLORS[(int) (Math.random() * BALL_COLORS.length)];
    }

    public static int getColorByIndex(int colorIndex) {
        return BALL_COLORS[colorIndex%BALL_COLORS.length];
    }

}

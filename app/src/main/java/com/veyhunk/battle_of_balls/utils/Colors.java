package com.veyhunk.battle_of_balls.utils;

import com.veyhunk.battle_of_balls.R;

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
    public static final int[] BALL_COLORS = new int[]{
            R.color.color0, R.color.color1,
            R.color.color2, R.color.color3,
            R.color.color4, R.color.color5,
            R.color.color6, R.color.color7,
            R.color.color8, R.color.color9,
            R.color.color10, R.color.color11,
            R.color.color12, R.color.color13,
            R.color.color14, R.color.color15, };// 颜色表

}

package com.veyhunk.battle_of_balls.utils;


import static com.veyhunk.battle_of_balls.db.GameParams.GAME_TIME;

/**
 * Created by Veyhunk on 12/March/2017.
 * Game Clock
 */

public final class Clock {
    // time
    public static Long timeBegin;
    public static int timeMinute;// m
    public static int timeSecond;// s
    public static int timeGame;// total second of time
    private static int sortBegin;
    private static int sortRang = 500;

    public static void setTimeBegin() {
        timeBegin = System.currentTimeMillis();
        timeGame = GAME_TIME;
        timeRun();
        sortBegin = getClock()-sortRang;
    }

    public static boolean isSort() {
        if (isTimeOver(sortBegin, sortRang)) {
            sortBegin = getClock();
            return true;
        } else {
            return false;
        }
    }

    public static int getClock() {
        return (int) (System.currentTimeMillis() - timeBegin);
    }

    public static boolean isTimeOver(int begin, int range) {
        return (int) (System.currentTimeMillis() - timeBegin) - begin > range;
    }

    public static String getTimeStr() {

        String strTime;
        // 倒计时
        if (timeMinute > 9 && timeSecond > 9) {
            strTime = timeMinute + ":" + timeSecond;
        } else if (timeMinute > 9) {
            strTime = timeMinute + ":0" + timeSecond;
        } else if (timeSecond > 9) {
            strTime = "0" + timeMinute + ":" + timeSecond;
        } else {
            strTime = "0" + timeMinute + ":0" + timeSecond;
        }
        return strTime;
    }

    public static void timeRun() {

        timeSecond = (timeGame - getClock() / 1000) % 60;
        timeMinute = (timeGame - getClock() / 1000) / 60;
    }

    public static boolean isGameTimeout() {
        if (timeSecond < 0) {
            timeSecond = 0;
            return true;
        }
        return false;
    }
}

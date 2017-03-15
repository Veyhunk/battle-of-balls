package veyhunk.battle_of_balls.utils;

import static veyhunk.battle_of_balls.constants.Constants.GAME_TIME;

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

    public static void setTimeBegin() {
        timeBegin = System.currentTimeMillis();
        timeGame = GAME_TIME;
        timeRun();
    }

    public static int getClock() {
        return (int) (System.currentTimeMillis() - timeBegin);
    }

    public static boolean getClockIsInRange(int begin, int range) {
        return (int) (System.currentTimeMillis() - timeBegin) - begin <= range;
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

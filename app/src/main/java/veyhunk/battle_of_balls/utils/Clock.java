package veyhunk.battle_of_balls.utils;

/**
 * Created by Veyhunk on 12/March/2017.
 * Clock
 */

public class Clock {
    // time
    public static Long timeBegin;
    public static int timeMinute;// m
    public static int timeSecond;// s
    public static int timeGame;// total second of time
//    public static int timeNewRaceRange = 2000;// 开始游戏前的无敌时间（单位：ms）
    public Clock(){

        timeSecond = 0;
        timeMinute = 1;
        timeGame = 320;
        // Clock Record
        timeBegin = System.currentTimeMillis();
    }


    public static int getClock() {
        return (int) (System.currentTimeMillis() - timeBegin);
    }

    public static boolean getClockIsInRange(int begin, int range) {
        return (int) (System.currentTimeMillis() - timeBegin) - begin <= range;
    }
    public String getTimeStr(){

        String strTime;
        // 倒计时
        if (timeMinute > 9 && timeSecond > 9) {
            strTime=timeMinute + ":" + timeSecond;
        } else if (timeMinute > 9) {
            strTime=timeMinute + ":0" + timeSecond;
        } else if (timeSecond > 9) {
            strTime="0" + timeMinute + ":" + timeSecond;
        } else {
            strTime="0" + timeMinute + ":0" + timeSecond;
        }
        return strTime;
    }
}

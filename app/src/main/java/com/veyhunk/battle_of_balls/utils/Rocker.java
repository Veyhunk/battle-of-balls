package com.veyhunk.battle_of_balls.utils;

import android.graphics.PointF;

/**
 * Created by Veyhunk on 09/April/2017.
 * Game Rocker
 */

public  class Rocker {

    public static boolean isShow = false;// 是否显示遥感的标识位
    public static PointF basePosition;// 摇杆位置
    public static PointF rockerPosition = new PointF(0, 0);// 摇杆起始位置
    public final int ROCKER_RUDDER_RADIUS = 30;// 摇杆半径
    public final int ROCKER_ACTION_RADIUS = 75;// 摇杆活动范围半径
    public final int ROCKER_ACTIVITY_RADIUS = 30;// 摇杆活动范围半径
    public final int ROCKER_WHEEL_RADIUS = 60;// 摇杆底座范围半径
    public void setTarget(){
        Rocker.isShow = !Rocker.rockerPosition.equals(Rocker.basePosition);

    }
    public void getTarget(){

    }

}

package com.veyhunk.battle_of_balls.model;

import android.graphics.PointF;

/**
 * Created by Veyhunk on 08/April/2017.
 */

public class Camera {
    public static boolean isPlayerCamera = true;
    public PointF Focus = new PointF(0,0);
    public PointF Scale = new PointF(0,0);
    public PointF ScalePosition = new PointF(0,0);

    public Camera(PointF focus, PointF scale, PointF scalePosition) {
        Focus = focus;
        Scale = scale;
        ScalePosition = scalePosition;
    }
    public Camera() {
    }
}

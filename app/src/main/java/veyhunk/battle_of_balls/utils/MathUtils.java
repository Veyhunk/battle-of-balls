package veyhunk.battle_of_balls.utils;

import android.graphics.Point;

import static veyhunk.battle_of_balls.constants.Constants.MAX_ACCELERATED_SPEED;

/**
 * Game Math Utils
 */
public class MathUtils {
    // 获取两点间直线距离
    public static int getLength(float x1, float y1, float x2, float y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * 获取线段上某个点的坐标，长度为a.x - cutRadius
     *
     * @param a         点A
     * @param b         点B
     * @param cutRadius 截断距离
     * @return 截断点
     */
    public static Point getBorderPoint(Point a, Point b, int cutRadius) {
        float radian = getRadian(a, b);
        // System.out.println(radian);
        return new Point(a.x + (int) (cutRadius * Math.cos(radian)), a.y
                + (int) (cutRadius * Math.sin(radian)));
    }

    // 获取水平线夹角弧度
    public static float getRadian(Point start, Point end) {
        float lenA = end.x - start.x;
        float lenB = end.y - start.y;
        if (lenA == 0 && lenB == 0) {
            return 404;
            //
        }
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        float ang = (float) Math.acos(lenA / lenC);
        ang = ang * (end.y < start.y ? -1 : 1);
        return ang;
    }

    //	计算角度
    public static float getRadian(float x1, float x2, float y1, float y2) {
        float lenA = x2 - x1;
        float lenB = y2 - y1;
        if (lenA == 0 && lenB == 0) {
            return 404;
        }
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        float ang = (float) Math.acos(lenA / lenC);
        ang = ang * (y2 < y1 ? -1 : 1);
        return ang;
    }
    public static float getAcceleratedSpeed(){
            return (float) Math.random()*MAX_ACCELERATED_SPEED;
    }

}

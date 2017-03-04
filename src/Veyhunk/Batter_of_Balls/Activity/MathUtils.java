package Veyhunk.Batter_of_Balls.Activity;

import android.graphics.Point;

public class MathUtils {
	// ��ȡ�����ֱ�߾���
	public static int getLength(float x1, float y1, float x2, float y2) {
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	/**
	 * ��ȡ�߶���ĳ��������꣬����Ϊa.x - cutRadius
	 * 
	 * @param a
	 *            ��A
	 * @param b
	 *            ��B
	 * @param cutRadius
	 *            �ضϾ���
	 * @return �ضϵ�
	 */
	public static Point getBorderPoint(Point a, Point b, int cutRadius) {
		float radian = getRadian(a, b);
		// System.out.println(radian);
		return new Point(a.x + (int) (cutRadius * Math.cos(radian)), a.y
				+ (int) (cutRadius * Math.sin(radian)));
	}

	// ��ȡˮƽ�߼нǻ���
	public static float getRadian(Point a, Point b) {
		float lenA = b.x - a.x;
		float lenB = b.y - a.y;
		float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
		float ang = (float) Math.acos(lenA / lenC);
		ang = ang * (b.y < a.y ? -1 : 1);
		return ang;
	}
}

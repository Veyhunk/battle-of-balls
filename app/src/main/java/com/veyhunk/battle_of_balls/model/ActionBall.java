package com.veyhunk.battle_of_balls.model;

/**
 * Created by Veyhunk on 09/April/2017.
 * Old ActionBall
 */


import com.veyhunk.battle_of_balls.utils.Clock;
import com.veyhunk.battle_of_balls.utils.Rocker;

import static com.veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_WEIGHT_DEFAULT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.SQRT1_2;
import static com.veyhunk.battle_of_balls.db.GameParams.ballMoveSpeed;
import static com.veyhunk.battle_of_balls.utils.Colors.getColorRandom;

/**
 * 定义活动球球的类，即角色球
 */
public class ActionBall {
    int ID;
    int eatByID;
    int life;
    int state;
    int weight;
    int eatCount;
    int colorDraw;
    int timeRandomActionBegin;
    int timeRandomActionRang;
    float moveSpeed;
    float moveSpeedRandom;
    float radius;
    double positionX;
    double positionY;
    double targetX;
    double targetY;
    double direction = 0;
    double directionTarget = 0;
    double inscribedSquareLen_1_2;
    String name;

    ActionBall(double positionX, double positionY, int colorDraw,
               float weight, String nameString, int life) {
        this.state = 1;// 未被吃
        this.positionX = positionX;
        this.positionY = positionY;
        this.colorDraw = colorDraw;
        this.eatCount = 0;
        this.life = life;
        this.moveSpeed = ballMoveSpeed;
        this.name = nameString;
        this.targetX = positionX;
        this.targetY = positionY;
        this.weight = (int) weight;
        this.timeRandomActionBegin = Clock.getClock() + 500;
    }

    // positionX, positionY, colorDraw, size
    void reSetBall(double positionX, double positionY, int colorDraw) {
        if (life > 0) {
            this.state = 1;// 复活
            this.weight = BALL_WEIGHT_DEFAULT;
            this.positionX = positionX;
            this.positionY = positionY;
            this.colorDraw = colorDraw;
            this.targetX = positionX;
            this.targetY = positionY;
            this.radius = 0;
        } else {
            this.weight = 0;
            this.radius = 0;
        }
    }

    public void action() {
        if (state == 0) {
            // 死亡判断
            life--;
            reSetBall((int) (MAP_WIDTH * Math.random()),
                    (int) (MAP_HEIGHT * Math.random()), getColorRandom());
        }
        if ((int) radius < (int) Math.sqrt(weight)) {
            // 阻尼增重
            radius += (Math.sqrt(weight) - radius) / ACTION_DAMPING;
        }
        if ((int) radius > (int) Math.sqrt(weight)) {
            // 阻尼减重
            radius -= (radius - Math.sqrt(weight)) / ACTION_DAMPING;
        }
        weight -= (int) radius / 100 * 5;
        // 损耗减重

        if (radius > 400) {
            // 角色球尺寸限制，重置尺寸
            weight = BALL_WEIGHT_DEFAULT;
        }
    }

    void moveRandom() {
        // action();
        if (Clock.isTimeOver(timeRandomActionBegin,
                timeRandomActionRang)) {
            timeRandomActionBegin = Clock.getClock();
            timeRandomActionRang = (int) (Math.random() * 12000);
                directionTarget = (Math.random() * Math.PI * 2) - Math.PI;
            moveSpeedRandom = (float) Math.random();
        } else {
            direction += Math.abs((directionTarget - direction)) < Math.PI ? (((directionTarget - direction) / ACTION_DAMPING))
                    : ((directionTarget - direction) > 0 ? -(Math
                    .abs((directionTarget - direction - 2 * Math.PI)) / ACTION_DAMPING)
                    : +(Math.abs((directionTarget - direction + 2 * Math.PI)) / ACTION_DAMPING));
            direction += (direction >= Math.PI) ? (-2 * Math.PI)
                    : ((direction <= -Math.PI) ? (+2 * Math.PI) : 0);
            targetX += moveSpeed * Math.cos(directionTarget)
                    * (30 / radius * 1 + 0.6) * moveSpeedRandom;
            targetY += moveSpeed * Math.sin(directionTarget)
                    * (30 / radius * 1 + 0.6) * moveSpeedRandom;
            if (targetX < 0) {
                // 边界判断
                targetX = 0;
                // myBall.targetX = 0;
                // Rocker.basePosition.x = Rocker.rockerPosition.x;

            }
            if (targetX > MAP_WIDTH) {
                // 边界判断
                targetX = MAP_WIDTH;
                // myBall.targetX = MAP_WIDTH;
                // Rocker.basePosition.x = Rocker.rockerPosition.x;
            }
            if (targetY < 0) {
                // 边界判断
                targetY = 0;
                // myBall.targetY = 0;
                // Rocker.basePosition.y = Rocker.rockerPosition.y;
            }
            if (targetY > MAP_HEIGHT) {
                // 边界判断
                targetY = MAP_HEIGHT;
                // // myBall.targetY = MAP_HEIGHT;
                // Rocker.basePosition.y = Rocker.rockerPosition.y;
            }
            positionX += (targetX - positionX) / ACTION_DAMPING;
            positionY += (targetY - positionY) / ACTION_DAMPING;
        }
    }

    public void move(float rocker) {
        if (directionTarget != 404) {
            direction += Math.abs((directionTarget - direction)) < Math.PI ? (((directionTarget - direction) / ACTION_DAMPING))
                    : ((directionTarget - direction) > 0 ? -(Math
                    .abs((directionTarget - direction - 2 * Math.PI)) / ACTION_DAMPING)
                    : +(Math.abs((directionTarget - direction + 2 * Math.PI)) / ACTION_DAMPING));
            direction += (direction >= Math.PI) ? (-2 * Math.PI)
                    : ((direction <= -Math.PI) ? (+2 * Math.PI) : 0);
            targetX += moveSpeed * Math.cos(directionTarget)
                    * (30 / radius * 1 + 0.6) * rocker;
            targetY += moveSpeed * Math.sin(directionTarget)
                    * (30 / radius * 1 + 0.6) * rocker;
            inscribedSquareLen_1_2 = radius * SQRT1_2;
            if (targetX < 0 + inscribedSquareLen_1_2) {
                // 边界判断
//                    targetX = 0;
                directionTarget = getRadian(Rocker.rockerPosition.x,
                        Rocker.rockerPosition.x, Rocker.rockerPosition.y,
                        Rocker.basePosition.y);
                Rocker.basePosition.x = Rocker.rockerPosition.x;
                // myBall.targetX = 0;
                // Rocker.basePosition.x = Rocker.rockerPosition.x;

            }
            if (targetX > MAP_WIDTH - inscribedSquareLen_1_2) {
                // 边界判断
//                    targetX = MAP_WIDTH;
                directionTarget = getRadian(Rocker.rockerPosition.x,
                        Rocker.rockerPosition.x, Rocker.rockerPosition.y,
                        Rocker.basePosition.y);
                Rocker.basePosition.x = Rocker.rockerPosition.x;
                // myBall.targetX = MAP_WIDTH;
                // Rocker.basePosition.x = Rocker.rockerPosition.x;
            }
            if (targetY < 0 + inscribedSquareLen_1_2) {
                // 边界判断
//                    targetY = 0;
                directionTarget = getRadian(Rocker.rockerPosition.x,
                        Rocker.basePosition.x, Rocker.rockerPosition.y,
                        Rocker.rockerPosition.y);
                Rocker.basePosition.y = Rocker.rockerPosition.y;
                // myBall.targetY = 0;
                // Rocker.basePosition.y = Rocker.rockerPosition.y;
            }
            if (targetY > MAP_HEIGHT - inscribedSquareLen_1_2) {
                // 边界判断
//                    targetY = MAP_HEIGHT;
                directionTarget = getRadian(Rocker.rockerPosition.x,
                        Rocker.basePosition.x, Rocker.rockerPosition.y,
                        Rocker.rockerPosition.y);
                Rocker.basePosition.y = Rocker.rockerPosition.y;
                // // myBall.targetY = MAP_HEIGHT;
                // Rocker.basePosition.y = Rocker.rockerPosition.y;
            }

            if (targetX < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetX = inscribedSquareLen_1_2;
                directionTarget = directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2;

            }
            if (targetX > MAP_WIDTH - inscribedSquareLen_1_2) {
                // 边界判断
                targetX = MAP_WIDTH - inscribedSquareLen_1_2;
                directionTarget = directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2;
            }
            if (targetY < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetY = inscribedSquareLen_1_2;
                directionTarget = (directionTarget > (-Math.PI / 2) && directionTarget < Math.PI / 2) ? 0 : Math.PI;
            }
            if (targetY > MAP_HEIGHT - inscribedSquareLen_1_2) {
                // 边界判断
                targetY = MAP_HEIGHT - inscribedSquareLen_1_2;
                directionTarget = directionTarget > Math.PI / 2 ? Math.PI : 0;
            }
            positionX += (targetX - positionX) / ACTION_DAMPING;
            positionY += (targetY - positionY) / ACTION_DAMPING;
        }

    }

    float getRadian(float x1, float x2, float y1, float y2) {
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
}
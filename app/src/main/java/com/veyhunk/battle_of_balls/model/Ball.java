package com.veyhunk.battle_of_balls.model;

import android.graphics.PointF;

import com.veyhunk.battle_of_balls.db.GameParams;
import com.veyhunk.battle_of_balls.utils.GameMath;

import static com.veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_AVATAR_DISTANCE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_WEIGHT_DEFAULT;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_WEIGHT_MAX;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.MAX_ACCELERATED_SPEED;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.AVATAR;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.BATTLE;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.DANGER;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.EMPTY;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.SAFE;
import static com.veyhunk.battle_of_balls.constants.Constants.SQRT1_2;
import static com.veyhunk.battle_of_balls.utils.Clock.getClock;
import static com.veyhunk.battle_of_balls.utils.Clock.isTimeOver;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Ball
 */
public class Ball {
    // public
    // public int ID;
    public String name;
    public boolean state;
    public int colorDraw;
    public float radius;
    public float weight;
    public PointF position;
    public float direction = 0;
    public float directionTarget = 0;
    // private
    private BallTeam team;
    private int timeRandomActionBegin;
    private int timeRandomActionRang;
    private float moveSpeed = GameParams.ballMoveSpeed / 4;
    public PointF positionTarget;
    private PointF positionAvatar;
    // private PointF positionTarget;
    private float acceleratedSpeed = 0;
    private Message message;
    private boolean isAvatar = false;
    private boolean isEdgeCollision = false;

    /**
     * Initial new ball (born)
     *
     * @param team       team
     * @param nameString nameString
     */
    Ball(BallTeam team, String nameString) {
        this.team = team;
        this.name = nameString;
        this.colorDraw = team.teamColor;
        this.state = BALL_STATE_ALIVE;
        this.position = GameMath.getPointRandom();
        this.positionTarget = position;
        this.weight = BALL_WEIGHT_DEFAULT;
        this.radius = (int) sqrt(weight);
        this.message = new Message();
        this.timeRandomActionBegin = getClock() + 500;
    }


    /**
     * Initial new ball
     *
     * @param position basePosition
     * @param weight   weight
     */
    void resetBall(PointF position, float weight) {
        this.state = BALL_STATE_ALIVE;// 复活
        this.position = position;
        this.positionTarget = position;
        this.weight = weight;
        this.radius = (int) sqrt(weight);
        this.message = new Message();
    }


//    /**
//     * Initial new ball by avatar
//     *
//     * @param directionAvatar PointF
//     */
//    void resetBallForAvatar(PointF position, float weight, float directionAvatar) {
//        this.state = BALL_STATE_ALIVE;// 复活
//        this.position = GameMath.getTarget(position, directionAvatar, radius * 1.5F);
//        this.weight = weight;
//        this.radius = (int) sqrt(weight);
////        avatar
//        isAvatar = true;
//        this.positionAvatar = GameMath.getTarget(position, directionAvatar, radius * 2 + BALL_AVATAR_DISTANCE);
//        this.positionTarget = this.positionAvatar;
//    }

    /**
     * basic action
     */
    public void action() {
//        message.editMessage(EMPTY, position);
        grow();
        thinking();
        if (isAvatar) moveForAvatar();
        else move();
    }

    /**
     * basic action : die
     */
    public float die(Ball ball) {
        state = BALL_STATE_DEAD;
        return weight;
    }

    /**
     * basic action : eat
     */
    public boolean eat(Ball ball) {
        if (team.equals(ball.getTeam())) {
            // 是否同一个队伍
            if (message.type == BATTLE) return false;
        }
        weight += ball.die(this);
        return true;
    }

    /**
     * feeling environment
     *
     * @param enemy ball
     */
    public void feeling(Ball enemy, boolean isBigger) {
        if (team.equals(enemy.getTeam())) {
            if (enemy.message.type == BATTLE) {
                message.editMessage(AVATAR, enemy.position);
            } else return;
        }
        if (isBigger) {
            //send message
            if (enemy.radius * 2 < radius) {
                //avatar
                message.editMessage(AVATAR, enemy.position);
            } else {
                //battle
                message.editMessage(BATTLE, enemy.position);
            }
        } else {
            if (enemy.radius / 2 > radius) {
                //danger
                message.editMessage(DANGER, enemy.position);
            } else {
                //battle
                message.editMessage(SAFE, enemy.position);
            }
        }
        team.sendMessage(message);
    }

    private void thinking() {

//        if (!message.isCompleted()) {
//            message.work();
//            return;
//        }
        if (isTimeOver(timeRandomActionBegin,
                timeRandomActionRang)) {
            if (message.type == DANGER) {
                setVector(GameMath.getRadian(message.position, position), MAX_ACCELERATED_SPEED);
                avatar(GameMath.getRadian(message.position, position));
            } else if (message.type == BATTLE) {
                setVector(GameMath.getRadian(position, message.position), GameMath.getAcceleratedSpeed());
            } else if (message.type == SAFE) {
                setVector(GameMath.getRadian(message.position, position), MAX_ACCELERATED_SPEED);
            } else if (message.type == AVATAR) {
                setVector(GameMath.getRadian(position, message.position), GameMath.getAcceleratedSpeed());
                avatar(GameMath.getRadian(position, message.position));
            } else {
                if (team.readMessage().type == DANGER) {
                    setVector(GameMath.getRadian(team.readMessage().position, position), MAX_ACCELERATED_SPEED);
                } else if (team.readMessage().type == BATTLE) {
                    setVector(GameMath.getRadian(position, team.readMessage().position), GameMath.getAcceleratedSpeed());
                } else {
                    setVector(GameMath.getRadian(position, GameMath.getPointRandom()), GameMath.getAcceleratedSpeed());
                    if(Math.random()>.8){
                        avatar(directionTarget);
                    }
                }
            }
            timeRandomActionBegin = getClock();
            timeRandomActionRang = (int) (Math.random() * 1000);
        } else {
            if (directionTarget == 404 || acceleratedSpeed < .05 || isEdgeCollision) {
                setVector(GameMath.getRadian(position,GameMath.getPointRandom()), MAX_ACCELERATED_SPEED);
                message.editMessage(EMPTY, GameMath.getPointRandom());
                timeRandomActionBegin = getClock();
                timeRandomActionRang = (int) (Math.random() * 1000);
            }
        }
    }

    public void setVector(float direction, float acceleratedSpeed) {
//
        this.directionTarget = direction;
        this.acceleratedSpeed = acceleratedSpeed;
    }


    public void grow() {
        if ((int) radius < (int) sqrt(weight)) {
            // 阻尼增重
            radius += (sqrt(weight) - radius) / ACTION_DAMPING;
        }
        if ((int) radius > (int) sqrt(weight)) {
            // 阻尼减重
            radius -= (radius - sqrt(weight)) / ACTION_DAMPING;
        }
        // 损耗减重
        weight -= (int) radius / 100 * 5;

        if (weight > BALL_WEIGHT_MAX) {
            // 角色球尺寸限制，重置尺寸
            weight = BALL_WEIGHT_DEFAULT;
        }
    }

    public void move() {
        if (directionTarget != 404) {
            direction += Math.abs((directionTarget - direction)) < PI ? (((directionTarget - direction) / (ACTION_DAMPING / 2))) : ((directionTarget - direction) > 0 ? -(Math.abs((directionTarget - direction - 2 * PI)) / (ACTION_DAMPING / 2)) : +(Math.abs((directionTarget - direction + 2 * PI)) / (ACTION_DAMPING / 2)));
            direction += (direction >= PI) ? (-2 * PI) : ((direction <= -PI) ? (+2 * PI) : 0);
            if (acceleratedSpeed != 0) {
                positionTarget.x += moveSpeed * cos(directionTarget) * (10 / radius + 0.2) * acceleratedSpeed;
                positionTarget.y += moveSpeed * sin(directionTarget) * (10 / radius + 0.2) * acceleratedSpeed;
            }
            isEdgeCollision = false;
            float inscribedSquareLen_1_2 = (float) (radius * SQRT1_2);
            float inscribedSquareLen = inscribedSquareLen_1_2;
            if (position.x < 0 + inscribedSquareLen) {
                // 边界判断 left   < -PI/2  > PI/2
//                 directionTarget);
                if (positionTarget.x < inscribedSquareLen_1_2) {
                    positionTarget.x = (int) inscribedSquareLen_1_2;
                    acceleratedSpeed *= Math.abs(sin(directionTarget));
                    if (directionTarget > PI / 2 || directionTarget < -PI / 2) {
                        directionTarget = (float) (directionTarget > 0 ? PI / 2 : -PI / 2);
                    }
                    isEdgeCollision = true;
                }
//                 directionTarget+"  sin:"+sin(directionTarget));
            }
            if (position.x > MAP_WIDTH - inscribedSquareLen) {
                // 边界判断 right   > -PI/2  < PI/2
                if (positionTarget.x > (MAP_WIDTH - inscribedSquareLen_1_2)) {
                    positionTarget.x = (int) (MAP_WIDTH - inscribedSquareLen_1_2);
                    acceleratedSpeed *= Math.abs(sin(directionTarget));
                    if (directionTarget < PI / 2 && directionTarget > -PI / 2) {
                        directionTarget = (float) (directionTarget > 0 ? PI / 2 : -PI / 2);
                    }
                    isEdgeCollision = true;
                }
            }
            if (position.y < 0 + inscribedSquareLen) {
                // 边界判断 top     -PI < < 0
                if (positionTarget.y < inscribedSquareLen_1_2) {
                    positionTarget.y = (int) inscribedSquareLen_1_2;
                    acceleratedSpeed *= Math.abs(cos(directionTarget));
                    if (directionTarget < 0) {
                        directionTarget = directionTarget < -PI / 2 ? (float) PI : 0;
                    }
                    isEdgeCollision = true;
                }
            }
            if (position.y > MAP_HEIGHT - inscribedSquareLen) {
                // 边界判断 bottom  0> >PI
                if (positionTarget.y > (MAP_HEIGHT - inscribedSquareLen_1_2)) {
                    positionTarget.y = (int) (MAP_HEIGHT - inscribedSquareLen_1_2);
                    acceleratedSpeed *= Math.abs(cos(directionTarget));
                    if (directionTarget > 0) {
                        directionTarget = directionTarget > PI / 2 ? (float) PI : 0;
                    }
                    isEdgeCollision = true;
                }
            }
            position.x += (positionTarget.x - position.x) / ACTION_DAMPING;
            position.y += (positionTarget.y - position.y) / ACTION_DAMPING;
            if (acceleratedSpeed == 0 && positionTarget.equals(position)) directionTarget = 404;
        } else {
//            System.out.println(team.teamName+":"+name+"404");
        }

    }

    /**
     * moveForAvatar
     */
    private void moveForAvatar() {
        if (positionAvatar == null) return;
        if (Math.abs(positionAvatar.x - position.x) < 0.1 && Math.abs(positionAvatar.y - position.y) < 0.1) {
            isAvatar = false;
        }

        float inscribedSquareLen_1_2 = (float) (radius * SQRT1_2);
        float inscribedSquareLen = inscribedSquareLen_1_2;

        if (position.x < 0 + inscribedSquareLen) {
            // 边界判断 left   < -PI/2  > PI/2
//                 directionTarget);
            if (positionAvatar.x < inscribedSquareLen_1_2) {
                positionAvatar.x = (int) inscribedSquareLen_1_2;
                acceleratedSpeed *= Math.abs(sin(directionTarget));
                if (directionTarget > PI / 2 || directionTarget < -PI / 2) {
                    directionTarget = (float) (directionTarget > 0 ? PI / 2 : -PI / 2);
                }
            }
//                 directionTarget+"  sin:"+sin(directionTarget));
        }
        if (position.x > MAP_WIDTH - inscribedSquareLen) {
            // 边界判断 right   > -PI/2  < PI/2
            if (positionAvatar.x > (MAP_WIDTH - inscribedSquareLen_1_2)) {
                positionAvatar.x = (int) (MAP_WIDTH - inscribedSquareLen_1_2);
                acceleratedSpeed *= Math.abs(sin(directionTarget));
                if (directionTarget < PI / 2 && directionTarget > -PI / 2) {
                    directionTarget = (float) (directionTarget > 0 ? PI / 2 : -PI / 2);
                }
            }
        }
        if (position.y < 0 + inscribedSquareLen) {
            // 边界判断 top     -PI < < 0
            if (positionAvatar.y < inscribedSquareLen_1_2) {
                positionAvatar.y = (int) inscribedSquareLen_1_2;
                acceleratedSpeed *= Math.abs(cos(directionTarget));
                if (directionTarget < 0) {
                    directionTarget = directionTarget < -PI / 2 ? (float) PI : 0;
                }
            }
        }
        if (position.y > MAP_HEIGHT - inscribedSquareLen) {
            // 边界判断 bottom  0> >PI
            if (positionAvatar.y > (MAP_HEIGHT - inscribedSquareLen_1_2)) {
                positionAvatar.y = (int) (MAP_HEIGHT - inscribedSquareLen_1_2);
                acceleratedSpeed *= Math.abs(cos(directionTarget));
                if (directionTarget > 0) {
                    directionTarget = directionTarget > PI / 2 ? (float) PI : 0;
                }
            }
        }
        position.x += (positionAvatar.x - position.x) / ACTION_DAMPING;
        position.y += (positionAvatar.y - position.y) / ACTION_DAMPING;
        if (acceleratedSpeed == 0) directionTarget = 404;
    }

    /**
     * 分裂自身
     *
     * @param direction 传入一个目标，作为分裂出新球的方向
     */
    public void avatar(float direction) {
        message.editMessage(AVATAR, position);
        if (weight < BALL_WEIGHT_DEFAULT / 2) return;
        Ball newBall = team.initMember();
        if (newBall != null) {
            weight /= 2;
            newBall.resetBall(position, weight);
            // avatar
            isAvatar = true;
            radius = (int) sqrt(weight);
            position = GameMath.getTarget(position, direction, radius);
            positionAvatar = GameMath.getTarget(position, direction, radius * 2 + BALL_AVATAR_DISTANCE);
            positionTarget = positionAvatar;
        }
    }

    public void battle() {
        //battle
        message.editMessage(BATTLE, position);
        team.sendMessage(message);
    }

    public void danger() {
        //danger
        message.editMessage(DANGER, position);
        team.sendMessage(message);
    }

    public BallTeam getTeam() {
        return team;
    }

}

package com.veyhunk.battle_of_balls.model;

import android.graphics.PointF;

import com.veyhunk.battle_of_balls.db.GameParams;
import com.veyhunk.battle_of_balls.utils.GameMath;

import static com.veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_AVATAR_DISTANCE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_WEIGHT_MAX;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.MAX_ACCELERATED_SPEED;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.AVATAR;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.BATTLE;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.DANGER;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.EMPTY;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.ESCAPE;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.SAFE;
import static com.veyhunk.battle_of_balls.constants.Constants.SQRT1_2;
import static com.veyhunk.battle_of_balls.db.GameParams.BALL_WEIGHT_DEFAULT;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Ball
 */
public class Ball {
    // public
    public int id;
    public String name;
    public boolean state;
    public int colorDraw;
    public float radius;
    public float weight;
    public PointF position;
    public float direction = 0;
    public float directionTarget = 0;
    public PointF positionTarget;
    // private
    private BallTeam team;
    //    private int timeRandomActionBegin;
//    private int timeRandomActionRang;
    private float moveSpeed = GameParams.BALL_MOVE_SPEED;
    private PointF positionAvatar;
    // private PointF positionTarget;
    private float acceleratedSpeed = 0;
    private Message message;
    private Message msgRead;
    private boolean isAvatar = false;
    private boolean isEdgeCollision = false;

    /**
     * Initial new ball (born)
     *
     * @param team       team
     * @param nameString nameString
     */
    Ball(BallTeam team, String nameString, int id) {
        this.id = id;
        this.team = team;
        this.name = nameString;
        this.colorDraw = team.teamColor;
        this.state = BALL_STATE_ALIVE;
        this.position = GameMath.getPointRandom();
        this.positionTarget = new PointF(position.x, position.y);
        this.weight = BALL_WEIGHT_DEFAULT;
        this.radius = (int) sqrt(weight);
        this.message = new Message(this);
//        this.timeRandomActionBegin = getClock() + 500;
    }


    /**
     * Initial new ball
     *
     * @param position basePosition
     * @param weight   weight
     */
    void resetBall(PointF position, float weight) {
        this.isAvatar = false;
        this.state = BALL_STATE_ALIVE;// 复活
        this.position = position;
        this.positionTarget = position;
        this.weight = weight;
        this.radius = (int) sqrt(weight);
        this.message = new Message(this);
    }

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
        if (!team.equals(ball.getTeam())) {
            message.editMessage(DANGER, ball.position, ball);
            team.sendMessage(message);
        }
        return weight;
    }

    /**
     * basic action : eat
     */
    public boolean eat(Ball ball) {
        if (team.equals(ball.getTeam())) {
            // 是否同一个队伍
            if (message.type == SAFE || message.type == EMPTY) return false;
        }
        weight += ball.die(this);
        return true;
    }

    /**
     * feeling environment
     *
     * @param ballObject ball
     */
    public void feeling(Ball ballObject, boolean isBigger) {
        if (team.equals(ballObject.getTeam())) {
            if (ballObject.message.type == BATTLE) {
                setState(AVATAR, ballObject);
            } else return;
        }
        if (isBigger) {
            //send message
            if (ballObject.weight * 2 < weight) {
                setState(AVATAR, ballObject);
            } else {
                setState(BATTLE, ballObject);
            }
        } else {
            if (ballObject.weight / 2 > weight) {
                setState(ESCAPE, ballObject);
            } else {
                setState(DANGER, ballObject);
            }
        }
    }

    private void thinking() {
        msgRead = team.readMessage(this);

        if (message.isCompleted()) {
            setState(SAFE, this);
        }
//        isTimeOver(timeRandomActionBegin,timeRandomActionRang)
        if (true) {
            if (message.type == ESCAPE) {
                setVector(GameMath.getRadian(message.position, position), MAX_ACCELERATED_SPEED);
                avatar(GameMath.getRadian(message.position, position));
            } else if (message.type == DANGER) {
                setVector(GameMath.getRadian(message.position, position), MAX_ACCELERATED_SPEED);
            } else if (message.type == BATTLE) {
                setVector(GameMath.getRadian(position, message.position), GameMath.getAcceleratedSpeed());
            } else if (message.type == AVATAR) {
                setVector(GameMath.getRadian(position, message.position), GameMath.getAcceleratedSpeed());
                avatar(GameMath.getRadian(position, message.position));
            } else if (Math.random() > .7 && msgRead != null) {
                if (msgRead.type == DANGER) {
                    setVector(GameMath.getRadian(msgRead.position, position), MAX_ACCELERATED_SPEED);
                } else if (msgRead.type == BATTLE) {
                    setVector(GameMath.getRadian(position, msgRead.position), MAX_ACCELERATED_SPEED);
                }
            } else {
                if (Math.random() > .99) {
                    avatar(directionTarget);
                }

                if ((directionTarget == 404 || acceleratedSpeed < .0005)) {
                    setVector(GameMath.getRadian(position, GameMath.getPointRandom()), MAX_ACCELERATED_SPEED);
//            timeRandomActionBegin = getClock();
//            timeRandomActionRang = (int) (Math.random() * 1000);
                }
//              setVector(GameMath.getRadian(position, GameMath.getPointRandom()), MAX_ACCELERATED_SPEED);
            }
//            timeRandomActionBegin = getClock();
//            timeRandomActionRang = (int) (Math.random() * 1000);
        }
        if ((directionTarget == 404 || acceleratedSpeed < .0005) && isEdgeCollision && Math.random() > .7) {
            setVector(GameMath.getRadian(position, GameMath.getPointRandom()), MAX_ACCELERATED_SPEED);
            setState(EMPTY, this);
//            timeRandomActionBegin = getClock();
//            timeRandomActionRang = (int) (Math.random() * 1000);
        }
    }

    public void setVector(float direction, float acceleratedSpeed) {
//
        this.directionTarget = direction;
        this.acceleratedSpeed = acceleratedSpeed;
//        this.acceleratedSpeed = MAX_ACCELERATED_SPEED;
    }


    void grow() {
        if ((int) radius < (int) sqrt(weight)) {
            // 阻尼增重
            radius += (sqrt(weight) - radius) / ACTION_DAMPING;
        }
        if ((int) radius > (int) sqrt(weight)) {
            // 阻尼减重
            radius -= (radius - sqrt(weight)) / ACTION_DAMPING;
        }
        // 损耗减重
        if (weight > BALL_WEIGHT_DEFAULT) {
            weight -= (int) radius / 2;
        }
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
                positionTarget.x += cos(directionTarget) * moveSpeed * acceleratedSpeed / radius * 50;
                positionTarget.y += sin(directionTarget) * moveSpeed * acceleratedSpeed / radius * 50;
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
            if (acceleratedSpeed < .005 && positionTarget.equals(position)) directionTarget = 404;
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


    public void setState(short msgType, Ball ballObject) {
        if (ballObject != null && message.ballObject != null) {
            if (ballObject.id == message.ballObject.id && msgType == message.type) return;
        }
        switch (msgType) {
            case DANGER:
                //setDanger
                message.editMessage(DANGER, ballObject.position, ballObject);
                team.sendMessage(message);
                break;
            case ESCAPE:
                //setDanger
                message.editMessage(DANGER, ballObject.position, ballObject);
                team.sendMessage(message);
                if (Math.random() > .8) {
                    message.editMessage(ESCAPE, ballObject.position, ballObject);
                }
                break;
            case AVATAR:
                //setDanger
                if (message.type != DANGER) {
                    message.editMessage(AVATAR, ballObject.position, ballObject);
                    break;
                }
            case BATTLE:
                //setBattle
                if (message.type != DANGER) {
                    message.editMessage(BATTLE, ballObject.position, ballObject);
                    team.sendMessage(message);
                }
                break;
            case SAFE:
                //setDanger
                if (message.type != DANGER) {
                    message.editMessage(SAFE, position, ballObject);
                }
                break;
            default:
                //setEmpty
                if (message.type != DANGER) {
                    message.editMessage(EMPTY, position, ballObject);
                }
        }

    }

    public BallTeam getTeam() {
        return team;
    }

}

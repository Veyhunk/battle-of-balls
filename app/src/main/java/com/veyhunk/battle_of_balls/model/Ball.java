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
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.SAFE;
import static com.veyhunk.battle_of_balls.constants.Constants.SQRT1_2;
import static com.veyhunk.battle_of_balls.utils.Clock.getClock;
import static com.veyhunk.battle_of_balls.utils.Clock.isTimeOver;
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
    private PointF positionTarget;
    private PointF positionAvatar;
    //    private PointF positionTarget;
    private float acceleratedSpeed = 0;
    private Message message;
    private boolean isAvatar = false;

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
//                        directionTarget = (float) ((Math.random() * Math.PI * 2) - Math.PI);
//                        acceleratedSpeed = (float) Math.random();
//        }
                }
            }
        }
        timeRandomActionBegin = getClock();
        timeRandomActionRang = (int) (Math.random() * 1000);
    }

    public void setVector(float direction, float acceleratedSpeed) {
//        System.out.println(name+":"+acceleratedSpeed+"-"+direction);
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
            direction += Math.abs((directionTarget - direction)) < Math.PI ? (((directionTarget - direction) / (ACTION_DAMPING / 2)))
                    : ((directionTarget - direction) > 0 ? -(Math
                    .abs((directionTarget - direction - 2 * Math.PI)) / (ACTION_DAMPING / 2))
                    : +(Math.abs((directionTarget - direction + 2 * Math.PI)) / (ACTION_DAMPING / 2)));
            direction += (direction >= Math.PI) ? (-2 * Math.PI)
                    : ((direction <= -Math.PI) ? (+2 * Math.PI) : 0);
            positionTarget.x += moveSpeed * Math.cos(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;
            positionTarget.y += moveSpeed * Math.sin(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;


            float inscribedSquareLen_1_2 = (float) (radius * SQRT1_2);
            if (position.x < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                positionTarget.x = (int) inscribedSquareLen_1_2;
                acceleratedSpeed *= (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2) / directionTarget;
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);

            }
            if (position.x > MAP_WIDTH - inscribedSquareLen_1_2) {
                // 边界判断
                positionTarget.x = (int) (MAP_WIDTH - inscribedSquareLen_1_2);
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);
            }
            if (position.y < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                positionTarget.y = (int) inscribedSquareLen_1_2;
                directionTarget = (directionTarget > (-Math.PI / 2) && directionTarget < Math.PI / 2) ? 0 : (float) Math.PI;
            }
            if (position.y > MAP_HEIGHT - inscribedSquareLen_1_2) {
                // 边界判断
                positionTarget.y = (int) (MAP_HEIGHT - inscribedSquareLen_1_2);
                directionTarget = directionTarget > Math.PI / 2 ? (float) Math.PI : 0;
            }
            position.x += (positionTarget.x - position.x) / ACTION_DAMPING;
            position.y += (positionTarget.y - position.y) / ACTION_DAMPING;
        } else {

            setVector(GameMath.getRadian(position, GameMath.getPointRandom()), GameMath.getAcceleratedSpeed());

//            System.out.println(name+"2:"+acceleratedSpeed+"-"+direction);
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
        position.x += (positionAvatar.x - position.x) / ACTION_DAMPING;
        position.y += (positionAvatar.y - position.y) / ACTION_DAMPING;
    }

    /**
     * 分裂自身
     *
     * @param direction 传入一个目标，作为分裂出新球的方向
     */
    public void avatar(float direction) {
        System.out.println("avatar22222222");
        message.editMessage(AVATAR, position);
        if (weight < BALL_WEIGHT_DEFAULT / 2) return;
        System.out.println("avatar222222221111111111");
        Ball newBall = team.initMember();
        if (newBall != null) {
            weight /= 2;
            newBall.resetBall(position, weight);
//        avatar
            isAvatar = true;
            radius = (int) sqrt(weight);
            position = GameMath.getTarget(position, direction, radius);
            positionAvatar = GameMath.getTarget(position, direction, radius * 2 + BALL_AVATAR_DISTANCE);
            positionTarget = positionAvatar;
//            team.addMember(newBall);
            System.out.println("avatar3333333");
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

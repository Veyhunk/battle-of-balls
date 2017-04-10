package com.veyhunk.battle_of_balls.model;

import android.graphics.Point;

import com.veyhunk.battle_of_balls.db.GameParams;
import com.veyhunk.battle_of_balls.utils.MathUtils;

import static com.veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_DEFAULT_WEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static com.veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static com.veyhunk.battle_of_balls.constants.Constants.MAX_ACCELERATED_SPEED;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.AVATAR;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.BATTLE;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.DANGER;
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
    public Point position;
    public float direction = 0;
    public float directionTarget = 0;
    // private
    private BallTeam team;
    private int timeRandomActionBegin;
    private int timeRandomActionRang;
    private float moveSpeed = GameParams.ballMoveSpeed / 4;
    private Point targetPosition;
    private float acceleratedSpeed = 0;
    private Message message;

    /**
     * Initial new ball (born)
     *
     * @param team       team
     * @param nameString nameString
     */
    public Ball(BallTeam team, String nameString) {
        this.team = team;
        this.name = nameString;
        this.colorDraw = team.teamColor;
        this.state = BALL_STATE_ALIVE;
        this.position = MathUtils.getPointRandom();
        this.targetPosition = position;
        this.weight = BALL_DEFAULT_WEIGHT;
        this.radius = (int) sqrt(weight);
        this.message = new Message();
        this.timeRandomActionBegin = getClock() + 500;
    }


    /**
     * Initial new ball by avatar
     *
     * @param position basePosition
     * @param weight   weight
     */
    void resetBall(Point position, float weight) {
        this.state = BALL_STATE_ALIVE;// 复活
        this.position = position;
        this.targetPosition = position;
        this.weight = weight;
        this.radius = (int) sqrt(weight);
    }

    /**
     * basic action
     */
    public void action() {
        grow();
        thinking();

        move();
    }

    /**
     * basic action : die
     */
    public float die() {
        state = BALL_STATE_DEAD;
        return weight;
    }

    /**
     * basic action : eat
     */
    public boolean eat(Ball ball) {
        if (team.equals(ball.getTeam())) {
            // 是否同一个队伍
            if (message.type != BATTLE) return false;
        }
        weight += ball.die();
        return true;
    }

    /**
     * feeling environment
     *
     * @param enemy ball
     */
    public void feeling(Ball enemy, boolean isBigger) {
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
                message.editMessage(BATTLE, enemy.position);
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
                setVector(MathUtils.getRadian(message.position, position), MAX_ACCELERATED_SPEED);
            } else if (message.type == BATTLE) {
                setVector(MathUtils.getRadian(position, message.position), MathUtils.getAcceleratedSpeed());
            } else {
                if (team.readMessage().type == DANGER) {
                    setVector(MathUtils.getRadian(team.readMessage().position, position), MAX_ACCELERATED_SPEED);
                } else if (team.readMessage().type == BATTLE) {
                    setVector(MathUtils.getRadian(position, team.readMessage().position), MathUtils.getAcceleratedSpeed());
                } else {
                    if (message.type == AVATAR) {
                        avatar(direction);
                        setVector(MathUtils.getRadian(position, message.position), MathUtils.getAcceleratedSpeed());
                    } else {
                        setVector(MathUtils.getRadian(position, MathUtils.getPointRandom()), MathUtils.getAcceleratedSpeed());

//                        directionTarget = (float) ((Math.random() * Math.PI * 2) - Math.PI);
//                        acceleratedSpeed = (float) Math.random();
//        }
                    }
                }
            }
        }
        timeRandomActionBegin = getClock();
        timeRandomActionRang = (int) (Math.random() * 1000);
    }

    public void setVector(float direction, float acceleratedSpeed) {
        System.out.println(name+":"+acceleratedSpeed+"-"+direction);
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

        if (radius > 400) {
            // 角色球尺寸限制，重置尺寸
            weight = BALL_DEFAULT_WEIGHT;
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
            targetPosition.x += moveSpeed * Math.cos(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;
            targetPosition.y += moveSpeed * Math.sin(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;


            float inscribedSquareLen_1_2 = (float) (radius * SQRT1_2);
            if (position.x < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.x = (int) inscribedSquareLen_1_2;
                acceleratedSpeed *= (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2) / directionTarget;
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);

            }
            if (position.x > MAP_WIDTH - inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.x = (int) (MAP_WIDTH - inscribedSquareLen_1_2);
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);
            }
            if (position.y < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.y = (int) inscribedSquareLen_1_2;
                directionTarget = (directionTarget > (-Math.PI / 2) && directionTarget < Math.PI / 2) ? 0 : (float) Math.PI;
            }
            if (position.y > MAP_HEIGHT - inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.y = (int) (MAP_HEIGHT - inscribedSquareLen_1_2);
                directionTarget = directionTarget > Math.PI / 2 ? (float) Math.PI : 0;
            }
            position.x += (targetPosition.x - position.x) / ACTION_DAMPING;
            position.y += (targetPosition.y - position.y) / ACTION_DAMPING;
        }else {

            setVector(MathUtils.getRadian(position, MathUtils.getPointRandom()), MathUtils.getAcceleratedSpeed());

            System.out.println(name+"2:"+acceleratedSpeed+"-"+direction);
        }

    }

    /**
     * moveForAvatar
     */
    private void moveForAvatar() {
        if (directionTarget != 404) {
            targetPosition.x += moveSpeed * Math.cos(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;
            targetPosition.y += moveSpeed * Math.sin(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;
            float inscribedSquareLen_1_2 = (float) (radius * SQRT1_2);
            if (position.x < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.x = (int) inscribedSquareLen_1_2;
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);

            }
            if (position.x > MAP_WIDTH - inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.x = (int) (MAP_WIDTH - inscribedSquareLen_1_2);
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);
            }
            if (position.y < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.y = (int) inscribedSquareLen_1_2;
                directionTarget = (directionTarget > (-Math.PI / 2) && directionTarget < Math.PI / 2) ? 0 : (float) Math.PI;
            }
            if (position.y > MAP_HEIGHT - inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.y = (int) (MAP_HEIGHT - inscribedSquareLen_1_2);
                directionTarget = directionTarget > Math.PI / 2 ? (float) Math.PI : 0;
            }
            position.x += (targetPosition.x - position.x) / ACTION_DAMPING;
            position.y += (targetPosition.y - position.y) / ACTION_DAMPING;
        }
    }

    /**
     * 分裂自身
     *
     * @param direction 传入一个目标，作为分裂出新球的方向
     */
    public void avatar(float direction) {
        message.editMessage(AVATAR, position);
        if (weight < BALL_DEFAULT_WEIGHT) return;
        Point target = new Point();
        target.x += moveSpeed * Math.cos(direction) * (30 / radius * 1 + 0.6) * acceleratedSpeed;
        target.y += moveSpeed * Math.sin(direction) * (30 / radius * 1 + 0.6) * acceleratedSpeed;
        Ball newBall = team.initMember();
        if (newBall != null) {
            weight /= 2;
            newBall.position = position;
            newBall.resetBall(target, weight);
            team.addMember(newBall);
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

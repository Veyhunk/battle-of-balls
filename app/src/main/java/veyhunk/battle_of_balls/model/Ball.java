package veyhunk.battle_of_balls.model;

import android.graphics.Point;

import veyhunk.battle_of_balls.utils.MathUtils;

import static java.lang.Math.sqrt;
import static veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static veyhunk.battle_of_balls.constants.Constants.BALL_DEFAULT_WEIGHT;
import static veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static veyhunk.battle_of_balls.constants.Constants.MAX_ACCELERATED_SPEED;
import static veyhunk.battle_of_balls.constants.Constants.MessageType.AVATAR;
import static veyhunk.battle_of_balls.constants.Constants.MessageType.BATTLE;
import static veyhunk.battle_of_balls.constants.Constants.MessageType.DANGED;
import static veyhunk.battle_of_balls.constants.Constants.SQRT1_2;

/**
 * Ball
 */
public class Ball {
    //    public
    public int ID;
    public String name;
    public boolean state;
    public int colorDraw;
    public float radius;
    public Point position;
    public float direction = 0;
    public float directionTarget = 0;

    //    private
    private BallTeam team;
    private int weight;
    //    private int timeRandomActionBegin;
//    private int timeRandomActionRang;
    private float moveSpeed;
    private Point targetPosition;
    private float acceleratedSpeed = 0;
    private float inscribedSquareLen_1_2 = 0;
    private Message message;

    /**
     * Initial new ball
     *
     * @param position   position
     * @param colorDraw  colorDraw
     * @param nameString nameString
     * @param team       team
     */
//    Ball(Point position, int colorDraw, String nameString, BallTeam team) {
//        this.state = BALL_STATE_ALIVE;
//        this.position = position;
//        this.targetPosition = position;
//        this.colorDraw = colorDraw;
//        this.name = nameString;
//        this.weight = BALL_DEFAULT_WEIGHT;
//        moveSpeed = GameParams.ballMoveSpeed;
//        this.team = team;
//        message = new Message();
//    }

    /**
     * Initial new ball (born)
     *
     * @param colorDraw  colorDraw
     * @param nameString nameString
     * @param team       team
     */
    Ball(int colorDraw, String nameString, BallTeam team) {
        this.state = BALL_STATE_ALIVE;
        this.position = new Point(0, 0);
        this.targetPosition = position;
        this.colorDraw = colorDraw;
        this.name = nameString;
        this.weight = BALL_DEFAULT_WEIGHT;
        this.radius = (int) sqrt(weight);
        this.team = team;
        message = new Message();
    }

    /**
     * Initial new ball by avatar
     *
     * @param position position
     * @param weight   weight
     */
    public void reSetBall(Point position, int weight) {
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
    public int die() {
        state = BALL_STATE_DEAD;
        return weight;
    }

    /**
     * basic action : eat
     */
    public void eat(Ball enemy) {
        weight = enemy.die();
    }

    /**
     * feeling environment
     *
     * @param enemy ball
     */
    public void feeling(Ball enemy) {
        if ((enemy.position.x - position.x)
                * (enemy.position.x - position.x)
                + (enemy.position.y - position.y)
                * (enemy.position.y - position.y) < (radius / 2)
                * (radius / 2)) {
            eat(enemy);
        } else {
            if (enemy.radius * 2 < radius) {
                message.editMessage(AVATAR, position);
            } else if (enemy.radius / 2 > radius) {
                message.editMessage(DANGED, enemy.position);
            } else {
//            battle
                message.editMessage(BATTLE, enemy.position);
            }
        }
        team.sendMessage(message);
    }

    private void thinking() {
        if (message.type == DANGED) {
            setTarget(MathUtils.getRadian(message.position, position), MAX_ACCELERATED_SPEED);
        } else if (message.type == BATTLE) {
            setTarget(MathUtils.getRadian(position, message.position), MathUtils.getAcceleratedSpeed());
        } else {
            if (team.readMessage().type == DANGED) {
                setTarget(MathUtils.getRadian(team.readMessage().position, position), MAX_ACCELERATED_SPEED);
            } else if (team.readMessage().type == BATTLE) {
                setTarget(MathUtils.getRadian(position, team.readMessage().position), MathUtils.getAcceleratedSpeed());
            } else {
                if (message.type == AVATAR) {
                    avatar(message.position);
                    setTarget(MathUtils.getRadian(position, message.position), MathUtils.getAcceleratedSpeed());
                } else {
                    setTarget(MathUtils.getRadian(position, message.position), MathUtils.getAcceleratedSpeed());
                }
            }
        }
    }

    private void setTarget(float direction, float acceleratedSpeed) {
        this.directionTarget = direction;
        this.acceleratedSpeed = acceleratedSpeed;
        // action();
//        if (!getClockIsInRange(timeRandomActionBegin,
//                timeRandomActionRang)) {
//            timeRandomActionBegin = getClock();
//            timeRandomActionRang = (int) (Math.random() * 12000);
//            directionTarget = (float) ((Math.random() * Math.PI * 2) - Math.PI);
//            acceleratedSpeed = (float) Math.random();
//        }
    }


    private void grow() {
        if ((int) radius < (int) sqrt(weight)) {
            // 阻尼增重
            radius += (sqrt(weight) - radius) / ACTION_DAMPING;
        }
        if ((int) radius > (int) sqrt(weight)) {
            // 阻尼减重
            radius -= (radius - sqrt(weight)) / ACTION_DAMPING;
        }
//        weight -= (int) radius / 100 * 5;
//        // 损耗减重

//        if (radius > 400) {
//            // 角色球尺寸限制，重置尺寸
//            weight = BALL_DEFAULT_WEIGHT;
//        }
    }

    private void move() {
        if (directionTarget != 404) {
            direction += Math.abs((directionTarget - direction)) < Math.PI ? (((directionTarget - direction) / ACTION_DAMPING))
                    : ((directionTarget - direction) > 0 ? -(Math
                    .abs((directionTarget - direction - 2 * Math.PI)) / ACTION_DAMPING)
                    : +(Math.abs((directionTarget - direction + 2 * Math.PI)) / ACTION_DAMPING));
            direction += (direction >= Math.PI) ? (-2 * Math.PI)
                    : ((direction <= -Math.PI) ? (+2 * Math.PI) : 0);
            targetPosition.x += moveSpeed * Math.cos(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;
            targetPosition.y += moveSpeed * Math.sin(directionTarget)
                    * (30 / radius * 1 + 0.6) * acceleratedSpeed;


            inscribedSquareLen_1_2 = (float) (radius * SQRT1_2);
            if (targetPosition.x < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.x = (int) inscribedSquareLen_1_2;
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);

            }
            if (targetPosition.x > MAP_WIDTH - inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.x = (int) (MAP_WIDTH - inscribedSquareLen_1_2);
                directionTarget = (float) (directionTarget > 0 ? Math.PI / 2 : -Math.PI / 2);
            }
            if (targetPosition.y < 0 + inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.y = (int) inscribedSquareLen_1_2;
                directionTarget = (directionTarget > (-Math.PI / 2) && directionTarget < Math.PI / 2) ? 0 : (float) Math.PI;
            }
            if (targetPosition.y > MAP_HEIGHT - inscribedSquareLen_1_2) {
                // 边界判断
                targetPosition.y = (int) (MAP_HEIGHT - inscribedSquareLen_1_2);
                directionTarget = directionTarget > Math.PI / 2 ? (float) Math.PI : 0;
            }
            position.x += (targetPosition.x - position.x) / ACTION_DAMPING;
            position.y += (targetPosition.y - position.y) / ACTION_DAMPING;
        }

    }

    private void avatar(Point target) {
//        Ball newBall = team.initMember();
//        if (newBall != null) {
//            weight = weight / 2;
//            newBall.reSetBall(target, weight);
//            team.addMember(newBall);
//        }
        // TODO: 15/March/2017  add animate
        team.addMember(target, weight);
    }

//    private void escape(Point position) {
//
//    }

}

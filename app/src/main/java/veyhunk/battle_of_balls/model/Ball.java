package veyhunk.battle_of_balls.model;

import android.graphics.Point;

import static java.lang.Math.sqrt;
import static veyhunk.battle_of_balls.constants.Constants.ACTION_DAMPING;
import static veyhunk.battle_of_balls.constants.Constants.BALL_DEFAULT_WEIGHT;
import static veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static veyhunk.battle_of_balls.constants.Constants.MAP_HEIGHT;
import static veyhunk.battle_of_balls.constants.Constants.MAP_WIDTH;
import static veyhunk.battle_of_balls.constants.Constants.SQRT1_2;
import static veyhunk.battle_of_balls.utils.Clock.getClock;
import static veyhunk.battle_of_balls.utils.Clock.getClockIsInRange;

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
    private int timeRandomActionBegin;
    private int timeRandomActionRang;
    private float moveSpeed;
    private Point targetPosition;
    private float acceleratedSpeed = 0;
    private float inscribedSquareLen_1_2 = 0;

    Ball(Point position, int colorDraw, String nameString) {
        this.state = BALL_STATE_ALIVE;
        this.position = position;
        this.targetPosition = position;
        this.colorDraw = colorDraw;
        this.name = nameString;
        this.weight = BALL_DEFAULT_WEIGHT;
        this.timeRandomActionBegin = getClock() + 500;
    }

    // positionX, positionY, colorDraw, size
    public void reSetBall(Point position, int colorDraw, int weight) {
        this.state = BALL_STATE_ALIVE;// 复活
        this.position = position;
        this.targetPosition = position;
        this.colorDraw = colorDraw;
        this.weight = weight;
        this.radius = 0;
    }

    public void action() {
        grow();
        thinking();
        move();
    }

    public void feeling(Ball ball) {
        if (ball.radius * 2 < radius) {
//            avatar();
        } else if (ball.radius / 2 > radius) {
//            escape();
        } else {
//            fiting
//            help();
        }
    }

    private void thinking() {
        setTarget();
    }

    private void setTarget() {
        // action();
        if (!getClockIsInRange(timeRandomActionBegin,
                timeRandomActionRang)) {
            timeRandomActionBegin = getClock();
            timeRandomActionRang = (int) (Math.random() * 12000);
            directionTarget = (float) ((Math.random() * Math.PI * 2) - Math.PI);
            // TODO: 12/March/2017 speed
            acceleratedSpeed = (float) Math.random() * 10;
        }
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

    private void avatar() {

    }

    public void setTeam(BallTeam team) {
        this.team = team;
    }


}

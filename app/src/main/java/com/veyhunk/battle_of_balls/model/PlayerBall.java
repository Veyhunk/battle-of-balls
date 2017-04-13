package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.sounds.GameSounds;

import static com.veyhunk.battle_of_balls.db.GameParams.ballName;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.EAT_3;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.EAT_DEFAULT;

/**
 * 定义角色球球的类，即角色球
 */
public class PlayerBall extends Ball {

    private GameSounds gameSounds;

    public PlayerBall(Ball ball,GameSounds gameSounds) {
        super(ball.getTeam(), ballName);
        this.gameSounds = gameSounds;
    }

    /**
     * 复活角色
     * @param teamMate Ball
     */
    void resetBall(Ball teamMate){
        super.resetBall(teamMate.position,teamMate.die(this));
    }

    @Override
    public void action() {
        grow();
        move();
    }

    @Override
    public float die(Ball ball) {
        gameSounds.starMusic(EAT_DEFAULT);
        if(ball.getTeam().equals(getTeam())){
            position=ball.position;
            radius=ball.radius;
            weight+=ball.die(this);
            return 0;
        }
        else return super.die(ball);
    }

    @Override
    public boolean eat(Ball enemy) {
       if (super.eat(enemy)){
            gameSounds.starMusic(EAT_3);
           return true;
        }
        return false;
    }

    @Override
    public void setVector(float direction, float acceleratedSpeed) {
        super.setVector(direction, acceleratedSpeed);
    }
}

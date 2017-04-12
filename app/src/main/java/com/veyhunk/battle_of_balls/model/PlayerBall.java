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
     * @param ballMate
     */
    public void resetBall(Ball ballMate){
        super.resetBall(ballMate.position,ballMate.die());
    }

    @Override
    public void action() {
        grow();
        move();
    }

    @Override
    public float die() {
        gameSounds.starMusic(EAT_DEFAULT);
        return super.die();
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

package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.sounds.GameSounds;

import static com.veyhunk.battle_of_balls.db.GameParams.ballName;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.EAT_3;
import static com.veyhunk.battle_of_balls.sounds.GameSounds.EAT_DEFAULT;

/**
 * 定义角色球球的类，即角色球
 */
public class MyBall extends Ball {
    GameSounds gameSounds;

    public MyBall(Ball ball) {
        super(ball.colorDraw, ballName, ball.getTeam());
    }
    public void setGameSounds(GameSounds gameSounds) {
        this.gameSounds = gameSounds;
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
    public void eat(Ball enemy) {
        gameSounds.starMusic(EAT_3);
        super.eat(enemy);
    }

    @Override
    public void setTarget(float direction, float acceleratedSpeed) {
        super.setTarget(direction, acceleratedSpeed);
    }
}

package com.veyhunk.battle_of_balls.model;

import android.graphics.PointF;

import static com.veyhunk.battle_of_balls.constants.Constants.MessageDuration;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.EMPTY;

/**
 * Created by Veyhunk on 13/March/2017.
 * Message
 */

class Message {
    short type;
    public String content;
    public PointF position;
    public Ball ballSender;
    public Ball ballObject;
    private int duration;



    /**
     * Create Message
     */
    Message(Ball ballSender) {
        this.type = EMPTY;
        this.position = new PointF(0, 0);
        this.duration = 0;
        this.ballSender = ballSender;
    }

    /**
     * Edit Message
     *
     * @param type     Message type
     * @param position basePosition
     */
    void editMessage(short type, PointF position, Ball ballObject) {
        this.type = type;
        this.position = position;
        this.duration = MessageDuration[type];
        this.ballObject = ballObject;
    }

    boolean isCompleted() {
        if (duration <= 0) this.type = EMPTY;
        return duration-- <= 0;
    }
}
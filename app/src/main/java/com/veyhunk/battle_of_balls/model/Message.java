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
    PointF position;
    private int sender;

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    private int duration;


    /**
     * Create Message
     *
     * @param type     Message type
     * @param position basePosition
     */
    public Message(short type, PointF position, int duration) {
        this.type = type;
        this.position = position;
        this.duration = duration;

    }

    /**
     * Create Message
     */
    Message() {
        this.type = EMPTY;
        this.position = new PointF(0, 0);
        this.duration = 0;
    }

    /**
     * Create Message
     *
     * @param type     Message type
     * @param position basePosition
     * @param content  content
     * @param sender   sender
     */
    public Message(short type, PointF position, String content, int sender) {
        this.type = type;
        this.content = content;
        this.position = position;
        this.sender = sender;
    }

    /**
     * Edit Message
     *
     * @param type     Message type
     * @param position basePosition
     */
    void editMessage(short type, PointF position) {
        this.type = type;
        this.position = position;
        this.duration = MessageDuration[type];
    }

    public boolean isCompleted() {
        return duration <= 0;
    }

    void work() {
        --duration;
    }
}
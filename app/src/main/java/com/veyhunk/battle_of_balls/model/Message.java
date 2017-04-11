package com.veyhunk.battle_of_balls.model;

import android.graphics.Point;

import static com.veyhunk.battle_of_balls.constants.Constants.MessageDuration;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.EMPTY;

/**
 * Created by Veyhunk on 13/March/2017.
 * Message
 */

public class Message {
    public short type;
    public String content;
    public Point position;
    public int sender;
    public int duration;


    /**
     * Create Message
     *
     * @param type     Message type
     * @param position basePosition
     */
    public Message(short type, Point position, int duration) {
        this.type = type;
        this.position = position;
        this.duration = duration;

    }

    /**
     * Create Message
     */
    public Message() {
        this.type = EMPTY;
        this.position = new Point(0, 0);
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
    public Message(short type, Point position, String content, int sender) {
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
    public void editMessage(short type, Point position) {
        this.type = type;
        this.position = position;
        this.duration = MessageDuration[type];
    }

    public boolean isCompleted() {
        return duration <= 0;
    }

    public void work() {
        --duration;
    }
}
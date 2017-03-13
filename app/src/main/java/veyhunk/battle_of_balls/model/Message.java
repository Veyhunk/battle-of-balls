package veyhunk.battle_of_balls.model;

import android.graphics.Point;

/**
 * Created by Veyhunk on 13/March/2017.
 * Message
 */

public class Message {
    public short type;
    public String content;
    public Point position;
    public int sender;

    /**
     * Creat Message
     *
     * @param type     Message type
     * @param position position
     */
    public Message(short type, Point position) {
        this.type = type;
        this.position = position;
    }

    /**
     * Creat Message
     *
     * @param type     Message type
     * @param position position
     * @param content  content
     * @param sender   sender
     */
    public Message(short type, Point position, String content, int sender) {
        this.type = type;
        this.content = content;
        this.position = position;
        this.sender = sender;
    }
}
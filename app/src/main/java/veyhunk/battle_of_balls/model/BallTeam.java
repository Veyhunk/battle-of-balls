package veyhunk.battle_of_balls.model;

import android.graphics.Point;

import java.util.List;

import static veyhunk.battle_of_balls.constants.Constants.MessageType.SAFE;

/**
 * BallTeam
 */
class BallTeam {


    public List<Ball> team;
    public List<Message> CharRoom;

    public BallTeam(List<Ball> team) {
        this.team = team;
    }

    /**
     * send Message
     *
     * @param message
     * @return boolean
     */
    public boolean sendMessage(Message message) {
        return true;
    }

    /**
     * read Message
     *
     * @return message
     */
    public Message readMessage() {
        Message message = new Message(SAFE, new Point(0, 0));
        return message;
    }

    public boolean addMenber(){
        return false;
    }



}


package com.veyhunk.battle_of_balls.model;

import java.util.ArrayList;
import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.TEAM_PARAMS.MAX_TEAM_AMOUNT;
import static com.veyhunk.battle_of_balls.constants.Constants.getName;

/**
 * BallTeam
 */
public class BallTeam {

    public List<Ball> members;//团队成员
    public int teamColor;
    public String teamName;
    private List<Message> CharRoom;//聊天室
    private Message message;
    private int score = 0;


    BallTeam(int teamColor, String teamName) {
        members = new ArrayList<>();
        message = new Message();
        this.teamColor = teamColor;
        this.teamName = teamName;
    }

    public void action() {
        message.work();
    }



    /**
     * send Message
     *
     * @param message Message
     * @return boolean
     */
    boolean sendMessage(Message message) {
        try {
//            CharRoom.add(message);
            this.message = message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * read Message
     *
     * @return message
     */
    Message readMessage() {
        return message;
    }


    Ball initMember() {
        if (members.size() > MAX_TEAM_AMOUNT) return null;
        for (Ball member : members) {
            if (member.state == BALL_STATE_DEAD) {
                return member;
            }
        }
        return new Ball(this, getName());
    }

    public PlayerBall resetPlayer(PlayerBall deadPlayer) {
        PlayerBall newPlayer;
        for (int i = 0; i < members.size(); i++) {
            Ball member = members.get(i);
            if (member.state == BALL_STATE_ALIVE) {
                newPlayer = new PlayerBall(member);
                members.remove(i);
                members.add(newPlayer);
                return newPlayer;
            }
        }
        return deadPlayer;
    }

    public int getScore() {
        return score;
    }

    void countScore() {
        score = 0;
        for (Ball member : members) {
            if (member.state == BALL_STATE_ALIVE) {
                score += member.weight;
            }
        }
        score /= 20;
    }

    public boolean addMember(Ball member) {
        try {
            members.add(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


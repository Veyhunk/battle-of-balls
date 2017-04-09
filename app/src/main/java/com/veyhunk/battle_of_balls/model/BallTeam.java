package com.veyhunk.battle_of_balls.model;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
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
    private int score=0;


    public BallTeam(List<Ball> members, int teamColor, String teamName) {
        this.members = members;
        this.teamColor = teamColor;
        this.teamName = teamName;
    }

    public BallTeam(int teamColor, String teamName) {
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
     * @param message
     * @return boolean
     */
    public boolean sendMessage(Message message) {
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
    public Message readMessage() {
        return message;
    }


    public Ball initMember() {
        for (Ball member : members) {
            if (member.state == BALL_STATE_DEAD) {
                return member;
            }
        }
        return new Ball(teamColor, getName(), this);
    }

    public int getScore() {
        return score;
    }
    public void countScore() {
        score=0;
        for (Ball member : members) {
            if (member.state == BALL_STATE_ALIVE) {
                score+=member.weight;
            }
        }
        score/=20;
    }


    public boolean addMember(Ball member) {
        try {
            members.add(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addMember(Point target, float weight) {
        Ball newBall = initMember();
        if (newBall != null) {
            weight = weight / 2;
            newBall.reSetBall(target, weight);

            try {
                members.add(newBall);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}


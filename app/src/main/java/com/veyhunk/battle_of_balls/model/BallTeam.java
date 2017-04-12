package com.veyhunk.battle_of_balls.model;

import java.util.ArrayList;
import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.TEAM_PARAMS.TEAM_MEMBER_MAX;
import static com.veyhunk.battle_of_balls.constants.Constants.getName;

/**
 * BallTeam
 */
public class BallTeam {

    public List<Ball> members;//团队成员
    int teamColor;
    public String teamName;
    private List<Message> CharRoom;//聊天室
    private Message message;
    private int score = 0;
    int aliveSize;


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
        Ball newMember = null;
        aliveSize = 0;
        if (aliveSize > 0) return null;
        for (Ball member : members) {
            if (member.state == BALL_STATE_DEAD) {
                newMember = member;
                break;
            } else {
                ++aliveSize;
            }
            System.out.println();
        }

        System.out.println("aliveSize" + aliveSize);
        if (aliveSize >= TEAM_MEMBER_MAX) newMember = null;
        else if (newMember == null) {
            newMember = new Ball(this, getName());
            addMember(newMember);
        };
        return newMember;
    }

    public boolean resetPlayer(PlayerBall deadPlayer) {
        for (int i = 0; i < members.size(); i++) {
            Ball member = members.get(i);
            if (member.state == BALL_STATE_ALIVE) {
                deadPlayer.resetBall(member);
                return true;
            }
        }
        return false;
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


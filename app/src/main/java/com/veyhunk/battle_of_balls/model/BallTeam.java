package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.sounds.GameSounds;

import java.util.ArrayList;
import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_ID;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.getName;
import static com.veyhunk.battle_of_balls.db.GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX;

/**
 * BallTeam
 */
public class BallTeam {

    public List<Ball> members;//团队成员
    public int teamColor;
    public String teamName;
    private List<Message> CharRoom;//聊天室
    public int score = 0;
    public boolean isHaveMember;

    int charRoomIndex;
    int charRoomLength;

    BallTeam(int teamColor, String teamName) {
        members = new ArrayList<>();
        this.teamColor = teamColor;
        this.teamName = teamName;
        CharRoom = new ArrayList<>();
        isHaveMember = true;
    }

    public void action() {
    }


    /**
     * send Message
     *
     * @param message Message
     */
    void sendMessage(Message message) {
        try {
            CharRoom.add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("mm" + message.ballSender.name + ":" + MSG_TYPES[message.type] + " " + message.ballSender.radius + " " + message.ballObject.radius);
    }

    public String strMessage="";

    public void showMessage(String strMessage) {
        if (Math.random() > .9) {
            this.strMessage = strMessage;
        }
//        System.out.println(strMessage);
    }

    /**
     * read Message
     *
     * @return message
     */
    Message readMessage(Ball ballReader) {
        charRoomIndex = CharRoom.size();
        while (charRoomIndex > 0) {
            --charRoomIndex;
            if (ballReader.id != CharRoom.get(charRoomIndex).ballSender.id) {
                if (CharRoom.get(charRoomIndex).isCompleted()) {
                    CharRoom.remove(CharRoom.get(charRoomIndex));
                } else {
                    return CharRoom.get(charRoomIndex);
                }
            }
        }
        return null;
    }

    public PlayerBall initPlayer(GameSounds gameSounds) {
        PlayerBall playerBall = new PlayerBall(members.get(0), gameSounds);
        members.remove(0);
        addMember(playerBall);
        return playerBall;
    }

    Ball initMember() {
        Ball newMember = null;
        int aliveSize = 0;
        for (Ball member : members) {
            if (member.state == BALL_STATE_DEAD) {
                newMember = member;
                break;
            } else {
                ++aliveSize;
            }
        }
        if (aliveSize >= TEAM_MEMBER_MAX) newMember = null;
        else if (newMember == null) {
            newMember = new Ball(this, getName(), BALL_ID++);
            addMember(newMember);
        }
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
                score += member.radius;
            }
        }
        if (score == 0) isHaveMember = false;
    }

    public void addMember(Ball member) {
        try {
            members.add(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


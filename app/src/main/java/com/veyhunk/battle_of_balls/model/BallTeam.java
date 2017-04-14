package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.sounds.GameSounds;
import com.veyhunk.battle_of_balls.utils.GameMath;

import java.util.ArrayList;
import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_ALIVE;
import static com.veyhunk.battle_of_balls.constants.Constants.BALL_STATE_DEAD;
import static com.veyhunk.battle_of_balls.constants.Constants.MessageType.SAFE;
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
    private Message message;
    public int score = 0;
    public boolean isHaveMember;

    BallTeam(int teamColor, String teamName) {
        members = new ArrayList<>();
        message = new Message();
        this.teamColor = teamColor;
        this.teamName = teamName;
        CharRoom = new ArrayList<>();
        isHaveMember = true;
    }

    public void action() {
        message.work();
    }


    /**
     * send Message
     *
     * @param message Message
     */
    void sendMessage(Message message) {
        try {
//            CharRoom.add(message);
            this.message = message;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * read Message
     *
     * @return message
     */
    Message readMessage() {
//        if (CharRoom.size() > 10) {
//            CharRoom.clear();
//        }
        action();
       if(message.isCompleted()){message.editMessage(SAFE, GameMath.getPointRandom());}
        return message;

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
            newMember = new Ball(this, getName());
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


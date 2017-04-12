package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.veyhunk.battle_of_balls.constants.Constants.BALL_COLORS;
import static com.veyhunk.battle_of_balls.utils.Colors.getColorByIndex;

/**
 * TeamsManager
 * Created by Veyhunk on 27/March/2017.
 */

public class TeamsManager {
    //team
    public TeamsManager() {
        int index1, index2,randColor=(int)(Math.random() * BALL_COLORS.length);

        //team
        for (index1 = 0; index1 < Constants.TEAM_PARAMS.TEAM_AMOUNT; index1++) {
            BallTeam team = new BallTeam(getColorByIndex(index1+randColor), Constants.TEAM_PARAMS.TEAM_NAMES[index1]);
            for (index2 = 0; index2 < Constants.TEAM_PARAMS.TEAM_MEMBER_AMOUNT; index2++) {
                team.addMember(team.initMember());
            }
            teams[index1] = team;
        }
        allBalls = new ArrayList<>();
    }

    public BallTeam[] getTeams() {
        return teams;
    }


    public List<Ball> getAllBalls() {
        allBalls.clear();
        for (BallTeam team : teams) {
            for (Ball ball : team.members) {
                if (ball.state) allBalls.add(ball);
            }
        }
        return allBalls;
    }

    public void sort() {
        refresh();
        int length = teams.length;
        int index1, index2;
        for (index1 = 0; index1 < length - 1; index1++) {
            for (index2 = 0; index2 < length - 1; index2++) {
                if (teams[index2].getScore() < teams[index2 + 1].getScore()) {
                    BallTeam temp = teams[index2];
                    teams[index2] = teams[index2 + 1];
                    teams[index2 + 1] = temp;
                }
            }
        }
    }

    private void refresh() {
        for (BallTeam team : teams) {
            team.countScore();
        }
    }

    private BallTeam[] teams = new BallTeam[Constants.TEAM_PARAMS.TEAM_AMOUNT];


    private List<Ball> allBalls;//全部成员

}

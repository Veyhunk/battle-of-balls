package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.constants.Constants;

import static com.veyhunk.battle_of_balls.utils.Colors.getColorRandom;

/**
 * TeamsManager
 * Created by Veyhunk on 27/March/2017.
 */

public class TeamsManager {
    //team

    //    ArrayList teams = new ArrayList() ;
    private BallTeam[] teams = new BallTeam[Constants.TEAM_PARAMS.TEAM_AMOUNT];

    public TeamsManager() {
        int index1, index2;
        //team
        for (index1 = 0; index1 < Constants.TEAM_PARAMS.TEAM_AMOUNT; index1++) {
            BallTeam team = new BallTeam(getColorRandom(), Constants.TEAM_PARAMS.TEAM_NAMES[index1]);
            for (index2 = 0; index2 < Constants.TEAM_PARAMS.TEAM_MEMBER_AMOUNT; index2++) {
                team.addMember(team.initMember());
            }
            teams[index1] = team;
        }
    }

    public BallTeam[] getTeams() {
        return teams;
    }

    public void sort() {
//Collections.sort(teams, teamRank) ;
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
}

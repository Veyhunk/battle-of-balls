package com.veyhunk.battle_of_balls.model;

import com.veyhunk.battle_of_balls.constants.Constants;

import static com.veyhunk.battle_of_balls.utils.Colors.getColorByIndex;

/**
 * Created by Veyhunk on 27/March/2017.
 */

public class TeamsManager {
    //team
    private BallTeam[] teams = new BallTeam[Constants.TEAM_PARAMS.TEAM_AMOUNT];

    public TeamsManager() {
        int index1, index2;
        //team
        teams = new BallTeam[Constants.TEAM_PARAMS.TEAM_AMOUNT];
        for (index1 = 0; index1 < Constants.TEAM_PARAMS.TEAM_AMOUNT; index1++) {
            BallTeam team = new BallTeam(getColorByIndex(index1), Constants.TEAM_PARAMS.TEAM_NAMES[index1]);
            for (index2 = 0; index2 < Constants.TEAM_PARAMS.TEAM_MEMBER_AMOUNT; index2++) {
                team.addMember(team.initMember());
            }
            teams[index1] = team;
        }
    }

    public BallTeam[] getTeams() {
        return teams;
    }
    public void sorl(){

    }
}

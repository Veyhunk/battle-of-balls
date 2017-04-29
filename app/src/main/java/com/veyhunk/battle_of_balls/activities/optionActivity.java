package com.veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.veyhunk.battle_of_balls.R;
import com.veyhunk.battle_of_balls.db.GameParams;
import com.veyhunk.battle_of_balls.sounds.GameSounds;

public class optionActivity extends Activity {
    GameSounds gameSounds;
    private SeekBar sbrDifficult;
    private SeekBar sbrSpeed;
    private SeekBar sbrGrow;
    private SeekBar sbrTeamAccount;
    private SeekBar sbrTeamMemberAccount;
    private SeekBar sbrTeamMemberMax;
    private SeekBar sbrBallWeightDefault;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前屏幕常亮
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.options);

        gameSounds = new GameSounds(getApplication());
        sbrDifficult = (SeekBar) findViewById(R.id.sbrDifficult);
        sbrSpeed = (SeekBar) findViewById(R.id.sbrSpeed);
        sbrGrow = (SeekBar) findViewById(R.id.sbrGrow);
        sbrTeamAccount = (SeekBar) findViewById(R.id.teamAccount);
        sbrTeamMemberAccount = (SeekBar) findViewById(R.id.teamMemberAccount);
        sbrTeamMemberMax = (SeekBar) findViewById(R.id.teamMemberMax);
        sbrBallWeightDefault = (SeekBar) findViewById(R.id.ballWeightDefault);

        sbrDifficult.setProgress((int) GameParams.AI_DIFFICULT);
        sbrSpeed.setProgress((int) GameParams.BALL_MOVE_SPEED);
        sbrGrow.setProgress((int) GameParams.BALL_GROW_SPEED);
        sbrBallWeightDefault.setProgress((int) (Math.sqrt(GameParams.BALL_WEIGHT_DEFAULT)/50));
        sbrTeamAccount.setProgress(GameParams.TEAM_PARAMS.TEAM_AMOUNT);
        sbrTeamMemberAccount.setProgress(GameParams.TEAM_PARAMS.TEAM_MEMBER_AMOUNT);
        sbrTeamMemberMax.setProgress(GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX);
    }

    public void btnYes(View view) {
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        Intent intent = getIntent();
        GameParams.BALL_MOVE_SPEED = sbrSpeed.getProgress();
        GameParams.AI_DIFFICULT = sbrDifficult.getProgress();
        GameParams.BALL_GROW_SPEED = sbrGrow.getProgress();
        GameParams.BALL_WEIGHT_DEFAULT = (int) Math.pow(sbrBallWeightDefault.getProgress()*50,2);
        GameParams.TEAM_PARAMS.TEAM_AMOUNT = sbrTeamAccount.getProgress();
        GameParams.TEAM_PARAMS.TEAM_MEMBER_AMOUNT = sbrTeamMemberAccount.getProgress();
        GameParams.TEAM_PARAMS.TEAM_MEMBER_MAX = sbrTeamMemberMax.getProgress();
        if(GameParams.BALL_WEIGHT_DEFAULT<2500)GameParams.BALL_WEIGHT_DEFAULT=2500;
        if(GameParams.AI_DIFFICULT<10)GameParams.AI_DIFFICULT=10;
        if(GameParams.BALL_GROW_SPEED<1)GameParams.BALL_GROW_SPEED=1;
        if(GameParams.BALL_MOVE_SPEED<4)GameParams.BALL_MOVE_SPEED=4;

        setResult(1, intent);
        finish();
    }

    public void btnNo(View view) {
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        setResult(2, getIntent());
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            setResult(2, getIntent());
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}

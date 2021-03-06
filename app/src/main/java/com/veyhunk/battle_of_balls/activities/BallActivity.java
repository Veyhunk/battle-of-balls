package com.veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.veyhunk.battle_of_balls.R;
import com.veyhunk.battle_of_balls.sounds.GameSounds;
import com.veyhunk.battle_of_balls.surface_view.MySurfaceView.OnEndOfGameInterface;

public class BallActivity extends Activity implements OnEndOfGameInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_ball);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        GameSounds gameSounds;
        gameSounds = new GameSounds(getApplication());
        gameSounds.starMusic(GameSounds.CLICK);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = getIntent();
//			if (BEST_SCORE < MySurfaceView.score) {
//				BEST_SCORE = MySurfaceView.score;
//			}
            setResult(2, intent);
            gameSounds.recycle();
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEndOfGame() {
        Intent intent = getIntent();
        setResult(2, intent);
        finish();
    }
}

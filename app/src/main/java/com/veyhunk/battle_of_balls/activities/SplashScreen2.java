package com.veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.veyhunk.battle_of_balls.R;
import com.veyhunk.battle_of_balls.sounds.GameSounds;

/**
 * Created by Veyhunk on 12/March/2017.
 * 游戏启动画面
 * Game Splash Screen
 */
public class SplashScreen2 extends Activity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // 设置去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen_2);

        final GameSounds gameSounds;
        gameSounds = new GameSounds(getApplication());
        gameSounds.starMusic(GameSounds.LOGO2);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(SplashScreen2.this,
                        MainActivity.class);
                SplashScreen2.this.startActivity(mainIntent);
                SplashScreen2.this.finish();
                gameSounds.recycle();
            }
        }, 2000); // 2000 for release

    }
}
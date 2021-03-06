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
public class SplashScreen extends Activity {
/**
 *          V4.6 TODO list
 *          difficult
 *          z-index
 *          Add Sting system
 *          name size scale
 *
 * version V4.6
 *          Auto control for player ball
 *          Improved communication system
 *          Show game result
 *
 * version V4.5
 *          Speed with weight
 *          Camera scale
 *          Hide & show rank
 *
 * version V4.4
 *          Added operation
 *          Improved draw function
 *          Edge collision
 *
 * version V4.4
 *          High-DPI support
 *          Rank index use text
 *          Eat teammate
 *          Improved eat system
 *          Improved feeling system for avatar
 *
 * version V4.3
 *          Fix rank system
 *          Add sendBattle system
 *          Add new avatar system
 *
 */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // 设置去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        final GameSounds gameSounds;
        gameSounds = new GameSounds(getApplication());
        gameSounds.starMusic(GameSounds.LOGO1);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,
                        SplashScreen2.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
                gameSounds.recycle();
            }
        }, 1200); // 1200 for release

    }
}
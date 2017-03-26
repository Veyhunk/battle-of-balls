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
 * @version v4.1
 *          <p>
 *          Code refactor
 *          Removed bubble system
 *          Refactored Colors & Clock
 *          Remove avatar system
 *          <p>
 *          v4.x
 *          TODO list
 *          Code refactoring
 *          Remove bubble system
 *          Remove avatar system
 *          Add team system
 *          Add char system
 */

/**
 * Created by Veyhunk on 12/March/2017.
 * 游戏启动画面
 * Game Splash Screen
 */
public class SplashScreen extends Activity {
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
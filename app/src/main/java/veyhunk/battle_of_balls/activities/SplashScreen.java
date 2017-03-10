package veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.sounds.GameSounds;

public class SplashScreen extends Activity {
	/**
	 * Called when the activity is first created.
	 */

	private GameSounds gameSounds;
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


		gameSounds =new GameSounds(getApplication());
		gameSounds.starMusic(GameSounds.LOGO1);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(SplashScreen.this,
						SplashScreen2.class);
				SplashScreen.this.startActivity(mainIntent);
				SplashScreen.this.finish();
			}
		}, 1200); // 2900 for release

	}
}
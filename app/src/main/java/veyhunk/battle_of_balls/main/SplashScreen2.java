package veyhunk.battle_of_balls.main;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.sound.GameMusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen2 extends Activity {
	/**
	 * Called when the activity is first created.
	 */

	private GameMusic gameMusic;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// 设置去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashscreen2);


		gameMusic=new GameMusic(getApplication());
		gameMusic.starMusic(GameMusic.LOGO2);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(SplashScreen2.this,
						MainActivity.class);
				SplashScreen2.this.startActivity(mainIntent);
				SplashScreen2.this.finish();
			}
		}, 2000); // 2900 for release

	}
}
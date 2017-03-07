package veyhunk.battle_of_balls.activity;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.main.MainActivity;
import veyhunk.battle_of_balls.utils.GameMusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

public class optionActivity extends Activity {
	public static float speed = 20f;
	public static float grow = 4F;
	public static float difficult = 0;
	SeekBar sbrDifficult;
	SeekBar sbrSpeed;
	SeekBar sbrGrow;

	// 【Name: "感情淡了放盐啊"】 【 grow =20f】【speed = * 4F】 【color = 8】

	protected void onCreate(Bundle savedInstanceState) {
		// Button btnSure= (Button) findViewById(R.id.button1);;
		// Button btnCenter = (Button) findViewById(R.id.button3);
		// btnSure = (Button) findViewById(R.id.button1);
		// btnCenter = (Button) findViewById(R.id.button3);
		super.onCreate(savedInstanceState);
		// 设置去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置当前屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.options);
		sbrDifficult = (SeekBar) findViewById(R.id.sbrDifficult);
		sbrSpeed = (SeekBar) findViewById(R.id.sbrSpeed);
		sbrGrow = (SeekBar) findViewById(R.id.sbrGrow);

		sbrDifficult.setProgress((int) difficult);
		sbrSpeed.setProgress((int) speed);
		sbrGrow.setProgress((int) grow);

	}

	public static void Setting(float speed, float grow, float aiDifficult) {
		optionActivity.speed = speed;
		optionActivity.grow = grow;
		optionActivity.difficult = aiDifficult;
	}

	public void btnYes(View view) {
		MainActivity.gameMusic.starMusic(GameMusic.CLICK);
		Intent intent = getIntent();
		intent.putExtra("Speed", sbrSpeed.getProgress());
		intent.putExtra("AiDifficult", sbrDifficult.getProgress());
		intent.putExtra("Grow", sbrGrow.getProgress());
		setResult(1, intent);
		finish();
	}

	public void btnNo(View view) {
		MainActivity.gameMusic.starMusic(GameMusic.CLICK);
		setResult(2, getIntent());
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		MainActivity.gameMusic.starMusic(GameMusic.CLICK);
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			setResult(2, getIntent());
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}
}

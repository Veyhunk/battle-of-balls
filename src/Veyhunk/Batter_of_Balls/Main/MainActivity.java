package Veyhunk.Batter_of_Balls.Main;

import Veyhunk.Batter_of_Balls.R;
import Veyhunk.Batter_of_Balls.Activity.BallActivity;
import Veyhunk.Batter_of_Balls.Activity.optionActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements OnTouchListener {
	private Button button[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置当前屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		button = new Button[2];
		button[0] = (Button) findViewById(R.id.start);
		button[1] = (Button) findViewById(R.id.setting);
		for (int i = 0; i < button.length; i++) {
			button[i].setOnTouchListener(this);
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.start:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent intent = new Intent();
				intent.setClass(this, BallActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.setting:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent intent = new Intent();
				intent.setClass(this, optionActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

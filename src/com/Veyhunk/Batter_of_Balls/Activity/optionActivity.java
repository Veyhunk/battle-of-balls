package com.Veyhunk.Batter_of_Balls.Activity;

import com.Veyhunk.Batter_of_Balls.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class optionActivity extends Activity {
	public static float speed = 20f;
	public static float grow = 4F;
	public static float difficult = 0;
	SeekBar sbrDifficult;
	SeekBar sbrSpeed;
	SeekBar sbrGrow;

	// ��Name: "goodnight ����"�� �� grow =20f����speed = * 4F�� ��color = 8��

	protected void onCreate(Bundle savedInstanceState) {
		// Button btnSure= (Button) findViewById(R.id.button1);;
		// Button btnCenter = (Button) findViewById(R.id.button3);
		// btnSure = (Button) findViewById(R.id.button1);
		// btnCenter = (Button) findViewById(R.id.button3);
		super.onCreate(savedInstanceState);
		// ����ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ���õ�ǰ��Ļ����
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
		Intent intent = getIntent();
		intent.putExtra("Speed", sbrSpeed.getProgress());
		intent.putExtra("AiDifficult", sbrDifficult.getProgress());
		intent.putExtra("Grow", sbrGrow.getProgress());
		setResult(1, intent);
		finish();
	}

	public void btnNo(View view) {
		setResult(2, getIntent());
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			setResult(2, getIntent());
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}
}

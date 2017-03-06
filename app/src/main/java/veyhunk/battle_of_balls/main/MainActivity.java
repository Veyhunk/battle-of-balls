package veyhunk.battle_of_balls.main;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.activity.optionActivity;
import veyhunk.battle_of_balls.surface_view.BallActivity;
import veyhunk.battle_of_balls.surface_view.MySurfaceView;

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
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener {
	private static String bestScore="";
	private Button button[];
	private String fileName = "BallBastScore";
	static float ballMoveSpeed = 60;
	static float ballGrowSpeed = 300;
	static float aiDifficult = 6;
	static int ballColorIndex = 10;
	// public static String ballName = "感情淡了要放盐";// 感情淡了要放盐
	public static String ballName = "";// 感情淡了要放盐
	EditText edtName;
	TextView tvBestScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DataRead();
		if (ballName.length() == 0) {
			ballName = "感情淡了要放盐";
		}
		if (bestScore.length() == 0) {
			bestScore = "0";
		}
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
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);
		tvBestScore.setText("最高分:" + bestScore);
		edtName = (EditText) findViewById(R.id.edtName);
		edtName.setText(ballName);
		for (int i = 0; i < button.length; i++) {
			button[i].setOnTouchListener(this);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		DataSave();
		super.onDestroy();
	}

	public static void Setting(String name, float speed, float grow,
			float difficule, int color) {
		ballName = name;// 感情淡了要放盐
		ballMoveSpeed = speed;// ballGrowSpeed
		ballGrowSpeed = grow;// ballMoveSpeed
		ballColorIndex = color;// playerColor
		aiDifficult = difficule;// playerColor
	}

	public static void Setting(String name, float speed, float grow,
			float difficule, int color, int score) {
		// TODO Auto-generated method stub
		ballName = name;// 感情淡了要放盐
		ballMoveSpeed = speed;// ballGrowSpeed
		ballGrowSpeed = grow;// ballMoveSpeed
		ballColorIndex = color;// playerColor
		aiDifficult = difficule;// playerColor
		bestScore = score + "";
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.start:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// System.out.println("speed" + speed);
				// System.out.println("grow" + grow);
				// System.out.println("Difficult" + aiDifficult);
				ballName = edtName.getText().toString();
				MySurfaceView.Setting(ballName, ballMoveSpeed, ballGrowSpeed,
						aiDifficult, ballColorIndex, bestScore);
				Intent intent = new Intent();
				intent.setClass(this, BallActivity.class);
				startActivityForResult(intent, 1);

			}
			break;
		case R.id.setting:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// System.out.println("speed" + speed);
				// System.out.println("grow" + grow);
				// System.out.println("Difficult" + aiDifficult);
				optionActivity.Setting(ballMoveSpeed, ballGrowSpeed,
						aiDifficult);
				Intent intent = new Intent();
				intent.setClass(this, optionActivity.class);
				startActivityForResult(intent, 1);
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			ballMoveSpeed = data.getIntExtra("Speed", 0);
			ballGrowSpeed = data.getIntExtra("Grow", 0);
			aiDifficult = data.getIntExtra("AiDifficult", 0);
			// ballColorIndex = data.getIntExtra("Color", 0);
		}else if (resultCode == 2) {
			tvBestScore.setText("最高分:" + bestScore);
		}
	};

	public void DataSave() {
		String stringSave = "";
		try {
			// 为root创建一个JSONObject对象
			JSONObject ball = new JSONObject();
			// 为root JSONObject对象添加一个“名称,值”对
			ball.put("bestScore", bestScore);
			ballName = edtName.getText().toString();
			ball.put("ballName", ballName);
			ball.put("ballColorIndex", ballColorIndex);
			ball.put("ballGrowSpeed", ballGrowSpeed);
			ball.put("ballMoveSpeed", ballMoveSpeed);
			ball.put("aiDifficult", aiDifficult);
			System.out.println(ball.toString());
			stringSave = ball.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {

			FileOutputStream outputStream = openFileOutput(fileName,
					Activity.MODE_PRIVATE);
			outputStream.write(stringSave.getBytes());
			outputStream.flush();
			outputStream.close();
			System.out.println("save Ok:" + stringSave);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void DataRead() {

		try {
			FileInputStream inputStream = this.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();
			arrayOutputStream.close();
			String content = new String(arrayOutputStream.toByteArray());
			System.out.println("read Ok:" + content.trim());
			// return content.trim();
			jsonAnalysis(content.trim());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// return null;
	}

	public void jsonAnalysis(String Json) {
		try {
			// initialize
			// object_result
			JSONObject ball = new JSONObject(Json);

			// handle result
			bestScore = ball.getString("bestScore");
			ballName = ball.getString("ballName");
			ballColorIndex = Integer.parseInt(ball.getString("ballColorIndex"));
			ballGrowSpeed = Float.parseFloat(ball.getString("ballGrowSpeed"));
			ballMoveSpeed = Float.parseFloat(ball.getString("ballMoveSpeed"));
			aiDifficult = Integer.parseInt(ball.getString("aiDifficult"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
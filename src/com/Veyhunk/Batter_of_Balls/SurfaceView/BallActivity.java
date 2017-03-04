package com.Veyhunk.Batter_of_Balls.SurfaceView;

import com.Veyhunk.Batter_of_Balls.R;
import com.Veyhunk.Batter_of_Balls.Main.MainActivity;
import com.Veyhunk.Batter_of_Balls.SurfaceView.MySurfaceView.OnEndOfGameInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BallActivity extends Activity implements OnEndOfGameInterface  {
	MySurfaceView mGameSurfaceView ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ���õ�ǰ��Ļ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		mGameSurfaceView = (MySurfaceView) findViewById(R.id.gameView);  
//		mGameSurfaceView.setOnEndOfGame(this);  //����this���趨�Լ�Ϊ�ص�Ŀ��  
		setContentView(R.layout.activity_ball);
	}

	/**
	 * ��һ�Ű�ť�¼�
	 * 
	 * @param view
	 */
	public void nextBt(View view) {
		Toast.makeText(getApplicationContext(), "���ڻ����ܷ�����һ��ͺ���",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��һ�Ű�ť�¼�
	 * 
	 * @param view
	 */
	public void preBt(View view) {
		Toast.makeText(getApplicationContext(), "�����������¸���������",
				Toast.LENGTH_SHORT).show();
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = getIntent();
//			intent.putExtra("Speed", MySurfaceView.ballMoveSpeed*10);
//			intent.putExtra("AiDifficult", MySurfaceView.AiDifficult);
//			intent.putExtra("Grow",MySurfaceView.ballGrowSpeed*10);
//			intent.putExtra("Name",MySurfaceView.ballName);			
//			intent.putExtra("Color",MySurfaceView.ballColorIndex);
//
//			System.out.println("speed" + MySurfaceView.ballMoveSpeed*10);
//			System.out.println("grow" + MySurfaceView.ballGrowSpeed*10);
//			System.out.println("Difficult" +  MySurfaceView.AiDifficult);
//			System.out.println(intent);
			setResult(2, intent);
			MainActivity.Setting(MySurfaceView.ballName, MySurfaceView.ballMoveSpeed*10,
					MySurfaceView.ballGrowSpeed*10, MySurfaceView.aiDifficult, MySurfaceView.ballColorIndex);
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onEndOfGame() {
		// TODO Auto-generated method stub
		MainActivity.Setting(MySurfaceView.ballName, MySurfaceView.ballMoveSpeed*10,
				MySurfaceView.ballGrowSpeed*10, MySurfaceView.aiDifficult, MySurfaceView.ballColorIndex,MySurfaceView.score);
		finish();
	}
}

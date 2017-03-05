
package Veyhunk.Battle_of_Balls.SurfaceView;

import Veyhunk.Battle_of_Balls.R;
import Veyhunk.Battle_of_Balls.Main.MainActivity;
import Veyhunk.Battle_of_Balls.SurfaceView.MySurfaceView.OnEndOfGameInterface;

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
		// 设置去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置当前屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		mGameSurfaceView = (MySurfaceView) findViewById(R.id.gameView);  
//		mGameSurfaceView.setOnEndOfGame(this);  //传入this，设定自己为回调目标  
		setContentView(R.layout.activity_ball);
	}

	/**
	 * 下一张按钮事件
	 * 
	 * @param view
	 */
	public void nextBt(View view) {
		Toast.makeText(getApplicationContext(), "现在还不能分身，点一点就好了",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 上一张按钮事件
	 * 
	 * @param view
	 */
	public void preBt(View view) {
		Toast.makeText(getApplicationContext(), "来来来，快吐个豆豆出来",
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

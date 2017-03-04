package Veyhunk.Batter_of_Balls.Activity;



import Veyhunk.Batter_of_Balls.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class optionActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
//		Button btnSure= (Button) findViewById(R.id.button1);;
//		Button btnCenter = (Button) findViewById(R.id.button3);
//		btnSure = (Button) findViewById(R.id.button1);
//		btnCenter = (Button) findViewById(R.id.button3);
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
//		btnSure.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
//		btnCenter.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
	}
}

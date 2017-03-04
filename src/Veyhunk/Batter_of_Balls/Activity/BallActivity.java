package Veyhunk.Batter_of_Balls.Activity;


import Veyhunk.Batter_of_Balls.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BallActivity extends Activity {

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
	        setContentView(R.layout.activity_ball);
	    }
	    /**
	     * 下一张按钮事件
	     * @param view
	     */
	    public void nextBt(View view){
	    	Toast.makeText(getApplicationContext(), "现在还不能分身，点一点就好了", Toast.LENGTH_SHORT ).show();
	    }
	    /**
	     * 上一张按钮事件
	     * @param view
	     */
	    public void preBt(View view){
	    	Toast.makeText(getApplicationContext(), "来来来，快吐个豆豆出来", Toast.LENGTH_SHORT ).show();
	    }
}

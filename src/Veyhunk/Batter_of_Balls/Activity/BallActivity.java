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
	    	// ����ȥ��������
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// ����ȫ��
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			// ���õ�ǰ��Ļ����
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        setContentView(R.layout.activity_ball);
	    }
	    /**
	     * ��һ�Ű�ť�¼�
	     * @param view
	     */
	    public void nextBt(View view){
	    	Toast.makeText(getApplicationContext(), "���ڻ����ܷ�����һ��ͺ���", Toast.LENGTH_SHORT ).show();
	    }
	    /**
	     * ��һ�Ű�ť�¼�
	     * @param view
	     */
	    public void preBt(View view){
	    	Toast.makeText(getApplicationContext(), "�����������¸���������", Toast.LENGTH_SHORT ).show();
	    }
}

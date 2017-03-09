package veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.db.GameParams;
import veyhunk.battle_of_balls.db.GameProgress;
import veyhunk.battle_of_balls.sounds.GameSounds;

public class MainActivity extends Activity implements OnTouchListener {
    public static GameSounds gameSounds;
    public static GameProgress gameProgress;

    EditText edtName;
    TextView tvBestScore;
    //Double-click exit app
    boolean isExitAppState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gameSounds = new GameSounds(getApplication());
        gameProgress = new GameProgress(getApplication());
        gameProgress.Read();
        super.onCreate(savedInstanceState);
        // 设置去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前屏幕常亮
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        Button[] buttons = new Button[2];
        buttons[0] = (Button) findViewById(R.id.start);
        buttons[1] = (Button) findViewById(R.id.setting);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        TextView versionNumber = (TextView) findViewById(R.id.tvVersionNumber);
        edtName = (EditText) findViewById(R.id.edtName);

        tvBestScore.setText("最高分:" + GameParams.bestScore);

        if (GameParams.bestScore.length() == 0) {
            GameParams.bestScore = "0";
        }
        edtName.setText(GameParams.ballName);
        for (Button button : buttons) button.setOnTouchListener(this);

        try {
            versionNumber.setText("版本号：" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        gameProgress.Save();
        super.onDestroy();
    }

    private String getVersionName() throws Exception {
        // 获取PackageManager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gameSounds.starMusic(GameSounds.CLICK);
        switch (v.getId()) {

            case R.id.start:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // System.out.println("speed" + speed);
                    // System.out.println("grow" + grow);
                    // System.out.println("Difficult" + aiDifficult);
                    GameParams.ballName = edtName.getText().toString();
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
        return (item.getItemId() == R.id.action_settings) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            gameProgress.Save();
        } else if (resultCode == 2) {
            tvBestScore.setText("最高分:" + GameParams.bestScore);
        }
    }

    @Override
    public void onBackPressed() {
        if (isExitAppState) { //isExitAppState初始值为true
            isExitAppState = false;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    isExitAppState = true;
                }
            }, 2000);
        } else {
            finish();
        }
    }


}

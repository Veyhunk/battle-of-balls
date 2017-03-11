package veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
    private static GameSounds gameSounds;
    private static GameProgress gameProgress;

    private EditText edtName;
    private TextView tvBestScore;
    private boolean isNoExitAppState = true;//Double-click exit app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        buttons[1] = (Button) findViewById(R.id.btSetting);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        TextView versionNumber = (TextView) findViewById(R.id.tvVersionNumber);
        edtName = (EditText) findViewById(R.id.edtNickName);

        tvBestScore.setText(getString(R.string.bestScore) + ":" + GameParams.bestScore);
        edtName.setText(GameParams.ballName);
        for (Button button : buttons) button.setOnTouchListener(this);

        try {
            versionNumber.setText(getString(R.string.version) + ":" + getVersionName());
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
        if (isNoExitAppState) {
            isNoExitAppState = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    gameSounds = new GameSounds(getApplication());
                    gameSounds.starMusic(GameSounds.CLICK);
                    gameSounds.recycle();
                    switch (v.getId()) {

                        case R.id.start:
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                GameParams.ballName = edtName.getText().toString();
                                Intent intent = new Intent();
                                intent.setClass(this, BallActivity.class);
                                startActivityForResult(intent, 1);

                            }
                            break;
                        case R.id.btSetting:
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                Intent intent = new Intent();
                                intent.setClass(this, optionActivity.class);
                                startActivityForResult(intent, 1);
                            }
                            break;
                        default:
                            break;
                    }
                }
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isNoExitAppState = true;
        if (resultCode == 1) {
            gameProgress.Save();
        } else if (resultCode == 2) {
            tvBestScore.setText(getString(R.string.bestScore) + ":" + GameParams.bestScore);
        }
    }

    @Override
    public void onBackPressed() {
        gameSounds = new GameSounds(getApplication());
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        if (isNoExitAppState) { //isExitAppState初始值为true
            isNoExitAppState = false;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    isNoExitAppState = true;
                }
            }, 2000);
        } else {
            finish();
        }
    }


}


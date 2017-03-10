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
                    GameParams.ballName = edtName.getText().toString();
                    Intent intent = new Intent();
                    intent.setClass(this, BallActivity.class);
                    startActivityForResult(intent, 1);

                }
                break;
            case R.id.setting:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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


/**
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.2
 *新增了AI互吃；横屏锁定；
 *新增游戏本地文件存储
 *
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.3
 *完善排行榜UI
 *完善积分体重UI
 *修复AI的0生命被吃
 *修复AI是否达到复活条件
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.4
 *完成游戏音效
 *完成游戏的回调
 *实现游戏最高分累积
 *json的保存读取与处理
 *内录游戏配音失败，一个晚上啊，声卡缺陷害死人，
 *反编译游戏成功，然而并没有什么卵用，都是图片
 *
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.5
 *新增启动动画
 *修复了边界移动黑屏
 *完成游戏配音内录
 *完成全局配音
 *完善游戏文件架构@@@@@@@@
 *
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.6
 *完善游戏文件架构@@@@@@@@
 *新增被吃提示框
 *新增游戏胜利和失败的提示框
 *新增吐球功能
 *
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.7
 *修复了分身系统导致的无法复活
 *移动GameMusic到sound包

 *
 * @author Veyhunk
 * 更新日志
 *@version 3.8
 *修复了分身系统分身数量不对
 *增加分身系统吃豆豆
 *增加分身与ai的互吃
 *完成游戏时间限制
 *
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.9
 * Added doble-click exit
 * simplify code
 * TODO list
 * string merge
 * final value
 * class extract
 * improved bubble system
 * improved avatar system

 *
 * @author Veyhunk
 *  更新日志
 * @version 10.0 完成游戏
 */

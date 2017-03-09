
package veyhunk.battle_of_balls.main;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.activity.optionActivity;
import veyhunk.battle_of_balls.sound.GameMusic;
import veyhunk.battle_of_balls.surface_view.BallActivity;
import veyhunk.battle_of_balls.surface_view.MySurfaceView;

public class MainActivity extends Activity implements OnTouchListener {
    private static String bestScore = "";
    private String fileName = "BallBastScore";
    static float ballMoveSpeed;
    static float ballGrowSpeed;
    static float aiDifficult;
    static int ballColorIndex = 10;
    public static String ballName;// ballName
    EditText edtName;
    TextView tvBestScore;
    public static GameMusic gameMusic;
    //Double-click exit app
    boolean isExitAppState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataRead();
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

        Button[] buttons = new Button[2];
        buttons[0] = (Button) findViewById(R.id.start);
        buttons[1] = (Button) findViewById(R.id.setting);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        TextView versionNumber = (TextView) findViewById(R.id.tvVersionNumber);
        edtName = (EditText) findViewById(R.id.edtName);
        gameMusic = new GameMusic(getApplication());

        tvBestScore.setText("最高分:" + bestScore);

        if (ballName.length() == 0) {
            ballName = "感情淡了放盐啊";
        }

        if (bestScore.length() == 0) {
            bestScore = "0";
        }
        edtName.setText(ballName);
        for (Button button : buttons) button.setOnTouchListener(this);

        try {
            versionNumber.setText("版本号：" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        DataSave();
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

    public static void Setting(String name, float speed, float grow,
                               float difficult, int color, int score) {
        ballName = name;// ballName
        ballMoveSpeed = speed;// ballGrowSpeed
        ballGrowSpeed = grow;// ballMoveSpeed
        ballColorIndex = color;// playerColor
        aiDifficult = difficult;// playerColor
        bestScore = score + "";
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gameMusic.starMusic(GameMusic.CLICK);
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
        return (item.getItemId() == R.id.action_settings) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            ballMoveSpeed = data.getIntExtra("Speed", 0);
            ballGrowSpeed = data.getIntExtra("Grow", 0);
            aiDifficult = data.getIntExtra("AiDifficult", 0);
            // ballColorIndex = data.getIntExtra("Color", 0);
            DataSave();
        } else if (resultCode == 2) {
            tvBestScore.setText("最高分:" + bestScore);
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
            e.printStackTrace();
        }
    }

}

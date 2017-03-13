package veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.db.GameParams;
import veyhunk.battle_of_balls.sounds.GameSounds;

public class optionActivity extends Activity {
    GameSounds gameSounds;
    private SeekBar sbrDifficult;
    private SeekBar sbrSpeed;
    private SeekBar sbrGrow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前屏幕常亮
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.options);

        gameSounds = new GameSounds(getApplication());
        sbrDifficult = (SeekBar) findViewById(R.id.sbrDifficult);
        sbrSpeed = (SeekBar) findViewById(R.id.sbrSpeed);
        sbrGrow = (SeekBar) findViewById(R.id.sbrGrow);

        sbrDifficult.setProgress((int) GameParams.aiDifficult);
        sbrSpeed.setProgress((int) GameParams.ballMoveSpeed);
        sbrGrow.setProgress((int) GameParams.ballGrowSpeed);

    }

    public void btnYes(View view) {
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        Intent intent = getIntent();
        GameParams.ballMoveSpeed = sbrSpeed.getProgress();
        GameParams.aiDifficult = sbrDifficult.getProgress();
        GameParams.ballGrowSpeed = sbrGrow.getProgress();
        setResult(1, intent);
        finish();
    }

    public void btnNo(View view) {
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        setResult(2, getIntent());
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        gameSounds.starMusic(GameSounds.CLICK);
        gameSounds.recycle();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            setResult(2, getIntent());
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}

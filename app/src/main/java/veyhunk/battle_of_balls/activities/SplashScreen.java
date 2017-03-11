package veyhunk.battle_of_balls.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import veyhunk.battle_of_balls.R;
import veyhunk.battle_of_balls.sounds.GameSounds;

/**
 *
 * @author Veyhunk
 * 更新日志
 *@version 3.2
 *新增了AI互吃；横屏锁定；
 *新增游戏本地文件存储
 *
 *@version 3.3
 *完善排行榜UI
 *完善积分体重UI
 *修复AI的0生命被吃
 *修复AI是否达到复活条件
 *
 *@version 3.4
 *完成游戏音效
 *完成游戏的回调
 *实现游戏最高分累积
 *json的保存读取与处理
 *内录游戏配音失败，一个晚上啊，声卡缺陷害死人，
 *反编译游戏成功，然而并没有什么卵用，都是图片
 *
 *@version 3.5
 *新增启动动画
 *修复了边界移动黑屏
 *完成游戏配音内录
 *完成全局配音
 *完善游戏文件架构@@@@@@@@
 *
 *@version 3.6
 *完善游戏文件架构@@@@@@@@
 *新增被吃提示框
 *新增游戏胜利和失败的提示框
 *新增吐球功能
 *
 *@version 3.7
 *修复了分身系统导致的无法复活
 *移动GameMusic到sound包
 *
 *@version 3.8
 *修复了分身系统分身数量不对
 *增加分身系统吃豆豆
 *增加分身与ai的互吃
 *完成游戏时间限制
 *
 *@version 3.9
 * Added doble-click exit
 * simplify code
 * TODO list
 * string merge
 * final value
 * class extract
 * improved bubble system
 * improved avatar system
 */

public class SplashScreen extends Activity {
	/**
	 * Called when the activity is first created.
	 */

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// 设置去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash_screen);


		final GameSounds gameSounds;
		gameSounds =new GameSounds(getApplication());
		gameSounds.starMusic(GameSounds.LOGO1);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(SplashScreen.this,
						SplashScreen2.class);
				SplashScreen.this.startActivity(mainIntent);
				SplashScreen.this.finish();
                gameSounds.recycle();
			}
		}, 1200); // 2900 for release

	}
}
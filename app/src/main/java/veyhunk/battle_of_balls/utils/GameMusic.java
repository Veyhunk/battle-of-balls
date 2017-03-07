package veyhunk.battle_of_balls.utils;

import java.io.IOException;

import veyhunk.battle_of_balls.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class GameMusic {
	// 音乐类型
	public static final int BGM = 0;
	public static final int LOGO1 = 1;
	public static final int LOGO2 = 2;
	public static final int CLICK = 3;
	public static final int INVITE = 4;
	public static final int AVATAR = 5;
	public static final int FUTION = 6;
	public static final int BUBBLE = 7;
	public static final int EAT_DEFAULT = 8;
	public static final int EAT_QTDS = 9;
	public static final int EAT_QSDD = 10;
	public static final int EAT_ZBZG = 11;
	private Context context;

	SoundPool soundPool;// 音效播放器
	MediaPlayer player;// 背景音乐播放器
	boolean isOpen;// 音乐开关量

	public GameMusic(Context context) {
		this.context=context;
		// TODO Auto-generated constructor stub
		isOpen = true;
		player = MediaPlayer.create(context, R.raw.bgm);
		player.setLooping(true);
		soundPool = new SoundPool(11, AudioManager.STREAM_MUSIC, 5);
		soundPool.load(context, R.raw.logo1, 1);
		soundPool.load(context, R.raw.logo2, 1);
		soundPool.load(context, R.raw.click, 1);
		soundPool.load(context, R.raw.invite, 1);
		soundPool.load(context, R.raw.avatar, 1);
		soundPool.load(context, R.raw.fusion, 1);
		soundPool.load(context, R.raw.bubble, 1);
		soundPool.load(context, R.raw.eat_default, 1);
		soundPool.load(context, R.raw.eat_qingtingdianshui, 1);
		soundPool.load(context, R.raw.eat_quanshuidingdong, 1);
		soundPool.load(context, R.raw.eat_zhenbeizouge, 1);
//		try {
//			Thread.sleep(1000);// 给予初始化音乐文件足够时间
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 开启音乐
	 * 
	 * @param id
	 *            指定id的音乐
	 */
	public void starMusic(int id) {
		if (isOpen) {
			switch (id) {
			case BGM:
				player.start();
				break;
			case LOGO1:
				soundPool.play(LOGO1, 1, 1, 0, 0, 1);
				break;
			case LOGO2:
				soundPool.play(LOGO2, 1, 1, 0, 0, 1);
				break;
			case CLICK:
				soundPool.play(CLICK, 1, 1, 0, 0, 1);
				break;
			case INVITE:
				soundPool.play(INVITE, 1, 1, 0, 0, 1);
				break;
			case AVATAR:
				soundPool.play(AVATAR, 1, 1, 0, 0, 1);
				break;
			case FUTION:
				soundPool.play(FUTION, 1, 1, 0, 0, 1);
				break;
			case BUBBLE:
				soundPool.play(BUBBLE, 1, 1, 0, 0, 1);
				break;
			case EAT_DEFAULT:
				soundPool.play(EAT_DEFAULT, 1, 1, 0, 0, 1);
				break;
			case EAT_QTDS:
				soundPool.play(EAT_QTDS, 1, 1, 0, 0, 1);
				break;
			case EAT_QSDD:
				soundPool.play(EAT_QSDD, 1, 1, 0, 0, 1);
				break;
			case EAT_ZBZG:
				soundPool.play(EAT_ZBZG, 1, 1, 0, 0, 1);
				break;
			}
		}
	}

	/**
	 * 停止音乐播放
	 */
	public void stopMusic() {
		player.stop();
		isOpen = false;
	}
	public void stopBGM() {
		player.stop();
		
	}	public void restarBGM() {
		player = MediaPlayer.create(context, R.raw.bgm);
		player.setLooping(true);
		player.start();
	}
	/**
	 * 设置音乐开启
	 */
	public void setMusicOpen() {
		isOpen = true;
		starMusic(BGM);
	}

	/**
	 * 音频回收
	 */
	public void recycle() {
		player.release();
		soundPool.release();
	}

}

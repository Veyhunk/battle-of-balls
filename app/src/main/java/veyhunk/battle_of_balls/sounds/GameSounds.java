package veyhunk.battle_of_balls.sounds;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import veyhunk.battle_of_balls.R;

/**
 * GameSounds
 */
public class GameSounds {

    // 音乐列表
    public static final int BGM = 0;
    public static final int LOGO1 = 1;
    public static final int LOGO2 = 2;
    public static final int CLICK = 3;
    public static final int INVITE = 4;
    public static final int AVATAR = 5;
    public static final int BE_EATEN = 6;
    public static final int BUBBLE = 7;
    public static final int EAT_DEFAULT = 8;
    public static final int EAT_1 = 9;
    public static final int EAT_2 = 10;
    public static final int EAT_3 = 11;
    private final Context context;
    private final SoundPool soundPool;// 音效播放器
    private MediaPlayer player;// 背景音乐播放器
    private boolean isOpen;// 音乐开关量

    public GameSounds(Context context) {
        this.context = context.getApplicationContext();
        player = MediaPlayer.create(context, R.raw.bgm);
        player.setLooping(true);

        soundPool = new SoundPool(11, AudioManager.STREAM_MUSIC, 5);
        // TODO: 09/March/2017  Api version
        soundPool.load(context, R.raw.logo1, 1);
        soundPool.load(context, R.raw.logo2, 1);
        soundPool.load(context, R.raw.click, 1);
        soundPool.load(context, R.raw.invite, 1);
        soundPool.load(context, R.raw.avatar, 1);
        soundPool.load(context, R.raw.be_eaten, 1);
        soundPool.load(context, R.raw.bubble, 1);
        soundPool.load(context, R.raw.eat_default, 1);
        soundPool.load(context, R.raw.eat_qingtingdianshui, 1);
        soundPool.load(context, R.raw.eat_quanshuidingdong, 1);
        soundPool.load(context, R.raw.eat_zhenbeizouge, 1);
        setMusicOpen();

//		try {
//			Thread.sleep(1000);// 给予初始化音乐文件足够时间
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
    }

    /**
     * 开启音乐
     *
     * @param id 指定id的音乐
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
                case BE_EATEN:
                    soundPool.play(BE_EATEN, 1, 1, 0, 0, 1);
                    break;
                case BUBBLE:
                    soundPool.play(BUBBLE, 1, 1, 0, 0, 1);
                    break;
                case EAT_DEFAULT:
                    soundPool.play(EAT_DEFAULT, 1, 1, 0, 0, 1);
                    break;
                case EAT_1:
                    soundPool.play(EAT_1, 1, 1, 0, 0, 1);
                    break;
                case EAT_2:
                    soundPool.play(EAT_2, 1, 1, 0, 0, 1);
                    break;
                case EAT_3:
                    soundPool.play(EAT_3, 1, 1, 0, 0, 1);
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

    }

    public void restartBGM() {
        player = MediaPlayer.create(context, R.raw.bgm);
        player.setLooping(true);
        player.start();
    }

    /**
     * 设置音乐开启
     */
    private void setMusicOpen() {
        isOpen = true;
    }

    /**
     * 音频回收
     */
    public void recycle() {
        player.reset();
        player.release();
        soundPool.release();
    }

}

package tool;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

import dali.flabbybird.R;

/**
 * Created by qinkangli on 2015/11/27.
 */
public class MusicUtils {

    /**
     * 音效相关
     */
    private SoundPool sp;
    private Map<Integer, Integer> map_music;
    private Context context;

    public MusicUtils(Context context){
        this.context = context;
    }

    public void initSoundpool() {
        sp = new SoundPool(3,// 同时播放的音效
                AudioManager.STREAM_MUSIC, 1);
        map_music = new HashMap<Integer, Integer>();
        map_music.put(1, sp.load(context, R.raw.jump, 1));
        map_music.put(2, sp.load(context, R.raw.dead, 1));
        map_music.put(3, sp.load(context, R.raw.gameover, 1));
        map_music.put(4, sp.load(context, R.raw.button, 1));
        map_music.put(5, sp.load(context, R.raw.back, 1));
        map_music.put(6, sp.load(context, R.raw.start, 1));
        map_music.put(7, sp.load(context, R.raw.rate, 1));
        map_music.put(8, sp.load(context, R.raw.coin, 1));
    }



    public void playSound(int sound, int number) {
        if (SharedPreferencesUtils.SOUND){
            AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);// 实例化
            float audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 音效最大值
            float audioCurrentVolum = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            float audioRatio = audioCurrentVolum / audioMaxVolum;
            sp.play(map_music.get(sound),
                    audioRatio,// 左声道音量
                    audioRatio,// 右声道音量
                    1, // 优先级
                    number,// 循环播放次数
                    1);// 回放速度，该值在0.5-2.0之间 1为正常速度
        }
    }
}

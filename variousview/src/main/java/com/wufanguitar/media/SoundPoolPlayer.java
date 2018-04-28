package com.wufanguitar.media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.RawRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: 吴凡
 * @Email: wufan01@sunlands.com
 * @Time: 2018/4/17  15:32
 * @Description:
 */

public class SoundPoolPlayer {
    private static final int DEFAULT_MAX_STREAMS = 1;
    private static final int DEFAULT_SOUND_QUALITY = 0;
    public final static int TYPE_MUSIC = AudioManager.STREAM_MUSIC;
    public final static int TYPE_ALARM = AudioManager.STREAM_ALARM;
    public final static int TYPE_RING = AudioManager.STREAM_RING;

    @IntDef({TYPE_MUSIC, TYPE_ALARM, TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SoundType {
    }

    private Context mContext;
    private SoundPool mSoundPool;
    private int mSoundLoadId;
    private int mSoundPlayId;
    private int mSoundResId;
    private long mDuration;
    private boolean isPlaying = false;
    private boolean isLoaded = false;
    private Handler mHandler;
    private long mStartTime;
    private long mEndTime;
    private long mTimeSinceStart = 0;
    private MediaPlayer.OnCompletionListener mMediaPlayerOnCompletionListener;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                mSoundPool.release();
                isPlaying = false;
                if (mMediaPlayerOnCompletionListener != null) {
                    mMediaPlayerOnCompletionListener.onCompletion(null);
                }
            }
        }
    };

    public SoundPoolPlayer(Context context, @SoundType int soundType) {
        this(context, DEFAULT_MAX_STREAMS, soundType);

    }

    public SoundPoolPlayer(Context context, int maxStreams, @SoundType int soundType) {
        this(context, maxStreams, soundType, DEFAULT_SOUND_QUALITY);
    }

    public SoundPoolPlayer(Context context, int maxStreams, @SoundType int soundType, int soundQuality) {
        this.mContext = context;
        initSoundPool(maxStreams, soundType, soundQuality);
    }

    private void initSoundPool(int maxStreams, int soundType, int soundQuality) {
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(maxStreams);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(soundType);
            builder.setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        } else {
            mSoundPool = new SoundPool(maxStreams, soundType, soundQuality);
        }
    }

    public SoundPoolPlayer load(@RawRes int rawResId) {
        this.mSoundResId = rawResId;
        this.mDuration = getSoundDuration(rawResId);
        this.mSoundLoadId = mSoundPool.load(mContext, rawResId, 0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoaded = true;
            }
        });
        return this;
    }

    private void loadAndPlay() {
        mDuration = getSoundDuration(mSoundResId);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoaded = true;
                onPlay();
            }
        });
    }

    public SoundPoolPlayer setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.mMediaPlayerOnCompletionListener = listener;
        return this;
    }


    private void onPlay() {
        if (isLoaded && !isPlaying) {
            if (mTimeSinceStart == 0) {
                mSoundPlayId = mSoundPool.play(mSoundLoadId, 1f, 1f, 1, 0, 1f);
            } else {
                mSoundPool.resume(mSoundPlayId);
            }
            mStartTime = System.currentTimeMillis();
            mHandler = new Handler();
            mHandler.postDelayed(mRunnable, mDuration - mTimeSinceStart);
            isPlaying = true;
        }
    }

    private long getSoundDuration(int rawId) {
        MediaPlayer player = MediaPlayer.create(mContext, rawId);
        int duration = player.getDuration();
        return duration;
    }

    public void play() {
        if (!isLoaded) {
            loadAndPlay();
        } else {
            onPlay();
        }
    }

    public void pause() {
        if (mSoundPlayId > 0) {
            mEndTime = System.currentTimeMillis();
            mTimeSinceStart += mEndTime - mStartTime;
            mSoundPool.pause(mSoundPlayId);
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            isPlaying = false;
        }
    }

    public void stop() {
        if (mSoundPlayId > 0) {
            mTimeSinceStart = 0;
            mSoundPool.stop(mSoundPlayId);
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            isPlaying = false;
        }
    }
}

package com.bytedance.videoplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bytedance.videoplayer.Player.RawDataSourceProvider;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VedioPlayerActivity extends AppCompatActivity {

    private SeekBar seekBar;
   // private IjkMediaPlayer ijkMediaPlayer;
    private IjkMediaPlayer ijkMediaPlayer;
    private SurfaceView surfaceView;
    private Button playBtn;
    private Button pauseBtn;
    private static final int UPDATE_SEEKBAR=1;
    private static final String TAG = VedioPlayerActivity.class.getSimpleName();

    private Long progressContext = 0l;
    private boolean isPlaying = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_player);

        createPlayer();

        if(!loadVideo())
            loadVideo(R.raw.yuminhong);

        playBtn = findViewById(R.id.play_button);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ijkMediaPlayer.start();
            }
        });
        pauseBtn = findViewById(R.id.pause_button);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ijkMediaPlayer.pause();
            }
        });

        ijkMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                changeVideoSize();
            }
        });
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ijkMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateHandler.sendMessageDelayed(Message.obtain(updateHandler,UPDATE_SEEKBAR),100);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        progressContext = savedInstanceState.getLong("progress");
        isPlaying = savedInstanceState.getBoolean("isPlaying");
        Log.d(TAG, "onRestoreInstanceState() called with: newpos = [" + progressContext+" ], newstate =[ "+ isPlaying  + "]");
        super.onRestoreInstanceState(savedInstanceState);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("progress",ijkMediaPlayer.getCurrentPosition());
        outState.putBoolean("isPlaying",ijkMediaPlayer.isPlaying());
        Log.d(TAG, "onSaveInstanceState() called with: oldpos = [" + ijkMediaPlayer.getCurrentPosition() + "], oldstate = [" + ijkMediaPlayer.isPlaying() + "]");
        super.onSaveInstanceState(outState);
    }

    private void createPlayer() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.setDisplay(null);
            ijkMediaPlayer.release();
        }

        ijkMediaPlayer = new IjkMediaPlayer();
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);


        //setSurfaceView
        surfaceView = findViewById(R.id.ijkPlayer);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                ijkMediaPlayer.setDisplay(holder);
                ijkMediaPlayer.prepareAsync();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


        ijkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkMediaPlayer.seekTo(progressContext);
                Log.d(TAG, "onPrepared() called with: PlayerState = [" + iMediaPlayer.isPlaying()+ "] , savedState = ["+isPlaying+"]");
                if(isPlaying){
                    ijkMediaPlayer.start();
                }else {
                    ijkMediaPlayer.pause();
                }
                seekBar.setMax((int)iMediaPlayer.getDuration());
                seekBar.setProgress((int)iMediaPlayer.getCurrentPosition());
            }
        });

        //((IjkMediaPlayer) mMediaPlayer).setSpeed(2f);

    }

    private void loadVideo(int id){
        AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(id);
        RawDataSourceProvider provider = new RawDataSourceProvider(fileDescriptor);
        ijkMediaPlayer.setDataSource(provider);
    }

    private boolean loadVideo(){
        Uri url=getIntent().getData();
        try {
            if(url!=null){
                ijkMediaPlayer.setDataSource(this,url);
                Log.d(TAG, "loadVideo() called successfully");
                return true;
            }else {
                Log.d(TAG, "loadVideo() failed to call");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void changeVideoSize() {
        int videoWidth = ijkMediaPlayer.getVideoWidth();
        int videoHeight = ijkMediaPlayer.getVideoHeight();

        int surfaceWidth = surfaceView.getWidth();
        int surfaceHeight = surfaceView.getHeight();
        Log.d(TAG, "changeVideoSize() called width="+videoWidth+ ",height="+videoHeight);

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。

        ConstraintLayout.LayoutParams params = new Constraints.LayoutParams(videoWidth, videoHeight);
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.horizontalBias = 0.5f;
        params.verticalBias = 0.4f;

        surfaceView.setLayoutParams(params);
        Log.d(TAG, "changeVideoSize() called width="+videoWidth+ ",height="+videoHeight);
    }


    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_SEEKBAR:
                    seekBar.setMax((int)ijkMediaPlayer.getDuration());
                    seekBar.setProgress((int)ijkMediaPlayer.getCurrentPosition());
                    sendMessageDelayed(Message.obtain(updateHandler,UPDATE_SEEKBAR),100);
            }
        }
    };
}

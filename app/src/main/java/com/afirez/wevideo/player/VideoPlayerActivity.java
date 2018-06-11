package com.afirez.wevideo.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afirez.wevideo.R;
import com.afirez.wevideo.channel.model.Video;
import com.afirez.wevideo.channel.model.VideoWithType;
import com.afirez.wevideo.common.BaseActivity;
import com.afirez.wevideo.common.utils.DateUtils;
import com.afirez.wevideo.common.utils.SystemUtils;
import com.afirez.wevideo.common.widget.gesture.GestureHelper;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class VideoPlayerActivity extends BaseActivity {

    private static final int CHECK_TIME = 1;
    private static final int CHECK_BATTERY = 2;
    private static final int CHECK_PROGRESS = 3;
    private static final int AUTO_HIDE_TIME = 10000;
    private static final int AFTER_DRAGGLE_HIDE_TIME = 3000;

    private String url;
    private String title;
    private int steamType;
    private int currentPosition;
    private Video video;
    private AudioManager audioManager;

    public static Intent newIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        return intent;
    }

    public static void start(Activity activity, String url, String title) {
        Intent intent = newIntent(activity, url, title);
        activity.startActivity(intent);
    }

    private int maxVolume;
    private int currentVolume;
    private int maxBrightness = 255;
    private int currentBrightness;

    private ConstraintLayout clRoot;
    private ConstraintLayout topContainer;
    private ConstraintLayout bottomContainer;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivBattery;
    private TextView tvTime;
    private CheckBox cbPlayOrPause;
    private ImageView ivNext;
    private SeekBar sbProgress;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private TextView tvBitSteam;
    private TextView tvHorizontal;
    private TextView tvVertical;
    private ProgressBar pbLoading;
    private ConstraintLayout clLoading;
    private TextView tvLoading;
    private ImageView ivPlay;
    private IjkVideoView vvVideo;
    private boolean isPanelShowing;
    private boolean isDragging;

    @Override
    protected int layoutId() {
        return R.layout.wv_video_activity_player;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        steamType = intent.getIntExtra("type", 0);
        currentPosition = intent.getIntExtra("currentPosition", 0);
        video = intent.getParcelableExtra("video");

        initAudio();
        initBrightness();
        initBattery();
        initPlayer();
        initMediaController();
        initGesture();
    }

    private BroadcastReceiver receiver;

    private void initBattery() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batteryLevel = intent.getIntExtra("level", 0);
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void initPlayer() {
        vvVideo = (IjkVideoView) findViewById(R.id.player_vv_video);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        vvVideo.setVideoURI(Uri.parse(url));
        vvVideo.setOnPreparedListener(onPreparedListener);
        vvVideo.setOnInfoListener(onInfoListener);
    }

    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            switch (what) {
                case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    clLoading.setVisibility(View.VISIBLE);
                    break;
                case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    clLoading.setVisibility(View.GONE);
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            vvVideo.start();
            if (currentPosition > 0 && currentPosition < vvVideo.getDuration()) {
                vvVideo.seekTo(currentPosition);
            }
        }
    };

    private void initMediaController() {
        clRoot = (ConstraintLayout) findViewById(R.id.player_cl_root);
        clLoading = (ConstraintLayout) findViewById(R.id.player_cl_loading);
        pbLoading = (ProgressBar) findViewById(R.id.player_pb_loading);
        tvLoading = (TextView) findViewById(R.id.player_tv_loading);
        tvLoading.setText("正在加载中...");
        ivPlay = (ImageView) findViewById(R.id.player_iv_center_pause);
        topContainer = (ConstraintLayout) findViewById(R.id.player_cl_top_container);
        ivBack = (ImageView) findViewById(R.id.player_iv_back);
        tvTitle = (TextView) findViewById(R.id.player_tv_title);
        ivBattery = (ImageView) findViewById(R.id.player_iv_battery);
        tvTime = (TextView) findViewById(R.id.player_tv_time);
        bottomContainer = (ConstraintLayout) findViewById(R.id.player_cl_bottom_container);
        cbPlayOrPause = (CheckBox) findViewById(R.id.player_cb_play_or_pause);
        ivNext = (ImageView) findViewById(R.id.player_iv_next);
        sbProgress = (SeekBar) findViewById(R.id.player_sb_progress);
        sbProgress.setMax(1000);
        tvCurrentTime = (TextView) findViewById(R.id.player_tv_current_time);
        tvTotalTime = (TextView) findViewById(R.id.player_tv_total_time);
        tvBitSteam = (TextView) findViewById(R.id.player_tv_bit_steam);
        tvHorizontal = (TextView) findViewById(R.id.player_tv_horizontal_gesture);
        tvVertical = (TextView) findViewById(R.id.player_tv_vertical_gesture);
        sbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvVideo.start();
                showPlayState(true);
            }
        });
        cbPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean playing = vvVideo.isPlaying();
                if (playing) {
                    vvVideo.pause();
                } else {
                    vvVideo.start();
                }
                showPlayState(!playing);
            }
        });
        toggleTopAndBottomLayout();
    }

    private void toggleTopAndBottomLayout() {
        if (isPanelShowing) {
            hideTopAndBottomLayout();
        } else {
            showTopAndBottomLayout();
            //先显示,没有任何操作,就5s后隐藏
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTopAndBottomLayout();
                }
            }, AUTO_HIDE_TIME);
        }
    }

    private void showTopAndBottomLayout() {
        isPanelShowing = true;
        topContainer.setVisibility(View.VISIBLE);
        bottomContainer.setVisibility(View.VISIBLE);
        updateProgress();
        if (handler != null) {
            handler.removeMessages(CHECK_TIME);
            handler.removeMessages(CHECK_BATTERY);
            handler.removeMessages(CHECK_PROGRESS);
            handler.sendEmptyMessage(CHECK_TIME);
            handler.sendEmptyMessage(CHECK_BATTERY);
            handler.sendEmptyMessage(CHECK_PROGRESS);
        }
        switch (steamType) {
            case VideoWithType.TYPE_SUPER:
                tvBitSteam.setText(R.string.stream_super);
                break;
            case VideoWithType.TYPE_NORMAL:
                tvBitSteam.setText(R.string.stream_normal);
                break;
            case VideoWithType.TYPE_HIGH:
                tvBitSteam.setText(R.string.stream_high);
                break;
        }
    }

    private void updateProgress() {
        if (vvVideo == null) {
            return;
        }
        int currentPosition = vvVideo.getCurrentPosition();//当前的视频位置
        int duration = vvVideo.getDuration();//视频时长
        if (sbProgress != null) {
            if (duration > 0) {
                //转成long型,避免溢出
                long pos = currentPosition * 1000L / duration;
                sbProgress.setProgress((int) pos);
            }
            int perent = vvVideo.getBufferPercentage();//已经缓冲的进度
            sbProgress.setSecondaryProgress(perent);//设置缓冲进度
            tvCurrentTime.setText(stringForTime(currentPosition));
            tvTotalTime.setText(stringForTime(duration));
        }
    }

    private int batteryLevel;

    private void setCurrentBattery() {
        if (0 < batteryLevel && batteryLevel <= 10) {
            ivBattery.setBackgroundResource(R.drawable.player_ic_battery_10);
        } else if (10 < batteryLevel && batteryLevel <= 20) {
            ivBattery.setBackgroundResource(R.drawable.player_ic_battery_20);
        } else if (20 < batteryLevel && batteryLevel <= 50) {
            ivBattery.setBackgroundResource(R.drawable.player_ic_battery_50);
        } else if (50 < batteryLevel && batteryLevel <= 80) {
            ivBattery.setBackgroundResource(R.drawable.player_ic_battery_80);
        } else if (80 < batteryLevel && batteryLevel <= 100) {
            ivBattery.setBackgroundResource(R.drawable.player_ic_battery_100);
        }
    }

    private void hideTopAndBottomLayout() {
        if (isDragging) {
            return;
        }
        isPanelShowing = false;
        topContainer.setVisibility(View.GONE);
        bottomContainer.setVisibility(View.GONE);

    }

    private void showPlayState(boolean isPlaying) {
        ivPlay.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        cbPlayOrPause.invalidate();
        cbPlayOrPause.setChecked(isPlaying);
        cbPlayOrPause.refreshDrawableState();
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            int duration = vvVideo.getDuration();
            long newPosition = (duration * progress) / 1000L;
            tvCurrentTime.setText(stringForTime((int) newPosition));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDragging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isDragging = false;
            int progress = seekBar.getProgress();
            int duration = vvVideo.getDuration();
            long newPosition = (duration * progress) / 1000L;
            tvCurrentTime.setText(stringForTime((int) newPosition));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTopAndBottomLayout();
                }
            }, AFTER_DRAGGLE_HIDE_TIME);
        }
    };

    private class MyGestureHelper extends GestureHelper {

        public MyGestureHelper(View view) {
            super(view);
        }

        @Override
        public void onScroll(int scrollMode, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            switch (scrollMode) {
                case ScrollMode.HORIZONTAL:
                    tvHorizontal.setVisibility(View.VISIBLE);
                    int duration = vvVideo.getDuration();
                    int width = getResources().getDisplayMetrics().widthPixels;
                    int newPosition = (int) ((e2.getX() - e1.getX()) * duration / width + vvVideo.getCurrentPosition());
                    if (newPosition < 0) {
                        newPosition = 0;
                    } else if (newPosition > duration) {
                        newPosition = duration;
                    }
                    VideoPlayerActivity.this.currentPosition = newPosition;
                    updateHorizontalText(newPosition);
                    break;
                case ScrollMode.LEFT_VERTICAL:
                    tvVertical.setVisibility(View.VISIBLE);
                    setCompoundDrawable(tvVertical, R.drawable.player_ic_brightness);
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int offset = (int) (maxBrightness * (e1.getY() - e2.getY()) / height);
                    int newBrightness = offset + SystemUtils.getBrightness(VideoPlayerActivity.this);
                    if (newBrightness < 0) {
                        newBrightness = 0;
                    } else if (newBrightness > 255) {
                        newBrightness = 255;
                    }
                    currentBrightness = newBrightness;
                    SystemUtils.setBrightness(VideoPlayerActivity.this, newBrightness);
                    SystemUtils.setDefaultBrightness(VideoPlayerActivity.this, newBrightness);
                    updateVerticalText(newBrightness, maxBrightness);
                    break;
                case ScrollMode.RIGHT_VERTICAL:
                    tvVertical.setVisibility(View.VISIBLE);
                    int height1 = getResources().getDisplayMetrics().heightPixels;
                    int offset1 = (int) (maxVolume * (e1.getY() - e2.getY()) / height1);
                    int newVolume = offset1 + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (newVolume < 0) {
                        newVolume = 0;
                    } else if (newVolume > maxVolume) {
                        newVolume = maxVolume;
                    }
                    if (newVolume > 0) {
                        setCompoundDrawable(tvVertical, R.drawable.player_ic_volume_normal);
                    } else {
                        setCompoundDrawable(tvVertical, R.drawable.player_ic_volume_mute);
                    }
                    currentVolume = newVolume;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
                    updateVerticalText(newVolume, maxVolume);
                    break;
            }
        }

        @Override
        public void onScrollModeHorizontalComplete(MotionEvent event) {
            vvVideo.seekTo(currentPosition);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            toggleTopAndBottomLayout();
            return true;
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                tvVertical.setVisibility(View.GONE);
                tvHorizontal.setVisibility(View.GONE);
            }
        }
    }


    private void updateHorizontalText(long position) {
        String text = stringForTime((int) position) + "/" + stringForTime(vvVideo.getDuration());
        tvHorizontal.setText(text);
    }

    private void updateVerticalText(int current, int total) {
        NumberFormat formater = NumberFormat.getPercentInstance();
        formater.setMaximumFractionDigits(0);//设置整数部分允许最大小数位 66.5%->66%
        String percent = formater.format((double) (current) / (double) total);
        tvVertical.setText(percent);
    }

    private void setCompoundDrawable(TextView textView, int drawableId) {
        Drawable drawable = textView.getResources().getDrawable(drawableId);
        //这四个参数表示把drawable绘制在矩形区域
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //设置图片在文字的上方
        //The Drawables must already have had drawable.setBounds called.
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    private MyGestureHelper gestureHelper;

    private void initGesture() {
        gestureHelper = new MyGestureHelper(clRoot);
    }

    private void initBrightness() {
        currentBrightness = SystemUtils.getDefaultBrightness(this);
        if (currentBrightness == -1) {
            currentBrightness = SystemUtils.getBrightness(this);
        }
    }

    private void initAudio() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void initData() {

    }

    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_TIME:
                    tvTime.setText(DateUtils.getCurrentTime());
                    break;
                case CHECK_BATTERY:
                    setCurrentBattery();
                    break;
                case CHECK_PROGRESS:
                    long duration = vvVideo.getDuration();
                    long nowDuration = (sbProgress.getProgress() * duration) / 1000L;
                    tvCurrentTime.setText(stringForTime((int) nowDuration));
                    break;
            }
            return true;
        }
    });

    private StringBuilder formatterBuilder = new StringBuilder();

    private Formatter formatter = new Formatter(formatterBuilder, Locale.getDefault());

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60; //换成秒
        int minutes = (totalSeconds / 60) % 60;
        int hours = (totalSeconds / 3600);
        formatterBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (vvVideo != null) {
            vvVideo.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}

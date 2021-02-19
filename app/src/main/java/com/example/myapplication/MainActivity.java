package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dto.CurrentMusic;
import com.example.dto.MusicInfo;
import com.example.service.MusicService;
import com.example.state.stop;
import com.example.thread.seekBarAutoFlow;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MusicService musicService = new MusicService();
    private SeekBar seekBar;
    private TextView textView;
    private boolean isLoop;
    private ListView list;
    Button play ;

    public SeekBar getSeekBar() {
        return seekBar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置当前视图
        setContentView(R.layout.activity_main);
        //初始化滚动条变量
        seekBar = findViewById(R.id.sb);
        //初始化文本框变量
        textView = findViewById(R.id.playInfo);
        //初始化列表数据
        list = this.findViewById(R.id.musicList);
        play = findViewById(R.id.play);
        showMusicList();
        registBtnNext();
        registBtnLast();
        registBthStop();
        registBthPlay();
        registDragSeekBar();
        startSeekBarFlow();
        registListClick();
    }

    private void registListClick() {
        list.setOnItemClickListener((parent, view, position, id) -> {
            MusicInfo m = (MusicInfo) parent.getItemAtPosition(position);
            musicService.play(position,play);
        });
    }

    private void showMusicList() {
        List<MusicInfo> arr = musicService.getMusics();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item_1, arr);
        list.setAdapter(adapter);
    }

    private void registBtnNext() {
        Button btn = findViewById(R.id.next);
        btn.setOnClickListener((view) -> {
            musicService.next(play);
        });

    }

    private void registBtnLast() {
        Button btn = findViewById(R.id.last);
        btn.setOnClickListener((view) -> {
            musicService.last(play);
        });

    }

    private void registBthStop() {
        Button btn = findViewById(R.id.stop);
        btn.setOnClickListener((view) -> {
            musicService.setCurrentState(new stop());
            musicService.getCurrentState().Do(musicService, play);
        });
    }

    private void registBthPlay() {

        play.setOnClickListener((view) -> {
            musicService.setCurrentState(musicService.getCurrentState().getNextState());
            musicService.getCurrentState().Do(musicService, play);
        });

    }


    private void registDragSeekBar() {
        SeekBar seekBar = findViewById(R.id.sb);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //停止拖动，这才是想要的
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //得到进度条当前的位置
                int progress = seekBar.getProgress();
                int musicMaxSec = musicService.getCurrentMusic().getMaxSec(); //得到该首歌曲最长秒数
                int seekBarMax = seekBar.getMax();
                musicService.getMediaPlayer().seekTo(musicMaxSec * progress / seekBarMax);//跳到该曲该秒
            }
        });
    }

    //开始使滚动条自动流动
    private void startSeekBarFlow() {
        //更新UI程序
        Handler headler = new Handler((msg -> {
            if (!musicService.getMediaPlayer().isPlaying()) {
                return true;
            }
            int mMax = musicService.getMediaPlayer().getDuration();//最大秒数
            CurrentMusic data = (CurrentMusic) msg.obj;
            //seekBar进度条的百分比
            int progress = data.getCurrentSec() * seekBar.getMax() / data.getMaxSec();
            seekBar.setProgress(progress);
            textView.setText(musicService.getPlayInfo(data));
            if (!isLoop) {
                Looper.loop();
                isLoop = true;
            }
            return true;
        }));
        //开启更新子线程
        Thread thread = new Thread(new seekBarAutoFlow(headler, seekBar, musicService));
        thread.start();
    }


}

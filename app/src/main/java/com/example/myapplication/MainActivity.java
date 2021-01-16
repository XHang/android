package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dto.MusicInfo;
import com.example.service.MusicService;
import com.example.service.logService;
import com.example.state.stop;
import com.example.thread.seekBarAutoFlow;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private MusicService musicService = new MusicService();
    private SeekBar seekBar;
    private TextView textView;
    private boolean isLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //设置当前视图
        setContentView(R.layout.activity_main);
        //初始化滚动条变量
        seekBar = findViewById(R.id.sb);
        //初始化文本框变量
        textView = findViewById(R.id.playInfo);
        showMusicList();
        registBtnNext();
        registBtnLast();
        registBthStop();
        registBthPlay();
        registDragSeekBar();
        startSeekBarFlow();
    }

    private void showMusicList() {
        String[] arr = new String[musicService.getMusicPaths().size()];
        for (int i = 0; i < musicService.getMusicPaths().size(); i++) {
            String path = musicService.getMusicPaths().get(i);
            File file = new File(path);
            arr[i] = file.getName();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item_1, arr);
        ListView view = this.findViewById(R.id.musicList);
        view.setAdapter(adapter);
    }

    private void registBtnNext() {
        Button btn = findViewById(R.id.next);
        btn.setOnClickListener((view) -> {
            musicService.next(findViewById(R.id.play));
        });

    }

    private void registBtnLast() {
        Button btn = findViewById(R.id.last);
        btn.setOnClickListener((view) -> {
            musicService.last(findViewById(R.id.play));
        });

    }

    private void registBthStop() {
        Button btn = findViewById(R.id.stop);
        btn.setOnClickListener((view) -> {
            musicService.setCurrentState(new stop());
            musicService.getCurrentState().Do(musicService,findViewById(R.id.play));
        });
    }

    private void registBthPlay() {
        Button btn = findViewById(R.id.play);

        btn.setOnClickListener((view) -> {
            musicService.setCurrentState(musicService.getCurrentState().getNextState());
            musicService.getCurrentState().Do(musicService,btn);
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
                int musicMaxSec = musicService.mediaPlayer.getDuration(); //得到该首歌曲最长秒数
                int seekBarMax = seekBar.getMax();
                musicService.mediaPlayer.seekTo(musicMaxSec * progress / seekBarMax);//跳到该曲该秒
            }
        });
    }

    //开始使滚动条自动流动
    private void startSeekBarFlow() {
        //更新UI程序
        Handler headler = new Handler((msg -> {
            int mMax = musicService.mediaPlayer.getDuration();//最大秒数
            MusicInfo data = (MusicInfo) msg.obj;
            //seekBar进度条的百分比
            int progress = data.getCurrentSec() * data.getSeekbarMaxSec() / data.getMusicMaxSec();
            seekBar.setProgress(progress);
            textView.setText(musicService.getPlayInfo(data.getCurrentSec(), data.getMusicMaxSec(), data.getMusicName()));
            if (!isLoop){
                Looper.loop();
                isLoop = true;
            }
            return true;
        }));
        //开启更新子线程
        Thread thread = new Thread(new seekBarAutoFlow(headler,seekBar,musicService));
        thread.start();
    }





}

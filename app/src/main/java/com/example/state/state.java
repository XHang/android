package com.example.state;

import android.widget.Button;

import com.example.service.MusicService;

public interface state {
    //播放状态改变
    void Do(MusicService musicService, Button playBtn);

    //得到状态的中文名
    int getCurrentName();

    //得到当前状态的下一个状态
    state getNextState();
}

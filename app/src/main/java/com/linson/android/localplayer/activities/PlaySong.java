package com.linson.android.localplayer.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linson.android.localplayer.R;


//!todo 1.显示菜单，并测试功能。2.显示底部菜单。3.进行底部菜单的单元测试.4.启动主页时，就启动services。
//!todo services 建立播放，发送基本信息。定时发送广播功能。并进行测试。
//!todo 4.连接services，并调用方法 播放歌曲。 5.services 发送定时广播,
public class PlaySong extends Fragment
{
    public static final String argumentname_ls="list_song";
    private app.model.V_List_Song mListSong;

    public PlaySong()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_play_song, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mListSong=(app.model.V_List_Song)getArguments().getSerializable(argumentname_ls);


        //连接services，并调用播放功能
    }
}
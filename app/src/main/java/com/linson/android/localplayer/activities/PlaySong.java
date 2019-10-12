package com.linson.android.localplayer.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.linson.android.localplayer.R;

import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.customUI.LSCircleImage;


//!todo findcontrol 还是可以试下，放入到扩展类中。
//!todo 1.显示菜单，并测试功能。2.显示底部菜单。3.进行底部菜单的单元测试.4.启动主页时，就启动services。
//!todo services 先建立一个测试方法，并测试连接。之后 建立播放，发送基本信息,定时发送广播功能。并进行测试。
//!todo 4.连接services，并调用方法 播放歌曲。 5.services 发送定时广播,
public class PlaySong extends BaseFragment implements View.OnClickListener
{
    private ConstraintLayout mContent;
    private ConstraintLayout mBottonMenu;
    private LSCircleImage mBtnPre;
    private LSCircleImage mBtnPlay;
    private LSCircleImage mBtnNext;

    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mContent = (ConstraintLayout) getMaster().findViewById(R.id.content);
        mBottonMenu = (ConstraintLayout) getMaster().findViewById(R.id.botton_menu);
        mBtnPre = (LSCircleImage) getMaster().findViewById(R.id.btn_pre);
        mBtnPlay = (LSCircleImage) getMaster().findViewById(R.id.btn_play);
        mBtnNext = (LSCircleImage) getMaster().findViewById(R.id.btn_next);

        //set event handler
        mBtnPre.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_pre:
            {
                pre();
                break;
            }
            case R.id.btn_play:
            {
                play();
                break;
            }
            case R.id.btn_next:
            {
                next();
                break;
            }
            default:
            {
                break;
            }
        }
    }
    //endregion

    //region other member variable
    //endregion


    public static final String argumentname_ls = "list_song";


    private app.model.V_List_Song mListSong;
    private app.bll.V_List_Song mV_list_song_bll;


    public PlaySong()
    {
        mV_list_song_bll = new app.bll.V_List_Song(getContext());
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
        findControls();
        mListSong = (app.model.V_List_Song) getArguments().getSerializable(argumentname_ls);
        getMaster().setupToolbarMenu(mV_list_song_bll.getMenuPlayerTitle(), new MenuClickHandler());

        //连接services，并调用播放功能
    }


    private void pre()
    {

    }

    private void play()
    {

    }

    private void next()
    {

    }


    //region not static class: extend for top class
    public class MenuClickHandler implements Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            LSLog.Log_INFO(menuItem.getTitle().toString());
            return false;
        }
    }
    //endregion
}
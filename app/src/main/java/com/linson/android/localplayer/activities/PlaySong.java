package com.linson.android.localplayer.activities;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.IpPrefix;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.linson.android.localplayer.AIDL.IPlayer;
import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.appHelper;

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
    public static final String argumentLsid = "ls_id";


    private int mlsid=-1;
    private final app.bll.V_List_Song mV_list_song_bll=new app.bll.V_List_Song(MainActivity.appContext);
    private MyConnection mMyConnection=new MyConnection();
    //endregion





    public PlaySong()
    {
        //连接services,对应于destroy的释放。连接放到构造函数中。
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
        requireActivity().unbindService(mMyConnection);
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
        mlsid = getArguments().getInt(argumentLsid);
        LSLog.Log_INFO(String.format ("lsid:%d",mlsid));
        getMaster().setupToolbarMenu(mV_list_song_bll.getMenuPlayerTitle(), new MenuClickHandler());

        //虽然onActivityCreated，每次回退都会调用。但是bind如果连接存在是不会连2此的。
        requireActivity().bindService(appHelper.getServiceIntent(), mMyConnection, Context.BIND_AUTO_CREATE);

    }


    private void pre()
    {

    }

    private void play()
    {
        if(mMyConnection.mPlayerProxy!=null)
        {
            try
            {
                mMyConnection.mPlayerProxy.playSong(4);
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
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

    public class MyConnection implements ServiceConnection
    {
        IPlayer mPlayerProxy;
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mPlayerProxy=(IPlayer.Stub.asInterface(service));//必须用它提供的转义方法，它有一个是否是远程服务的区别。
            if(mPlayerProxy==null)
            {
                LSLog.Log_INFO("error get null services");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    }
    //endregion


}
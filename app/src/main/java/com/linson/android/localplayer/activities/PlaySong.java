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

import java.util.ArrayList;
import java.util.List;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.customUI.LSCircleImage;


//!todo findcontrol 还是可以试下，放入到扩展类中。
//!todo 逻辑ok。开始大框架。0.status class pracable,1. player class 2.init ,set,play ,pre...getstatusinfo. 3.media player.
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
    public static final String argumentLsid = "lid";


    private int mlsid=-1;
    private int mIndex=-1;
    private final app.bll.V_List_Song mV_list_song_bll=new app.bll.V_List_Song(MainActivity.appContext);
    private MyConnection mMyConnection=new MyConnection();
    private List<app.model.V_List_Song> mV_list_songs=new ArrayList<>();

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
        initMemberVariable();
        getMaster().setupToolbarMenu(mV_list_song_bll.getMenuPlayerTitle(), new MenuClickHandler());

        //虽然onActivityCreated，每次回退都会调用。但是bind如果连接存在是不会连2此的。
        requireActivity().bindService(appHelper.getServiceIntent(), mMyConnection, Context.BIND_AUTO_CREATE);


    }

    private void initMemberVariable()
    {
        mlsid = getArguments().getInt(argumentLsid);
        mV_list_songs=mV_list_song_bll.getModelByLid(mlsid);
    }


    private void pre()
    {
        if(mMyConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=mMyConnection.mPlayerProxy.pre();
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }

    private void play()
    {
        if(mMyConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=mMyConnection.mPlayerProxy.playSong(mIndex);
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }

    private void next()
    {
        if(mMyConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=mMyConnection.mPlayerProxy.next();
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }


    //region not static class: extend for top class
    public class MenuClickHandler implements Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if(menuItem.getTitle().toString()==V_List_Song.menu_IncressVolume)
            {
                LSLog.Log_INFO("popup volume window!");
            }
            else if(menuItem.getTitle().toString()==V_List_Song.menu_PlayerMode)
            {
                if(mMyConnection.mPlayerProxy!=null)
                {
                    try
                    {
                        int res=mMyConnection.mPlayerProxy.changemode();
                        //!todo get status from services to local. and display in ui.
                    }
                    catch (Exception e)
                    {
                        LSLog.Log_Exception(e);
                    }
                }
            }
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
            else
            {
                try
                {
                    mPlayerProxy.setAllSongs(mV_list_songs);
                    mPlayerProxy.playSong(mIndex);
                } catch (Exception e)
                {
                    LSLog.Log_Exception(e);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    }
    //endregion


}
package com.linson.android.localplayer.activities;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.linson.android.localplayer.AIDL.IPlayer;
import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Dialog.Dialog_Volume;
import com.linson.android.localplayer.appHelper;

import java.util.ArrayList;
import java.util.List;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSSystemServices;
import app.lslibrary.customUI.LSCircleImage;
import app.model.PlayerBaseInfo;


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
    public static final String argumentindex="index";


    private int mlsid=-1;
    private int mIndex=-1;
    private MyConnection mMyConnection=new MyConnection();
    private List<app.model.V_List_Song> mV_list_songs=new ArrayList<>();
    private app.model.PlayerBaseInfo mBaseInfo=new PlayerBaseInfo();

    //endregion

    public PlaySong()
    {
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mMyConnection!=null && mMyConnection.mPlayerProxy!=null)
        {
            try
            {
                mMyConnection.mPlayerProxy.ondisconnected();
            } catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
        requireActivity().unbindService(mMyConnection);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_play_song, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        findControls();
        initMemberVariable();
        getMaster().setupToolbarMenu(app.bll.V_List_Song.getMenuPlayerTitle(), new MenuClickHandler());

        //虽然onActivityCreated，每次回退都会调用。但是bind如果连接存在是不会连2此的。
        requireActivity().bindService(appHelper.getServiceIntent(), mMyConnection, Context.BIND_AUTO_CREATE);
    }


    @SuppressLint("DefaultLocale")
    private void initMemberVariable()
    {
        if(getArguments()!=null)
        {
            mlsid = getArguments().getInt(argumentLsid);
            mIndex = getArguments().getInt(argumentindex);
        }
        else
        {
            mlsid=-1;
            mIndex=-1;
        }
        mV_list_songs=app.bll.V_List_Song.getModelByLid(mlsid);
        LSLog.Log_INFO(String.format("init playsong. id:%d,index:%d",mlsid,mIndex));
    }


    private void pre()
    {
        if(mMyConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=mMyConnection.mPlayerProxy.pre();
                mBaseInfo=mMyConnection.mPlayerProxy.getBaseInfo();
                UpdateUi_mode(mBaseInfo);
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
                int res=mMyConnection.mPlayerProxy.playOrPause();
                //直接重新获得数据，这种非网络的数据，如硬盘，数据库等，如果直接获得更简洁那么不需要优化这点效率，除非大数据和非常频繁。
                mBaseInfo=mMyConnection.mPlayerProxy.getBaseInfo();
                UpdateUi_mode(mBaseInfo);
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
                mBaseInfo=mMyConnection.mPlayerProxy.getBaseInfo();
                UpdateUi_mode(mBaseInfo);
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }


    private void UpdateUi_mode(@NonNull app.model.PlayerBaseInfo baseInfo)
    {
        getMaster().changeMenuTitel(1, baseInfo.getModeName());

        boolean IsPlaying=false;
        if(baseInfo.status==PlayerBaseInfo.status_playing)
        {
            IsPlaying=true;
        }

        String imgpath=mBtnPlay.getImage()==R.drawable.video?"play":"pause";
        LSLog.Log_INFO("server mode IsPlaying:"+IsPlaying+". img:"+imgpath+"."+baseInfo.displayStatus());
        if(IsPlaying==true && mBtnPlay.getImage()==R.drawable.video)
        {
            mBtnPlay.setImage(R.drawable.pause);
        }
        else if(IsPlaying==false && mBtnPlay.getImage()==R.drawable.pause)
        {
            mBtnPlay.setImage(R.drawable.video);
        }
    }


    //region not static class: extend for top class
    public class MenuClickHandler implements Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if(menuItem.getIntent().getStringExtra(MasterPage.FIXMENUTITLENAME).equals(V_List_Song.menu_IncressVolume))
            {
                LSSystemServices.StreamVolumeInfo info=LSSystemServices.getVolumeInfo(MainActivity.appContext, AudioManager.STREAM_MUSIC);
                Dialog_Volume dialog=new Dialog_Volume(getContext(), info.max, info.now, new Dialog_Volume.IVolumeHander()
                {
                    @Override
                    public void onChangeValue(int value)
                    {
                        LSSystemServices.setVolume(AudioManager.STREAM_MUSIC, MainActivity.appContext, value);
                    }
                });
                dialog.show();
            }
            else if(menuItem.getIntent().getStringExtra(MasterPage.FIXMENUTITLENAME).equals(V_List_Song.menu_PlayerMode))
            {
                LSLog.Log_INFO("change mode!"+menuItem.getIntent());
                if(mMyConnection.mPlayerProxy!=null)
                {
                    try
                    {
                        mBaseInfo.changeMode();
                        mMyConnection.mPlayerProxy.changemode(mBaseInfo.playMode);
                        UpdateUi_mode(mBaseInfo);
                    }
                    catch (Exception e)
                    {
                        LSLog.Log_Exception(e);
                    }
                }
            }
            return true;
        }
    }

    public class MyConnection implements ServiceConnection
    {
        IPlayer mPlayerProxy;


        @Override
        public void onBindingDied(ComponentName name)
        {
            LSLog.Log_INFO("error onBindingDied");
        }

        @Override
        public void onNullBinding(ComponentName name)
        {
            LSLog.Log_INFO("error get null services");
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            LSLog.Log_INFO("onServiceConnected");
            mPlayerProxy=(IPlayer.Stub.asInterface(service));//必须用它提供的转义方法，它有一个是否是远程服务的区别。

            if(mPlayerProxy!=null)
            {
                try
                {
                    mBaseInfo = mPlayerProxy.getBaseInfo();//查看基本信息，判断是查看，还是播放。

                    LSLog.Log_INFO(String.format("onServiceConnected.argument lid %d,index:%d, base :lid:%d,sid:%d", mlsid, mIndex, mBaseInfo.lid, mBaseInfo.index));
                    if (mBaseInfo.lid == mlsid && mBaseInfo.index == mIndex)
                    {
                    }
                    else
                    {
                        mPlayerProxy.playSong(mlsid,mIndex);
                    }
                    UpdateUi_mode(mBaseInfo);
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
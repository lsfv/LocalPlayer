package com.linson.android.localplayer.activities;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Dialog.Dialog_Volume;
import com.linson.android.localplayer.appHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSSystemServices;
import app.lslibrary.androidHelper.LSUI;
import app.lslibrary.customUI.LSCircleImage;
import app.model.PlayerBaseInfo;

//功能：1.显示播放歌曲的信息。2。实现基本操作面板。3实现菜单功能。
public class PlaySong extends BaseFragment implements View.OnClickListener
{
    public static final String argumentLsid = "lid";
    public static final String argumentindex="index";

    private int mlsid=-1;
    private int mIndex=-1;


    public static void StartMe(FragmentManager fragmentManager, int lid, int index)
    {
        Fragment fragment=new PlaySong();
        Bundle bundle=new Bundle();
        bundle.putInt(PlaySong.argumentLsid, lid);
        bundle.putInt(PlaySong.argumentindex, index);
        fragment.setArguments(bundle);
        appHelper.startPageWithBack(fragmentManager,fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_play_song, container, false);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mMyControls=new MyControls();//cut it into 'onCreate'
        controlsEvent();
        initParameter();
        getMaster().setupToolbarMenu(app.bll.V_List_Song.getMenuPlayerTitle(), new MenuClickHandler());
        setupWidget();
    }

    //region private funcions
    private void controlsEvent()
    {
        mMyControls.mBtnNext.setOnClickListener(this);
        mMyControls.mBtnPlay.setOnClickListener(this);
        mMyControls.mBtnPre.setOnClickListener(this);
    }

    @SuppressLint("DefaultLocale")
    private void initParameter()
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
        LSLog.Log_INFO(String.format("init playsong. id:%d,index:%d",mlsid,mIndex));
    }

    private void pre()
    {
        if(MainActivity.appServiceConnection!=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=MainActivity.appServiceConnection.mPlayerProxy.pre();
                setupWidget();
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }

    private void play()
    {
        if(MainActivity.appServiceConnection!=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=MainActivity.appServiceConnection.mPlayerProxy.playOrPause();
                setupWidget();
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }

    private void next()
    {
        if(MainActivity.appServiceConnection!=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
        {
            try
            {
                int res=MainActivity.appServiceConnection.mPlayerProxy.next();
                setupWidget();
            }
            catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }

    private void setupWidget()
    {
        PlayerBaseInfo baseInfo=appHelper.getServiceBaseInfo(MainActivity.appServiceConnection);
        if(baseInfo!=null)
        {
            getMaster().changeMenuTitel(1, baseInfo.getModeName());

            boolean IsPlaying=false;
            if(baseInfo.status==PlayerBaseInfo.status_playing)
            {
                IsPlaying=true;
            }

            if(IsPlaying==true)
            {
                mMyControls.mBtnPlay.setImage(R.drawable.pause);
            }
            else if(IsPlaying==false)
            {
                mMyControls.mBtnPlay.setImage(R.drawable.video);
            }
        }
    }
    //endregion

    //region menu's handler
    public class MenuClickHandler implements Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            LSLog.Log_INFO("");
            if(LSUI.getToolbarItemKeyID(menuItem).equals(V_List_Song.menu_IncressVolume))
            {
                LSSystemServices.StreamVolumeInfo info=LSSystemServices.getVolumeInfo(MainActivity.appContext, AudioManager.STREAM_MUSIC);
                Dialog_Volume dialog=new Dialog_Volume(getContext(), info.max, info.now, new Dialog_Volume.IVolumeHander()
                {
                    @Override
                    public void onChangeValue(int value)
                    {
                        LSLog.Log_INFO("");
                        LSSystemServices.setVolume(AudioManager.STREAM_MUSIC, MainActivity.appContext, value);
                    }
                });
                dialog.show();
            }
            else if(LSUI.getToolbarItemKeyID(menuItem).equals(V_List_Song.menu_PlayerMode))
            {
                if(MainActivity.appServiceConnection!=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
                {
                    try
                    {
                        PlayerBaseInfo info= appHelper.getServiceBaseInfo(MainActivity.appServiceConnection);
                        if(info!=null)
                        {
                            info.changeMode();
                            MainActivity.appServiceConnection.mPlayerProxy.changemode(info.playMode);
                            setupWidget();
                        }
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
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private ConstraintLayout mContent;
        private ConstraintLayout mBottonMenu;
        private LSCircleImage mBtnPre;
        private LSCircleImage mBtnPlay;
        private LSCircleImage mBtnNext;

        public MyControls()
        {
            mContent = (ConstraintLayout)  PlaySong.this.getActivity().findViewById(R.id.content);
            mBottonMenu = (ConstraintLayout) PlaySong.this.getActivity().findViewById(R.id.botton_menu);
            mBtnPre = (LSCircleImage) PlaySong.this.getActivity().findViewById(R.id.btn_pre);
            mBtnPlay = (LSCircleImage) PlaySong.this.getActivity().findViewById(R.id.btn_play);
            mBtnNext = (LSCircleImage) PlaySong.this.getActivity().findViewById(R.id.btn_next);
        }
    }
    //endregion
}
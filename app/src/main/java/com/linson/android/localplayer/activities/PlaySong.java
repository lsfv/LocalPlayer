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
public class PlaySong extends BaseFragment
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mMyControls=new MyControls();//cut it into 'onCreate'
        initParameter();
    }

    //region private funcions


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
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private ConstraintLayout mContent;

        public MyControls()
        {
            mContent = (ConstraintLayout)  PlaySong.this.getActivity().findViewById(R.id.content);
        }
    }
    //endregion
}
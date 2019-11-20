package com.linson.android.localplayer.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.appHelperCommon;

import app.lslibrary.androidHelper.LSLog;
import app.model.PlayerBaseInfo;
import app.model.V_List_Song;


//功能：1.显示播放歌曲的信息。2。实现基本操作面板。3实现菜单功能。
public class PlaySong extends BaseFragment
{
    public static final String argumentLsid = "lid";
    public static final String argumentindex="index";

    private int mlsid=-1;
    private int mIndex=-1;
    private boolean mStopThread=true;

    public static void StartMe(FragmentManager fragmentManager, int lid, int index)
    {
        Fragment fragment=new PlaySong();
        Bundle bundle=new Bundle();
        bundle.putInt(PlaySong.argumentLsid, lid);
        bundle.putInt(PlaySong.argumentindex, index);
        fragment.setArguments(bundle);
        appHelperCommon.startPageWithBack(fragmentManager,fragment);
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
        getMaster().setupToolbarMenu(null, null);
        setupSeekBar();

    }

    //一定要注意方案的简洁和直接性。我这里直接开一个线程，一秒查询一次。完全没有任何效率问题。浪费的这点效率比管理线程的启动，停止还是划得来。
    private void setupSeekBar()
    {
        V_List_Song thesong=app.bll.V_List_Song.getModelByLid(mlsid).get(mIndex);
        mMyControls.mSbPlaying.setMax(thesong.S_duration);
        mMyControls.mSbPlaying.setOnSeekBarChangeListener(new SeekBarListener());
        Thread thread_getDuration=new Thread(new GetDuration());
        mStopThread=false;
        thread_getDuration.start();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mStopThread=true;
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

    //region thread for get duration
    private  class GetDuration implements Runnable
    {
        @Override
        public void run()
        {
            while (mStopThread==false)
            {
                final PlayerBaseInfo info=appHelperCommon.getServiceBaseInfo(MainActivity.appServiceConnection);
                if(info!=null)
                {
                    PlaySong.this.getMaster().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mMyControls.mSbPlaying.setProgress(info.playingSeconds);
                        }
                    });
                }
                try
                {
                    Thread.sleep(1500);
                }
                catch (Exception e)
                {
                    LSLog.Log_Exception(e);
                }
            }
        }
    }
    //endregion

    //region seekbarChangeListener
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {}
        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            try
            {
                MainActivity.appServiceConnection.mPlayerProxy.setPosition(seekBar.getProgress());
            } catch (RemoteException e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private ConstraintLayout mContent;
        private ConstraintLayout mBottom;
        private SeekBar mSbPlaying;

        public MyControls()
        {
            mContent = (ConstraintLayout)getActivity().findViewById(R.id.content);
            mBottom = (ConstraintLayout) getActivity().findViewById(R.id.bottom);
            mSbPlaying = (SeekBar) getActivity().findViewById(R.id.sb_playing);
        }
    }
    //endregion
}
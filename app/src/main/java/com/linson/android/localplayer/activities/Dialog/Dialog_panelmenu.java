package com.linson.android.localplayer.activities.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_Songs;

import java.util.Arrays;
import java.util.List;

import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSSystemServices;
import app.lslibrary.customUI.Adapter.Adapter_RadioButton;
import app.model.PlayerBaseInfo;
import app.model.V_List_Song;

import static com.linson.android.localplayer.appHelper.PlayerBaseInfo.getServiceBaseInfo;

public class Dialog_panelmenu extends Dialog
{
    private boolean hasdown=false;
    private boolean hasmove=false;

    public Dialog_panelmenu(Context context)
    {
        super(context,R.style.Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_panelmenu);

        mMyControls=new MyControls();//cut it into 'onCreate'
        mMyControls.mToptransport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        setupPlayModes();
        setupVolumeSeekBar();
        setupSongsList();
    }

    //region private funtion
    private void setupSongsList()
    {
        PlayerBaseInfo baseInfo=getServiceBaseInfo(MainActivity.appServiceConnection);
        if(baseInfo!=null)
        {
            List<V_List_Song> songs=app.bll.V_List_Song.getModelByLid(baseInfo.lid);
            mMyControls.mRvList.setAdapter(new Adapter_Songs(songs, new RecycleSongsListener(), baseInfo.index,1 ));
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
            mMyControls.mRvList.setLayoutManager(linearLayoutManager);
        }
    }

    private void setupVolumeSeekBar()
    {
        LSSystemServices.StreamVolumeInfo volumeInfo = LSSystemServices.getVolumeInfo(getContext(), AudioManager.STREAM_MUSIC);
        mMyControls.mSbVolume.setMax(volumeInfo.max);
        mMyControls.mSbVolume.setProgress(volumeInfo.now);
        mMyControls.mSbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                LSSystemServices.setVolume(AudioManager.STREAM_MUSIC, getContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            { }
        });
    }

    private void setupPlayModes()
    {
        List<String> playmodes= Arrays.asList(PlayerBaseInfo.playmodestr);
        PlayerBaseInfo info= getServiceBaseInfo(MainActivity.appServiceConnection);
        int mode=0;
        if(info!=null)
        {
            mode=info.playMode;
        }
        Adapter_RadioButton adapter_radioButton=new Adapter_RadioButton(playmodes,mode,new DialogRadioListener());
        mMyControls.mRvPlaymode.setAdapter(adapter_radioButton);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(), 4);
        mMyControls.mRvPlaymode.setLayoutManager(layoutManager);
    }
    //endregion

    //region Radio's handler
    public class DialogRadioListener implements Adapter_RadioButton.IDialogRadioListener
    {
        @Override
        public void onClickItem(int index)
        {
            if(MainActivity.appServiceConnection!=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
            {
                try
                {
                    MainActivity.appServiceConnection.mPlayerProxy.changemode(index);
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion

    //region RecycleView listener
    public class RecycleSongsListener implements Adapter_Songs.IItemHander
    {
        @Override
        public void onClick(int ls_id, int index)
        {
            //update services2.up playing
            if(MainActivity.appServiceConnection !=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
            {
                try
                {
                    app.model.PlayerBaseInfo info=MainActivity.appServiceConnection.mPlayerProxy.getBaseInfo();
                    if(info.lid!=ls_id || info.index!=index)//点击正在播放的应该无响应。
                    {
                        if(mMyControls.mRvList.getAdapter()!=null)
                        {
                            MainActivity.appServiceConnection.mPlayerProxy.playSong(ls_id, index);
                            ((Adapter_Songs) ((Adapter_Songs) mMyControls.mRvList.getAdapter())).showImagePlaying(index);
                        }
                    }
                }
                catch (Exception e)
                {
                    LSLog.Log_Exception(e);
                }
            }
        }
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private ConstraintLayout mToptransport;
        private TextView mTextView2;
        private RecyclerView mRvPlaymode;
        private TextView mTextView4;
        private SeekBar mSbVolume;
        private RecyclerView mRvList;

        public MyControls()
        {
            mToptransport = (ConstraintLayout) findViewById(R.id.toptransport);
            mTextView2 = (TextView) findViewById(R.id.textView2);
            mRvPlaymode = (RecyclerView) findViewById(R.id.rv_playmode);
            mTextView4 = (TextView) findViewById(R.id.textView4);
            mSbVolume = (SeekBar) findViewById(R.id.sb_volume);
            mRvList = (RecyclerView) findViewById(R.id.rv_list);
        }
    }
    //endregion
}
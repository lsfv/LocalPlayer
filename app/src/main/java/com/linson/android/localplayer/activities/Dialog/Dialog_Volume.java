package com.linson.android.localplayer.activities.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.linson.android.localplayer.R;

public class Dialog_Volume extends Dialog
{
    private int mmaxValue;//=100;
    private int mcurrentValue;//=10;
    private IVolumeHander mIVolumeHander;

    public Dialog_Volume(Context context,int maxValue,int currentValue,IVolumeHander hander)
    {
        super(context);
        mmaxValue=maxValue;
        mcurrentValue=currentValue;
        mIVolumeHander=hander;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_changepercent);

        mMyControls=new MyControls();//cut it into 'onCreate'

        mMyControls.mSbVolume.setMax(mmaxValue);
        mMyControls.mSbVolume.setProgress(mcurrentValue);
        mMyControls.mSbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(mIVolumeHander!=null)
                {
                    mIVolumeHander.onChangeValue(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

    }

    public interface IVolumeHander
    {
        void onChangeValue(int value);
    }


    //region FindControls Helper
    private MyControls mMyControls=null;
    public class MyControls implements View.OnClickListener
    {
        private SeekBar mSbVolume;

        MyControls()
        {
            mSbVolume = (SeekBar)  findViewById(R.id.sb_volume);
        }

        @Override
        public void onClick(View v)
        {
            //you can bind click event in here , and then put click event 's function into activity.
            //if (v.getId() == xxx){}
        }
    }
    //endregion
}
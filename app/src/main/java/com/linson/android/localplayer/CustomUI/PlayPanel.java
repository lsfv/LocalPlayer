package com.linson.android.localplayer.CustomUI;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Dialog.Dialog_panelmenu;
import com.linson.android.localplayer.appHelper.appHelperPlayerBaseInfo;

import java.util.List;

import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSTouch;
import app.lslibrary.customUI.LSCircleImage;
import app.lslibrary.pattern.LSObserver;
import app.model.PlayerBaseInfo;
import app.model.V_List_Song;

import static com.linson.android.localplayer.appHelper.appHelperPlayerBaseInfo.*;

//功能 。1连接server。展示信息。2提供 播放，暂停。 上下首。 音量，模式等功能。3.并提供订阅者模式。转给master。master作为中转的观察者。
//1.布局页。2.测试初始和加载后，动作都必须合理。3.观察者的实现。
public class PlayPanel extends ConstraintLayout implements View.OnClickListener
{
    private Context mContext;
    private LSObserver.IObserverListener<app.model.PlayerBaseInfo> mObserver;

    public PlayPanel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.custom_panel, this, true);
        mMyControls=new MyControls();//cut it into 'onCreate'
        setupUI();
        mMyControls.mPanelGroup.setOnTouchListener(new GroupTouchListener());
        mMyControls.mIconPlay.setOnClickListener(this);
        mMyControls.mIconMenu.setOnClickListener(this);

        mObserver=new InfoObserver();
        appHelperPlayerBaseInfo.baseInfoLSObserver.registerObserver(mObserver);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        appHelperPlayerBaseInfo.baseInfoLSObserver.unRegisterObserver(mObserver);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.icon_play)
        {
            ClickPlay();
        }
        else if(v.getId()==R.id.icon_menu)
        {
            clickMenu();
        }
    }

    private void clickMenu()
    {
        Dialog_panelmenu dialog_panelmenu=new Dialog_panelmenu(mContext);
        dialog_panelmenu.show();
    }

    public  void setupUI()
    {
        PlayerBaseInfo info= getServiceBaseInfo(MainActivity.appServiceConnection);
        if(info!=null)
        {
            if(info.status==PlayerBaseInfo.status_init)
            {
                mMyControls.mTvSongname.setText("等待播放");
                mMyControls.mIconPlay.setImage(R.drawable.play);
            }
            else if(info.status==PlayerBaseInfo.status_playing)
            {
                List<V_List_Song> songs=app.bll.V_List_Song.getModelByLid(info.lid);
                V_List_Song thesong=songs.get(info.index);
                mMyControls.mTvSongname.setText(thesong.S_musicName);
                mMyControls.mIconPlay.setImage(R.drawable.pause);
            }
            else if(info.status==PlayerBaseInfo.status_pause)
            {
                List<V_List_Song> songs=app.bll.V_List_Song.getModelByLid(info.lid);
                V_List_Song thesong=songs.get(info.index);
                mMyControls.mTvSongname.setText(thesong.S_musicName);
                mMyControls.mIconPlay.setImage(R.drawable.play);
            }
            else if(info.status==PlayerBaseInfo.status_error_other)
            {
                mMyControls.mTvSongname.setText(mContext.getString(R.string.error));
                mMyControls.mIconPlay.setImage(R.drawable.play);
            }
            else if(info.status==PlayerBaseInfo.status_error_nofile)
            {
                mMyControls.mTvSongname.setText(mContext.getString(R.string.nofile));
                mMyControls.mIconPlay.setImage(R.drawable.play);
            }
        }
    }

    private void ClickPlay()
    {
        if (MainActivity.appServiceConnection != null && MainActivity.appServiceConnection.mPlayerProxy != null)
        {
            try
            {
                MainActivity.appServiceConnection.mPlayerProxy.playOrPause();
                setupUI();
            } catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
        }
    }

    //region infoobserver
    public class  InfoObserver implements LSObserver.IObserverListener<PlayerBaseInfo>
    {
        @Override
        public void onHappen(PlayerBaseInfo p)
        {
            setupUI();
        }
    }
    //endregion

    //region groupTouch
    public class GroupTouchListener implements OnTouchListener
    {
        private MotionEvent mDown;
        private MotionEvent mFirstMove;
        private int left=-1;
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
                mDown=MotionEvent.obtain(event);
            }
            else if(event.getAction()==MotionEvent.ACTION_MOVE)
            {
                if(mDown!=null && mFirstMove==null)
                {
                    mFirstMove=MotionEvent.obtain(event);
                    if(LSTouch.getscrollDirection(mDown, mFirstMove) == LSTouch.scrollDirection.LEFT)
                    {
                        left=1;
                    }
                    else if(LSTouch.getscrollDirection(mDown, mFirstMove) == LSTouch.scrollDirection.RIGHT)
                    {
                        left=0;
                    }
                }
            }
            else if(event.getAction()==MotionEvent.ACTION_CANCEL || event.getAction()==MotionEvent.ACTION_UP)
            {
                if(MainActivity.appServiceConnection!=null && MainActivity.appServiceConnection.mPlayerProxy!=null && left!=-1)
                {
                    try
                    {
                        if(left==1)
                        {
                            MainActivity.appServiceConnection.mPlayerProxy.pre();
                            PlayerBaseInfo info= getServiceBaseInfo(MainActivity.appServiceConnection);
                            appHelperPlayerBaseInfo.baseInfoLSObserver.NoticeObsserver(info);
                        }
                        else if(left==0)
                        {
                            PlayerBaseInfo info= getServiceBaseInfo(MainActivity.appServiceConnection);
                            appHelperPlayerBaseInfo.baseInfoLSObserver.NoticeObsserver(info);
                            MainActivity.appServiceConnection.mPlayerProxy.next();
                        }
                    }
                    catch (Exception e)
                    {
                        LSLog.Log_Exception(e);
                    }
                }
                left=-1;
                mDown=null;
                mFirstMove=null;
            }
            return true;
        }
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private ConstraintLayout mPanelGroup;
        private TextView mTvSongname;
        private LSCircleImage mIconPlay;
        private LSCircleImage mIconMenu;

        public MyControls()
        {
            mPanelGroup = (ConstraintLayout) findViewById(R.id.panel_group);
            mTvSongname = (TextView) findViewById(R.id.tv_songname);
            mIconPlay = (LSCircleImage) findViewById(R.id.icon_play);
            mIconMenu = (LSCircleImage) findViewById(R.id.icon_menu);
        }
    }
    //endregion
}
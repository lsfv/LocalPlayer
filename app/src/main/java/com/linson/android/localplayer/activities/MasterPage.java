package com.linson.android.localplayer.activities;

import android.Manifest;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linson.android.localplayer.CustomUI.PlayPanel;
import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Dialog.Dialog_panelmenu;
import com.linson.android.localplayer.appHelperCommon;

import java.util.ArrayList;
import java.util.List;

import app.bll.LocalSong;
import app.lslibrary.androidHelper.LSContentResolver;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSUI;
import app.lslibrary.customUI.LSCircleImage;
import app.model.PlayerBaseInfo;

//!todo 图标和进度条提示
//!todo ui的美化。歌词先不需要，用一个动态的cd代替就ok。
//!todo 适配的总结。dp。
//!todo mvvm？测试发现用观察者模式，非常简洁清晰有效。    可以开新分支测试mvvm.
//!todo fullscreen dialog 的提起。
//!todo 还是需要自带的常用所有控件都过一遍
//!todo savedInstanceState 实际工程使用范例.public ListDetail(int a) 什么时候fragment需要从建立开始恢复？ 导致得到参数必须是通过argumentbundle。
//!todo 还是需要一个模板啊。比如adapter 的大致样子都是差不多的。

//功能:母模板实现大框架功能。并提供public方法让fragment访问。
public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    private boolean loadMenu=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_master_page);

            mMyControls = new MyControls();//cut it into 'onCreate'//1.控件绑定
            controlsEvent();
            CleanStackAndReplaceFragment(new ListIndex());//3.加载首页
            LSContentResolver.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, new LocalSong.UpdateDB_Songs(MainActivity.appContext));//4.自动跟新歌曲
            this.getContentResolver().registerContentObserver(LSContentResolver.uri_audio_external, false, new MyAudioObserver(null));//5.监听歌曲变化
            mMyControls.mPlaypanel.setLisener(new PanelListener());
        } catch (Exception e)
        {
            LSLog.Log_Exception(e);
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.btn_page1)
        {
            CleanStackAndReplaceFragment(new ListIndex());
            mMyControls.mDrawerMainMenu.closeDrawer(Gravity.START);
        }
        else if(v.getId()==R.id.btn_back)
        {
            if(getSupportFragmentManager().getBackStackEntryCount()>0)
            {
                getSupportFragmentManager().popBackStack();
            }
        }
        else if(v.getId()==R.id.btn_menu)
        {
            mMyControls.mDrawerMainMenu.openDrawer(Gravity.START);
        }
        else if(v.getId()==R.id.btn_pagesetting)
        {
            Dialog_panelmenu dialog_panelmenu=new Dialog_panelmenu(this);
            dialog_panelmenu.show();
        }
        else if(v.getId()==R.id.btn_pageabout)
        {
            CleanStackAndReplaceFragment(new About());
            mMyControls.mDrawerMainMenu.closeDrawer(Gravity.START);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LSContentResolver.progressCheck(this, requestCode, grantResults, 1, new app.bll.LocalSong.UpdateDB_Songs(MainActivity.appContext));
    }

    //region panel's listener
    public class PanelListener implements PlayPanel.IPanelLisener
    {
        @Override
        public void onStartActivity_PlayingSong()
        {
            LSLog.Log_INFO();
            PlayerBaseInfo info=appHelperCommon.getServiceBaseInfo(MainActivity.appServiceConnection);
            if(app.bll.MusicServices.canSeeSongDetail(info))
            {
                if(getSupportFragmentManager().findFragmentById(R.id.mainFragment) instanceof PlaySong==false)
                {
                    PlaySong.StartMe(getSupportFragmentManager(), info.lid, info.index);
                }
            }
        }
    }
    //endregion

    //region audio's observer
    public class MyAudioObserver extends ContentObserver
    {
        public MyAudioObserver(Handler handler)
        {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            super.onChange(selfChange, uri);
            LSLog.Log_INFO();
            if(uri.equals(LSContentResolver.uri_audio_external))
            {
                LSContentResolver.checkPermission(MasterPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, new app.bll.LocalSong.UpdateDB_Songs(MainActivity.appContext));
            }
        }
    }
    //endregion

    //region private function
    private void controlsEvent()
    {
        mMyControls.mBtnBack.setOnClickListener(this);
        mMyControls.mBtnPage1.setOnClickListener(this);
        mMyControls.mBtnMenu.setOnClickListener(this);
        mMyControls.mBtnPagesetting.setOnClickListener(this);
        mMyControls.mBtnPageabout.setOnClickListener(this);
    }

    private void CleanStackAndReplaceFragment(Fragment fragment)
    {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        appHelperCommon.startPageNoBack(getSupportFragmentManager(), fragment);
    }
    //endregion

    //region 提供给fragment访问的功能
    public List<ImageView> getBtnArray()
    {
        List<ImageView> res=new ArrayList<>();
        res.add(mMyControls.mBtnRight1);
        res.add(mMyControls.mBtnRight2);
        res.add(mMyControls.mBtnRight3);
        return res;
    }

    public void SetupTitle(String title)
    {
        if(title!=null)
        {
            mMyControls.mFragmentTitle.setText(title);
            mMyControls.mFragmentTitle.setVisibility(View.VISIBLE);
        }
        else
        {
            mMyControls.mFragmentTitle.setText("");
            mMyControls.mFragmentTitle.setVisibility(View.GONE);
        }
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private DrawerLayout mDrawerMainMenu;
        private Toolbar mToolbar;
        private LSCircleImage mBtnMenu;
        private LSCircleImage mBtnBack;
        private ImageView mBtnRight1;
        private ImageView mBtnRight2;
        private ImageView mBtnRight3;
        private TextView mFragmentTitle;
        private ConstraintLayout mMainFragment;
        private PlayPanel mPlaypanel;

        private TextView mTextView7;
        private Button mBtnPageabout;
        private Button mBtnPagesetting;
        private TextView mTextView6;
        private Button mBtnPage1;
        private TextView mTextView5;
        private TextView mTextView8;





        public MyControls()
        {
            mDrawerMainMenu = (DrawerLayout) findViewById(R.id.drawerMainMenu);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mBtnMenu = (LSCircleImage) findViewById(R.id.btn_menu);
            mBtnBack = (LSCircleImage) findViewById(R.id.btn_back);
            mBtnRight1 = (ImageView) findViewById(R.id.btn_right1);
            mBtnRight2 = (ImageView) findViewById(R.id.btn_right2);
            mBtnRight3 = (ImageView) findViewById(R.id.btn_right3);
            mFragmentTitle = (TextView) findViewById(R.id.fragmentTitle);
            mMainFragment = (ConstraintLayout) findViewById(R.id.mainFragment);
            mBtnPage1 = (Button) findViewById(R.id.btn_page1);
            mPlaypanel = (PlayPanel) findViewById(R.id.playpanel);

            mTextView7 = (TextView) findViewById(R.id.textView7);
            mBtnPageabout = (Button) findViewById(R.id.btn_pageabout);
            mBtnPagesetting = (Button) findViewById(R.id.btn_pagesetting);
            mTextView6 = (TextView) findViewById(R.id.textView6);
            mBtnPage1 = (Button) findViewById(R.id.btn_page1);
            mTextView5 = (TextView) findViewById(R.id.textView5);
            mTextView8 = (TextView) findViewById(R.id.textView8);
        }
    }
    //endregion
}
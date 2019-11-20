package com.linson.android.localplayer.activities;

import android.Manifest;
import android.database.ContentObserver;
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
import com.linson.android.localplayer.CustomUI.PlayPanel;
import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.appHelperCommon;
import app.lslibrary.androidHelper.LSContentResolver;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSUI;

//!todo 进度条：主动新线程。每秒一次询问，再通知主线程更新seekbar。 通知的方法中需要一个是否正在监听事件的boolean 判断值。
//!todo ui的美化。歌词问题。
//!todo mvvm？测试发现用观察者模式，非常简洁清晰有效。    可以开新分支测试mvvm.
//!todo fullscreen dialog 的提起。
//!todo 无法触发服务sub代理的释放.android 一般也是不完全关闭app的。所以我这里是保证服务停止播放，最多释放播放器而已。单元测试好像会提示哪个activity没有释放。
//!todo 还是需要自带的常用所有控件都过一遍
//!todo savedInstanceState 实际工程使用范例.
//!todo 还是需要一个模板啊。比如adapter 的大致样子都是差不多的。
//!todo public ListDetail(int a) 什么时候fragment需要从建立开始恢复？ 导致得到参数必须是通过argumentbundle。

//功能:母模板实现大框架功能。并提供public方法让fragment访问。
public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    private boolean loadMenu=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);

        mMyControls=new MyControls();//cut it into 'onCreate'//1.控件绑定
        controlsEvent();
        setupDrawerMenu();//2.配置左侧滑动菜单
        CleanStackAndReplaceFragment(new ListIndex());//3.加载首页
        LSContentResolver.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, new app.bll.LocalSong.UpdateDB_Songs(MainActivity.appContext));//4.自动跟新歌曲
        this.getContentResolver().registerContentObserver(LSContentResolver.uri_audio_external, false, new MyAudioObserver(null));//5.监听歌曲变化
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_page1:
            {
                CleanStackAndReplaceFragment(new ListIndex());
                mMyControls.mDrawerMainMenu.closeDrawer(Gravity.START);
                break;
            }
            case R.id.btn_back:
            {
                if(getSupportFragmentManager().getBackStackEntryCount()>0)
                {
                    getSupportFragmentManager().popBackStack();
                }
            }
            default: { break; }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LSContentResolver.progressCheck(this, requestCode, grantResults, 1, new app.bll.LocalSong.UpdateDB_Songs(MainActivity.appContext));
    }

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
    }

    private void setupDrawerMenu()
    {
        mMyControls.mToolbar.setNavigationIcon(R.drawable.list);
        mMyControls.mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mMyControls.mDrawerMainMenu.openDrawer(Gravity.START);
            }
        });
    }

    private void CleanStackAndReplaceFragment(Fragment fragment)
    {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        appHelperCommon.startPageNoBack(getSupportFragmentManager(), fragment);
    }
    //endregion

    //region 提供给fragment访问的功能
    public void setupToolbarMenu(java.util.List<String> menus, android.support.v7.widget.Toolbar.OnMenuItemClickListener handler)
    {
        LSUI.setupToolbarMenu(mMyControls.mToolbar, menus, handler);
    }

    public void onBaseinfoChange()
    {
        mMyControls.mPlaypanel.setupUI();
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private DrawerLayout mDrawerMainMenu;
        private Toolbar mToolbar;
        private Button mBtnBack;
        private ConstraintLayout mMainFragment;
        private Button mBtnPage1;
        private PlayPanel mPlaypanel;

        public MyControls()
        {
            mDrawerMainMenu = (DrawerLayout) findViewById(R.id.drawerMainMenu);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mBtnBack = (Button) findViewById(R.id.btn_back);
            mMainFragment = (ConstraintLayout) findViewById(R.id.mainFragment);
            mBtnPage1 = (Button) findViewById(R.id.btn_page1);
            mPlaypanel = (PlayPanel) findViewById(R.id.playpanel);
        }
    }
    //endregion
}
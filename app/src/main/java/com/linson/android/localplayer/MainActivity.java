package com.linson.android.localplayer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linson.android.localplayer.AIDL.IPlayer;
import com.linson.android.localplayer.activities.MasterPage;

import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.pattern.LSObserver;
import app.model.PlayerBaseInfo;


//功能：初始化全局变量:context.serviceConnection.  并释放全局全局变量（引用了外部对象）
public class MainActivity extends AppCompatActivity
{
    public static Context appContext;
    public static MyConnection appServiceConnection;
    public static LSObserver<PlayerBaseInfo> baseInfoLSObserver=new LSObserver<>();

    private boolean isFirstLoad=true;
    public BroadcastReceiver serverReceiver=new BroadcastServiceReceiver();

    public final boolean ExistNotBackground=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initGlobalArgument();//初始化全局变量

        StartServicesabc();//启动服务，并在这里关闭服务。其他页面，绑动服务就ok了。
        registerServicesBroadcast();//注册广播
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if(isFirstLoad)
        {
            startIndex();//跳转首页
            isFirstLoad=false;
        }
        else
        {
            //把静态变量(引用了外部对象)先清空掉。以免外部对象无法释放。
            //服务也解绑和停止。
            app.bll.MusicDB.setDBContext(null);//把引用了自己的静态变量也先清掉。
            if(ExistNotBackground)
            {
                unbindService(appServiceConnection);
                stopService(app.bll.MusicServices.getServiceIntent());
            }
            else
            {
                unbindService(appServiceConnection);
            }
            appContext=null;
            appServiceConnection=null;

            UnRegisterServicesBroadcast();//注销广播
            isFirstLoad=true;
            finish();
        }
    }

    private void startIndex()
    {
        startActivity(new Intent(this, MasterPage.class));
    }

    private void initGlobalArgument()
    {
        appContext = getApplicationContext();//全局变量
        app.bll.MusicDB.setDBContext(appContext);
        LSLog.Log_DBinfo();//数据库地址
    }

    //start and test services
    private boolean StartServicesabc()
    {
        startService(app.bll.MusicServices.getServiceIntent());
        appServiceConnection = new MyConnection();
        bindService(app.bll.MusicServices.getServiceIntent(), appServiceConnection, Context.BIND_AUTO_CREATE);

        return true;
    }


    private void registerServicesBroadcast()
    {
        if(appContext!=null)
        {
            IntentFilter intentFilter=new IntentFilter(PlayerBaseInfo.BROADCASTNAME);
            MainActivity.appContext.registerReceiver(serverReceiver, intentFilter);
        }
    }

    private  void UnRegisterServicesBroadcast()
    {
        if(appContext!=null)
        {
            appContext.unregisterReceiver(serverReceiver);
        }
    }


    //region serverConneciton
    public static class MyConnection implements ServiceConnection
    {
        public IPlayer mPlayerProxy;

        @Override
        public void onBindingDied(ComponentName name)
        {
            LSLog.Log_INFO("");
        }

        @Override
        public void onNullBinding(ComponentName name)
        {
            LSLog.Log_INFO("");
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mPlayerProxy=(IPlayer.Stub.asInterface(service));//必须用它提供的转义方法，它有一个是否是远程服务的区别。

            if(mPlayerProxy==null)
            {
                LSLog.Log_INFO("onServiceConnected. result :failed");
            }
            else
            {
                LSLog.Log_INFO("onServiceConnected. result :success");
            }
        }


        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            LSLog.Log_INFO("");
        }
    }

    //endregion

    //region Broadcast receiver

    public class BroadcastServiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            PlayerBaseInfo info= appHelperCommon.getServiceBaseInfo(MainActivity.appServiceConnection);
            MainActivity.baseInfoLSObserver.NoticeObsserver(info);
        }
    }
    //endregion
}
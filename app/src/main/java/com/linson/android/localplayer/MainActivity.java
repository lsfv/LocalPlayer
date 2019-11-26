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

import app.bll.MusicServices;
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
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        int waitSum=0;
        if(isFirstLoad)
        {
            while (appContext==null && waitSum<50)//必须连接上server才能跳转。除非5秒都连接不上。
            {
                try
                {
                    Thread.sleep(100);
                    waitSum++;
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if(appContext!=null)
            {
                startIndex();//跳转首页
                isFirstLoad = false;
            }
            else
            {
                finish();
            }

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
        try
        {
            startService(MusicServices.getServiceIntent());
            appServiceConnection = new MyConnection();
            bindService(MusicServices.getServiceIntent(), appServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e)
        {
            LSLog.Log_Exception(e);
        }

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
    public  class MyConnection implements ServiceConnection
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
                registerServicesBroadcast();//注册广播
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
package com.linson.android.localplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.ConnectionService;
import android.util.Log;
import android.widget.Toast;

import com.linson.android.localplayer.AIDL.IPlayer;
import com.linson.android.localplayer.Services.PlayServices;
import com.linson.android.localplayer.activities.MasterPage;

import app.lslibrary.androidHelper.LSLog;
import app.model.V_List_Song;

//自动生成model。 dbhelper. 测试DBHELPER。 dal.

public class MainActivity extends AppCompatActivity
{
    public static Context appContext;
    //private myConnection mm;
    private boolean isFirstLoad=true;

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
        if(isFirstLoad)
        {
            startIndex();//跳转首页
            isFirstLoad=false;
        }
        else
        {
            //stopService(appHelper.getServiceIntent());//还是不能退出服务。服务就让用户手动关闭把。
            finish();
            System.exit(0);
        }
    }


    private void startIndex()
    {
        startActivity(new Intent(this, MasterPage.class));
    }

    private void initGlobalArgument()
    {
        appContext = getApplicationContext();//全局变量
        LSLog.Log_DBinfo();//数据库地址
    }

    //start and test services
    private boolean StartServicesabc()
    {
        startService(appHelper.getServiceIntent());
        return true;
    }
}
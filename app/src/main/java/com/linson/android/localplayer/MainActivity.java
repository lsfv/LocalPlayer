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

//自动生成model。 dbhelper. 测试DBHELPER。 dal.

public class MainActivity extends AppCompatActivity
{
    public static Context appContext;
    private myConnection mm;
    private boolean isback=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initGlobalArgument();//初始化全局变量
        boolean isServiceOK= StartServicesabc();//测试服务
        startIndex(isServiceOK);//跳转首页
    }


    private void startIndex(boolean isServiceOK)
    {
        if(isServiceOK)
        {
            startActivity(new Intent(this, MasterPage.class));
        }
        else
        {
            Toast.makeText(appContext, "fail to start Service!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initGlobalArgument()
    {
        appContext = getApplicationContext();//全局变量
        LSLog.Log_DBinfo();//数据库地址
        isback=true;
    }


    //start and test services
    private boolean StartServicesabc()
    {
        Intent intent_service=new Intent();
        intent_service.setPackage("com.linson.android.localplayer");//哪个程序
        intent_service.setAction("abc");//哪个服务
        startService(intent_service);
        mm=new myConnection();
        return bindService(intent_service, mm , BIND_AUTO_CREATE);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if(isback)
        {
            unbindService(mm);
            Intent intent_service=new Intent();
            intent_service.setPackage("com.linson.android.localplayer");//哪个程序
            intent_service.setAction("abc");//哪个服务
            stopService(intent_service);

            finish();
        }
    }


    private class myConnection implements ServiceConnection
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            try
            {
                IPlayer res=IPlayer.Stub.asInterface(service);
                int a = res.add(3, 4);
                LSLog.Log_INFO("StartServices:ok"+"."+a);
            } catch (Exception e)
            {
                LSLog.Log_Exception(e,"StartServices: ");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    }
}
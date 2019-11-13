package com.linson.android.localplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linson.android.localplayer.activities.MasterPage;

import app.lslibrary.androidHelper.LSLog;

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
            app.bll.MusicDB.setDBContext(null);//把自己的context，全部收回，以免自己不能被自动回收。
            stopService(appHelper.getServiceIntent());
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
        app.bll.MusicDB.setDBContext(appContext);
        LSLog.Log_DBinfo();//数据库地址
    }

    //start and test services
    private boolean StartServicesabc()
    {
        startService(appHelper.getServiceIntent());
        return true;
    }
}
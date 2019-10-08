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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appContext=getApplicationContext();
        LSLog.Log_DBinfo();
        startActivity(new Intent(this, MasterPage.class));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.finish();
    }
}
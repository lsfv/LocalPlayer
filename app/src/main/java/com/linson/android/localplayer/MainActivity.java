package com.linson.android.localplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.lslibrary.androidHelper.LSLog;

public class MainActivity extends AppCompatActivity
{
    public static Context appContext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appContext=getApplicationContext();
        LSLog.Log_DBinfo();
        startActivity(new Intent(this, null));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.finish();
    }
}
package com.linson.android.localplayer.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.linson.android.localplayer.AIDL.IPlayer;

import app.lslibrary.androidHelper.LSLog;
import app.model.V_List_Song;

public class PlayServices extends Service
{
    public PlayServices()
    {
        LSLog.Log_INFO("services constr");
    }

    @Nullable @Override
    public IBinder onBind(Intent intent)
    {
        LSLog.Log_INFO("services");
        return new RemoteServiceProxy();
    }


    public class RemoteServiceProxy extends IPlayer.Stub
    {
        @Override
        public int add(int a, int b) throws RemoteException
        {
            return a+b;
        }
//        @Override
//        public String modifymodel(V_List_Song mm) throws RemoteException
//        {
//            return mm.L_name+"new name";
//        }
    }
}
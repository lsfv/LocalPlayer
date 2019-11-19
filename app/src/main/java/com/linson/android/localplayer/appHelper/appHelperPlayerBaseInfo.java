package com.linson.android.localplayer.appHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.linson.android.localplayer.MainActivity;
import app.lslibrary.pattern.LSObserver;
import app.model.PlayerBaseInfo;


public abstract class appHelperPlayerBaseInfo
{
    public static app.model.PlayerBaseInfo getServiceBaseInfo(MainActivity.MyConnection conn)
    {
        app.model.PlayerBaseInfo res=null;
        if(conn!=null && conn.mPlayerProxy!=null)
        {
            try
            {
                res = conn.mPlayerProxy.getBaseInfo();
            }
            catch (Exception e)
            {
            }
        }
        return res;
    }

    public static LSObserver<app.model.PlayerBaseInfo> baseInfoLSObserver=new LSObserver<>();


    //region server 's broadcast
    public static BroadcastReceiver serverReceiver=new BroadcastServiceReceiver();

    public static void registerServicesBroadcast()
    {
        if(MainActivity.appContext!=null)
        {
            IntentFilter intentFilter=new IntentFilter(PlayerBaseInfo.BROADCASTNAME);
            MainActivity.appContext.registerReceiver(serverReceiver, intentFilter);
        }
    }

    public static void UnRegisterServicesBroadcast()
    {
        if(MainActivity.appContext!=null)
        {
            MainActivity.appContext.unregisterReceiver(serverReceiver);
        }
    }

    public static class BroadcastServiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            PlayerBaseInfo info=appHelperPlayerBaseInfo.getServiceBaseInfo(MainActivity.appServiceConnection);
            appHelperPlayerBaseInfo.baseInfoLSObserver.NoticeObsserver(info);
        }
    }
    //endregion
}
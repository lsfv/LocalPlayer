package com.linson.android.localplayer;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import app.lslibrary.androidHelper.LSActivity;
import app.lslibrary.androidHelper.LSContentResolver;
import app.model.PlayerBaseInfo;

//此程序的一些复用方法，非业务逻辑，不适合放入逻辑层。,但是又不通用，也不太适合放入到库中。如此程序特有的android 的一些api方法的简写
public abstract class appHelper
{
    public static final int defaultListID=1;

    public static void startPageNoBack(FragmentManager fragmentManager, Fragment fragment)
    {
        LSActivity.replaceFragment(fragmentManager, false, R.id.mainFragment, fragment);
    }

    public static void startPageWithBack(FragmentManager fragmentManager, Fragment fragment)
    {
        LSActivity.replaceFragment(fragmentManager, true, R.id.mainFragment, fragment);
    }

    public static Intent getServiceIntent()
    {
        Intent intent_services=new Intent();
        intent_services.setAction("musicService");
        intent_services.setPackage("com.linson.android.localplayer");
        return intent_services;
    }

    public static PlayerBaseInfo getServiceBaseInfo(MainActivity.MyConnection conn)
    {
        PlayerBaseInfo res=null;
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

    public static class  UpdateDB_Songs implements LSContentResolver.VoidHandler
    {
        @Override
        public void doit()
        {
            LSContentResolver lsContentResolver=new LSContentResolver(MainActivity.appContext);
            java.util.List<LSContentResolver.SongInfo> localSongs= lsContentResolver.SearchSong(60*1000);
            app.bll.LocalSong.updateSongsFromLocal(localSongs);
        }
    }
}
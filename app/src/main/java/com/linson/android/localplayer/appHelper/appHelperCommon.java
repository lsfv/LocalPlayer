package com.linson.android.localplayer.appHelper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;

import app.lslibrary.androidHelper.LSActivity;
import app.lslibrary.androidHelper.LSContentResolver;

public abstract class appHelperCommon
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
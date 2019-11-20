package com.linson.android.localplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import app.lslibrary.androidHelper.LSActivity;

//非基础性方法。一般是
//1.在lib或bll上稍微再封装一下
//2.含只有app层能访问的东西。如server的接口。需要复用几行代码，所以稍微封装一下成为一个方法。
public abstract class appHelperCommon
{
    public static void startPageNoBack(FragmentManager fragmentManager, Fragment fragment)
    {
        LSActivity.replaceFragment(fragmentManager, false, R.id.mainFragment, fragment);
    }

    public static void startPageWithBack(FragmentManager fragmentManager, Fragment fragment)
    {
        LSActivity.replaceFragment(fragmentManager, true, R.id.mainFragment, fragment);
    }

    //!todo iplaye需要放入到bll层面?可能可以试下。这里？？ok.最后开一个分支测试下。
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
            {   res=null; }
        }
        return res;
    }
}
package com.linson.android.localplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import app.lslibrary.androidHelper.LSActivity;

//此程序的一些复用方法，非业务逻辑，不适合放入逻辑层。,但是又不通用，也不太适合放入到库中。如此程序特有的android 的一些api方法的简写
public abstract class appHelper
{
    public static void startPageWithBack(FragmentManager fragmentManager, Fragment fragment)
    {
        LSActivity.replaceFragment(fragmentManager, true, R.id.mainFragment, fragment);
    }
}

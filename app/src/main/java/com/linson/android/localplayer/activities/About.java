package com.linson.android.localplayer.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.appHelperCommon;



public class About extends BaseFragment
{
    //提供自己启动的静态方法。
    public static void StartMe(FragmentManager fragmentManager)
    {
        About fragment = new About();
        appHelperCommon.startPageWithBack(fragmentManager, fragment);
    }

    //初始化参数
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    //加载界面
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getMaster().SetupTitle("关于温阳");
    }
}
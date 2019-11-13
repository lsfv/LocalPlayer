package com.linson.android.localplayer.activities;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment
{
    public BaseFragment()
    {
    }

    //提供方法而不是成员变量，因为想让调用者自己清楚调用的时机，必须是activity之后。错误可以给出提示。
    public MasterPage getMaster()
    {
        MasterPage res=null;
        if(getActivity() instanceof  MasterPage)
        {
            res=(MasterPage) getActivity();
        }
        if(res==null)
        {
            throw new Error("master is null,please check call the function after activity is created.");
        }
        else
        {
            return res;
        }
    }
}
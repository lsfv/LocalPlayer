package com.linson.android.localplayer.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.linson.android.localplayer.R;

import java.util.ArrayList;
import java.util.LinkedList;

import app.lslibrary.androidHelper.LSLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDetail extends Fragment implements MasterPage.IFragmentForMaster
{
    public ListDetail()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_detail, container, false);
    }


    @Override
    public java.util.List<String> getMenuTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add("编辑列表");
        return res;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {
        LSLog.Log_INFO(menuItem.getTitle().toString());
        return false;
    }
}
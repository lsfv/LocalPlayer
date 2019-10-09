package com.linson.android.localplayer.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_Songs;
import java.util.List;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;


public class ListDetail extends Fragment
{
    private TextView mTvListname;
    private RecyclerView mRvSonglist;

    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mTvListname = (TextView) getActivity().findViewById(R.id.tv_listname);
        mRvSonglist = (RecyclerView) getActivity().findViewById(R.id.rv_songlist);
    }
    //endregion

    //region other member variable
    private int mListID=0;
    private app.bll.V_List_Song mV_list_song_bll;
    //endregion


    public ListDetail()
    {
        mV_list_song_bll=new V_List_Song(MainActivity.appContext);
    }

    public void setListID(int lid)
    {
        mListID=lid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_detail, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        findControls();

        setupRecyleview();
        setupToolbarMenu();
    }


    private void setupRecyleview()
    {
        List<app.model.V_List_Song> res=null;
        if(mListID==0)
        {
            res= mV_list_song_bll.getModelByZeroList();
        }
        else if(mListID>0)
        {
            res=mV_list_song_bll.getModelByLid(mListID);
        }

        Adapter_Songs adapter_songs=new Adapter_Songs(res);
        mRvSonglist.setAdapter(adapter_songs);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRvSonglist.setLayoutManager(linearLayoutManager);
    }


    private void setupToolbarMenu()
    {
        ((MasterPage)getActivity()).getToolbar().getMenu().clear();
        ((MasterPage)getActivity()).getToolbar().setOnMenuItemClickListener(new MenuHandler());
        java.util.List<String> menus=mV_list_song_bll.getMenuTitle();

        for(int i=0;i<menus.size();i++)
        {
            ((MasterPage)getActivity()).getToolbar().getMenu().add(menus.get(i));
            ((MasterPage)getActivity()).getToolbar().getMenu().getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }


    public class MenuHandler implements android.support.v7.widget.Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if(menuItem.getTitle().toString()==V_List_Song.menu_editlist)
            {
                //popup window for add list
                LSLog.Log_INFO("edit list");
            }
            return true;
        }
    }


}
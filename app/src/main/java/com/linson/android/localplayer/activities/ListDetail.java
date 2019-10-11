package com.linson.android.localplayer.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_Songs;

import java.util.ArrayList;
import java.util.List;

import app.bll.List_Song;
import app.bll.Song;
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
    private String mListName="";
    private app.bll.V_List_Song mV_list_song_bll;
    private app.bll.List_Song mList_song_bll;


    public static String argumentname_lid="lid";
    public static String argumentname_lname="lname";
    //endregion


    public ListDetail()
    {
        mV_list_song_bll=new V_List_Song(MainActivity.appContext);
        mList_song_bll=new List_Song(MainActivity.appContext);
    }


    //public ListDetail(int a)!todo 什么时候fragment需要从建立开始恢复？ 导致得到参数必须是通过argumentbundle。


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
        mListID=getArguments().getInt(argumentname_lid, mListID);//把类的自定义初始化禁止了，导致起不到初始化作用。糟糕的设计。
        mListName=getArguments().getString(argumentname_lname, "");
        mTvListname.setText(mListName);
        setupRecyleview();
        ((MasterPage)getActivity()).setupToolbarMenu(mV_list_song_bll.getMenuTitle(), new MenuHandler());
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
        else
        {
            res=new ArrayList<>();
        }

        Adapter_Songs adapter_songs=new Adapter_Songs(res);
        mRvSonglist.setAdapter(adapter_songs);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRvSonglist.setLayoutManager(linearLayoutManager);
    }


    public class MultiChoiceHandler implements DialogInterface.OnMultiChoiceClickListener
    {
        boolean[] ischooseList;

        public MultiChoiceHandler(boolean[] choose)
        {
            ischooseList=choose;
        }
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked)
        {
            ischooseList[which] = isChecked;
        }
    }

    public class MultiChoiceClick implements DialogInterface.OnClickListener
    {
        List<app.model.V_List_Song> allSongs;
        boolean[] ischooseList;

        public MultiChoiceClick(List<app.model.V_List_Song> Songs,boolean[] ischoose)
        {
            allSongs=Songs;
            ischooseList=ischoose;
        }

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if(which==-dialog.BUTTON_POSITIVE)
            {
                List<app.model.V_List_Song> newChoosed = mV_list_song_bll.getChooseList(allSongs, ischooseList);
                ((Adapter_Songs) mRvSonglist.getAdapter()).updateData(newChoosed);
                mList_song_bll.updateBatch(mListID, mV_list_song_bll.getsidList(newChoosed));
                dialog.dismiss();
            }
            else if(which==dialog.BUTTON_NEGATIVE)
            {
                dialog.dismiss();
            }
        }
    }


    public class MenuHandler implements android.support.v7.widget.Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if(menuItem.getTitle().toString()==V_List_Song.menu_editlist)
            {
                if(mListID!=0)
                {

                    List<app.model.V_List_Song> allSongs = mV_list_song_bll.getModelByZeroList();
                    List<app.model.V_List_Song> mySongs = ((Adapter_Songs) mRvSonglist.getAdapter()).getCloneData();
                    mySongs = mySongs == null ? new ArrayList<app.model.V_List_Song>() : mySongs;
                    CharSequence[] nameList = mV_list_song_bll.getNameList(allSongs);
                    boolean[] ischooseList = mV_list_song_bll.getIsChoose(allSongs, mySongs);

                    //数据检测,正常就弹出系统api，多选对话窗口。
                    //实时修改 是否选中的局部数据,确定按钮后才把局部数据更新到adapter和数据库。
                    if (allSongs != null && mySongs != null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListDetail.this.getContext());
                        builder.setMultiChoiceItems(nameList, ischooseList, new MultiChoiceHandler(ischooseList));

                        builder.setPositiveButton("确定", new MultiChoiceClick(allSongs,ischooseList ));
                        builder.setNegativeButton("取消", new MultiChoiceClick(allSongs,ischooseList ));
                        builder.show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "'所有歌曲'不需要编辑!", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    }
}
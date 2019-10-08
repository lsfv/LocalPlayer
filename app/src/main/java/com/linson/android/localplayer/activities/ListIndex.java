package com.linson.android.localplayer.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_List;
import com.linson.android.localplayer.activities.Dialog.Dialog_addlist;

import java.util.ArrayList;
import java.util.LinkedList;

import app.lslibrary.androidHelper.LSContentResolver;
import app.lslibrary.androidHelper.LSLog;
import app.model.List;


//1.一个列表展示（带删除） 2.一个新建列表。3。一个更新歌曲。
//3.独立模块。1.仅仅一个展示和一个删除逻辑。2.一个添加逻辑，并对2模块进行刷新。
//以上逻辑和母模板页面没有任何交互。
//!todo 2:新加的时候有数据库就出错，删除新家的也会出粗。  3.
//!todo getMenuTitle 并没有保证会加入所有菜单。
public class ListIndex extends Fragment implements MasterPage.IFragmentForMaster
{
    private RecyclerView mRvList;

    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mRvList = (RecyclerView) this.getActivity().findViewById(R.id.rv_list);
    }

    //endregion

    //region other member variable
    private app.bll.List mList_bll;
    //endregion


    public ListIndex()
    {
        mList_bll=new app.bll.List(MainActivity.appContext);
    }


    //一般只需要执行一次的代码(不涉及到activity)放到这里
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    //固定的加载视图的地方,初始和回退都会触发
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_index, container, false);
    }


    //一般绑定事件,和处理逻辑的地方.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //建立adapter，并给rcyleview。
        findControls();
        setupRecycle();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void setupRecycle()
    {
        java.util.List<List> res=mList_bll.getModelList("");
        Adapter_List adapter_list=new Adapter_List(res, new Adapter_listHandler());
        mRvList.setAdapter(adapter_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        mRvList.setLayoutManager(linearLayoutManager);
    }


    @Override
    public java.util.List<String> getMenuTitle()
    {
        return mList_bll.getMenuTitle();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {
        if(menuItem.getTitle().toString()==app.bll.List.menu_addlist)
        {
            //popup window for add list
            Dialog_addlist dialog_addlist=new Dialog_addlist(getContext(), new popupWindowHander());
            dialog_addlist.show();

        }
        else if(menuItem.getTitle().toString()==app.bll.List.menu_upsong)
        {
            LSLog.Log_INFO("update songs");
            //获得本地歌曲。插入到临时表：localsong。更新歌曲表:song
            LSContentResolver lsContentResolver=new LSContentResolver(MainActivity.appContext);
            java.util.List<LSContentResolver.SongInfo> localSongs= lsContentResolver.SearchSong(60*1000);

        }
        return true;
    }

    //region no static class
    public class Adapter_listHandler implements Adapter_List.IAdapter_ListHander
    {
        @Override
        public void onClickDelete(int index)
        {
            RecyclerView.Adapter adapter= mRvList.getAdapter();
            if(adapter instanceof Adapter_List)
            {
                Adapter_List adapter_list=(Adapter_List)adapter;
                app.model.List temp=adapter_list.getitem(index);
                mList_bll.delete(temp.L_id);//从数据库删除
                adapter_list.deleteItem(index);//从内存删除
            }
        }
    }

    public class popupWindowHander implements Dialog_addlist.Idialogcallback
    {
        @Override
        public void submit(String name)
        {
            app.model.List temp=new List(name, "info", "pic", "ps");
            int id= mList_bll.add(temp);
            temp=mList_bll.getModel(id);
            ( (Adapter_List)mRvList.getAdapter()).addItem(temp);
        }
    }

    //endregion
}
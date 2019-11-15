package com.linson.android.localplayer.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_List;
import com.linson.android.localplayer.activities.Dialog.Dialog_addlist;
import com.linson.android.localplayer.appHelper;

import app.lslibrary.androidHelper.LSContentResolver;
import app.lslibrary.androidHelper.LSUI;
import app.model.List;


//功能：展示列表。实现母模板的菜单功能。
public class ListIndex extends BaseFragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_index, container, false);
    }

    //一般绑定事件,和处理逻辑的地方.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mMyControls=new MyControls();//cut it into 'onCreate'
        setupRecycle();
        getMaster().setupToolbarMenu(app.bll.List.getMenuTitle(), new MenuHandler());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LSContentResolver.progressCheck(getActivity(), requestCode, grantResults, 1, new RequestHandler());
    }

    //region private functions
    private void setupRecycle()
    {
        java.util.List<List> res=app.bll.List.getAlllList();
        Adapter_List adapter_list=new Adapter_List(res, new Adapter_listHandler());
        mMyControls.mRvList.setAdapter(adapter_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        mMyControls.mRvList.setLayoutManager(linearLayoutManager);
    }


    private void realUpdataLocalSongs()
    {
        LSContentResolver lsContentResolver=new LSContentResolver(MainActivity.appContext);
        java.util.List<LSContentResolver.SongInfo> localSongs= lsContentResolver.SearchSong(60*1000);
        app.bll.LocalSong.updateSongsFromLocal(localSongs);
        Toast.makeText(getContext(), "更新完毕",Toast.LENGTH_SHORT ).show();
    }
    //endregion

    //region recycle's handler
    public class Adapter_listHandler implements Adapter_List.IAdapter_ListHander
    {
        @Override
        public void onClickDelete(int index)
        {
            RecyclerView.Adapter adapter = mMyControls.mRvList.getAdapter();
            if (adapter instanceof Adapter_List)
            {
                Adapter_List adapter_list = (Adapter_List) adapter;
                app.model.List temp = adapter_list.getitem(index);
                if (temp.L_id != appHelper.defaultListID)
                {
                    app.bll.List.delete(temp.L_id);//从数据库删除
                    adapter_list.deleteItem(index);//从内存删除
                }
                else//list 'allsongs' should't delete.
                {
                    Toast.makeText(getContext(), "you can't delete it", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onClickItem(int index)
        {
            if(mMyControls.mRvList.getAdapter()!=null)
            {
                app.model.List theItem = ((Adapter_List) mMyControls.mRvList.getAdapter()).getitem(index);
                ListDetail.StartMe(getFragmentManager(), theItem.L_id, theItem.L_name);
            }
        }

    }
    //endregion

    //region menu's  dialog callback handler
    public class popupWindowHander implements Dialog_addlist.Idialogcallback
    {
        @Override
        public void submit(String name)
        {
            if(name!=null && name.trim().length()!=0 && mMyControls.mRvList.getAdapter()!=null)
            {
                app.model.List temp = new List(name, "info", "pic", "ps");
                int id = app.bll.List.add(temp);
                temp = app.bll.List.getModel(id);
                ((Adapter_List) mMyControls.mRvList.getAdapter()).addItem(temp);
            }
        }
    }
    //endregion

    //region menu's  dialog click handler
    public class MenuHandler implements android.support.v7.widget.Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if(LSUI.getToolbarItemKeyID(menuItem).equals(app.bll.List.menu_addlist))
            {
                //popup window for add list
                Dialog_addlist dialog_addlist=new Dialog_addlist(getContext(), new popupWindowHander());
                dialog_addlist.show();

            }
            else if(LSUI.getToolbarItemKeyID(menuItem).equals(app.bll.List.menu_upsong))
            {
                LSContentResolver.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, new RequestHandler());
            }
            return true;
        }
    }
    //endregion

    //region permisson's handler
    public class RequestHandler implements LSContentResolver.VoidHandler
    {
        @Override
        public void doit()
        {
            realUpdataLocalSongs();
        }
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private RecyclerView mRvList;

        public MyControls()
        {
            mRvList = (RecyclerView)ListIndex.this.getActivity().findViewById(R.id.rv_list);
        }
    }
    //endregion
}
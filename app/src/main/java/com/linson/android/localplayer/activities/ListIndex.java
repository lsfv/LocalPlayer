package com.linson.android.localplayer.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_List;
import com.linson.android.localplayer.activities.Dialog.Dialog_addlist;

import app.lslibrary.androidHelper.LSActivity;
import app.lslibrary.androidHelper.LSContentResolver;
import app.lslibrary.androidHelper.LSLog;
import app.model.List;


//1.一个列表展示（带删除） 2.一个新建列表。3。一个更新歌曲。
//3.独立模块。1.仅仅一个展示和一个删除逻辑。2.一个添加逻辑，并对2模块进行刷新。
//以上逻辑和母模板页面没有任何交互。
//!todo getMenuTitle 并没有保证会加入所有菜单。
public class ListIndex extends Fragment
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
    private app.bll.LocalSong mLocalSong_bll;
    //endregion

    public ListIndex()
    {
        mList_bll=new app.bll.List(MainActivity.appContext);
        mLocalSong_bll=new app.bll.LocalSong(MainActivity.appContext);
    }

    //一般只需要执行一次的代码(不涉及到activity)放到这里
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    //固定的加载视图的地方,初始和回退都会触发.不明白为什么google要如此设计？
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
        findControls();

        setupRecycle();
        //最早放入到母模板回调，但是1.母模板不能列出所有页面跳转。2，要额外写回退时加载菜单。
        //所以放入到首次加载和回退都会执行的生命周期中，会比较好。
        setupToolbarMenu();
    }

    private void setupToolbarMenu()
    {
        ((MasterPage)getActivity()).getToolbar().getMenu().clear();
        ((MasterPage)getActivity()).getToolbar().setOnMenuItemClickListener(new MenuHandler());
        java.util.List<String> menus=mList_bll.getMenuTitle();

        for(int i=0;i<menus.size();i++)
        {
            ((MasterPage)getActivity()).getToolbar().getMenu().add(menus.get(i));
            ((MasterPage)getActivity()).getToolbar().getMenu().getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    private void setupRecycle()
    {
        java.util.List<List> res=mList_bll.getAllLists();
        Adapter_List adapter_list=new Adapter_List(res, new Adapter_listHandler());
        mRvList.setAdapter(adapter_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        mRvList.setLayoutManager(linearLayoutManager);
        mRvList.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LSContentResolver.progressCheck(getActivity(), requestCode, grantResults, 1, new LSContentResolver.VoidHandler()
        {
            @Override
            public void doit()
            {
                realUpdataLocalSongs();
            }
        });
    }

    private void realUpdataLocalSongs()
    {
        LSContentResolver lsContentResolver=new LSContentResolver(MainActivity.appContext);
        java.util.List<LSContentResolver.SongInfo> localSongs= lsContentResolver.SearchSong(60*1000);
        mLocalSong_bll.updateSongsFromLocal(localSongs);
        Toast.makeText(getContext(), "更新完毕",Toast.LENGTH_SHORT ).show();
    }

    //region no static class
    public class Adapter_listHandler implements Adapter_List.IAdapter_ListHander
    {
        @Override
        public void onClickDelete(int index)
        {
            RecyclerView.Adapter adapter = mRvList.getAdapter();
            if (adapter instanceof Adapter_List)
            {
                Adapter_List adapter_list = (Adapter_List) adapter;
                app.model.List temp = adapter_list.getitem(index);
                if (temp.L_id > 0)
                {
                    mList_bll.delete(temp.L_id);//从数据库删除
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
            app.model.List theItem= getAdapter().getitem(index);
            ListDetail fragment= new ListDetail();
            Bundle bundle=new Bundle();
            bundle.putInt(ListDetail.argumentname_lid, theItem.L_id);
            bundle.putString(ListDetail.argumentname_lname, theItem.L_name);
            fragment.setArguments(bundle);
            ((MasterPage)getActivity()).startPageWithBack(fragment);
        }
    }

    private Adapter_List getAdapter()
    {
        return (Adapter_List)mRvList.getAdapter();
    }

    public class popupWindowHander implements Dialog_addlist.Idialogcallback
    {
        @Override
        public void submit(String name)
        {
            if(name!=null && name.trim().length()!=0)
            {
                app.model.List temp = new List(name, "info", "pic", "ps");
                int id = mList_bll.add(temp);
                temp = mList_bll.getModel(id);
                ((Adapter_List) mRvList.getAdapter()).addItem(temp);
            }
        }
    }

    public class MenuHandler implements android.support.v7.widget.Toolbar.OnMenuItemClickListener
    {
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
                //获得本地歌曲。插入到临时表：localsong。更新歌曲表:song
                LSContentResolver.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, new LSContentResolver.VoidHandler()
                {
                    @Override
                    public void doit()
                    {
                        realUpdataLocalSongs();
                    }
                });
            }
            return true;
        }
    }
    //endregion
}
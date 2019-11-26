package com.linson.android.localplayer.activities;

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
import com.linson.android.localplayer.appHelperCommon;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSUI;
import app.lslibrary.pattern.LSObserver;
import app.model.List;
import app.model.PlayerBaseInfo;


//功能：展示列表。实现母模板的菜单功能。
public class ListIndex extends BaseFragment
{
    private LSObserver.IObserverListener<PlayerBaseInfo> mObserverListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_index, container, false);
    }

    //一般绑定事件,和处理逻辑的地方.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        try
        {
            super.onActivityCreated(savedInstanceState);
            mMyControls = new MyControls();//cut it into 'onCreate'
            setupRecycle();
            setupMasterMenu();
            getMaster().SetupTitle("我的播放列表");
            mObserverListener = new ServiceListener();
            MainActivity.baseInfoLSObserver.registerObserver(mObserverListener);
        } catch (Exception e)
        {
            LSLog.Log_Exception(e);
        }
    }

    private void setupMasterMenu()
    {
        getMaster().getBtnArray().get(0).setVisibility(View.GONE);
        getMaster().getBtnArray().get(1).setVisibility(View.GONE);
        getMaster().getBtnArray().get(2).setVisibility(View.VISIBLE);
        getMaster().getBtnArray().get(2).setImageDrawable(getResources().getDrawable(R.drawable.icon_newlist));
        getMaster().getBtnArray().get(2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Dialog_addlist dialog_addlist=new Dialog_addlist(getContext(), new popupWindowHander());
                dialog_addlist.show();
            }
        });
        //getMaster().setupToolbarMenu(app.bll.List.getMenuTitle(), new MenuHandler());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        MainActivity.baseInfoLSObserver.unRegisterObserver(mObserverListener);
    }

    //region private functions
    private void setupRecycle()
    {
        java.util.List<List> res=app.bll.List.getAlllList();
        PlayerBaseInfo info= appHelperCommon.getServiceBaseInfo(MainActivity.appServiceConnection);
        int lid=-1;
        if(info!=null)
        {
            lid=info.lid;
        }
        Adapter_List adapter_list=new Adapter_List(res, new Adapter_listHandler(),lid);
        mMyControls.mRvList.setAdapter(adapter_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        mMyControls.mRvList.setLayoutManager(linearLayoutManager);
    }
    //endregion

    //region baseinfoListener
    public class ServiceListener implements LSObserver.IObserverListener<PlayerBaseInfo>
    {
        @Override
        public void onHappen(PlayerBaseInfo p)
        {
            ((Adapter_List)mMyControls.mRvList.getAdapter()).setMplayingLID(p.lid);
        }
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
                if (temp.L_id != V_List_Song.defaultListID)
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
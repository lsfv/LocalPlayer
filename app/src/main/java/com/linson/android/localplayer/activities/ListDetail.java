package com.linson.android.localplayer.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import com.linson.android.localplayer.appHelper.appHelperCommon;
import com.linson.android.localplayer.appHelper.appHelperPlayerBaseInfo;

import java.util.ArrayList;
import java.util.List;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;
import app.lslibrary.androidHelper.LSUI;
import app.lslibrary.pattern.LSObserver;
import app.model.PlayerBaseInfo;

import static com.linson.android.localplayer.appHelper.appHelperPlayerBaseInfo.*;
import static com.linson.android.localplayer.appHelper.appHelperPlayerBaseInfo.getServiceBaseInfo;

//功能。1获得参数。2初始化列表。3实现菜单功能。 通过内部类对activity顶级类功能的划分，发挥了内部类的分担职责的职能。
public class ListDetail extends BaseFragment
{
    public static final String STRLID="lid";
    public static final String STRLNAME="LNAME";

    private int mListID=appHelperCommon.defaultListID;
    private String mListName="";

    private LSObserver.IObserverListener<app.model.PlayerBaseInfo> mObserverListener;

    public static void StartMe(FragmentManager fragmentManager, int lid,String lname)
    {
        ListDetail fragment= new ListDetail();
        Bundle bundle=new Bundle();
        bundle.putInt(STRLID, lid);
        bundle.putString(STRLNAME, lname);
        fragment.setArguments(bundle);
        appHelperCommon.startPageWithBack(fragmentManager, fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_detail, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mMyControls=new MyControls();//cut it into 'onCreate'
        initParameter();
        setupWidget();
        setupRecyleview();
        getMaster().setupToolbarMenu(app.bll.V_List_Song.getMenuTitle(), new MenuHandler());

        mObserverListener=new oob();
        appHelperPlayerBaseInfo.baseInfoLSObserver.registerObserver(mObserverListener);
        LSLog.Log_INFO(String.format("id:%d,name:%s",mListID,mListName));
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        baseInfoLSObserver.unRegisterObserver(mObserverListener);
    }

    //region private functions
    private void setupWidget()
    {
        mMyControls.mTvListname.setText(mListName);
    }

    private void initParameter()
    {
        mListID=getArguments().getInt(STRLID, mListID);//把类的自定义初始化禁止了，导致起不到初始化作用。糟糕的设计。
        mListName=getArguments().getString(STRLNAME, "");
    }

    private void setupRecyleview()
    {
        List<app.model.V_List_Song> res=app.bll.V_List_Song.getModelByLid(mListID);
        if(res==null)
        {
            res=new ArrayList<>();
        }

        int playingIndex=-1;
        PlayerBaseInfo baseInfo=getServiceBaseInfo(MainActivity.appServiceConnection);
        if(baseInfo!=null && baseInfo.lid==mListID)
        {
            playingIndex=baseInfo.index;
        }

        Adapter_Songs adapter_songs=new Adapter_Songs(res,new RecycleHandler(),playingIndex,0);
        mMyControls.mRvSonglist.setAdapter(adapter_songs);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mMyControls.mRvSonglist.setLayoutManager(linearLayoutManager);
    }
    //endregions

    //region infoobserver
    public class oob implements LSObserver.IObserverListener<app.model.PlayerBaseInfo>
    {

        @Override
        public void onHappen(app.model.PlayerBaseInfo p)
        {
            ((Adapter_Songs) ((Adapter_Songs) mMyControls.mRvSonglist.getAdapter())).showImagePlaying(p.index);
        }
    }
    //endregion

    //region recycleView's handler
    public class RecycleHandler implements Adapter_Songs.IItemHander
    {
        @Override
        public void onClick(int lid,int index)
        {
            //连接服务，播放歌曲。
            if(MainActivity.appServiceConnection !=null && MainActivity.appServiceConnection.mPlayerProxy!=null)
            {
                try
                {
                    app.model.PlayerBaseInfo info=MainActivity.appServiceConnection.mPlayerProxy.getBaseInfo();
                    if(info!=null && info.lid!=mListID || info.index!=index)//点击正在播放的应该无响应。
                    {
                        MainActivity.appServiceConnection.mPlayerProxy.playSong(lid, index);
                        info=MainActivity.appServiceConnection.mPlayerProxy.getBaseInfo();
                        baseInfoLSObserver.NoticeObsserver(info);
                    }
                    else
                    {
                        PlaySong.StartMe(getFragmentManager(), lid, index);
                    }
                }
                catch (Exception e)
                {
                    LSLog.Log_Exception(e);
                }
            }
        }
    }
    //endregion

    //region Menu's Handler
    public class MenuHandler implements android.support.v7.widget.Toolbar.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            if(LSUI.getToolbarItemKeyID(menuItem).equals(V_List_Song.menu_editlist))
            {
                if(mListID>1 && getActivity()!=null)
                {

                    List<app.model.V_List_Song> allSongs = app.bll.V_List_Song.getModelByLid(appHelperCommon.defaultListID);
                    List<app.model.V_List_Song> mySongs = ((Adapter_Songs) mMyControls.mRvSonglist.getAdapter()).getCloneData();
                    mySongs = mySongs == null ? new ArrayList<app.model.V_List_Song>() : mySongs;
                    CharSequence[] nameList = app.bll.V_List_Song.getNameList(allSongs);
                    boolean[] ischooseList = app.bll.V_List_Song.getIsChoose(allSongs, mySongs);

                    //数据检测,正常就弹出系统api，多选对话窗口。
                    //实时修改 是否选中的局部数据,确定按钮后才把局部数据更新到adapter和数据库。
                    if (allSongs != null && mySongs != null && getActivity()!=null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListDetail.this.getContext());
                        builder.setMultiChoiceItems(nameList, ischooseList, new MultiChoiceHandler(ischooseList));

                        builder.setPositiveButton("确定", new MultiChoiceClick(allSongs,ischooseList ));
                        builder.setNegativeButton("取消", new MultiChoiceClickCancel());
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
    //endregion

    //region MultiChoiceListener onchoose's handler
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
    //endregion

    //region MultiChoiceListener onclick's handler
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
            if(which==dialog.BUTTON_POSITIVE && getActivity()!=null)
            {
                //逻辑是硬伤，这里获得是默认清单的数据，包含了清单数据,如lid，其实还是应该用song这种更正确的类。
                List<app.model.V_List_Song> newChoosed = app.bll.V_List_Song.getChooseList(allSongs, ischooseList);
                app.bll.List_Song.updateBatch(mListID, app.bll.V_List_Song.getsidList(newChoosed));
                newChoosed=app.bll.V_List_Song.getModelByLid(mListID);
                ((Adapter_Songs) mMyControls.mRvSonglist.getAdapter()).updateData(newChoosed,-1);

                dialog.dismiss();
            }
            else if(which==dialog.BUTTON_NEGATIVE)
            {
                dialog.dismiss();
            }
        }
    }
    //endregion

    //region MultiChoiceListener onclick cancel's handler
    public class MultiChoiceClickCancel implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.dismiss();
        }
    }
    //endregion

    //region The class of FindControls
    private MyControls mMyControls=null;
    public class MyControls
    {
        private TextView mTvListname;
        private RecyclerView mRvSonglist;

        public MyControls()
        {
            mTvListname = (TextView) ListDetail.this.getActivity().findViewById(R.id.tv_listname);
            mRvSonglist = (RecyclerView) ListDetail.this.getActivity().findViewById(R.id.rv_songlist);
        }
    }
    //endregion
}
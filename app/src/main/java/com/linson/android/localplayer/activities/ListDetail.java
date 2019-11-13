package com.linson.android.localplayer.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
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

import com.linson.android.localplayer.AIDL.IPlayer;
import com.linson.android.localplayer.R;
import com.linson.android.localplayer.activities.Adapter.Adapter_Songs;
import com.linson.android.localplayer.appHelper;

import java.util.ArrayList;
import java.util.List;

import app.bll.V_List_Song;
import app.lslibrary.androidHelper.LSLog;
import app.model.PlayerBaseInfo;

@SuppressWarnings("FieldCanBeLocal")
public class ListDetail extends BaseFragment
{
    private TextView mTvListname;
    private RecyclerView mRvSonglist;

    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        if(getActivity()!=null)
        {
            mTvListname = (TextView) getActivity().findViewById(R.id.tv_listname);
            mRvSonglist = (RecyclerView) getActivity().findViewById(R.id.rv_songlist);
        }
    }
    //endregion

    //region other member variable
    private int mListID=appHelper.defaultListID;
    private String mListName="";
    private MyConnection mMyConnection;


    public static String argumentname_lid="lid";
    public static String argumentname_lname="lname";

    //endregion





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
        findControls();
        mListID=getArguments().getInt(argumentname_lid, mListID);//把类的自定义初始化禁止了，导致起不到初始化作用。糟糕的设计。
        mListName=getArguments().getString(argumentname_lname, "");
        mTvListname.setText(mListName);
        mMyConnection=new MyConnection();
        if(getActivity()!=null)
        {
            getActivity().bindService(appHelper.getServiceIntent(), mMyConnection, Context.BIND_AUTO_CREATE);
        }

        setupRecyleview();
        getMaster().setupToolbarMenu(app.bll.V_List_Song.getMenuTitle(), new MenuHandler());
        LSLog.Log_INFO(String.format("init listdetail. id:%d,name:%s",mListID,mListName));
    }


    private void setupRecyleview()
    {
        List<app.model.V_List_Song> res=app.bll.V_List_Song.getModelByLid(mListID);
        if(res==null)
        {
            res=new ArrayList<>();
        }

        Adapter_Songs adapter_songs=new Adapter_Songs(res,new RecycleHandler(),-1);
        mRvSonglist.setAdapter(adapter_songs);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRvSonglist.setLayoutManager(linearLayoutManager);
    }


    //region not static class: extend for top class


    public class MyConnection implements ServiceConnection
    {
        private IPlayer mPlayer;
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mPlayer=IPlayer.Stub.asInterface(service);
            if(mPlayer!=null)
            {
                try
                {
                    PlayerBaseInfo baseInfo = mPlayer.getBaseInfo();
                    if(baseInfo.lid==mListID && getActivity()!=null)
                    {
                        ((Adapter_Songs) mRvSonglist.getAdapter()).showImagePlaying(baseInfo.index);
                    }
                } catch (Exception e)
                {
                    LSLog.Log_Exception(e);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
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
            if(which==dialog.BUTTON_POSITIVE && getActivity()!=null)
            {
                //逻辑是硬伤，这里获得是默认清单的数据，包含了清单数据,如lid，其实还是应该用song这种更正确的类。导致最初直接给list来显示，用了错lid.
                //那就只用list的song id。重新读一次数据库把。这个逻辑才是正确，避免以后又添加了什么新数据，手工是避免不了错误的。
                List<app.model.V_List_Song> newChoosed = app.bll.V_List_Song.getChooseList(allSongs, ischooseList);
                app.bll.List_Song.updateBatch(mListID, app.bll.V_List_Song.getsidList(newChoosed));
                newChoosed=app.bll.V_List_Song.getModelByLid(mListID);
                ((Adapter_Songs) mRvSonglist.getAdapter()).updateData(newChoosed,-1);

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
            if(menuItem.getTitle().toString().equals(V_List_Song.menu_editlist))
            {
                if(mListID>1 && getActivity()!=null)
                {

                    List<app.model.V_List_Song> allSongs = app.bll.V_List_Song.getModelByLid(appHelper.defaultListID);
                    List<app.model.V_List_Song> mySongs = ((Adapter_Songs) mRvSonglist.getAdapter()).getCloneData();
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


    public class RecycleHandler implements Adapter_Songs.IItemHander
    {
        @Override
        public void onClick(int lid,int index)
        {
            //连接服务，播放歌曲。
            if(mMyConnection!=null && mMyConnection.mPlayer!=null)
            {
                try
                {
                    app.model.PlayerBaseInfo info=mMyConnection.mPlayer.getBaseInfo();
                    if(info.lid!=mListID || info.index!=index && mRvSonglist.getAdapter()!=null)//点击正在播放的应该无响应。
                    {
                        mMyConnection.mPlayer.playSong(lid,index);
                        ((Adapter_Songs) ((Adapter_Songs) mRvSonglist.getAdapter())).showImagePlaying(index);
                    }
                    else
                    {
                        Fragment fragment=new PlaySong();
                        Bundle bundle=new Bundle();
                        bundle.putInt(PlaySong.argumentLsid, lid);
                        bundle.putInt(PlaySong.argumentindex, index);
                        fragment.setArguments(bundle);

                        appHelper.startPageWithBack(getFragmentManager(),fragment);
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
}
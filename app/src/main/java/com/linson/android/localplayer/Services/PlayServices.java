package com.linson.android.localplayer.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.linson.android.localplayer.AIDL.IPlayer;

import java.util.ArrayList;
import java.util.List;

import app.lslibrary.androidHelper.LSLog;
import app.model.PlayerBaseInfo;
import app.model.V_List_Song;

public class PlayServices extends Service
{
    public PlayServices()
    {
        LSLog.Log_INFO("services constr");
    }

    @Nullable @Override
    public IBinder onBind(Intent intent)
    {
        LSLog.Log_INFO("services");
        return new RemoteServiceProxy();
    }


    //所有歌曲全部copy过来。1万首歌曲的信息一般也就1m，可以考虑极端情况，但是不要顾全所有极端情况。满足95%的用户就达到标准了.
    //客户端提出需求，服务端实现接口。
    public class RemoteServiceProxy extends IPlayer.Stub
    {
        //region test funcion discard
        @Override
        public int add(int a, int b) throws RemoteException
        {
            return a+b;
        }

        @Override
        public void modifymodel(V_List_Song mm) throws RemoteException
        {
            mm.L_name+="v1";
        }
        //endregion

        private List<app.model.V_List_Song> mAllSongs=null;
        private app.model.PlayerBaseInfo mBaseInfo=new app.model.PlayerBaseInfo();

        private String displayStatus()
        {
            return (String.format("status:%d,index:%d.",mBaseInfo.status,mBaseInfo.index));
        }

        @Override
        public int playSong(int index,List<app.model.V_List_Song> songs)
        {

            LSLog.Log_INFO("i get index:"+index+".song count:"+songs.size());
            int res=-1;
            if(true)//任何状态下都可以进行播放动作。
            {
                if(songs!=null && index>=0 && index<songs.size())
                {
                    mAllSongs=songs;
                    mBaseInfo.index=(int) index;
                    mBaseInfo.status=playNow();
                    if(mBaseInfo.status==PlayerBaseInfo.status_playing)
                    {
                        LSLog.Log_INFO("play ok:"+displayStatus());
                        res=1;
                    }
                    else
                    {
                        LSLog.Log_INFO("play error"+displayStatus());
                    }
                }
            }
            return res;
        }

        @Override
        public int playOneSong(int index)
        {
            int res=-1;
            if(mBaseInfo.status!=PlayerBaseInfo.status_init)//必须初始化之后才能点播
            {
                mBaseInfo.index=mBaseInfo.index>=0?mBaseInfo.index:0;
                mBaseInfo.index=mBaseInfo.index<mAllSongs.size()?mBaseInfo.index:mAllSongs.size()-1;

                mBaseInfo.status=playNow();
                if(mBaseInfo.status==PlayerBaseInfo.status_playing)
                {
                    res=1;
                }
            }
            LSLog.Log_INFO("play playOneSong "+displayStatus());
            return res;
        }

        @Override
        public int playOrPause()
        {
            if(mBaseInfo.status==PlayerBaseInfo.status_pause)//只有playing和pause才能互转
            {
                mBaseInfo.status=PlayerBaseInfo.status_playing;
            }
            else if(mBaseInfo.status==PlayerBaseInfo.status_playing)
            {
                mBaseInfo.status=PlayerBaseInfo.status_pause;
            }
            LSLog.Log_INFO("playOrPause:"+displayStatus());
            return 1;
        }


        private int playNow()
        {
            LSLog.Log_INFO("suppess playing success:"+displayStatus());
            return PlayerBaseInfo.status_playing;
        }


        @Override
        public int pre() throws RemoteException
        {
            int res=-1;
            if(mBaseInfo.status!=PlayerBaseInfo.status_init)//必须初始化之后才能点播
            {
                mBaseInfo.index--;
                mBaseInfo.index=mBaseInfo.index>=0?mBaseInfo.index:0;
                mBaseInfo.index=mBaseInfo.index<mAllSongs.size()?mBaseInfo.index:mAllSongs.size()-1;

                mBaseInfo.status=playNow();
                if(mBaseInfo.status==PlayerBaseInfo.status_playing)
                {
                    res=1;
                }
            }
            LSLog.Log_INFO("pre res"+displayStatus());
            return res;
        }

        @Override
        public int next() throws RemoteException
        {
            int res=-1;
            if(mBaseInfo.status!=PlayerBaseInfo.status_init)//必须初始化之后才能点播
            {
                mBaseInfo.index++;
                mBaseInfo.index=mBaseInfo.index>=0?mBaseInfo.index:0;
                mBaseInfo.index=mBaseInfo.index<mAllSongs.size()?mBaseInfo.index:mAllSongs.size()-1;

                mBaseInfo.status=playNow();
                if(mBaseInfo.status==PlayerBaseInfo.status_playing)
                {
                    res=1;
                }
            }
            LSLog.Log_INFO("next res"+displayStatus());
            return res;
        }


        @Override
        public int changemode(int mode) throws RemoteException
        {
            mBaseInfo.playMode=mode;//任何时候都可以修改播放模式
            LSLog.Log_INFO("changemode "+displayStatus());
            return 1;
        }
    }
}
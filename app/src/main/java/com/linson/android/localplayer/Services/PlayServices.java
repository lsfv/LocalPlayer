package com.linson.android.localplayer.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.linson.android.localplayer.AIDL.IPlayer;

import java.util.ArrayList;
import java.util.List;

import app.lslibrary.androidHelper.LSLog;
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
        private List<app.model.V_List_Song> mAllSongs=new ArrayList<>();
        private int mPlayingIndex=-1;
        private int mStatus=-1;
        private int mPlayMode=1;


//        public static List<app.model.Song> mSongs=null;
//        public static int mSongsindex=-1;
//        public static int mMode=1;//single loop, 2.random 3.sequance.
//        public static int mStatus=0;//0.init   1.playing.   2.pause  3.error:no file 4.error format
//        public static MediaPlayer mMediaPlayer=new MediaPlayer();

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

        @Override
        public void setAllSongs(List<app.model.V_List_Song> songs)
        {
            mAllSongs=songs;
        }

        @Override
        public int playSong(int index)
        {
            LSLog.Log_INFO("play "+index);
            return 0;
        }
    }
}
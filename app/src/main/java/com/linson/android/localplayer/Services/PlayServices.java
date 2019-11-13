package com.linson.android.localplayer.Services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.linson.android.localplayer.AIDL.IPlayer;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import app.lslibrary.androidHelper.LSLog;
import app.model.PlayerBaseInfo;


public class PlayServices extends Service
{
    public RemoteServiceProxy mRemoteServiceProxy;

    @Override
    public void onCreate()
    {
        LSLog.Log_INFO("");
        super.onCreate();
        app.bll.MusicDB.setDBContext(getApplicationContext());
        mRemoteServiceProxy = new RemoteServiceProxy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        LSLog.Log_INFO("");
        return mRemoteServiceProxy;
    }

    @Override
    public void onDestroy()
    {
        LSLog.Log_INFO("");
        mRemoteServiceProxy.ReasePlayer();//先手动调用释放播放器。
        app.bll.MusicDB.setDBContext(null);
        super.onDestroy();
    }

    public static class RemoteServiceProxy extends IPlayer.Stub
    {
        public static int gotoStartSecond = 5000;

        public MediaPlayer mMediaPlayer;
        private app.model.PlayerBaseInfo mBaseInfo;

        public RemoteServiceProxy()
        {
            mBaseInfo = new PlayerBaseInfo();
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new OnComplete());
        }


        @SuppressLint("DefaultLocale")
        private String displayStatus()
        {
            return (String.format("status:%d,index:%d.", mBaseInfo.status, mBaseInfo.index));
        }

        //set list id and song's index and play.
        @Override
        public int playSong(int lid,int index)
        {
            int res = -1;
            List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(lid);

            if (1==1)//任何状态下都可以进行播放动作。
            {
                if (songs != null && index >= 0 && index < songs.size())
                {
                    mBaseInfo.lid =lid;
                    mBaseInfo.index =index;
                    mBaseInfo.status = playNow();
                    if (mBaseInfo.status == PlayerBaseInfo.status_playing)
                    {
                        LSLog.Log_INFO("play ok:" + displayStatus());
                        res = 1;
                    }
                    else
                    {
                        LSLog.Log_INFO("play error." + displayStatus());
                    }
                }
            }
            return res;
        }

        @Override
        public int playOneSong(int index)
        {
            int res = -1;
            if (mBaseInfo.status != PlayerBaseInfo.status_init)//必须初始化之后才能点播
            {
                List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(mBaseInfo.lid);

                mBaseInfo.index = index;
                mBaseInfo.index = mBaseInfo.index >= 0 ? mBaseInfo.index : 0;
                mBaseInfo.index = mBaseInfo.index < songs.size() ? mBaseInfo.index : songs.size() - 1;

                mBaseInfo.status = playNow();
                if (mBaseInfo.status == PlayerBaseInfo.status_playing)
                {
                    res = 1;
                }
            }
            LSLog.Log_INFO("play playOneSong " + displayStatus());
            return res;
        }

        @Override
        public int playOrPause()
        {
            if (mBaseInfo.status == PlayerBaseInfo.status_pause)//只有playing和pause才能互转
            {
                mMediaPlayer.start();
                mBaseInfo.status = PlayerBaseInfo.status_playing;
            } else if (mBaseInfo.status == PlayerBaseInfo.status_playing)
            {
                mMediaPlayer.pause();
                mBaseInfo.status = PlayerBaseInfo.status_pause;
            }
            LSLog.Log_INFO("playOrPause:" + displayStatus());
            return 1;
        }

        private void showlist()
        {
            List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(mBaseInfo.lid);
            for (int i = 0; i < songs.size(); i++)
            {
                LSLog.Log_INFO("list name:" + songs.get(i).L_name + ". musci name:" + songs.get(i).S_musicName + ". music path:" + songs.get(i).S_path);
            }
        }

        private int playNow()
        {
            int res = PlayerBaseInfo.status_error_other;
            String songPath = "";
            List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(mBaseInfo.lid);
            try
            {
                songPath = songs.get(mBaseInfo.index).S_path;
            } catch (Exception e)
            {
                LSLog.Log_Exception(e);
            }
            LSLog.Log_INFO(songPath);
            if (songPath != null && songPath.trim().length() > 0)
            {
                try
                {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(songPath);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    res = PlayerBaseInfo.status_playing;
                } catch (IOException e)
                {
                    res = PlayerBaseInfo.status_error_nofile;
                    LSLog.Log_Exception(e);
                } catch (Exception e)
                {
                    res = PlayerBaseInfo.status_error_other;
                    LSLog.Log_Exception(e);
                }
            } else
            {
                res = PlayerBaseInfo.status_error_nofile;
            }

            return res;
        }


        @Override
        public int pre()
        {
            int res = -1;
            if (mBaseInfo.status != PlayerBaseInfo.status_init)//必须初始化之后才能点播
            {
                List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(mBaseInfo.lid);
                if (mMediaPlayer.getCurrentPosition() >= gotoStartSecond)
                {
                    mBaseInfo.status = playNow();
                    if (mBaseInfo.status == PlayerBaseInfo.status_playing)
                    {
                        res = 1;
                    }
                }
                else
                {
                    mBaseInfo.index--;
                    mBaseInfo.index = mBaseInfo.index >= 0 ? mBaseInfo.index : 0;
                    mBaseInfo.index = mBaseInfo.index < songs.size() ? mBaseInfo.index : songs.size() - 1;

                    mBaseInfo.status = playNow();
                    if (mBaseInfo.status == PlayerBaseInfo.status_playing)
                    {
                        res = 1;
                    }
                }
            }
            LSLog.Log_INFO("pre res" + displayStatus());
            return res;
        }

        @Override
        public int next()
        {
            int res = -1;
            if (mBaseInfo.status != PlayerBaseInfo.status_init)//必须初始化之后才能点播
            {
                List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(mBaseInfo.lid);
                mBaseInfo.index++;
                mBaseInfo.index = mBaseInfo.index >= 0 ? mBaseInfo.index : 0;
                mBaseInfo.index = mBaseInfo.index < songs.size() ? mBaseInfo.index : songs.size() - 1;

                mBaseInfo.status = playNow();
                if (mBaseInfo.status == PlayerBaseInfo.status_playing)
                {
                    res = 1;
                }
            }
            LSLog.Log_INFO("next res" + displayStatus());
            return res;
        }


        @Override
        public int changemode(int mode)
        {
            mBaseInfo.playMode = mode;//任何时候都可以修改播放模式
            LSLog.Log_INFO("changemode " + displayStatus());
            return 1;
        }

        @Override
        public app.model.PlayerBaseInfo getBaseInfo()
        {
            return mBaseInfo;
        }


        @Override
        public void ondisconnected()
        {
            LSLog.Log_INFO("on disconnected.beacause it's a share connection.  a connecter will use it later.don't clear anything.");
        }


        public void ReasePlayer()
        {
            if (mMediaPlayer.isPlaying())
            {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }

        //region onsongOver auto...
        public class OnComplete implements OnCompletionListener
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                LSLog.Log_INFO("song is over.auto start:" + mBaseInfo.getModeName());
                if (mBaseInfo.playMode == PlayerBaseInfo.playModel_single)
                {
                } else if (mBaseInfo.playMode == PlayerBaseInfo.playModel_singleRepeat)
                {
                    playNow();
                } else if (mBaseInfo.playMode == PlayerBaseInfo.playModel_all)
                {
                    try
                    {
                        next();
                    } catch (Exception e)
                    {
                        LSLog.Log_Exception(e);
                    }
                } else if (mBaseInfo.playMode == PlayerBaseInfo.playModel_random)
                {
                    try
                    {
                        List<app.model.V_List_Song> songs= app.bll.V_List_Song.getModelByLid(mBaseInfo.lid);
                        Random random = new Random();
                        int a = random.nextInt(songs.size());
                        a = a % songs.size() - 1;
                        playOneSong(a);
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
}
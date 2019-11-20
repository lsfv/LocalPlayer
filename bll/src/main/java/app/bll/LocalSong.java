package app.bll;

import android.content.Context;

import app.lslibrary.androidHelper.LSContentResolver;

public abstract class LocalSong
{
    private static  app.dal.LocalSong dal=new app.dal.LocalSong();

    public static int add(app.model.LocalSong model)
    {
        return dal.add(model);
    }

    public static boolean delete(int id)
    {
        return dal.delete(id);
    }

    public static boolean update(app.model.LocalSong model)
    {
        return dal.update(model);
    }

    public static app.model.LocalSong getModel(int id)
    {
        return dal.getModel(id);
    }

    public static java.util.List<app.model.LocalSong> getModelList(String where)
    {
        return dal.getModelList(where);
    }


    //extend
    //region what will do after permission is ok.
    public static class  UpdateDB_Songs implements LSContentResolver.VoidHandler
    {
        private Context mContext;
        public UpdateDB_Songs(Context context)
        {
            mContext=context;
        }
        @Override
        public void doit()
        {
            LSContentResolver lsContentResolver=new LSContentResolver(mContext);
            java.util.List<LSContentResolver.SongInfo> localSongs= lsContentResolver.SearchSong(60*1000);
            app.bll.LocalSong.updateSongsFromLocal(localSongs);
        }
    }
    //endregion

    public static  void clear()
    {
        dal.clear();
    }

    public static  boolean updateSongsFromLocal(java.util.List<LSContentResolver.SongInfo> songInfos)
    {
        //1.获得本地歌曲。2.插入到临时表：localsong。3.更新歌曲表:song
        clear();
        for (int i = 0; i < songInfos.size(); i++)
        {
            LSContentResolver.SongInfo templocal = songInfos.get(i);
            app.model.LocalSong temp = new app.model.LocalSong();
            temp.LS_version = 0;
            temp.LS_songID = templocal.songId;
            temp.LS_path = templocal.path;
            temp.LS_musicName = templocal.musicName;
            temp.LS_duration = templocal.duration;
            temp.LS_artist = templocal.artist;
            add(temp);
        }

        dal.updateSongs();
        V_List_Song.cacheIsLast=false;//只要更新过本地。那么缓存设置为过期。必须重新读起数据库。
        return true;
    }
}
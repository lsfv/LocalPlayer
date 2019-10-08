package app.bll;

import android.content.Context;

import app.lslibrary.androidHelper.LSContentResolver;

public class LocalSong
{
    private app.dal.LocalSong dal;
    public LocalSong(Context context)
    {
        dal=new app.dal.LocalSong(context);
    }

    public int add(app.model.LocalSong model)
    {
        return dal.add(model);
    }

    public boolean delete(int id)
    {
        return dal.delete(id);
    }

    public boolean update(app.model.LocalSong model)
    {
        return dal.update(model);
    }

    public app.model.LocalSong getModel(int id)
    {
        return dal.getModel(id);
    }

    public java.util.List<app.model.LocalSong> getModelList(String where)
    {
        return dal.getModelList(where);
    }


    //extend
    //extend
    public void clear()
    {
        dal.clear();
    }

    public boolean updateSongsFromLocal(java.util.List<LSContentResolver.SongInfo> songInfos)
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
        return true;
    }
}
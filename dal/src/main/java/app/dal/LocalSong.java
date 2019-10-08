package app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Calendar;

import app.dal.dbhelper.Music;

public class LocalSong
{
    private Music DBHelper;
    public LocalSong(Context context)
    {
        DBHelper=Music.getInstance(context);
    }

    public int add(app.model.LocalSong model)
    {
        int res=0;
        SQLiteDatabase db=DBHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            String sql = "insert into LocalSong (LS_musicName,LS_artist,LS_duration,LS_path,LS_songID,LS_version) values (?,?,?,?,?,?)";
            Object[] parameters = new Object[]{model.LS_musicName,model.LS_artist,model.LS_duration,model.LS_path,model.LS_songID,model.LS_version};
            db.execSQL(sql, parameters);

            Cursor cursor = db.rawQuery("select LS_id from LocalSong order by LS_id desc limit 1", null);
            if (cursor.moveToFirst())
            {
                res = cursor.getInt(0);
            }

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
        return res;
    }

    public boolean delete(int id)
    {
        int res=DBHelper.getWritableDatabase().delete("LocalSong", "LS_id=?", new String[]{id+""});
        return res==1?true:false;
    }

    public boolean update(app.model.LocalSong model)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("LS_musicName", model.LS_musicName);
        contentValues.put("LS_artist", model.LS_artist);
        contentValues.put("LS_duration", model.LS_duration);
        contentValues.put("LS_path", model.LS_path);
        contentValues.put("LS_songID", model.LS_songID);
        contentValues.put("LS_version", model.LS_version);

        int res=DBHelper.getWritableDatabase().update("LocalSong", contentValues,"LS_id=?" , new String[]{model.LS_id+""});
        return res==1?true:false;
    }

    public app.model.LocalSong getModel(int id)
    {
        app.model.LocalSong model=null;

        Cursor cursor = DBHelper.getReadableDatabase().rawQuery("select LS_id,LS_musicName,LS_artist,LS_duration,LS_path,LS_songID,LS_version from LocalSong where LS_id=" + id, null);
        if (cursor.moveToFirst())
        {
            model = new app.model.LocalSong();
            model.LS_id = cursor.getInt(0);
            model.LS_musicName = cursor.getString(1);
            model.LS_artist = cursor.getString(2);
            model.LS_duration = cursor.getInt(3);
            model.LS_path = cursor.getString(4);
            model.LS_songID = cursor.getInt(5);
            model.LS_version = cursor.getInt(6);

        }

        return model;
    }

    public java.util.List<app.model.LocalSong> getModelList(String appendWhereSql)
    {
        java.util.List<app.model.LocalSong> lists = new ArrayList<>();
        Cursor cursor = DBHelper.getReadableDatabase().rawQuery("select LS_id,LS_musicName,LS_artist,LS_duration,LS_path,LS_songID,LS_version from LocalSong " + appendWhereSql, null);
        if (cursor.moveToFirst())
        {
            int size = cursor.getCount();
            for (int i = 0; i < size; i++)
            {
                app.model.LocalSong model = new app.model.LocalSong();
            model.LS_id = cursor.getInt(0);
            model.LS_musicName = cursor.getString(1);
            model.LS_artist = cursor.getString(2);
            model.LS_duration = cursor.getInt(3);
            model.LS_path = cursor.getString(4);
            model.LS_songID = cursor.getInt(5);
            model.LS_version = cursor.getInt(6);


                lists.add(model);
                cursor.moveToNext();
            }
        }

        return lists;
    }


    //extend
    public void clear()
    {
        DBHelper.getWritableDatabase().execSQL("delete from LocalSong");
    }

    public void updateSongs()
    {
        /*跟新存在的version，添加新的，删除odl version */
        int version=Calendar.getInstance().get(Calendar.SECOND)+Calendar.getInstance().get(Calendar.MINUTE)*100+Calendar.getInstance().get(Calendar.HOUR)*10000+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)*1000000;
        String strVersion=  version+"";
        String sql1="update Song set S_version="+strVersion+" where  S_path in (select LS_path from localSong)";
        String sql2="delete from localSong where LS_path in(select S_path from Song)";
        String sql3="insert into Song(S_musicName,S_artist,S_duration,S_path,S_songID,S_version,S_ps) select LS_musicName,LS_artist,LS_duration,LS_path,LS_songID,"+strVersion+",'' from localsong";
        String sql4="delete from Song where S_version !="+strVersion;
        String sql5="delete from localSong";

        SQLiteDatabase db= DBHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.execSQL(sql1);
            db.execSQL(sql2);
            db.execSQL(sql3);
            db.execSQL(sql4);
            db.execSQL(sql5);
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }
}
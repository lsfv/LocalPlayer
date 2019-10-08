package app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

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
}
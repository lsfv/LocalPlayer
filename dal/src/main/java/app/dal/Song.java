package app.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import app.dal.dbhelper.Music;

public class Song
{
    private Music DBHelper;
    public Song()
    {
        DBHelper=Music.getInstance();
    }

    public int add(app.model.Song model)
    {
        int res=0;
        SQLiteDatabase db=DBHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            String sql = "insert into Song (S_musicName,S_artist,S_duration,S_path,S_songID,S_version,S_ps) values (?,?,?,?,?,?,?)";
            Object[] parameters = new Object[]{model.S_musicName,model.S_artist,model.S_duration,model.S_path,model.S_songID,model.S_version,model.S_ps};
            db.execSQL(sql, parameters);

            Cursor cursor = db.rawQuery("select S_id from Song order by S_id desc limit 1", null);
            if (cursor.moveToFirst())
            {
                res = cursor.getInt(0);
            }

            cursor.close();
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
        db.close();
        return res;
    }

    public boolean delete(int id)
    {
        SQLiteDatabase db=DBHelper.getWritableDatabase();
        int res=db.delete("Song", "S_id=?", new String[]{id+""});
        db.close();
        return res==1;
    }

    public boolean update(app.model.Song model)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("S_musicName", model.S_musicName);
        contentValues.put("S_artist", model.S_artist);
        contentValues.put("S_duration", model.S_duration);
        contentValues.put("S_path", model.S_path);
        contentValues.put("S_songID", model.S_songID);
        contentValues.put("S_version", model.S_version);
        contentValues.put("S_ps", model.S_ps);

        SQLiteDatabase db=DBHelper.getWritableDatabase();
        int res=db.update("Song", contentValues,"S_id=?" , new String[]{model.S_id+""});
        db.close();
        return res==1;
    }

    public app.model.Song getModel(int id)
    {
        app.model.Song model=null;

        SQLiteDatabase db=DBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select S_id,S_musicName,S_artist,S_duration,S_path,S_songID,S_version,S_ps from Song where S_id=" + id, null);
        if (cursor.moveToFirst())
        {
            model = new app.model.Song();
            model.S_id = cursor.getInt(0);
            model.S_musicName = cursor.getString(1);
            model.S_artist = cursor.getString(2);
            model.S_duration = cursor.getInt(3);
            model.S_path = cursor.getString(4);
            model.S_songID = cursor.getInt(5);
            model.S_version = cursor.getInt(6);
            model.S_ps = cursor.getString(7);

        }
        cursor.close();
        db.close();
        return model;
    }

    public java.util.List<app.model.Song> getModelList(String appendWhereSql)
    {
        java.util.List<app.model.Song> lists = new ArrayList<>();
        SQLiteDatabase db=DBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select S_id,S_musicName,S_artist,S_duration,S_path,S_songID,S_version,S_ps from Song " + appendWhereSql, null);
        if (cursor.moveToFirst())
        {
            int size = cursor.getCount();
            for (int i = 0; i < size; i++)
            {
                app.model.Song model = new app.model.Song();
            model.S_id = cursor.getInt(0);
            model.S_musicName = cursor.getString(1);
            model.S_artist = cursor.getString(2);
            model.S_duration = cursor.getInt(3);
            model.S_path = cursor.getString(4);
            model.S_songID = cursor.getInt(5);
            model.S_version = cursor.getInt(6);
            model.S_ps = cursor.getString(7);


                lists.add(model);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return lists;
    }
}
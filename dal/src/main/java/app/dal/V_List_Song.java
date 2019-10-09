package app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import app.dal.dbhelper.Music;

public class V_List_Song
{
    private Music DBHelper;
    public V_List_Song(Context context)
    {
        DBHelper=Music.getInstance(context);
    }

    public app.model.V_List_Song getModel(int id)
    {
        app.model.V_List_Song model=null;

        Cursor cursor = DBHelper.getReadableDatabase().rawQuery("select LS_id,LS_lid,S_artist,S_duration,S_path,S_songID,S_version,S_ps,LS_sid,L_id,L_name,L_info,L_pic,L_ps,S_id,S_musicName from V_List_Song where LS_id=" + id, null);
        if (cursor.moveToFirst())
        {
            model = new app.model.V_List_Song();
            model.LS_id = cursor.getInt(0);
            model.LS_lid = cursor.getInt(1);
            model.S_artist = cursor.getString(2);
            model.S_duration = cursor.getInt(3);
            model.S_path = cursor.getString(4);
            model.S_songID = cursor.getInt(5);
            model.S_version = cursor.getInt(6);
            model.S_ps = cursor.getString(7);
            model.LS_sid = cursor.getInt(8);
            model.L_id = cursor.getInt(9);
            model.L_name = cursor.getString(10);
            model.L_info = cursor.getString(11);
            model.L_pic = cursor.getString(12);
            model.L_ps = cursor.getString(13);
            model.S_id = cursor.getInt(14);
            model.S_musicName = cursor.getString(15);

        }

        return model;
    }

    public java.util.List<app.model.V_List_Song> getModelList(String appendWhereSql)
    {
        java.util.List<app.model.V_List_Song> lists = new ArrayList<>();
        Cursor cursor = DBHelper.getReadableDatabase().rawQuery("select LS_id,LS_lid,S_artist,S_duration,S_path,S_songID,S_version,S_ps,LS_sid,L_id,L_name,L_info,L_pic,L_ps,S_id,S_musicName from V_List_Song " + appendWhereSql, null);
        if (cursor.moveToFirst())
        {
            int size = cursor.getCount();
            for (int i = 0; i < size; i++)
            {
                app.model.V_List_Song model = new app.model.V_List_Song();
                model.LS_id = cursor.getInt(0);
                model.LS_lid = cursor.getInt(1);
                model.S_artist = cursor.getString(2);
                model.S_duration = cursor.getInt(3);
                model.S_path = cursor.getString(4);
                model.S_songID = cursor.getInt(5);
                model.S_version = cursor.getInt(6);
                model.S_ps = cursor.getString(7);
                model.LS_sid = cursor.getInt(8);
                model.L_id = cursor.getInt(9);
                model.L_name = cursor.getString(10);
                model.L_info = cursor.getString(11);
                model.L_pic = cursor.getString(12);
                model.L_ps = cursor.getString(13);
                model.S_id = cursor.getInt(14);
                model.S_musicName = cursor.getString(15);


                lists.add(model);
                cursor.moveToNext();
            }
        }

        return lists;
    }
}
package app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import app.dal.dbhelper.Music;

public class List_Song
{
    private Music DBHelper;
    public List_Song(Context context)
    {
        DBHelper=Music.getInstance(context);
    }

    public int add(app.model.List_Song model)
    {
        int res=0;
        SQLiteDatabase db=DBHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            String sql = "insert into List_Song (LS_lid,LS_sid) values (?,?)";
            Object[] parameters = new Object[]{model.LS_lid,model.LS_sid};
            db.execSQL(sql, parameters);

            Cursor cursor = db.rawQuery("select LS_id from List_Song order by LS_id desc limit 1", null);
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
        db.close();
        return res;
    }

    public boolean delete(int id)
    {
        SQLiteDatabase db=DBHelper.getWritableDatabase();
        int res=db.delete("List_Song", "LS_id=?", new String[]{id+""});
        db.close();
        return res==1?true:false;
    }

    public boolean update(app.model.List_Song model)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("LS_lid", model.LS_lid);
        contentValues.put("LS_sid", model.LS_sid);

        SQLiteDatabase db=DBHelper.getWritableDatabase();
        int res=db.update("List_Song", contentValues,"LS_id=?" , new String[]{model.LS_id+""});
        db.close();
        return res==1?true:false;
    }

    public app.model.List_Song getModel(int id)
    {
        app.model.List_Song model=null;
        SQLiteDatabase db=DBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select LS_id,LS_lid,LS_sid from List_Song where LS_id=" + id, null);
        if (cursor.moveToFirst())
        {
            model = new app.model.List_Song();
            model.LS_id = cursor.getInt(0);
            model.LS_lid = cursor.getInt(1);
            model.LS_sid = cursor.getInt(2);

        }
        db.close();
        return model;
    }

    public java.util.List<app.model.List_Song> getModelList(String appendWhereSql)
    {
        java.util.List<app.model.List_Song> lists = new ArrayList<>();
        SQLiteDatabase db=DBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select LS_id,LS_lid,LS_sid from List_Song " + appendWhereSql, null);
        if (cursor.moveToFirst())
        {
            int size = cursor.getCount();
            for (int i = 0; i < size; i++)
            {
                app.model.List_Song model = new app.model.List_Song();
                model.LS_id = cursor.getInt(0);
                model.LS_lid = cursor.getInt(1);
                model.LS_sid = cursor.getInt(2);


                lists.add(model);
                cursor.moveToNext();
            }
        }
        db.close();
        return lists;
    }


    //extend
    public void updateBatch(int lid,@NonNull List<Integer> songid)
    {
        if(lid>0)
        {
            SQLiteDatabase db=DBHelper.getWritableDatabase();
            db.beginTransaction();
            try
            {
                db.delete("List_Song", "LS_lid=?", new String[]{lid+""});
                for(int i=0;i<songid.size();i++)
                {
                    String sql = "insert into List_Song (LS_lid,LS_sid) values (?,?)";
                    Object[] parameters = new Object[]{lid,songid.get(i)};

                    db.execSQL(sql, parameters);
                }

                db.setTransactionSuccessful();
            }
            finally
            {
                db.endTransaction();
            }
        }
    }
}
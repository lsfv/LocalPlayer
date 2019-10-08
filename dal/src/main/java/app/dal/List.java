package app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import app.dal.dbhelper.Music;

public class List
{
    private Music DBHelper;
    public List(Context context)
    {
        DBHelper=Music.getInstance(context);
    }

    public int add(app.model.List model)
    {
        int res=0;
        SQLiteDatabase db=DBHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            String sql = "insert into List (L_name,L_info,L_pic,L_ps) values (?,?,?,?)";
            Object[] parameters = new Object[]{model.L_name,model.L_info,model.L_pic,model.L_ps};
            db.execSQL(sql, parameters);

            Cursor cursor = db.rawQuery("select L_id from List order by L_id desc limit 1", null);
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
        int res=DBHelper.getWritableDatabase().delete("List", "L_id=?", new String[]{id+""});
        return res==1?true:false;
    }

    public boolean update(app.model.List model)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("L_name", model.L_name);
        contentValues.put("L_info", model.L_info);
        contentValues.put("L_pic", model.L_pic);
        contentValues.put("L_ps", model.L_ps);

        int res=DBHelper.getWritableDatabase().update("List", contentValues,"L_id=?" , new String[]{model.L_id+""});
        return res==1?true:false;
    }

    public app.model.List getModel(int id)
    {
        app.model.List model=null;

        Cursor cursor = DBHelper.getReadableDatabase().rawQuery("select L_id,L_name,L_info,L_pic,L_ps from List where L_id=" + id, null);
        if (cursor.moveToFirst())
        {
            model = new app.model.List();
            model.L_id = cursor.getInt(0);
            model.L_name = cursor.getString(1);
            model.L_info = cursor.getString(2);
            model.L_pic = cursor.getString(3);
            model.L_ps = cursor.getString(4);

        }

        return model;
    }

    public java.util.List<app.model.List> getModelList(String appendWhereSql)
    {
        java.util.List<app.model.List> lists = new ArrayList<>();
        Cursor cursor = DBHelper.getReadableDatabase().rawQuery("select L_id,L_name,L_info,L_pic,L_ps from List " + appendWhereSql, null);
        if (cursor.moveToFirst())
        {
            int size = cursor.getCount();
            for (int i = 0; i < size; i++)
            {
                app.model.List model = new app.model.List();
            model.L_id = cursor.getInt(0);
            model.L_name = cursor.getString(1);
            model.L_info = cursor.getString(2);
            model.L_pic = cursor.getString(3);
            model.L_ps = cursor.getString(4);


                lists.add(model);
                cursor.moveToNext();
            }
        }

        return lists;
    }
}
/*------ 代码生成时出现错误: ------
C:\Program Files (x86)\Maticsoft\Codematic2\Template\TemplateFile\新文件夹\androiddal.cmt(0,0) : warning CS0219: Compiling transformation: 变量“space_bll”已赋值，但其值从未使用过
*/
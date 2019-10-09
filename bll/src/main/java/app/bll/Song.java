package app.bll;

import android.content.Context;

import java.util.LinkedList;

public class Song
{
    private app.dal.Song dal;

    public Song(Context context)
    {
        dal=new app.dal.Song(context);
    }

    public int add(app.model.Song model)
    {
        return dal.add(model);
    }

    public boolean delete(int id)
    {
        return dal.delete(id);
    }

    public boolean update(app.model.Song model)
    {
        return dal.update(model);
    }

    public app.model.Song getModel(int id)
    {
        return dal.getModel(id);
    }

    public java.util.List<app.model.Song> getModelList(String where)
    {
        return dal.getModelList(where);
    }


    //extend



}
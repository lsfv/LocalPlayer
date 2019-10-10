package app.bll;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import app.dal.Song;

public class List_Song
{
    private app.dal.List_Song dal;
    public List_Song(Context context)
    {
        dal=new app.dal.List_Song(context);
    }

    public int add(app.model.List_Song model)
    {
        return dal.add(model);
    }

    public boolean delete(int id)
    {
        return dal.delete(id);
    }

    public boolean update(app.model.List_Song model)
    {
        return dal.update(model);
    }

    public app.model.List_Song getModel(int id)
    {
        return dal.getModel(id);
    }

    public java.util.List<app.model.List_Song> getModelList(String where)
    {
        return dal.getModelList(where);
    }

    //extend
    public void updateBatch(int lid,@NonNull List<Integer> songid)
    {
        dal.updateBatch(lid, songid);
    }
}
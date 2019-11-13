package app.bll;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import app.dal.Song;

public abstract class List_Song
{
    private static app.dal.List_Song dal=new app.dal.List_Song();

    public static int add(app.model.List_Song model)
    {
        return dal.add(model);
    }

    public static boolean delete(int id)
    {
        return dal.delete(id);
    }

    public static boolean update(app.model.List_Song model)
    {
        return dal.update(model);
    }

    public static app.model.List_Song getModel(int id)
    {
        return dal.getModel(id);
    }

    public static java.util.List<app.model.List_Song> getModelList(String where)
    {
        return dal.getModelList(where);
    }

    //extend
    public static void updateBatch(int lid,@NonNull List<Integer> songid)
    {
        dal.updateBatch(lid, songid);
    }
}
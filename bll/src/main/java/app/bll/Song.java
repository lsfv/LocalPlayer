package app.bll;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public abstract class Song
{
    private static app.dal.Song dal=new app.dal.Song();

    public  static int add(app.model.Song model)
    {
        return dal.add(model);
    }

    public  static boolean delete(int id)
    {
        return dal.delete(id);
    }

    public  static boolean update(app.model.Song model)
    {
        return dal.update(model);
    }

    public  static app.model.Song getModel(int id)
    {
        return dal.getModel(id);
    }

    public  static java.util.List<app.model.Song> getAllSongs()
    {
        return dal.getModelList("");
    }
}
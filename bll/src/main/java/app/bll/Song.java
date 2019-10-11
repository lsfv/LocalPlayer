package app.bll;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

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

    public java.util.List<app.model.Song> getAllSongs()
    {
        return dal.getModelList("");
    }


    //extend
    public static String menu_IncressVolume=" + ";
    public static String menu_DecressVolume=" - ";
    public static String menu_PlayerMode="随机播放";

    public java.util.List<String> getMenuTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add(menu_IncressVolume);
        res.add(menu_DecressVolume);
        res.add(menu_PlayerMode);
        return res;
    }


}
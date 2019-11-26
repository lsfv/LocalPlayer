package app.bll;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import app.dal.Song;
import app.lslibrary.androidHelper.LSLog;

public abstract class V_List_Song
{
    private static app.dal.V_List_Song dal=new app.dal.V_List_Song();
    private static List<app.model.V_List_Song> cache_list_songs=null;//加入缓存策略，简单有效。
    public static boolean cacheIsLast=false;
    public static final int allsongid=1;

    public  static app.model.V_List_Song getModel(int id)
    {
        return dal.getModel(id);
    }

    public  static java.util.List<app.model.V_List_Song> getAllModel()
    {
        return dal.getModelList("");
    }

    //extend
    public static final int defaultListID=1;

    public static String menu_editlist="编辑列表";

    public  static java.util.List<String> getMenuTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add(menu_editlist);
        return res;
    }


    //extend
    public static String menu_IncressVolume="音量";
    public static String menu_PlayerMode="播放模式";

    public  static java.util.List<String> getMenuPlayerTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add(menu_IncressVolume);
        res.add(menu_PlayerMode);
        return res;
    }

    public  static java.util.List<app.model.V_List_Song> getModelByLid(int lid)
    {
        if(lid==allsongid)
        {
            if (cacheIsLast == false)
            {
                cache_list_songs = dal.getModelList("where LS_lid = " + lid);
                cacheIsLast = true;
            }
            return cache_list_songs;
        }
        else
        {
            return dal.getModelList("where LS_lid = " + lid);
        }
    }

    public  static CharSequence[] getNameList(@NonNull java.util.List<app.model.V_List_Song> songs)
    {
        if(songs==null)
        {
            return null;
        }
        int size=songs.size();
        CharSequence[] res=new CharSequence[size];

        for (int i = 0; i < songs.size(); i++)
        {
            res[i] = songs.get(i).getSongTitle();
        }

        return res;
    }

    public  static List<Integer> getsidList(@NonNull java.util.List<app.model.V_List_Song> songs)
    {
        if(songs==null)
        {
            return null;
        }
        int size=songs.size();
        List<Integer> res=new ArrayList<>();

        for (int i = 0; i < songs.size(); i++)
        {
            res.add(songs.get(i).S_id);
        }

        return res;
    }

    public  static java.util.List<app.model.V_List_Song> getChooseList(@NonNull java.util.List<app.model.V_List_Song> songs ,boolean[] choose)
    {
        java.util.List<app.model.V_List_Song> res=new ArrayList<>();
        if(songs.size()==choose.length)
        {
            for(int i=0;i<choose.length;i++)
            {
                if(choose[i])
                {
                    res.add(songs.get(i));
                }
            }
        }
        return res;
    }

    public  static boolean[] getIsChoose(@NonNull java.util.List<app.model.V_List_Song> songs, @NonNull List<app.model.V_List_Song> mysongs)
    {
        if(songs==null)
        {
            return null;
        }

        int size=songs.size();
        boolean[] res=new boolean[size];

        if(mysongs==null)
        {
            mysongs=new ArrayList<>();
        }

        for(int i=0;i<size;i++)
        {
            res[i]=false;
            for(int j=0;j<mysongs.size();j++)
            {
                if(mysongs.get(j).S_id==songs.get(i).S_id)
                {
                    res[i]=true;
                    break;
                }
            }
        }

        return res;
    }
}
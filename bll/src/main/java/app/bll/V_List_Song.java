package app.bll;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import app.dal.Song;

public class V_List_Song
{
    private app.dal.V_List_Song dal;
    private Context mContext;
    public V_List_Song(Context context)
    {
        mContext=context;
        dal=new app.dal.V_List_Song(context);
    }

    public app.model.V_List_Song getModel(int id)
    {
        return dal.getModel(id);
    }

    public java.util.List<app.model.V_List_Song> getAllModel()
    {
        return dal.getModelList("");
    }

    //extend
    //extend
    public static String menu_editlist="编辑清单";

    public java.util.List<String> getMenuTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add(menu_editlist);
        return res;
    }

    public java.util.List<app.model.V_List_Song> getModelByLid(int lid)
    {
        return dal.getModelList("where LS_lid = "+lid);
    }

    public java.util.List<app.model.V_List_Song> getModelByZeroList()
    {
        app.dal.Song dal_song = new Song(mContext);
        java.util.List<app.model.Song> allsongs = dal_song.getModelList("");

        java.util.List<app.model.V_List_Song> res = new ArrayList<>();
        for (int i = 0; i < allsongs.size(); i++)
        {
            app.model.V_List_Song temp=new app.model.V_List_Song();
            temp.LS_lid=0;
            temp.LS_sid=allsongs.get(i).S_id;
            temp.LS_id=0;

            temp.L_id=0;
            temp.L_name="all songs";

            temp.S_id=allsongs.get(i).S_id;
            temp.S_artist=allsongs.get(i).S_artist;
            temp.S_duration=allsongs.get(i).S_duration;
            temp.S_songID=allsongs.get(i).S_songID;
            temp.S_version=allsongs.get(i).S_version;
            temp.S_musicName=allsongs.get(i).S_musicName;
            temp.S_path=allsongs.get(i).S_path;
            temp.S_ps=allsongs.get(i).S_ps;
            res.add(temp);
        }
        return res;
    }

    public CharSequence[] getNameList(@NonNull java.util.List<app.model.V_List_Song> songs)
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

    public List<Integer> getsidList(@NonNull java.util.List<app.model.V_List_Song> songs)
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



    public java.util.List<app.model.V_List_Song> getChooseList(@NonNull java.util.List<app.model.V_List_Song> songs ,boolean[] choose)
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

    public boolean[] getIsChoose(@NonNull java.util.List<app.model.V_List_Song> songs, @NonNull List<app.model.V_List_Song> mysongs)
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
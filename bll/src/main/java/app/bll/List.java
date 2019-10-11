package app.bll;

import android.content.Context;

import java.util.LinkedList;

public class List
{
    private app.dal.List dal;
    public List(Context context)
    {
        dal=new app.dal.List(context);
    }

    public int add(app.model.List model)
    {
        return dal.add(model);
    }

    public boolean delete(int id)
    {
        return dal.delete(id);
    }

    public boolean update(app.model.List model)
    {
        return dal.update(model);
    }

    public app.model.List getModel(int id)
    {
        return dal.getModel(id);
    }

    /**
     *
     * @param where eg: "where 1=1" . ""
     * @return
     */
    public java.util.List<app.model.List> getModelList(String where)
    {
        return dal.getModelList(where);
    }




    //extend
    public static final String allname="所有歌曲";
    public java.util.List<app.model.List> getAllLists()
    {
        app.model.List allSong=new app.model.List();
        allSong.L_name=allname;
        allSong.L_id=0;

        java.util.List<app.model.List> res=dal.getModelList(" order by L_id");
        res.add(0, allSong);
        return res;
    }


    public static String menu_upsong="更新歌曲";
    public static String menu_addlist="添加列表";
    public java.util.List<String> getMenuTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add(menu_upsong);
        res.add(menu_addlist);
        return res;
    }
}
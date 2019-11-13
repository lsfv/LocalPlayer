package app.bll;

import android.content.Context;

import java.util.LinkedList;

public abstract class List
{
    private static app.dal.List dal=new app.dal.List();

    public static int add(app.model.List model)
    {
        return dal.add(model);
    }

    public static boolean delete(int id)
    {
        return dal.delete(id);
    }

    public static boolean update(app.model.List model)
    {
        return dal.update(model);
    }

    public static app.model.List getModel(int id)
    {
        return dal.getModel(id);
    }

    /**
     *
     * @param where eg: "where 1=1" . ""
     * @return
     */
    public static java.util.List<app.model.List> getModelList(String where)
    {
        return dal.getModelList(where);
    }

    public static java.util.List<app.model.List> getAlllList()
    {
        return dal.getModelList("order by L_id");
    }



    //extend
    public static String menu_upsong="更新歌曲";
    public static String menu_addlist="添加列表";
    public static java.util.List<String> getMenuTitle()
    {
        java.util.List<String> res=new LinkedList<>();
        res.add(menu_upsong);
        res.add(menu_addlist);
        return res;
    }
}
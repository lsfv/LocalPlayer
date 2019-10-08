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
    public java.util.List<app.model.List> Test_getModelList()
    {
        java.util.List<app.model.List> res=new LinkedList<>();

        app.model.List temp1=new app.model.List("好听","info","pic","pic");
        temp1.L_id=1;

        app.model.List temp2=new app.model.List("好听2","info","pic","pic");
        temp2.L_id=2;
        res.add(temp1);
        res.add(temp2);


        return  res;
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
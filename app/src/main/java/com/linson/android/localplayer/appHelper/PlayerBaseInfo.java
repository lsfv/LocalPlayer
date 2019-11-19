package com.linson.android.localplayer.appHelper;

import com.linson.android.localplayer.MainActivity;
import java.util.ArrayList;
import java.util.List;

import app.lslibrary.pattern.LSObserver;


public abstract class PlayerBaseInfo
{
    public static app.model.PlayerBaseInfo getServiceBaseInfo(MainActivity.MyConnection conn)
    {
        app.model.PlayerBaseInfo res=null;
        if(conn!=null && conn.mPlayerProxy!=null)
        {
            try
            {
                res = conn.mPlayerProxy.getBaseInfo();
            }
            catch (Exception e)
            {
            }
        }
        return res;
    }

    public static LSObserver<app.model.PlayerBaseInfo> baseInfoLSObserver=new LSObserver<>();


//    //region observer
//    public interface IBaseInfoListener
//    {
//        void onBaseInfoChange();
//    }
//    public static class BaseinfoObserver
//    {
//        public List<IBaseInfoListener> LISTENERS = new ArrayList<>();
//
//        public void registerObserver(IBaseInfoListener listener)
//        {
//            LISTENERS.add(listener);
//        }
//
//        public void unRegisterObserver(IBaseInfoListener listener)
//        {
//            LISTENERS.remove(listener);
//        }
//
//        public void NoticeObsserver()
//        {
//            for (IBaseInfoListener item : LISTENERS)
//            {
//                if (item != null)
//                {
//                    item.onBaseInfoChange();
//                }
//            }
//        }
//    }
//    //endregion

}

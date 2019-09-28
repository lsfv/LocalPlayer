package app.lslibrary.androidHelper;

import android.util.Log;

//按android的组件和模块来分。activity,broadcast,content,services,animation,customview,log.
public class LSLog
{
    public static String LOGTAG="MYCUSTOM~!@";
    public static void Log_INFO(String msg)
    {
        Log.i(LOGTAG, getAutoJumpLogInfos()+" : "+msg);
    }
    public static void Log_INFO(String msgForamt,Object... values)
    {
        Log.i(LOGTAG, getAutoJumpLogInfos()+" : "+String.format(msgForamt, values));
    }
    public static void Log_DEBUG(String msg)
    {
        Log.d(LOGTAG, msg);
    }
    public static void Log_Error(String msg)
    {
        Log.e(LOGTAG, msg);
    }
    public static void Log_Exception(Exception e)
    {
        Log.i(LOGTAG, getAutoJumpLogInfos()+" : "+e.toString()+".\r\n");
        for(StackTraceElement item : e.getStackTrace())
        {
            Log.i(LOGTAG, item.toString()+".\r\n");
        }
    }
    public static void Log_Exception(Exception e,String prefix) {
        Log.i(LOGTAG,prefix);
        Log_Exception(e);
    }

    private static String getAutoJumpLogInfos() {
        String[] infos = new String[]{"", "", ""};
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            return "";
        } else {
            infos[0] = elements[4].getClassName().substring(
                    elements[4].getClassName().lastIndexOf(".") + 1);
            String ff=elements[4].getMethodName();

            infos[1] = " ["+ff + "].";
            infos[2] = " Line:" +elements[4].getLineNumber() + "";
            return infos[0]+infos[1]+infos[2];
        }
    }
}
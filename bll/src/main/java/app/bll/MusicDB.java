package app.bll;

import android.content.Context;

import app.dal.dbhelper.Music;

public abstract class MusicDB
{
    public static void setDBContext(Context context)
    {
        Music.mContext=context;
    }
}

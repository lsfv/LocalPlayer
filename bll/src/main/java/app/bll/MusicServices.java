package app.bll;

import android.content.Intent;

import app.model.PlayerBaseInfo;

public abstract class MusicServices
{
    public static Intent getServiceIntent()
    {
        Intent intent_services=new Intent();
        intent_services.setAction("musicService");
        intent_services.setPackage("com.linson.android.localplayer");
        return intent_services;
    }

    public static boolean canSeeSongDetail(PlayerBaseInfo info)
    {
        if(info.status!=PlayerBaseInfo.status_init && info.lid!=-1 && info.index!=-1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
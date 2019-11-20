package app.bll;

import android.content.Intent;

public abstract class MusicServices
{
    public static Intent getServiceIntent()
    {
        Intent intent_services=new Intent();
        intent_services.setAction("musicService");
        intent_services.setPackage("com.linson.android.localplayer");
        return intent_services;
    }


}
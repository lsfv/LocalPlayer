// IPlayer.aidl
package com.linson.android.localplayer.AIDL;
import app.model.V_List_Song;
import java.util.List;
import app.model.PlayerBaseInfo;

// Declare any non-default types here with import statements

interface IPlayer
{
    int playSong(int lid,int index);
    int playOneSong(int index);
    int pre();
    int next();
    int changemode(int mode);
    int playOrPause();
    void ondisconnected();
    app.model.PlayerBaseInfo getBaseInfo();
}
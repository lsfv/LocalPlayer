// IPlayer.aidl
package com.linson.android.localplayer.AIDL;
import app.model.V_List_Song;
import java.util.List;
import app.model.PlayerBaseInfo;

// Declare any non-default types here with import statements

interface IPlayer
{
    int add(int a,int b);
    void modifymodel(inout app.model.V_List_Song mm);

    int playSong(int i,in List<app.model.V_List_Song> songs);
    int playOneSong(int index);
    int pre();
    int next();
    int changemode(int mode);
    int playOrPause();
}
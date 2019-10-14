// IPlayer.aidl
package com.linson.android.localplayer.AIDL;
import app.model.V_List_Song;
import java.util.List;

// Declare any non-default types here with import statements

interface IPlayer
{
    int add(int a,int b);
    void modifymodel(inout app.model.V_List_Song mm);

    void setAllSongs(in List<app.model.V_List_Song> songs);
    int playSong(int index);
}
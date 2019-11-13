package app.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class PlayerBaseInfo implements Parcelable
{
    public int lid;
    public int index;
    public int playMode;
    public int status;
    public int playingSeconds;


    public static final int lid_empty=-1;
    public static final int index_empty=-1;

    public static final int playModel_singleRepeat=1;
    public static final int playModel_single=2;
    public static final int playModel_all=3;
    public static final int playModel_random=4;

    public static final int status_init=-1;
    public static final int status_playing=1;
    public static final int status_pause=0;
    public static final int status_error_nofile=-2;
    public static final int status_error_other=-3;

    public PlayerBaseInfo()
    {
        lid=lid_empty;
        index=index_empty;
        playMode=playModel_singleRepeat;
        status=status_init;
    }

    protected PlayerBaseInfo(Parcel in)
    {
        lid = in.readInt();
        index = in.readInt();
        playMode = in.readInt();
        status = in.readInt();
        playingSeconds = in.readInt();
    }

    //这里用枚举就不用 写死 hardcode了。
    public void changeMode()
    {
        playMode++;
        playMode=playMode>playModel_random?playModel_singleRepeat:playMode;
    }

    public String getModeName()
    {
        String res="";
        switch (playMode)
        {
            case playModel_single:
            {
                res="播放一首";break;
            }
            case playModel_singleRepeat:
            {
                res="单曲循环";break;
            }
            case playModel_random:
            {
                res="随机播放";break;
            }
            case playModel_all:
            {
                res="顺序全部";break;
            }
        }
        return res;
    }

    public static final Creator<PlayerBaseInfo> CREATOR = new Creator<PlayerBaseInfo>()
    {
        @Override
        public PlayerBaseInfo createFromParcel(Parcel in)
        {
            return new PlayerBaseInfo(in);
        }

        @Override
        public PlayerBaseInfo[] newArray(int size)
        {
            return new PlayerBaseInfo[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(lid);
        dest.writeInt(index);
        dest.writeInt(playMode);
        dest.writeInt(status);
        dest.writeInt(playingSeconds);
    }

    @SuppressLint("DefaultLocale")
    public String displayStatus()
    {
        return (String.format("status:%d,index:%d.mode:%s",status,index,getModeName()));
    }
}

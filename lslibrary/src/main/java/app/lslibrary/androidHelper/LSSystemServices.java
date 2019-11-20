package app.lslibrary.androidHelper;

import android.app.ActivityManager;
import android.content.Context;
import android.media.AudioManager;

import java.util.List;

public abstract class LSSystemServices
{
    public static class StreamVolumeInfo
    {
        public int max;
        public int min;
        public int now;
    }


    /**
     *
     * @param context
     *
     * @return
     */
    public static StreamVolumeInfo getVolumeInfo(Context context,int type)
    {
        StreamVolumeInfo streamVolumeInfo=new StreamVolumeInfo();
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        streamVolumeInfo.now = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolumeInfo.min=0;
        streamVolumeInfo.max=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return streamVolumeInfo;
    }

    public static void setVolume(int type,Context context,int value)
    {
        StreamVolumeInfo streamVolumeInfo=new StreamVolumeInfo();
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(type, value, AudioManager.FLAG_PLAY_SOUND);
    }


    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0))
        {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


}
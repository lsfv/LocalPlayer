package app.lslibrary.androidHelper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LSContentResolver
{
    private Context mContext;

    public LSContentResolver(Context context)
    {
        mContext=context;
    }

    //region song
    public List<SongInfo> SearchSong(int duration)
    {
        List<SongInfo> res=new ArrayList<>();
        ContentResolver contentResolver=mContext.getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        StringBuffer searchsql=new StringBuffer();
        searchsql.append(" 1=1 ");
        searchsql.append(" and " +MediaStore.Audio.Media.DURATION+" > " +duration);
        searchsql.append(" and " +MediaStore.Audio.Media.DURATION+" < 1000*60*20");//应该要小于20分钟，要不太占内存。
        String[] paras={MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION};

        Cursor cursor = contentResolver.query(uri, paras, searchsql.toString(), null, "");

        if(cursor.moveToFirst())
        {
            int size=cursor.getCount();
            for(int i=0;i<size;i++)
            {
                int tempID=cursor.getInt(0);
                String tempTitle=cursor.getString(1);
                String tempArtist=cursor.getString(2);
                String tempPath=cursor.getString(3);
                int tempDuration=cursor.getInt(4);
                SongInfo tempSonginfo=new SongInfo(tempTitle, tempArtist, tempDuration,  tempPath, tempID);
                res.add(tempSonginfo);
                cursor.moveToNext();
            }
        }
        return res;
    }

    public static class SongInfo
    {
        public String musicName;
        public String artist;
        public int duration;
        public String path;
        public int songId;//android系统的id

        public SongInfo(String name,String player,int durationa,String songpath,int _id)
        {
            musicName=name;
            artist=player;
            duration=durationa;
            path=songpath;
            songId=_id;
        }
    }

    public interface VoidHandler
    {
        public void doit();
    }
    //endregion

    //region pic
    public static Bitmap getBitmap(Activity activity, Intent intent)
    {
        Bitmap bitmap=null;
        String imagePath="";
        Uri imageUri=intent.getData();
        if(imagePath!=null)
        {
            if(Build.VERSION.SDK_INT>=19)
            {
                if(DocumentsContract.isDocumentUri(activity, imageUri))
                {
                    String docid=DocumentsContract.getDocumentId(imageUri);
                    if("com.android.providers.media.documents".equalsIgnoreCase(imageUri.getAuthority()))
                    {
                        String id=docid.split(":")[1];
                        String selection=MediaStore.Images.Media._ID+"="+id;
                        imagePath=getImagePaht(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, activity);
                    }
                }
                else if("content".equalsIgnoreCase(imageUri.getScheme()))
                {
                    imagePath=getImagePaht(imageUri, null, activity);
                }
                else if("file".equalsIgnoreCase(imageUri.getScheme()))
                {
                    imagePath=imageUri.getPath();
                }
            }
            else
            {
                imagePath = getImagePaht(imageUri, null, activity);
            }
        }

        try
        {
            bitmap = BitmapFactory.decodeFile(imagePath);
        }
        catch (Exception e)
        {
            LSLog.Log_Exception(e);
        }
        return bitmap;
    }

    private static String getImagePaht(Uri uri,String select,Activity activity)
    {
        String path=null;
        Cursor cursor=activity.getContentResolver().query(uri, null, select, null,null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //endregion

    //region permissionHelper
    public static void checkPermission(Activity context, final String permission,int requestcode, VoidHandler voidHandler)
    {
        boolean hasPermission=false;
        hasPermission=ContextCompat.checkSelfPermission(context, permission)==PackageManager.PERMISSION_GRANTED;
        if(hasPermission)
        {
            voidHandler.doit();
        }
        else
        {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);
        }
    }
    public static void progressCheck(Activity context,int requestCode_iget,int[] grantResults,int requestCode_iset,VoidHandler voidHandler)
    {
        if(requestCode_iget==requestCode_iset)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                voidHandler.doit();
            }
        }
        else
        {
            Toast.makeText(context, "you deny permissions.", Toast.LENGTH_SHORT);
        }
    }
    //endregion
}
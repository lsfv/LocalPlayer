package app.dal.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Music extends SQLiteOpenHelper
{
    public static int defaultVersion=1;
    public static String dbName="music.db";

    public static Music getInstance(Context context)
    {
        return new Music(context, dbName, null, defaultVersion);
    }

    public Music(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        try
        {
            String sql_create1 = new String("CREATE TABLE List ( `L_id` INTEGER PRIMARY KEY AUTOINCREMENT, `L_name` TEXT NOT NULL, `L_info` TEXT NOT NULL, `L_pic` TEXT NOT NULL, `L_ps` TEXT NOT NULL )");
            String sql_create2 = new String("CREATE TABLE Song ( `S_id` integer PRIMARY KEY AUTOINCREMENT, `S_musicName` text NOT NULL, `S_artist` text NOT NULL, `S_duration` integer NOT NULL, `S_path` text NOT NULL, `S_songID` integer NOT NULL, `S_version` integer NOT NULL, `S_ps` TEXT NOT NULL )");
            String sql_create3 = new String("CREATE TABLE LocalSong ( `LS_id` integer PRIMARY KEY AUTOINCREMENT, `LS_musicName` text NOT NULL, `LS_artist` text NOT NULL, `LS_duration` integer NOT NULL, `LS_path` text NOT NULL, `LS_songID` integer NOT NULL, `LS_version` INTEGER NOT NULL )");
            String sql_create4 = new String("CREATE TABLE List_Song ( `LS_id` integer PRIMARY KEY AUTOINCREMENT, `LS_lid` integer NOT NULL, `LS_sid` integer NOT NULL )");
            String sql = "CREATE VIEW V_List_Song as select LS_id,LS_lid,LS_sid,L_id,L_name,L_info,L_pic,L_ps,S_id,S_musicName,S_artist,S_duration,S_path,S_songID,S_version,S_ps from list_song as a left join list as b on b.l_id=a.ls_lid left join song as c on c.s_id=a.LS_sid";

            String sql_defaultData="insert into List (L_name,L_info,L_pic,L_ps) values ('所有歌曲','默认添加，不要删除','','')";
            db.execSQL(sql_create1);
            db.execSQL(sql_create2);
            db.execSQL(sql_create3);
            db.execSQL(sql_create4);
            db.execSQL(sql);
            db.execSQL(sql_defaultData);

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
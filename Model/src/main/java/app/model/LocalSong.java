package app.model;
import java.util.Date;
public class LocalSong
{
	public int LS_id;
	public String LS_musicName;
	public String LS_artist;
	public int LS_duration;
	public String LS_path;
	public int LS_songID;
	public int LS_version;

	public LocalSong(){}

	public LocalSong(String _LS_musicName,String _LS_artist,int _LS_duration,String _LS_path,int _LS_songID,int _LS_version)
	{
		LS_id=0;
		LS_musicName=_LS_musicName;
		LS_artist=_LS_artist;
		LS_duration=_LS_duration;
		LS_path=_LS_path;
		LS_songID=_LS_songID;
		LS_version=_LS_version;

	}

}
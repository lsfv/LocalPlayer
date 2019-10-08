package app.model;
import java.util.Date;
public class Song
{
	public int S_id;
	public String S_musicName;
	public String S_artist;
	public int S_duration;
	public String S_path;
	public int S_songID;
	public int S_version;
	public String S_ps;

	public Song(){}

	public Song(String _S_musicName,String _S_artist,int _S_duration,String _S_path,int _S_songID,int _S_version,String _S_ps)
	{
		S_id=0;
		S_musicName=_S_musicName;
		S_artist=_S_artist;
		S_duration=_S_duration;
		S_path=_S_path;
		S_songID=_S_songID;
		S_version=_S_version;
		S_ps=_S_ps;
	}
}
package app.model;
import java.util.Date;
public class List_Song
{
	public int LS_id;
	public int LS_lid;
	public int LS_sid;

	public List_Song(){}

	public List_Song(int _LS_lid,int _LS_sid)
	{
		LS_id=0;
		LS_lid=_LS_lid;
		LS_sid=_LS_sid;

	}
}
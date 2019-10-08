package app.model;
import java.util.Date;
public class List
{
	public int L_id;
	public String L_name;
	public String L_info;
	public String L_pic;
	public String L_ps;

	public List(){}

	public List(String _L_name,String _L_info,String _L_pic,String _L_ps)
	{
		L_id=0;
		L_name=_L_name;
		L_info=_L_info;
		L_pic=_L_pic;
		L_ps=_L_ps;

	}

}
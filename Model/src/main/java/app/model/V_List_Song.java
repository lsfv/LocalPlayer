package app.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
public class V_List_Song  implements Serializable,Parcelable
{
	public int LS_id;
	public int LS_lid;
	public int LS_sid;
	public int L_id;
	public String L_name;
	public String L_info;
	public String L_pic;
	public String L_ps;
	public int S_id;
	public String S_musicName;
	public String S_artist;
	public int S_duration;
	public String S_path;
	public int S_songID;
	public int S_version;
	public String S_ps;

	public V_List_Song(){}

	protected V_List_Song(Parcel in)
	{
		LS_id = in.readInt();
		LS_lid = in.readInt();
		LS_sid = in.readInt();
		L_id = in.readInt();
		L_name = in.readString();
		L_info = in.readString();
		L_pic = in.readString();
		L_ps = in.readString();
		S_id = in.readInt();
		S_musicName = in.readString();
		S_artist = in.readString();
		S_duration = in.readInt();
		S_path = in.readString();
		S_songID = in.readInt();
		S_version = in.readInt();
		S_ps = in.readString();
	}

	public static final Creator<V_List_Song> CREATOR = new Creator<V_List_Song>()
	{
		@Override
		public V_List_Song createFromParcel(Parcel in)
		{
			return new V_List_Song(in);
		}

		@Override
		public V_List_Song[] newArray(int size)
		{
			return new V_List_Song[size];
		}
	};

	public String getSongTitle()
	{
		return S_musicName+" - "+S_artist;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(LS_id);
		dest.writeInt(LS_lid);
		dest.writeInt(LS_sid);
		dest.writeInt(L_id);
		dest.writeString(L_name);
		dest.writeString(L_info);
		dest.writeString(L_pic);
		dest.writeString(L_ps);
		dest.writeString(S_musicName);
		dest.writeString(S_artist);
		dest.writeInt(S_duration);
		dest.writeString(S_path);
		dest.writeInt(S_songID);
		dest.writeInt(S_version);
		dest.writeString(S_ps);
	}
}
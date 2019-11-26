package com.linson.android.localplayer.activities.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linson.android.localplayer.MainActivity;
import com.linson.android.localplayer.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


//对于非常简单的布局，需要2中不同风格，还是可以复用下adapter的。稍微复杂一点还是分开。基本上如果2种风格，稍有不慎，以后就会发展为2种布局，而不单单是2种风格。那么要一个
//adapter来满足2个布局。方法就冗余，杂乱的太多了。很多不能服用。
public class Adapter_Songs extends RecyclerView.Adapter<Adapter_Songs.MyViewHolder>
{
    private List<app.model.V_List_Song> msongs=new LinkedList<>();
    private IItemHander mHander;
    private int mPlayingIndex;
    private int mlayout=0;


    public Adapter_Songs(@NonNull List<app.model.V_List_Song> songs,@NonNull IItemHander hander,int playingIndex,int layouttype)
    {
        msongs=songs;
        mHander=hander;
        mPlayingIndex=playingIndex;
        mlayout=layouttype;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i)
    {
        if(mlayout==0)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_onetextview, viewGroup, false);
            return new MyViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_onetextviewsmall, viewGroup, false);
            return new MyViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i)
    {
        final app.model.V_List_Song tempSong=msongs.get(i);
        myViewHolder.mTvItem.setText(tempSong.getSongTitle());
        if(i==mPlayingIndex)
        {
            myViewHolder.mIvPlaying.setVisibility(View.VISIBLE);
            //myViewHolder.mView.setBackgroundColor(MainActivity.appContext.getResources().getColor(R.color.itemOrangebg));
        }
        else
        {
            //myViewHolder.mView.setBackgroundColor(0xffffffff);
            myViewHolder.mIvPlaying.setVisibility(View.INVISIBLE);
        }
        myViewHolder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mHander!=null)
                {
                    mHander.onClick(tempSong.L_id,i);
                }
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return msongs.size();
    }


    public List<app.model.V_List_Song> getCloneData()
    {
        return  new ArrayList<>(msongs);
    }

    public void updateData(List<app.model.V_List_Song> data,int playingIndex)
    {
        msongs=data;
        mPlayingIndex=playingIndex;
        this.notifyDataSetChanged();
    }


    public void showImagePlaying(int playingIndex)
    {
        mPlayingIndex=playingIndex;
        this.notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mTvItem;
        private ImageView mIvPlaying;
        private View mView;


        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTvItem = (TextView) itemView.findViewById(R.id.tv_item);
            mIvPlaying = (ImageView)itemView.findViewById(R.id.iv_playing);
            mView=itemView;
        }
    }


    public interface IItemHander
    {
        void onClick(int ls_id,int index);
    }
}
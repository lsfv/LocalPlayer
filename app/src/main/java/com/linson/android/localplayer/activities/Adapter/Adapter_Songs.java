package com.linson.android.localplayer.activities.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.linson.android.localplayer.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class Adapter_Songs extends RecyclerView.Adapter<Adapter_Songs.MyViewHolder>
{
    private List<app.model.V_List_Song> msongs=new LinkedList<>();
    private IItemHander mHander;
    private int mPlayingIndex;

    public Adapter_Songs(@NonNull List<app.model.V_List_Song> songs,@NonNull IItemHander hander,int playingIndex)
    {
        msongs=songs;
        mHander=hander;
        mPlayingIndex=playingIndex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_onetextview, viewGroup, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i)
    {
        final app.model.V_List_Song tempSong=msongs.get(i);
        myViewHolder.mTvItem.setText(tempSong.getSongTitle());
        if(i==mPlayingIndex)
        {
            myViewHolder.mIvPlaying.setVisibility(View.VISIBLE);
        }
        else
        {
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
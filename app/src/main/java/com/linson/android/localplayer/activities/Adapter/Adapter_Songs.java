package com.linson.android.localplayer.activities.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.linson.android.localplayer.R;
import java.util.LinkedList;
import java.util.List;


public class Adapter_Songs extends RecyclerView.Adapter<Adapter_Songs.MyViewHolder>
{
    private List<app.model.V_List_Song> msongs;
    public Adapter_Songs(@NonNull List<app.model.V_List_Song> songs)
    {
        msongs=songs;
        if(msongs==null)
        {
            msongs=new LinkedList<>();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_onetextview, viewGroup, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {
        app.model.V_List_Song tempSong=msongs.get(i);
        myViewHolder.mTvItem.setText(tempSong.S_musicName);
    }


    @Override
    public int getItemCount()
    {
        return msongs.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mTvItem;
        private View mView;


        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTvItem = (TextView) itemView.findViewById(R.id.tv_item);
            mView=itemView;
        }
    }

}
package com.linson.android.localplayer.activities.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.linson.android.localplayer.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class Adapter_Songs extends RecyclerView.Adapter<Adapter_Songs.MyViewHolder>
{
    private List<app.model.V_List_Song> msongs;
    private IItemHander mHander;

    public Adapter_Songs(@NonNull List<app.model.V_List_Song> songs,@NonNull IItemHander hander)
    {
        msongs=songs;
        mHander=hander;
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i)
    {
        final app.model.V_List_Song tempSong=msongs.get(i);
        myViewHolder.mTvItem.setText(tempSong.getSongTitle());
        myViewHolder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mHander!=null)
                {
                    mHander.onClick(tempSong.LS_id);
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
        return  new ArrayList<app.model.V_List_Song>(msongs);
    }

    public void updateData(List<app.model.V_List_Song> data)
    {
        msongs=data;
        this.notifyDataSetChanged();
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

    public interface IItemHander
    {
        void onClick(int ls_id);
    }

}
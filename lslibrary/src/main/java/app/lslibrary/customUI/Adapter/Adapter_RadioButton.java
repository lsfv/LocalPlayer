package app.lslibrary.customUI.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.List;
import app.lslibrary.R;

public class Adapter_RadioButton extends RecyclerView.Adapter<Adapter_RadioButton.MyViewHolder>
{
    private List<String> mBtns;
    private int mCheckIndex=0;
    private int bgc=Color.GRAY;
    private int bgcCheck=0xffff9900;
    private IDialogRadioListener mIDialogEvent;

    public Adapter_RadioButton(List<String> data,int checkedIndex,IDialogRadioListener dialogEvent,int bgca,int bgcchecka)
    {
        mBtns=data;
        mCheckIndex=checkedIndex;
        mIDialogEvent=dialogEvent;
//        bgc=bgca;
//        bgcCheck=bgcchecka;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_radio_button,viewGroup ,false );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i)
    {
        myViewHolder.mBtnItem.setText(mBtns.get(i));
        if(i==mCheckIndex)
        {
            myViewHolder.mBtnItem.setTextColor(bgcCheck);
        }
        else
        {
            myViewHolder.mBtnItem.setTextColor(bgc);
        }
        myViewHolder.mBtnItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCheckIndex=i;
                notifyDataSetChanged();
                if(mIDialogEvent!=null)
                {
                    mIDialogEvent.onClickItem(i);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mBtns.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private Button mBtnItem;
        private View mview;


        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mBtnItem = (Button) itemView.findViewById(R.id.btn_item);
            mview=itemView;
        }
    }


    public interface IDialogRadioListener
    {
        public void onClickItem(int index);
    }
}

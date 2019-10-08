package com.linson.android.localplayer.activities.Adapter;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.linson.android.localplayer.R;
import java.util.LinkedList;
import java.util.List;


public class Adapter_List extends RecyclerView.Adapter<Adapter_List.MyHolderView>
{
    private List<app.model.List> mdata=new LinkedList<>();
    private float prex=-1;
    private boolean isMove=false;
    private int maxWidth=240;
    private boolean isHiden=true;
    private IAdapter_ListHander mIAdapter_listHander;

    public Adapter_List(@NonNull List<app.model.List> data,@NonNull IAdapter_ListHander IAdapter_listHander)
    {
        mdata=data;
        mIAdapter_listHander=IAdapter_listHander;
    }


    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_delete, viewGroup, false);
        return new MyHolderView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyHolderView myHolderView, final int i)
    {
        myHolderView.mTvItem.setText(mdata.get(i).L_name);
        myHolderView.mButton21.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mIAdapter_listHander!=null)
                {
                    ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)myHolderView.mTvItem.getLayoutParams();
                    lp.setMargins(0, 0, 0, 0);
                    myHolderView.mTvItem.setLayoutParams(lp);
                    mIAdapter_listHander.onClickDelete(i);
                }
            }
        });

        myHolderView.mView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    prex=event.getX();
                    isMove=false;
                }
                else if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    isMove=true;
                    ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)myHolderView.mTvItem.getLayoutParams();

                    float currentX=event.getX();
                    float movex=currentX-prex;
                    int lastMarginend=(int)(lp.getMarginEnd()-movex);
                    lastMarginend=lastMarginend>maxWidth?maxWidth:lastMarginend;
                    lastMarginend=lastMarginend<0?0:lastMarginend;
                    lp.setMargins(0, 0, lastMarginend, 0);
                    myHolderView.mTvItem.setLayoutParams(lp);
                    prex=currentX;
                }
                else if(event.getAction()==MotionEvent.ACTION_CANCEL ||event.getAction()==MotionEvent.ACTION_UP )
                {
                    //处理2类事件。点击和滑动
                    if(isMove==false && event.getAction()==MotionEvent.ACTION_UP && isHiden)
                    {
                        if(mIAdapter_listHander!=null)
                        {
                            mIAdapter_listHander.onClickItem(i);
                        }
                    }
                    else
                    {

                        final ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) myHolderView.mTvItem.getLayoutParams();
                        boolean wantToShow = true;
                        int currentMargin = lp.getMarginEnd();
                        if (isHiden)
                        {
                            wantToShow = (float) currentMargin / maxWidth >= 0.5 ? true : false;//键盘判断，拖动超过一半就显示.
                        } else
                        {
                            wantToShow = false;//显示状态，除非触碰按钮，否则无条件关闭按钮。
                        }

                        int endMargin;
                        if (wantToShow)
                        {
                            endMargin = maxWidth;
                            isHiden = false;
                        } else
                        {
                            endMargin = 0;
                            isHiden = true;
                        }
                        //play animator
                        ValueAnimator animator = ValueAnimator.ofInt(currentMargin, endMargin);
                        animator.setDuration(300);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation)
                            {
                                int changingValue = (int) animation.getAnimatedValue();
                                lp.setMargins(0, 0, changingValue, 0);
                                myHolderView.mTvItem.setLayoutParams(lp);
                            }
                        });
                        animator.start();
                        //end play animator
                    }
                }
                return true;
            }
        });
    }


    public void deleteItem(int index)
    {
        mdata.remove(index);
        this.notifyDataSetChanged();
    }


    public app.model.List getitem(int index)
    {
        return mdata.get(index);
    }


    public void addItem(app.model.List item)
    {
        mdata.add(item);
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount()
    {
        return mdata.size();
    }


    //region out class

    public static class MyHolderView extends RecyclerView.ViewHolder
    {
        private TextView mTvItem;
        private Button mButton21;
        private View mView;

        public MyHolderView(@NonNull View itemView)
        {
            super(itemView);
            mTvItem = (TextView) itemView.findViewById(R.id.tv_item);
            mButton21 = (Button) itemView.findViewById(R.id.button21);
            mView=itemView;
        }
    }


    public interface IAdapter_ListHander
    {
        void onClickDelete(int index);
        void onClickItem(int index);
    }
    //endregion
}
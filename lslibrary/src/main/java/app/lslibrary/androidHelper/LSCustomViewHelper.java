package app.lslibrary.androidHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

public class LSCustomViewHelper
{
    public enum Enum_MeasureType
    {
        fixDefault,
        rate
    }

    public static class SuggestMeasure
    {
        public int width;
        public int height;
    }

//    public static TypedArray getTypedArray(Context context, AttributeSet attrs, int[] attrFileName)
//    {
//        return context.obtainStyledAttributes(attrs, attrFileName);
//    }

    public static String getModeStr(int value)
    {
        if(value==View.MeasureSpec.UNSPECIFIED)
        {
            return "UNSPECIFIED";
        }
        else if(value==View.MeasureSpec.EXACTLY)
        {
            return "EXACTLY";
        }
        else if(value==View.MeasureSpec.AT_MOST)
        {
            return "AT_MOST";
        }
        else
        {
            return "Exception!";
        }
    }

    public static String getMeasureStr(int MeasureSpec)
    {
        int width=View.MeasureSpec.getSize(MeasureSpec);
        int wMode=View.MeasureSpec.getMode(MeasureSpec);
        return String.format("vaule:%d,mode:%s",width,getModeStr(wMode));
    }

    //如果2个都是exactly那么不变，如果2个都是contextWrap.那么设置为默认值.
    //如果一个exactly,一个contextWrap.那么有2种常见的策略：
    //1.contextWrap会用依据exactly和默认值比例来设置值，
    //2.contextwarp固定为是default.
    public static SuggestMeasure getCommonMeasure(int widthMeasureSpec,int heightMeasureSpec,int defaultWidth,int defaultHeigh,Enum_MeasureType type)
    {
        SuggestMeasure suggestMeasure=new SuggestMeasure();
        int width=View.MeasureSpec.getSize(widthMeasureSpec);
        int wMode=View.MeasureSpec.getMode(widthMeasureSpec);
        int height=View.MeasureSpec.getSize(heightMeasureSpec);
        int hMode=View.MeasureSpec.getMode(heightMeasureSpec);

        suggestMeasure.height=height;
        suggestMeasure.width=width;

        if(wMode!=View.MeasureSpec.EXACTLY && hMode!=View.MeasureSpec.EXACTLY)
        {
            suggestMeasure.height=defaultHeigh;
            suggestMeasure.width=defaultWidth;
        }
        else if(wMode==View.MeasureSpec.EXACTLY && hMode!=View.MeasureSpec.EXACTLY)
        {
            if(type==Enum_MeasureType.rate)
            {
                suggestMeasure.height = width * (defaultHeigh / defaultWidth);
            }
            else
            {
                suggestMeasure.height=defaultHeigh;
            }
            suggestMeasure.width=width;
        }
        else if(wMode!=View.MeasureSpec.EXACTLY && hMode==View.MeasureSpec.EXACTLY)
        {
            if(type==Enum_MeasureType.rate)
            {
                suggestMeasure.width = height * (defaultWidth / defaultHeigh);
            }
            else
            {
                suggestMeasure.width=defaultWidth;
            }
            suggestMeasure.height=height;
        }
        return suggestMeasure;
    }
}

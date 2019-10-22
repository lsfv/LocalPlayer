package app.lslibrary.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;

import app.lslibrary.R;
import app.lslibrary.androidHelper.LSCustomViewHelper;
import app.lslibrary.androidHelper.LSLog;


//获得宽高，半径。宽高从属性获得。默认为100px。 半径，从宽/2,高/2，半径中选最小的。
//最后图片压缩为2×半径为边长的正方形， 并作为画笔的shander,
//画布移动（cx-r,cy-r）,让靠左上角画图的效果和在图中心画图方位一致，
//又达到了正确使用shader. shader不可移动,从左上角铺满canvas,所以移动canvas,让shader好像移动了一样.
public class LSCircleImage extends View
{
    private static int mDefaultSize=100;

    private int mRadius=0;
    private int mImgResourceID=0;
    private Paint mPaint;

    public LSCircleImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.LSCircleImage);
        mRadius=(int)array.getDimension(R.styleable.LSCircleImage_CircleImage_radius, Integer.MAX_VALUE);
        mImgResourceID=array.getResourceId(R.styleable.LSCircleImage_CircleImage_img,0 );
        mPaint=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LSCustomViewHelper.SuggestMeasure suggestMeasure= LSCustomViewHelper.getCommonMeasure(widthMeasureSpec, heightMeasureSpec, mDefaultSize, mDefaultSize, LSCustomViewHelper.Enum_MeasureType.rate);
        setMeasuredDimension(suggestMeasure.width, suggestMeasure.height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(mImgResourceID!=0)
        {
            initPaint(mPaint);//会多次执行此函数，所以最好开始重设。

            int Width=getWidth();
            int Height=getHeight();

            int tempRadius=Math.min(Width/2, Height/2);
            tempRadius=Math.min(tempRadius, mRadius);
            mRadius=tempRadius;

            int centerX=Width/2;
            int centerY=Height/2;

            Bitmap bitmap_origetation=BitmapFactory.decodeResource(getResources(), mImgResourceID);
            Bitmap bitmap_scale=Bitmap.createScaledBitmap(bitmap_origetation, mRadius*2, mRadius*2, false);
            mPaint.setShader(new BitmapShader(bitmap_scale, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.save();
            canvas.translate(centerX-mRadius, centerY-mRadius);
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
            canvas.restore();
        }
        else
        {
            super.onDraw(canvas);
        }
    }

    private void initPaint(Paint paint)
    {
        paint.reset();
    }


    public void setImage( int id)
    {
        if(id!=0 && id!=mImgResourceID)
        {
            mImgResourceID=id;
            invalidate();
        }
    }

    public int getImage()
    {
        return mImgResourceID;
    }
}
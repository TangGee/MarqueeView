package com.yumin.ma;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class MarqueeView extends View{

    private TextPaint mTextPaint;
    private String mText;
    private float mTextSize;
    private float mMeasureTextWidth;
    private long firstDrawTime = -1;



    public MarqueeView(Context context) {
        this(context,null);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextSize = 50.0F;
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        //TODO 可以配置的testsize
        mTextPaint.setTextSize(mTextSize);

        //TODO shadow color
        int color = Color.YELLOW;
        int textColor = Color.YELLOW;
        mTextPaint.setShadowLayer(1f,1f,1f,color);
        mTextPaint.setColor(textColor);
        Typeface typeface = null;
        if (typeface!=null)
            mTextPaint.setTypeface(typeface);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mText) && mMeasureTextWidth>0) {
            long currentTime = SystemClock.uptimeMillis();
            if (firstDrawTime == 0 ){
                firstDrawTime = currentTime;
            }
            int firstX = getFirstX(currentTime);
           canvas.drawText(mText,firstX,-mTextPaint.ascent(),mTextPaint);
           int secondX = getSecondX(firstX);
           canvas.drawText(mText,secondX,-mTextPaint.ascent(),mTextPaint);

           postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float blow =mTextPaint.descent(); //底部距离基线的距离
        float above = mTextPaint.ascent(); //上不距离基线的激励
        blow -=above;
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, (int) blow);
    }


    public void setText(String text) {
        StringBuilder builder = new StringBuilder(text).append("    ");
        mText = builder.toString();
        mMeasureTextWidth = mTextPaint.measureText(mText);
        firstDrawTime =0;
        requestLayout();
    }

    private int getFirstX(long currentTime) {
        if (mText == null) return 0;
        float duration = currentTime-firstDrawTime;
        float oneLineDur = mMeasureTextWidth *10;
        return -(int) (duration%oneLineDur/oneLineDur*mMeasureTextWidth);
    }

    private int getSecondX(int firstX){
        int width = getWidth();
        if (width>0) {
            return (int) (firstX+mMeasureTextWidth);
        }
        return -1;
    }
}

package com.yumin.ma;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MarqueeView extends View{

    private TextPaint mTextPaint;
    private String mText;
    private float mTextSize;
    private float mMeasureTextWidth;
    private long firstDrawTime = -1;
    private float mLoopMargin=0f;
    private int mLoopMarginTxtCount = 0;
    private int mSpeed;



    public MarqueeView(Context context) {
        this(context,null);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.marquee_view);
        mTextSize = typedArray.getDimension(R.styleable.marquee_view_text_size, 50);
        int textColor = typedArray.getColor(R.styleable.marquee_view_text_color, Color.BLACK);
        int shadowColor = typedArray.getColor(R.styleable.marquee_view_txt_shadow_color, textColor);
        mLoopMargin = typedArray.getDimension(R.styleable.marquee_view_marquee_margin, 0);
        mLoopMarginTxtCount = typedArray.getInt(R.styleable.marquee_view_marquee_margin_txtcount, -1);
        mSpeed = typedArray.getInt(R.styleable.marquee_view_marquee_speed,10);
        typedArray.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        mTextPaint.setShadowLayer(1f,1f,1f,shadowColor);
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

            int currentX = getFirstX(currentTime);
            float top = -mTextPaint.ascent();
            while (currentX<getWidth()) {
                canvas.drawText(mText,currentX,top,mTextPaint);
                currentX = (int) (currentX+mMeasureTextWidth);
            }
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
        mText = text;
        if (mLoopMarginTxtCount>0) {
            StringBuilder builder = new StringBuilder(mLoopMarginTxtCount);
            for (int i=0;i<mLoopMarginTxtCount;i++){
                builder.append(" ");
            }
            mLoopMargin = mTextPaint.measureText(builder.toString());
        }
        mMeasureTextWidth = mTextPaint.measureText(mText)+mLoopMargin;
        firstDrawTime =0;
        requestLayout();
    }

    private int getFirstX(long currentTime) {
        if (mText == null) return 0;
        float duration = currentTime-firstDrawTime;
        float oneLineDur = mMeasureTextWidth *mSpeed;
        return -(int) (duration%oneLineDur/oneLineDur*mMeasureTextWidth);
    }
}

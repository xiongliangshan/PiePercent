package com.xls.piepercentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public class PiePercentView extends View {

    private Paint mPaint;
    private Paint mArcTextPaint;
    private Paint mInnerTextPaint;
    private int mArcTextColor;
    private int mInnerTextColor;
    private float mArcTextSize;
    private float mInnerTextSize;
    private List<PieData> mData;
    private float mStartAngle ;
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private boolean mIsArcTextVisible;
    private boolean mIsInnerTextVisible;
    private float total;


    public PiePercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PiePercentView);
        mArcTextColor = a.getColor(R.styleable.PiePercentView_arcTextColor,Color.BLACK);
        mInnerTextColor = a.getColor(R.styleable.PiePercentView_innerTextColor,Color.BLACK);
        mArcTextSize = a.getDimension(R.styleable.PiePercentView_arcTextSize,20f);
        mInnerTextSize = a.getDimension(R.styleable.PiePercentView_innerTextSize,20f);
        mStartAngle = a.getFloat(R.styleable.PiePercentView_startAngle,0f);
        mIsArcTextVisible = a.getBoolean(R.styleable.PiePercentView_arcTextVisible,true);
        mIsInnerTextVisible = a.getBoolean(R.styleable.PiePercentView_innerTextVisible,true);
        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcTextPaint.setTextSize(mArcTextSize);
        mArcTextPaint.setColor(mArcTextColor);
        mInnerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerTextPaint.setColor(mInnerTextColor);
        mInnerTextPaint.setTextSize(mInnerTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mData==null || mData.size()==0){
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = mWidth-paddingLeft-paddingRight;
        int height = mHeight-paddingTop-paddingBottom;
        float startAngle = mStartAngle;
        mRadius = Math.min(width,height)/2;
        canvas.translate(mWidth/2,mHeight/2);
        RectF rect = new RectF(-mRadius,-mRadius,mRadius,mRadius);
        RectF rectf = new RectF(-mRadius/2,-mRadius/2,mRadius/2,mRadius/2);
        Path path = new Path();
        for(PieData pie:mData){
            float percent = pie.getValue()/total;
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rect,startAngle,360*percent,true,mPaint);
            if(mIsArcTextVisible){
                path.addArc(rect,startAngle,360*percent);
                String content = (int)(percent*100)+"%";
                float strWidth = mArcTextPaint.measureText(content);
                //如果占比百分百，则文字绘制在水平方向
                if(percent==1.0f){
                    canvas.drawTextOnPath(content,path, (float) (2*Math.PI*mRadius*270/360-strWidth/2),-10, mArcTextPaint);
                }else{
                    canvas.drawTextOnPath(content,path, (float) ((Math.PI*mRadius*percent*2-strWidth)/2),-10, mArcTextPaint);
                }

                path.reset();
            }

            if(mIsInnerTextVisible){
                path.addArc(rectf,startAngle,360*percent);
                String name = pie.getName();
                float strWidhName = mInnerTextPaint.measureText(name);
                //如果占比百分百，则文字绘制在水平方向
                if(percent==1.0f){
                    canvas.drawTextOnPath(name,path, (float) (Math.PI*mRadius*270/360-strWidhName/2),0,mInnerTextPaint);
                }else{
                    canvas.drawTextOnPath(name,path, (float) ((Math.PI*mRadius*percent-strWidhName)/2),0,mInnerTextPaint);
                }

                path.reset();
            }

            startAngle+=360*percent;

        }


    }

    public void setmIsArcTextVisible(boolean mIsArcTextVisible) {
        this.mIsArcTextVisible = mIsArcTextVisible;
        invalidate();
    }

    public void setmIsInnerTextVisible(boolean mIsInnerTextVisible) {
        this.mIsInnerTextVisible = mIsInnerTextVisible;
        invalidate();
    }

    public boolean ismIsArcTextVisible() {
        return mIsArcTextVisible;
    }

    public boolean ismIsInnerTextVisible() {
        return mIsInnerTextVisible;
    }

    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();
    }

    public void setData(List<PieData> datas) {
        this.mData = datas;
        int sum = 0;
        for(PieData pie:datas){
            sum+=pie.getValue();
        }
        total = sum;
        invalidate();
    }

    public static class PieData{
        private String name;
        private float value;
        private int color;


        public PieData(String name, float value, int color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }


}

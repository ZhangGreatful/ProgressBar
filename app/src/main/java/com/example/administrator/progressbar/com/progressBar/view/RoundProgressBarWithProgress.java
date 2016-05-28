package com.example.administrator.progressbar.com.progressBar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.administrator.progressbar.R;

/**
 * 需要定义一些自定义属性,在attrs内定义
 * Created by Administrator on 2016/5/28 0028.
 */
public class RoundProgressBarWithProgress extends
        HorizontalProgressBarWithProgress {

    private int mRadius = dp2px(30);
    private int mMaxPaintWidth;//用于计算宽度

    public RoundProgressBarWithProgress(Context context) {
        this(context, null);
//        让一个参数的调用两个参数
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        让两个参数的调用三个参数
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mReachHeight = (int) (mUnReachHeight * 2.5f);//纯粹为了好看
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        mRadius = (int) ta.getDimension(R.styleable.RoundProgressBarWithProgress_radius, mRadius);

        ta.recycle();//每一个ta,对应一个recycle()方法
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight, mUnReachHeight);
//如果用火没有定义的话,默认四个Padding一致
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int readWidth = Math.min(width, height);

        mRadius = (readWidth - getPaddingRight() - getPaddingLeft() - mMaxPaintWidth) / 2;
        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
//         draw unreach bar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

//        draw reach bar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
//        计算弧度,用进度值除以最大值,乘以360得到
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0,
                sweepAngle, false, mPaint);
//draw text
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
        canvas.restore();
    }
}

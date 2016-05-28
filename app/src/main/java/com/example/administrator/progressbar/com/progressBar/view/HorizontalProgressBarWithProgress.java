package com.example.administrator.progressbar.com.progressBar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.example.administrator.progressbar.R;

/**
 * Created by Administrator on 2016/5/28 0028.
 */
public class HorizontalProgressBarWithProgress extends ProgressBar {
//在这里将一些属性定义成protect类型,因为要做的RoundProgressBar要继承这个类

    private static final int DEFAULT_TEXT_SIZE      = 30;//sp
    private static final int DEFAULT_TEXT_COLOR     = 0xFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH  = 0XFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 5;//dp
    private static final int DEFAULT_COLOR_REACH    = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH   = 5;//dp
    private static final int DEFAULT_TEXT_OFFSET    = 20;//dp

    protected int mTextSize      = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor     = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor  = DEFAULT_COLOR_UNREACH;
    protected int mUnReachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mReachColor    = DEFAULT_COLOR_REACH;
    protected int mReachHeight   = dp2px(DEFAULT_HEIGHT_REACH);
    protected int mTextOffset    = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();
    protected int mRealWidth;//可能在OnMeasure中赋值

    public HorizontalProgressBarWithProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        obtainStyleAttrs(attrs);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyleAttrs(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBarWithProgress);
        mTextSize = (int) ta.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progress_text_size,
                mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_text_color,
                mTextColor);
        mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_offset,
                mTextOffset);
        mUnReachColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_color,
                mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_height,
                mUnReachHeight);
        mReachColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_reach_color,
                mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_reach_height,
                mReachHeight);

        ta.recycle();

        mPaint.setTextSize(mTextSize);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取模式//获取数值,宽度一般用户给固定值,所以宽度没有测量的过程
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);//获取数值,宽度一般用户给固定值
//              高度根据用户要求进行设置
        int height = measuredHeight(heightMeasureSpec);
//        确定View的宽和高
        setMeasuredDimension(widthVal, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int measuredHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom()
                    + Math.max(Math.max(mReachHeight, mUnReachHeight),
                    Math.abs(textHeight));//测量控件的高度
//            测量值不能超过给定的size值
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnReac = false;
//      drawa reachBar
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReac = true;
        }
//        将整型转化为浮点型

        float endX = radio * mRealWidth - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);

            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }
//        draw text
        mPaint.setColor(mTextColor);
        int y = (int) -((mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

//        draw unReach bar
        if (!noNeedUnReac) {
            float start = progressX + mTextOffset / 2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();

    }


    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                getResources().getDisplayMetrics());
    }

}

package com.colonel.viewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gaojian on 2018/8/5.
 */

public class LineProgressView extends View {

    private int mProgress;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    public LineProgressView(Context context) {
        super(context);
        init();
    }

    public LineProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth * mProgress / 100 , mHeight, mPaint);
        super.onDraw(canvas);
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }

        if (progress < 0) {
            progress = 0;
        }

        mProgress = progress;
        invalidate();
    }
}

package com.colonel.viewlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by colonel on 2017/11/17.
 */

public class WaterView extends View {

    private static final String TAG = "WaterView";
    private Paint paint;
    private Path path;
    private int itemWaveLength = 500;
    private int dx;
    private int dy;
    private ValueAnimator xValueAnimator;
    private ValueAnimator yValueAnimator;

    public WaterView(Context context) {
        super(context);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GREEN);
        path = new Path();
        xValueAnimator = ValueAnimator.ofInt(0, itemWaveLength);
        xValueAnimator.setDuration(2000);
        xValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        xValueAnimator.setInterpolator(new LinearInterpolator());
        xValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        Log.e(TAG, "init: getHeight = " + getHeight());
        yValueAnimator = ValueAnimator.ofInt(0, 1920);
        yValueAnimator.setDuration(40000);
        yValueAnimator.setInterpolator(new LinearInterpolator());
        yValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dy = (int) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: dy = " + dy);
                postInvalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        path.moveTo(100, 300); // 起点
//        path.quadTo(200, 200, 300, 300); // 绝对位置 200,200控制点 300,300终点
//        path.quadTo(400, 400, 500, 300); // 绝对位置 400, 400控制点 500,300终点
//        path.rQuadTo(100, -100, 200, 0); // 相对上一个终点(100,300)做偏移 100+100=200, 300-100=200控制点 100+200=300, 300+0=300终点
//        path.rQuadTo(100, 100, 200, 0); // 相对上一个终点(300,300)做偏移

        path.reset();
        int originY = 300;
        int halfWaveLength = itemWaveLength / 2;
        if (originY + dy > getHeight() + 100) {
            cancelAnimation();
        }
        path.moveTo(-itemWaveLength + dx, originY + dy);
        for (int i = -itemWaveLength; i <= getWidth() + itemWaveLength; i += itemWaveLength) {
            path.rQuadTo(halfWaveLength / 2, -50, halfWaveLength, 0);
            path.rQuadTo(halfWaveLength / 2, 50, halfWaveLength, 0);
        }

        //path闭合
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();

        canvas.drawPath(path, paint);
    }

    public void startAnimation() {
        xValueAnimator.start();
        yValueAnimator.start();
    }

    public void cancelAnimation() {
        if (xValueAnimator.isRunning()) {
            xValueAnimator.cancel();
        }
        if (yValueAnimator.isRunning()) {
            yValueAnimator.cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimation();
    }
}

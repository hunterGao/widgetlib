package com.colonel.viewlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by colonel on 2017/11/20.
 */

public class PathPaintEffect extends View {

    private static final String TAG = "PathPaintEffect";

    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private float length;
    private ValueAnimator valueAnimator;
    private Bitmap bitmap;
    private Matrix matrix;
    private float[] pos;
    private float[] tan;
    private boolean isMove;
    private float bitmapValue;

    public PathPaintEffect(Context context) {
        super(context);
        init();
    }

    public PathPaintEffect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathPaintEffect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        path = new Path();
        path.moveTo(100, 100);
        path.lineTo(500, 100);
        path.lineTo(700, 500);

        pathMeasure = new PathMeasure();
        pathMeasure.setPath(path, false);
        length = pathMeasure.getLength();
        pos = new float[2];
        tan = new float[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow, options);
        matrix = new Matrix();

        valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                DashPathEffect effect = new DashPathEffect(new float[]{length, length}, value * length);
                paint.setPathEffect(effect);
                postInvalidate();
            }
        });

        final ValueAnimator bitmapAnimator = ValueAnimator.ofFloat(0, 1);
        bitmapAnimator.setDuration(5000);
        bitmapAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bitmapValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bitmapAnimator.start();
                isMove = true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        if (isMove) {
            float bitmapLength = length * bitmapValue;
            pathMeasure.getPosTan(bitmapLength, pos, tan);
            Log.e(TAG, "onDraw: pos = " + pos[0] + " tan = " + tan[0]);
            // 获取旋转角度
            float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
            // 设置图片旋转角度和偏移量
            matrix.reset();
            matrix.postRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
            Log.e(TAG, "onDraw: matrix = " + matrix.toString());
            canvas.drawBitmap(bitmap, matrix, paint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }
}

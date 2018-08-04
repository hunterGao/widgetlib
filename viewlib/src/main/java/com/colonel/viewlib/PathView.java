package com.colonel.viewlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
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

public class PathView extends View {

    private static final String TAG = "PathView";
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private float length;
    private Path dstPath;
    private ValueAnimator valueAnimator;
    private float value;

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        path = new Path();
        path.addCircle(400, 400, 100, Path.Direction.CW);

        pathMeasure = new PathMeasure();
        pathMeasure.setPath(path, true);
        length = pathMeasure.getLength();

        dstPath = new Path();

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dstPath.reset();

        // 硬件加速的bug
        dstPath.lineTo(0,0);
        float stop = length * value;
        float start = (float) (stop - ((0.5 - Math.abs(value - 0.5)) * length));
        // 截取Path中的一部分
        pathMeasure.getSegment(start, stop, dstPath, true);
        canvas.drawPath(dstPath, paint);
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

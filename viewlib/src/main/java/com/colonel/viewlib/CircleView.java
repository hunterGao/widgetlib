package com.colonel.viewlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.colonel.widgetlib.R;

/**
 * Created by colonel on 2017/11/20.
 */

public class CircleView extends View {

    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private float length;
    private Bitmap bitmap;
    private Matrix matrix;
    private ValueAnimator valueAnimator;
    private float value;
    private float[] pos;
    private float[] tan;


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        path = new Path();
        path.addCircle(500, 500, 200, Path.Direction.CW);

        pathMeasure = new PathMeasure(path, false);
        length = pathMeasure.getLength();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow, options);
        matrix = new Matrix();
        pos = new float[2];
        tan = new float[2];

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);

        pathMeasure.getPosTan(length * value, pos, tan);
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        // 设置bitmap的旋转度和偏移量
        matrix.reset();
        matrix.postRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
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

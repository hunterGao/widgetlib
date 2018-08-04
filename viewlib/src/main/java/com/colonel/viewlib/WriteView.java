package com.colonel.viewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 根据手势绘制路径
 * Created by colonel on 2017/11/16.
 */

public class WriteView extends View {
    private static final String TAG = "WriteView";

    private Path path = new Path();

    public WriteView(Context context) {
        super(context);
    }

    public WriteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: ACTION_DOWN");
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: ACTION_MOVE");
                path.lineTo(event.getX(), event.getY());
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouchEvent: ACTION_CANCEL");
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw: ");
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }
}

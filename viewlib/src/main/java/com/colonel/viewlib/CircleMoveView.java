package com.colonel.viewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by colonel on 2017/11/22.
 */

public class CircleMoveView extends View {
    private static final String TAG = "CircleMoveView";

    private static final int MAX_RADIUS = 120;
    private static final int INTERVAL_RADIUS = 30;
    private static final int DISTANCE = 50;
    private Paint paint;
    private int screenWidth;
    private int screenHeight;

    private float move;
    private float start;

    public CircleMoveView(Context context) {
        super(context);
        init();
    }

    public CircleMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleMoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        calculateScreen();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 5; i++) {
            float cx = screenWidth / 2 + (i - 2) * 220 + move;
            if (cx < -60) {
                cx = screenWidth + cx;
            } else if (cx > screenWidth + 60) {
                cx = cx - screenWidth;
            }
            float distance = Math.abs(cx - screenWidth / 2);
            float radius = 120 - 30 * distance / 200;

            canvas.drawCircle(cx, screenHeight / 2, radius, paint);
        }
    }

    private void calculateScreen() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start = x;
                break;
            case MotionEvent.ACTION_MOVE:
                move = x - start;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                move = 0;
                postInvalidate();
                break;
        }
        return true;
    }
}

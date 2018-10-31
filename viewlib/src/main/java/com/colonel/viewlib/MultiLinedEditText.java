package com.colonel.viewlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

import com.colonel.viewlib.utils.UIUtils;

/**
 * Created by  on 2018/1/23.
 */

@SuppressLint("AppCompatCustomView")
public class MultiLinedEditText extends EditText {
    private Paint linePaint;
    private Rect mRect;

    public MultiLinedEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        mRect = new Rect();
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.GRAY);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = mRect;
        int lineCount = getLineCount();
        int lineHeight = getLineHeight();
        int maxLineNum = getMeasuredHeight() / lineHeight;
        if (lineCount < maxLineNum) {
            lineCount = maxLineNum;
        }
        int lineBounds = getLineBounds(0, rect);
        for (int i = 0; i < lineCount; i++) {
            int y = lineHeight * i + lineBounds + getMarginBottom();
            canvas.drawLine(0, y, rect.right, y, linePaint);
        }
    }

    private int getMarginBottom() {
        return UIUtils.dp2px(getContext(), 10);
    }
}
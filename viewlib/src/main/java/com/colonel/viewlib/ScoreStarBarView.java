package com.colonel.viewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by gaojian on 2018/8/1.
 */

public class ScoreStarBarView extends View {

    private int mStarDistance = 0; //星星间距
    private int mStarCount = 5;  //星星个数
    private int mStarSize = 20;     //星星高度大小，星星一般正方形，宽度等于高度
    private float mStarMark = 0.0F;   //评分星星
    private Bitmap mStarFillBitmap; //亮星星
    private Drawable mStarEmptyDrawable; //暗星星
    private OnStarChangeListener onStarChangeListener;//监听星星变化接口
    private Paint mPaint;         //绘制星星画笔
    private boolean integerMark = false;
    private boolean isEnableTouch = false;

    public ScoreStarBarView(Context context, @DrawableRes int emptyResId, @DrawableRes int fillResId) {
        super(context);
        mStarEmptyDrawable = context.getResources().getDrawable(emptyResId);
        mStarFillBitmap = BitmapFactory.decodeResource(context.getResources(), fillResId);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(mStarFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }

    public ScoreStarBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScoreStarBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI组件
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs){
        setClickable(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        this.mStarDistance = (int) mTypedArray.getDimension(R.styleable.RatingBar_starDistance, 0);
        this.mStarSize = (int) mTypedArray.getDimension(R.styleable.RatingBar_starSize, 20);
        this.mStarCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        this.mStarEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        this.mStarFillBitmap =  drawableToBitmap(mTypedArray.getDrawable(R.styleable.RatingBar_starFill));
        mTypedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(mStarFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }

    /**
     * 设置是否需要整数评分
     * @param integerMark
     */
    public void setIntegerMark(boolean integerMark){
        this.integerMark = integerMark;
    }

    /**
     * 设置显示的星星的分数
     *
     * @param mark
     */
    public void setStarMark(float mark){
        if (integerMark) {
            mStarMark = (int)Math.ceil(mark);
        }else {
            mStarMark = Math.round(mark * 10) * 1.0f / 10;
        }
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(mStarMark);  //调用监听接口
        }
        invalidate();
    }

    /**
     * 获取显示星星的数目
     *
     * @return mStarMark
     */
    public float getStarMark(){
        return mStarMark;
    }

    public void setStarDistance(int distance) {
        mStarDistance = distance;
    }

    public void setStarSize(int size) {
        mStarSize = size;
    }


    /**
     * 定义星星点击的监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    /**
     * 设置监听
     * @param onStarChangeListener
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener){
        this.onStarChangeListener = onStarChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mStarSize * mStarCount + mStarDistance * (mStarCount - 1), mStarSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStarFillBitmap == null || mStarEmptyDrawable == null) {
            return;
        }
        for (int i = 0; i < mStarCount; i++) {
            mStarEmptyDrawable.setBounds((mStarDistance + mStarSize) * i, 0, (mStarDistance + mStarSize) * i + mStarSize, mStarSize);
            mStarEmptyDrawable.draw(canvas);
        }
        if (mStarMark > 1) {
            canvas.drawRect(0, 0, mStarSize, mStarSize, mPaint);
            if(mStarMark -(int)(mStarMark) == 0) {
                for (int i = 1; i < mStarMark; i++) {
                    canvas.translate(mStarDistance + mStarSize, 0);
                    canvas.drawRect(0, 0, mStarSize, mStarSize, mPaint);
                }
            }else {
                for (int i = 1; i < mStarMark - 1; i++) {
                    canvas.translate(mStarDistance + mStarSize, 0);
                    canvas.drawRect(0, 0, mStarSize, mStarSize, mPaint);
                }
                canvas.translate(mStarDistance + mStarSize, 0);
                canvas.drawRect(0, 0, mStarSize * (Math.round((mStarMark - (int) (mStarMark))*10)*1.0f/10), mStarSize, mPaint);
            }
        }else {
            canvas.drawRect(0, 0, mStarSize * mStarMark, mStarSize, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnableTouch) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX();
        if (x < 0) x = 0;
        if (x > getMeasuredWidth()) x = getMeasuredWidth();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/ mStarCount));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/ mStarCount));
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null)return null;
        Bitmap bitmap = Bitmap.createBitmap(mStarSize, mStarSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, mStarSize, mStarSize);
        drawable.draw(canvas);
        return bitmap;
    }
}

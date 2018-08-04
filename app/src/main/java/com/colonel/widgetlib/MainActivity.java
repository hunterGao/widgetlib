package com.colonel.widgetlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "dispatchTouchEvent: ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "dispatchTouchEvent: ACTION_UP");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "dispatchTouchEvent: ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.e(TAG, "dispatchTouchEvent: ACTION_CANCEL");
//                break;
//        }
//        boolean dispatch = super.dispatchTouchEvent(ev);
//        Log.e(TAG, "dispatchTouchEvent: dispatch = " + dispatch);
//        return dispatch;
//    }
}

package com.capshot.capshotassignmentnew.Gestures;

import android.content.Context;
import android.view.MotionEvent;

public abstract class BaseGestureDetector {
    protected static final float PRESSURE_THRESHOLD = 0.67f;
    protected final Context mContext;
    protected boolean mGestureInProgress;
    protected MotionEvent mPrevEvent;
    protected MotionEvent mCurrEvent;
    protected float mCurrPressure;
    protected float mPrevPressure;
    protected long mTimeDelta;


    public BaseGestureDetector(Context context) {
        mContext = context;
    }
    public boolean onTouchEvent(MotionEvent event) {
        final int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        if (!mGestureInProgress) {
            handleStartProgressEvent(actionCode, event);
        } else {
            handleInProgressEvent(actionCode, event);
        }
        return true;
    }

    //The handling in this implementation may set the gesture in progress
    protected abstract void handleStartProgressEvent(int actionCode, MotionEvent event);

    //Called when the current event occurred when a gesture IS in progress. The
    protected abstract void handleInProgressEvent(int actionCode, MotionEvent event);


    protected void updateStateByEvent(MotionEvent curr) {
        final MotionEvent prev = mPrevEvent;

        // Reset mCurrEvent
        if (mCurrEvent != null) {
            mCurrEvent.recycle();
            mCurrEvent = null;
        }
        mCurrEvent = MotionEvent.obtain(curr);


        // Delta time
        mTimeDelta = curr.getEventTime() - prev.getEventTime();

        // Pressure
        mCurrPressure = curr.getPressure(curr.getActionIndex());
        mPrevPressure = prev.getPressure(prev.getActionIndex());
    }

    protected void resetState() {
        if (mPrevEvent != null) {
            mPrevEvent.recycle();
            mPrevEvent = null;
        }
        if (mCurrEvent != null) {
            mCurrEvent.recycle();
            mCurrEvent = null;
        }
        mGestureInProgress = false;
    }


    //inprogress or not
    public boolean isInProgress() {
        return mGestureInProgress;
    }


    //Return the time difference in milliseconds between the previous accepted GestureDetector event and the current GestureDetector event.
    public long getTimeDelta() {
        return mTimeDelta;
    }


    //Return the event time of the current GestureDetector event being processed.
    public long getEventTime() {
        return mCurrEvent.getEventTime();
    }

}

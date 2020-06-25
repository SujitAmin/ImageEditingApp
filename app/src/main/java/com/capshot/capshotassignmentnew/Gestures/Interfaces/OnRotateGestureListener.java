package com.capshot.capshotassignmentnew.Gestures.Interfaces;

// Listener which must be implemented which is used by RotateGestureDetector
//to perform callbacks to any implementing class which is registered to a
//RotateGestureDetector via the constructor.

import com.capshot.capshotassignmentnew.Gestures.RotateGestureDetector;

public interface OnRotateGestureListener {
    public boolean onRotate(RotateGestureDetector detector);

    public boolean onRotateBegin(RotateGestureDetector detector);

    public void onRotateEnd(RotateGestureDetector detector);
}

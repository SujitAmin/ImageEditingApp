package com.capshot.capshotassignmentnew.Gestures.Interfaces;

import com.capshot.capshotassignmentnew.Gestures.MoveGestureDetector;
// Listener which must be implemented which is used by MoveGestureDetector to perform call to any implementing class
// which is registered to MoveGestureDetector via the constructor.
public interface OnMoveGestureListener {
    public boolean onMove(MoveGestureDetector detector);

    public boolean onMoveBegin(MoveGestureDetector detector);

    public void onMoveEnd(MoveGestureDetector detector);
}

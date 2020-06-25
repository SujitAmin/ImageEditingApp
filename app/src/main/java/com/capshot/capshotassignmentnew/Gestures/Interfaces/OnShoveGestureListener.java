package com.capshot.capshotassignmentnew.Gestures.Interfaces;

import com.capshot.capshotassignmentnew.Gestures.ShoveGestureDetector;

/**
 * Listener which must be implemented which is used by ShoveGestureDetector
 * to perform callbacks to any implementing class which is registered to a
 * ShoveGestureDetector via the constructor.
 *
 * @see ShoveGestureDetector.SimpleOnShoveGestureListener
 */
public interface OnShoveGestureListener {
    public boolean onShove(ShoveGestureDetector detector);

    public boolean onShoveBegin(ShoveGestureDetector detector);

    public void onShoveEnd(ShoveGestureDetector detector);
}

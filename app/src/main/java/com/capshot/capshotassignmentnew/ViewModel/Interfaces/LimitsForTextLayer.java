package com.capshot.capshotassignmentnew.ViewModel.Interfaces;

public interface LimitsForTextLayer {
    /**
     * limit text size to view bounds
     * so that users don't put small font size and scale it 100+ times
     */
    float MAX_SCALE = 1.0F;
    float MIN_SCALE = 0.2F;

    float MIN_BITMAP_HEIGHT = 0.13F;
    float FONT_SIZE_STEP = 0.008F;
    float INITIAL_FONT_SIZE = 0.075F;
    int INITIAL_FONT_COLOR = 0xffffffff;
    // to avoid text scaling
    float INITIAL_SCALE = 0.8F;
}

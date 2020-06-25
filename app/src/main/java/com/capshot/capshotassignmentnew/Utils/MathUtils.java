package com.capshot.capshotassignmentnew.Utils;

import android.graphics.PointF;

import androidx.annotation.NonNull;

public class MathUtils {

    public static boolean pointInTriangle(@NonNull PointF pt, @NonNull PointF v1,
                                          @NonNull PointF v2, @NonNull PointF v3) {
        boolean b1 = crossProduct(pt, v1, v2) < 0.0f;
        boolean b2 = crossProduct(pt, v2, v3) < 0.0f;
        boolean b3 = crossProduct(pt, v3, v1) < 0.0f;
        return (b1 == b2) && (b2 == b3);
    }

    private static float crossProduct(@NonNull PointF a, @NonNull PointF b, @NonNull PointF c) {
        return crossProduct(a.x, a.y, b.x, b.y, c.x, c.y);
    }

    private static float crossProduct(float ax, float ay, float bx, float by, float cx, float cy) {
        return (ax - cx) * (by - cy) - (bx - cx) * (ay - cy);
    }
}
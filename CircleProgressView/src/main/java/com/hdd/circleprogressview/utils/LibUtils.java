package com.hdd.circleprogressview.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Create on 2019-05-27
 *
 * @author duonghd
 */

public class LibUtils {

    public static int dpToPx(Context context, float dp) {
        if (context == null) throw new RuntimeException("Context is null, init(Context)");
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
}

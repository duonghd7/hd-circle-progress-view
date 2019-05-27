package com.hdd.circleprogressview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created on 1/24/2018.
 *
 * @author duonghd
 */
@SuppressLint("DrawAllocation,Recycle")
public class CircleProgress extends View {
    private static final String TAG = CircleProgress.class.getSimpleName();
    private Paint drawPaint;
    private float cpvSize;
    private float cpvStartAngle;
    private float cpvSweepAngle;

    private boolean useCenter;
    private float painStrokeWidth;
    private Paint.Cap painCap;
    private Paint.Style painStyle;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float pad = painStrokeWidth / 2f;
        RectF outerOval = new RectF(pad, pad, cpvSize - pad, cpvSize - pad);
        drawPaint.setStrokeWidth(painStrokeWidth);
        drawPaint.setStrokeCap(painCap);
        drawPaint.setStyle(painStyle);
        canvas.drawArc(outerOval, cpvStartAngle, cpvSweepAngle, useCenter, drawPaint);
    }

    public void initProgress(float cpvSize, float cpvStartAngle, float cpvSweepAngle, int progressColor,
                             boolean useCenter, float painStrokeWidth, Paint.Cap painCap, Paint.Style painStyle) {
        this.cpvSize = cpvSize;
        this.cpvStartAngle = cpvStartAngle;
        this.cpvSweepAngle = cpvSweepAngle;
        this.useCenter = useCenter;
        this.painStrokeWidth = painStrokeWidth;
        this.painCap = painCap;
        this.painStyle = painStyle;

        try {
            drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            drawPaint.setColor(progressColor);
            drawPaint.setAntiAlias(true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void updateStart(float cpvStartAngle) {
        this.cpvStartAngle = cpvStartAngle;
        invalidate();
    }

    public void updateSweep(float cpvSweepAngle) {
        this.cpvSweepAngle = cpvSweepAngle;
        invalidate();
    }
}

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
    private float cpvRectFLeft;
    private float cpvRectFTop;
    private float cpvRectFRight;
    private float cpvRectFBottom;
    private float cpvStartAngle;
    private float cpvSweepAngle;
    
    public CircleProgress(Context context) {
        this(context, null);
    }
    
    public CircleProgress(Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onDraw(final Canvas canvas) {
        RectF rectF = new RectF(cpvRectFLeft, cpvRectFTop, cpvRectFRight, cpvRectFBottom);
        canvas.drawArc(rectF, cpvStartAngle, cpvSweepAngle, true, drawPaint);
    }
    
    public void initProgress(float cpvRectFLeft, float cpvRectFTop, float cpvRectFRight, float cpvRectFBottom,
                             float cpvStartAngle, float cpvSweepAngle, int progressColor) {
        
        this.cpvRectFLeft = cpvRectFLeft;
        this.cpvRectFTop = cpvRectFTop;
        this.cpvRectFRight = cpvRectFRight;
        this.cpvRectFBottom = cpvRectFBottom;
        this.cpvStartAngle = cpvStartAngle;
        this.cpvSweepAngle = cpvSweepAngle;
        
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

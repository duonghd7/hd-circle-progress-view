package com.hdd.circleprogressview.v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hdd.circleprogressview.R;
import com.hdd.circleprogressview.utils.CircleProgressTime;
import com.hdd.circleprogressview.utils.LibUtils;

/**
 * Created on 1/23/2018.
 *
 * @author duonghd
 */

@SuppressLint("DrawAllocation,Recycle")
public class CircleProgressView1 extends RelativeLayout implements CircleProgressInterface1 {
    private static final String TAG = CircleProgressView1.class.getSimpleName();
    private FrameLayout flRoot;
    private CircleProgress circleProgress;
    private View viewBackground;

    private float cpvStartAngle;
    private float cpvSweepAngle;
    private int cpvStrokeWidth;

    private boolean isSetStartAngle = false;
    private boolean isSetSweepAngle = false;
    private String cpvBackground;
    private String cpvProgressColor;
    private int cpvTotalTime;
    private int progressColor;
    private int backgroundColor;
    private int cpvSize;
    private TypedArray typedArray;

    private CountDownTimer countDownTimer;
    private int countDownTime;
    private int countDownInterval;
    private int mainTime;
    private boolean isRun = false;
    private long lastMillisUntil;
    private long sumErrorNumber;

    private boolean useCenter;
    private float painStrokeWidth;
    private Paint.Cap painCap;
    private Paint.Style painStyle;

    private CircleProgressView1.OnPlayListener onPlayListener;

    public CircleProgressView1(Context context) {
        this(context, null);
    }

    public CircleProgressView1(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final int size03 = LibUtils.dpToPx(context, 0.6f);
        this.addView(flRoot = new FrameLayout(context));

        RelativeLayout.LayoutParams vLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        vLayoutParam.setMargins(size03, size03, size03, size03);
        viewBackground = new View(context);
        viewBackground.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_background));
        viewBackground.setLayoutParams(vLayoutParam);

        FrameLayout.LayoutParams cpLayoutParam = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        circleProgress = new CircleProgress(context);
        circleProgress.setLayoutParams(cpLayoutParam);
        circleProgress.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_background));

        flRoot.removeAllViews();
        flRoot.addView(viewBackground);
        flRoot.addView(circleProgress);

        typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
        cpvBackground = typedArray.getString(R.styleable.CircleProgressView_cpvBackground);
        cpvProgressColor = typedArray.getString(R.styleable.CircleProgressView_cpvProgressColor);
        cpvStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_cpvStrokeWidth, 20);
        cpvTotalTime = 30 * 1000;    //default = 30 second
        int cpvMode = typedArray.getInteger(R.styleable.CircleProgressView_cpvMode, 0);
        if (cpvMode == 0) {
            useCenter = true;
            painStrokeWidth = 0f;
            painCap = Paint.Cap.BUTT;
            painStyle = Paint.Style.FILL;
        } else if (cpvMode == 1) {
            useCenter = false;
            painStrokeWidth = cpvStrokeWidth;
            painCap = Paint.Cap.BUTT;
            painStyle = Paint.Style.STROKE;
        } else if (cpvMode == 2) {
            useCenter = false;
            painStrokeWidth = cpvStrokeWidth;
            painCap = Paint.Cap.ROUND;
            painStyle = Paint.Style.STROKE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        cpvSize = Math.min(width, height);
        initData();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initData() {
        flRoot.getLayoutParams().width = cpvSize;
        flRoot.getLayoutParams().height = cpvSize;

        cpvStartAngle = isSetStartAngle ? cpvStartAngle : typedArray.getFloat(R.styleable.CircleProgressView_cpvStartAngle, -90);
        cpvSweepAngle = isSetSweepAngle ? cpvSweepAngle : typedArray.getFloat(R.styleable.CircleProgressView_cpvSweepAngle, 0);
        progressColor = getContext().getResources().getColor(R.color.colorPrimary);
        backgroundColor = getContext().getResources().getColor(R.color.colorWhite);

        if (cpvProgressColor != null) {
            try {
                progressColor = Color.parseColor(cpvProgressColor);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        if (cpvBackground != null) {
            try {
                backgroundColor = Color.parseColor(cpvBackground);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        viewBackground.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC);
        circleProgress.initProgress(cpvSize, cpvStartAngle, cpvSweepAngle,
                progressColor, useCenter, painStrokeWidth, painCap, painStyle);
    }

    private void initTime() {
        try {
            countDownInterval = countDownInterval > 20 ? countDownInterval : 20;
            int timeAdd = cpvTotalTime / 5;
            lastMillisUntil = countDownTime + timeAdd;
            sumErrorNumber = 0;
            countDownTimer = new CountDownTimer(countDownTime + timeAdd, countDownInterval - 1) {
                public void onTick(long millisUntilFinished) {
                    sumErrorNumber += (lastMillisUntil - millisUntilFinished);
                    mainTime = (int) (cpvTotalTime + timeAdd - millisUntilFinished - sumErrorNumber);
                    lastMillisUntil = millisUntilFinished - countDownInterval;

                    if (mainTime < cpvTotalTime) {
                        cpvSweepAngle = mainTime * 360f / cpvTotalTime;
                        circleProgress.updateSweep(cpvSweepAngle);
                        if (onPlayListener != null) {
                            onPlayListener.onPlay(mainTime);
                        }
                    } else {
                        mainTime = cpvTotalTime;
                        cpvSweepAngle = 360f;
                        circleProgress.updateSweep(cpvSweepAngle);
                        if (onPlayListener != null) {
                            onPlayListener.onPlay(mainTime);
                            onPlayListener.onFinish();
                        }
                        countDownTimer.cancel();
                    }
                }

                public void onFinish() {

                }
            };
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /*
     * default is millisecond
     * */
    @Override
    public CircleProgressView1 setTotalTime(int cpvTotalTime) {
        return setTotalTime(cpvTotalTime, CircleProgressTime.MILLI_SECOND);
    }

    @Override
    public CircleProgressView1 setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime) {
        switch (circleProgressTime) {
            case SECOND:
                this.cpvTotalTime = cpvTotalTime * 1000;
                break;
            case MILLI_SECOND:
                this.cpvTotalTime = cpvTotalTime;
                break;
        }
        return this;
    }

    @Override
    public CircleProgressView1 setCountDownInterval(int countDownInterval) {
        return setCountDownInterval(cpvTotalTime, CircleProgressTime.MILLI_SECOND);
    }

    @Override
    public CircleProgressView1 setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime) {
        switch (circleProgressTime) {
            case SECOND:
                this.countDownInterval = countDownInterval * 1000;
                break;
            case MILLI_SECOND:
                this.countDownInterval = countDownInterval;
                break;
        }
        this.countDownInterval = this.countDownInterval > 20 ? this.countDownInterval : 20;
        return this;
    }

    @Override
    public CircleProgressView1 setStartAngle(float cpvStartAngle) {
        this.cpvStartAngle = cpvStartAngle;
        circleProgress.updateStart(this.cpvStartAngle);
        isSetStartAngle = true;
        return this;
    }

    @Override
    public CircleProgressView1 setSweepAngle(float cpvSweepAngle) {
        this.cpvSweepAngle = cpvSweepAngle;
        circleProgress.updateSweep(this.cpvSweepAngle);
        isSetSweepAngle = true;
        return this;
    }

    @Override
    public CircleProgressView1 cpvStart() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isRun = false;
        }
        mainTime = 0;
        countDownTime = cpvTotalTime;
        initTime();
        countDownTimer.start();
        isRun = true;
        return this;
    }

    @Override
    public CircleProgressView1 cpvPause() {
        if (isRun) {
            isRun = false;
            countDownTimer.cancel();
        }
        return this;
    }

    @Override
    public CircleProgressView1 cpvResume() {
        if (!isRun) {
            mainTime = (int) (cpvSweepAngle * cpvTotalTime / 360f);
            countDownTime = cpvTotalTime - mainTime;
            initTime();
            countDownTimer.start();
            isRun = true;
        }
        return this;
    }

    @Override
    public void setOnPlayListener(CircleProgressView1.OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    public interface OnPlayListener {
        void onPlay(int momentTime);

        void onFinish();
    }

    @SuppressLint("DrawAllocation,Recycle")
    private class CircleProgress extends View {
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

}
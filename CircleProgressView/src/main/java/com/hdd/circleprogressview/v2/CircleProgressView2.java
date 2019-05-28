package com.hdd.circleprogressview.v2;

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

/**
 * Create on 2019-05-27
 *
 * @author duonghd
 */

@SuppressLint("DrawAllocation,Recycle")
public class CircleProgressView2 extends RelativeLayout implements CircleProgressInterface2 {
    private static final String TAG = CircleProgressView2.class.getSimpleName();
    private Context context;

    private FrameLayout flRoot;
    private CircleProgress circleProgressTop;
    private CircleProgress circleProgressSecond;
    private CircleProgress circleProgressBorder;
    private View viewBackground;

    private int cpvTotalTime;
    private int cpvMode;

    private float cpvStartAngle;
    private float cpvSweepAngle;

    private int cpvStrokeWidth;
    private int cpvBorderWidth;
    private int cpvBorderMargin;

    private String cpvBackgroundColor;
    private String cpvBorderColor;
    private String cpvProgressColor;
    private String cpvProgressSecondColor;

    private int backgroundColor;
    private int borderColor;
    private int progressColor;
    private int progressSecondColor;

    private TypedArray typedArray;

    private CountDownTimer countDownTimer;
    private int countDownTime;
    private int countDownInterval;
    private boolean isRun = false;
    private long lastMillisUntil;
    private long sumErrorNumber;

    private Paint.Cap painCap;
    private Paint.Style painStyle;
    private int rootViewSize;

    private CircleProgressView2.OnPlayListener onPlayListener;

    public CircleProgressView2(Context context) {
        this(context, null);
    }

    public CircleProgressView2(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        cpvTotalTime = 30 * 1000;    //default = 30 second
        typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView2, 0, 0);

        cpvBackgroundColor = typedArray.getString(R.styleable.CircleProgressView2_cpvBackground2);
        cpvBorderColor = typedArray.getString(R.styleable.CircleProgressView2_cpvBorderColor2);
        cpvProgressColor = typedArray.getString(R.styleable.CircleProgressView2_cpvProgressColor2);
        cpvProgressSecondColor = typedArray.getString(R.styleable.CircleProgressView2_cpvProgressSecondColor2);

        cpvStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView2_cpvStrokeWidth2, 20);
        cpvBorderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView2_cpvBorderWidth2, 20);
        cpvBorderMargin = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView2_cpvBorderMargin2, 10);

        this.addView(flRoot = new FrameLayout(context));

        cpvMode = typedArray.getInteger(R.styleable.CircleProgressView2_cpvMode2, 0);
        if (cpvMode == CircleProgressMode.STROKE_BUTT_1.label) {
            /*
             * cpvMode == cpvStrokeButt1
             * */
            painCap = Paint.Cap.BUTT;
            painStyle = Paint.Style.STROKE;
        } else if (cpvMode == CircleProgressMode.STROKE_BUTT_2.label) {
            /*
             * cpvMode == cpvStrokeButt2
             * */
            painCap = Paint.Cap.BUTT;
            painStyle = Paint.Style.STROKE;
        } else if (cpvMode == CircleProgressMode.STROKE_ROUND.label) {
            /*
             * cpvMode == cpvStrokeRound1
             * */
            painCap = Paint.Cap.ROUND;
            painStyle = Paint.Style.STROKE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        rootViewSize = Math.min(width, height);
        addView();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void addView() {
        flRoot.getLayoutParams().width = rootViewSize;
        flRoot.getLayoutParams().height = rootViewSize;

        FrameLayout.LayoutParams backgroundLayoutParam = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int backgroundMargin = cpvBorderMargin + cpvBorderWidth / 2;
        backgroundMargin = backgroundMargin != 0 ? backgroundMargin : 1;
        backgroundLayoutParam.setMargins(backgroundMargin, backgroundMargin, backgroundMargin, backgroundMargin);
        viewBackground = new View(context);
        viewBackground.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_background));
        viewBackground.setLayoutParams(backgroundLayoutParam);

        int borderSize = rootViewSize - 2 * cpvBorderMargin;
        borderSize = borderSize > 0 ? borderSize : 0;
        FrameLayout.LayoutParams borderLayoutParam = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        borderLayoutParam.setMargins(cpvBorderMargin, cpvBorderMargin, cpvBorderMargin, cpvBorderMargin);
        circleProgressBorder = new CircleProgress(context);
        circleProgressBorder.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_background));
        circleProgressBorder.setLayoutParams(borderLayoutParam);

        circleProgressSecond = new CircleProgress(context);
        circleProgressSecond.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_background));
        circleProgressSecond.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        circleProgressTop = new CircleProgress(context);
        circleProgressTop.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_background));
        circleProgressTop.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        flRoot.removeAllViews();
        flRoot.addView(viewBackground);
        flRoot.addView(circleProgressBorder);
        flRoot.addView(circleProgressSecond);
        flRoot.addView(circleProgressTop);

        initColor();

        cpvStartAngle = typedArray.getFloat(R.styleable.CircleProgressView2_cpvStartAngle2, -90);
        cpvSweepAngle = typedArray.getFloat(R.styleable.CircleProgressView2_cpvSweepAngle2, 0);

        viewBackground.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC);
        circleProgressBorder.initProgress(borderSize, -90, 360, borderColor, cpvBorderWidth, Paint.Cap.BUTT, Paint.Style.STROKE);

        initSweepAngle();
    }

    private void initSweepAngle() {
        if (cpvMode == CircleProgressMode.STROKE_BUTT_2.label) {
            /*
             * cpvMode == cpvStrokeButt2
             * */
            circleProgressSecond.setVisibility(VISIBLE);
            cpvSweepAngle = cpvSweepAngle != 360 ? cpvSweepAngle % 360 : cpvSweepAngle;
            cpvSweepAngle = cpvSweepAngle < 0 ? cpvSweepAngle + 360 : cpvSweepAngle;
            float secondProgress = cpvSweepAngle < 180 ? cpvSweepAngle : 180;
            circleProgressSecond.initProgress(rootViewSize, cpvStartAngle, secondProgress, progressSecondColor, cpvStrokeWidth, painCap, painStyle);
            float topProgress = cpvSweepAngle > 180 ? cpvSweepAngle - 180 : 0;
            circleProgressTop.initProgress(rootViewSize, cpvStartAngle + 180, topProgress, progressColor, cpvStrokeWidth, painCap, painStyle);
        } else {
            /*
             * cpvMode == cpvStrokeRound1 ||
             * cpvMode == cpvStrokeButt1
             * */
            circleProgressSecond.setVisibility(GONE);
            cpvSweepAngle = cpvSweepAngle != 360 ? cpvSweepAngle % 360 : cpvSweepAngle;
            cpvSweepAngle = cpvSweepAngle < 0 ? cpvSweepAngle + 360 : cpvSweepAngle;
            circleProgressSecond.initProgress(rootViewSize, 0, 0, progressSecondColor, cpvStrokeWidth, painCap, painStyle);
            circleProgressTop.initProgress(rootViewSize, cpvStartAngle, cpvSweepAngle, progressColor, cpvStrokeWidth, painCap, painStyle);
        }
    }

    private void initColor() {
        backgroundColor = getContext().getResources().getColor(R.color.colorWhite);
        borderColor = getContext().getResources().getColor(R.color.colorWhite);
        progressSecondColor = getContext().getResources().getColor(R.color.colorPrimary);
        progressColor = getContext().getResources().getColor(R.color.colorPrimary);

        if (cpvBackgroundColor != null) {
            try {
                backgroundColor = Color.parseColor(cpvBackgroundColor);
            } catch (Exception e) {
                backgroundColor = getContext().getResources().getColor(R.color.colorWhite);
                Log.e(TAG, e.getMessage());
            }
        }

        if (cpvBorderColor != null) {
            try {
                borderColor = Color.parseColor(cpvBorderColor);
            } catch (Exception e) {
                borderColor = getContext().getResources().getColor(R.color.colorWhite);
                Log.e(TAG, e.getMessage());
            }
        }

        if (cpvProgressSecondColor != null) {
            try {
                progressSecondColor = Color.parseColor(cpvProgressSecondColor);
            } catch (Exception e) {
                progressSecondColor = getContext().getResources().getColor(R.color.colorPrimary);
                Log.e(TAG, e.getMessage());
            }
        }

        if (cpvProgressColor != null) {
            try {
                progressColor = Color.parseColor(cpvProgressColor);
            } catch (Exception e) {
                progressColor = getContext().getResources().getColor(R.color.colorPrimary);
                Log.e(TAG, e.getMessage());
            }
        }


    }

    /*
     * default is millisecond
     * */
    @Override
    public CircleProgressView2 setTotalTime(int cpvTotalTime) {
        return setTotalTime(cpvTotalTime, CircleProgressTime.MILLI_SECOND);
    }

    @Override
    public CircleProgressView2 setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime) {
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
    public CircleProgressView2 setCountDownInterval(int countDownInterval) {
        return setCountDownInterval(cpvTotalTime, CircleProgressTime.MILLI_SECOND);
    }

    @Override
    public CircleProgressView2 setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime) {
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
    public CircleProgressView2 setStartTime(int cpvStartTime) {
        setStartTime(cpvStartTime, CircleProgressTime.MILLI_SECOND);
        return this;
    }

    @Override
    public CircleProgressView2 setStartTime(int cpvStartTime, CircleProgressTime circleProgressTime) {
        if (circleProgressTime == CircleProgressTime.SECOND) {
            cpvStartTime = cpvStartTime * 1000;
        }

        if (!isRun) {
            cpvSweepAngle = cpvStartTime * 360f / cpvTotalTime;
            updateSweepAngle();
        }

        return this;
    }

    @Override
    public CircleProgressView2 setMode(CircleProgressMode circleProgressMode) {
        cpvMode = circleProgressMode.label;
        if (circleProgressSecond != null) {
            if (cpvMode == CircleProgressMode.STROKE_BUTT_2.label) {
                circleProgressSecond.setVisibility(VISIBLE);
            } else {
                circleProgressSecond.setVisibility(GONE);
            }
        }

        return this;
    }

    @Override
    public CircleProgressView2 cpvStart() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isRun = false;
        }

        countDownTime = cpvTotalTime;
        initTime();
        countDownTimer.start();
        isRun = true;
        return this;
    }

    @Override
    public CircleProgressView2 cpvPause() {
        if (isRun) {
            isRun = false;
            countDownTimer.cancel();
        }
        return this;
    }

    @Override
    public CircleProgressView2 cpvResume() {
        if (!isRun) {
            countDownTime = (int) (cpvSweepAngle * cpvTotalTime / 360f);

            initTime();
            countDownTimer.start();
            isRun = true;
        }
        return this;
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
                    countDownTime = (int) (millisUntilFinished + sumErrorNumber - timeAdd);
                    lastMillisUntil = millisUntilFinished - countDownInterval;

                    if (countDownTime > 0) {
                        cpvSweepAngle = countDownTime * 360f / cpvTotalTime;
                        updateSweepAngle();
                        if (onPlayListener != null) onPlayListener.onPlay(countDownTime);
                    } else {
                        cpvSweepAngle = 0;
                        updateSweepAngle();
                        countDownTimer.cancel();
                        isRun = false;
                        if (onPlayListener != null) {
                            onPlayListener.onPlay(countDownTime);
                            onPlayListener.onFinish();
                        }
                    }
                }

                public void onFinish() {

                }
            };
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void updateSweepAngle() {
        if (cpvMode == CircleProgressMode.STROKE_BUTT_2.label) {
            /*
             * cpvMode == cpvStrokeButt2
             * */
            cpvSweepAngle = cpvSweepAngle != 360 ? cpvSweepAngle % 360 : cpvSweepAngle;
            cpvSweepAngle = cpvSweepAngle < 0 ? cpvSweepAngle + 360 : cpvSweepAngle;
            float secondProgress = cpvSweepAngle < 180 ? cpvSweepAngle : 180;
            circleProgressSecond.updateSweep(secondProgress);
            float topProgress = cpvSweepAngle > 180 ? cpvSweepAngle - 180 : 0;
            circleProgressTop.updateSweep(topProgress);
        } else {
            /*
             * cpvMode == cpvStrokeRound1 ||
             * cpvMode == cpvStrokeButt1
             * */
            cpvSweepAngle = cpvSweepAngle != 360 ? cpvSweepAngle % 360 : cpvSweepAngle;
            cpvSweepAngle = cpvSweepAngle < 0 ? cpvSweepAngle + 360 : cpvSweepAngle;
            circleProgressTop.updateSweep(cpvSweepAngle);
        }
    }

    @Override
    public void setOnPlayListener(CircleProgressView2.OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    public interface OnPlayListener {
        void onPlay(int momentTime);

        void onFinish();
    }

    public enum CircleProgressMode {
        STROKE_BUTT_1(0),
        STROKE_BUTT_2(1),
        STROKE_ROUND(2);

        public int label;

        CircleProgressMode(int label) {
            this.label = label;
        }
    }


    @SuppressLint("DrawAllocation,Recycle")
    private class CircleProgress extends View {
        private Paint cpDrawPaint;
        private float cpSize;
        private float cpStartAngle;
        private float cpSweepAngle;

        private float cpPainStrokeWidth;
        private Paint.Cap cpPainCap;
        private Paint.Style cpPainStyle;

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
            float pad = cpPainStrokeWidth / 2f;
            RectF outerOval = new RectF(pad, pad, cpSize - pad, cpSize - pad);
            cpDrawPaint.setStrokeWidth(cpPainStrokeWidth);
            cpDrawPaint.setStrokeCap(cpPainCap);
            cpDrawPaint.setStyle(cpPainStyle);
            canvas.drawArc(outerOval, cpStartAngle, cpSweepAngle, false, cpDrawPaint);
        }

        public void initProgress(float cpSize, float cpStartAngle, float cpSweepAngle, int cpProgressColor, float cpPainStrokeWidth, Paint.Cap cpPainCap, Paint.Style cpPainStyle) {
            this.cpSize = cpSize;
            this.cpStartAngle = cpStartAngle;
            this.cpSweepAngle = cpSweepAngle;
            this.cpPainStrokeWidth = cpPainStrokeWidth;
            this.cpPainCap = cpPainCap;
            this.cpPainStyle = cpPainStyle;

            try {
                cpDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                cpDrawPaint.setColor(cpProgressColor);
                cpDrawPaint.setAntiAlias(true);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        public void updateStart(float cpStartAngle) {
            this.cpStartAngle = cpStartAngle;
            invalidate();
        }

        public void updateSweep(float cpSweepAngle) {
            this.cpSweepAngle = cpSweepAngle;
            invalidate();
        }
    }
}
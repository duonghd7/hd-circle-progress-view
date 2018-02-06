package com.hdd.circleprogressview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created on 1/23/2018.
 *
 * @author duonghd
 */

@SuppressLint("DrawAllocation,Recycle")
public class CircleProgressView extends RelativeLayout implements CircleProgressInterface {
    private static final String TAG = CircleProgressView.class.getSimpleName();
    private RelativeLayout rlRoot;
    private CircleProgress circleProgress;
    private View viewBackground;
    
    private float cpvRectFLeft;
    private float cpvRectFTop;
    private float cpvRectFRight;
    private float cpvRectFBottom;
    private float cpvStartAngle;
    private float cpvSweepAngle;
    private boolean isSetStartAngle = false;
    private boolean isSetSweepAngle = false;
    private String cpvBackground;
    private String cpvProgressColor;
    private int cpvTotalTime;
    private int progressColor;
    private int backgroundColor;
    private int cpvSize;
    private TypedArray a;
    
    private CountDownTimer countDownTimer;
    private int countDownTime;
    private int countDownInterval;
    private int mainTime;
    private boolean isRun = false;
    private long lastMillisUntil;
    private long sumErrorNumber;
    
    private CircleProgressView.OnPlayListener onPlayListener = new CircleProgressView.OnPlayListener() {
        @Override
        public void onPlay(int momentTime) {
            
        }
        
        @Override
        public void onFinish() {
            
        }
    };
    
    public CircleProgressView(Context context) {
        this(context, null);
    }
    
    public CircleProgressView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_circle_progress, this);
        rlRoot = findViewById(R.id.view_circle_progress_rl_root);
        circleProgress = findViewById(R.id.view_circle_progress_cp_circle_progress);
        viewBackground = findViewById(R.id.view_circle_progress_v_background);
        
        a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
        cpvBackground = a.getString(R.styleable.CircleProgressView_cpvBackground);
        cpvProgressColor = a.getString(R.styleable.CircleProgressView_cpvProgressColor);
        cpvTotalTime = a.getInteger(R.styleable.CircleProgressView_cpvTotalTime, 30 * 1000);    //default = 30 second
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        cpvSize = width < height ? width : height;
        initData();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private void initData() {
        rlRoot.getLayoutParams().width = cpvSize;
        rlRoot.getLayoutParams().height = cpvSize;
        
        cpvRectFLeft = a.getDimension(R.styleable.CircleProgressView_cpvRectFLeft, 0);
        cpvRectFTop = a.getDimension(R.styleable.CircleProgressView_cpvRectFTop, 0);
        cpvRectFRight = a.getDimension(R.styleable.CircleProgressView_cpvRectFRight, (float) cpvSize);
        cpvRectFBottom = a.getDimension(R.styleable.CircleProgressView_cpvRectFBottom, (float) cpvSize);
        cpvStartAngle = isSetStartAngle ? cpvStartAngle : a.getFloat(R.styleable.CircleProgressView_cpvStartAngle, -90);
        cpvSweepAngle = isSetSweepAngle ? cpvSweepAngle : a.getFloat(R.styleable.CircleProgressView_cpvSweepAngle, 0);
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
        
        circleProgress.initProgress(cpvRectFLeft, cpvRectFTop, cpvRectFRight, cpvRectFBottom,
                cpvStartAngle, cpvSweepAngle, progressColor);
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
                        onPlayListener.onPlay(mainTime);
                    } else {
                        mainTime = cpvTotalTime;
                        cpvSweepAngle = 360f;
                        circleProgress.updateSweep(cpvSweepAngle);
                        onPlayListener.onPlay(mainTime);
                        onPlayListener.onFinish();
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
    public CircleProgressView setTotalTime(int cpvTotalTime) {
        return setTotalTime(cpvTotalTime, CircleProgressTime.MILLI_SECOND);
    }
    
    @Override
    public CircleProgressView setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime) {
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
    public CircleProgressView setCountDownInterval(int countDownInterval) {
        return setCountDownInterval(cpvTotalTime, CircleProgressTime.MILLI_SECOND);
    }
    
    @Override
    public CircleProgressView setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime) {
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
    public CircleProgressView setStartAngle(float cpvStartAngle) {
        this.cpvStartAngle = cpvStartAngle;
        circleProgress.updateStart(this.cpvStartAngle);
        isSetStartAngle = true;
        return this;
    }
    
    @Override
    public CircleProgressView setSweepAngle(float cpvSweepAngle) {
        this.cpvSweepAngle = cpvSweepAngle;
        circleProgress.updateSweep(this.cpvSweepAngle);
        isSetSweepAngle = true;
        return this;
    }
    
    @Override
    public CircleProgressView cpvStart() {
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
    public CircleProgressView cpvPause() {
        if (isRun) {
            isRun = false;
            countDownTimer.cancel();
        }
        return this;
    }
    
    @Override
    public CircleProgressView cpvResume() {
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
    public void setOnPlayListener(CircleProgressView.OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }
    
    public interface OnPlayListener {
        void onPlay(int momentTime);
        
        void onFinish();
    }
}
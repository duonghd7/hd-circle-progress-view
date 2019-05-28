package com.hdd.circleprogressview.v1;

import com.hdd.circleprogressview.utils.CircleProgressTime;

/**
 * Created on 1/24/2018.
 *
 * @author duonghd
 */

interface CircleProgressInterface1 {
    CircleProgressView1 setTotalTime(int cpvTotalTime);

    CircleProgressView1 setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime);

    CircleProgressView1 setCountDownInterval(int countDownInterval);

    CircleProgressView1 setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime);

    CircleProgressView1 setStartAngle(float cpvStartAngle);

    CircleProgressView1 setSweepAngle(float cpvSweepAngle);

    CircleProgressView1 cpvStart();

    CircleProgressView1 cpvPause();

    CircleProgressView1 cpvResume();

    void setOnPlayListener(CircleProgressView1.OnPlayListener onPlayListener);
}

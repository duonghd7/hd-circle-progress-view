package com.hdd.circleprogressview;

/**
 * Created on 1/24/2018.
 *
 * @author duonghd
 */

interface CircleProgressInterface {
    CircleProgressView setTotalTime(int cpvTotalTime);

    CircleProgressView setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime);

    CircleProgressView setCountDownInterval(int countDownInterval);

    CircleProgressView setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime);

    CircleProgressView setStartAngle(float cpvStartAngle);

    CircleProgressView setSweepAngle(float cpvSweepAngle);

    CircleProgressView cpvStart();

    CircleProgressView cpvPause();

    CircleProgressView cpvResume();

    void setOnPlayListener(CircleProgressView.OnPlayListener onPlayListener);
}

package com.hdd.circleprogressview.v2;

import com.hdd.circleprogressview.utils.CircleProgressTime;

/**
 * Create on 2019-05-27
 *
 * @author duonghd
 */
public interface CircleProgressInterface2 {
    CircleProgressView2 setTotalTime(int cpvTotalTime);

    CircleProgressView2 setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime);

    CircleProgressView2 setCountDownInterval(int countDownInterval);

    CircleProgressView2 setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime);

    CircleProgressView2 setStartTime(int cpvStartTime);

    CircleProgressView2 setStartTime(int cpvStartTime, CircleProgressTime circleProgressTime);

    CircleProgressView2 setMode(CircleProgressView2.CircleProgressMode circleProgressMode);

    CircleProgressView2 cpvStart();

    CircleProgressView2 cpvPause();

    CircleProgressView2 cpvResume();

    void setOnPlayListener(CircleProgressView2.OnPlayListener onPlayListener);
}

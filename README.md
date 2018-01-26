# Android simple CircleProgressView
[![](https://jitpack.io/v/duonghd7/hd-circle-progress-view.svg)](https://jitpack.io/#duonghd7/hd-circle-progress-view)<br>

## Introduction
**`CircleProgressView`** is support for countdown process or show up process to view with util time through function **`OnPlayListener()`**

## GIF
![giphy1](https://user-images.githubusercontent.com/18477507/35432782-bc12913c-02b3-11e8-9b4c-bbd8d1fdb2b6.gif) ![giphy2](https://user-images.githubusercontent.com/18477507/35432808-d3a5411e-02b3-11e8-929d-cc9ad49ba3e3.gif)

## Options
<kbd>![image](https://user-images.githubusercontent.com/18477507/35434900-5bf65a56-02bb-11e8-95a0-cdcf3310fb49.png)</kbd>
![image](https://user-images.githubusercontent.com/18477507/35433473-1f140c6e-02b6-11e8-9897-75cd09a905d1.png)<br>
**`.setTotalTime(int cpvTotalTime)`** <br>
**`.setTotalTime(int cpvTotalTime, CircleProgressTime circleProgressTime)`** <br>

You can set `total time` for process with default value is `30 second`.

**`.setCountDownInterval(int countDownInterval)`** <br>
**`.setCountDownInterval(int countDownInterval, CircleProgressTime circleProgressTime)`** <br>

You can set `countdown interval` for process with default value and minimum of value is `20 millisecond`.

**`.setStartAngle(float cpvStartAngle)`** <br>

You can set `start angle` before process is run with default value is `-90f`.

**`.setSweepAngle(float cpvSweepAngle)`** <br>

You can set `sweep angle` before process is run with default value is `0f`.

## Functions
**`.cpvStart()`** <br>

Process run and update each countdown interval.

**`.cpvPause()`** <br>

Pause process

**`.cpvResume()`** <br>

Resume process

## OnPlayListener
**`.setOnPlayListener(CircleProgressView.OnPlayListener onPlayListener)`** <br>

You will receive execution time after each countdown interval.

```java
circleProgress.setTotalTime(10, CircleProgressTime.SECOND)
                .setCountDownInterval(20, CircleProgressTime.MILLI_SECOND)
                .setOnPlayListener(new CircleProgressView.OnPlayListener() {
                    @Override
                    public void onPlay(int momentTime) {
                        int min = momentTime / 60000;
                        int second = (momentTime / 1000) % 60;
                        int milliSecond = (momentTime % 1000) / 10;
                        String time = String.format("%s:%s:%s"
                                , min < 10 ? String.format("0%s", min) : min
                                , second < 10 ? String.format("0%s", second) : second
                                , milliSecond < 10 ? String.format("0%s", milliSecond) : milliSecond);
                        tvMomentTime.setText(time);
                    }
                    
                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    }
                });
```

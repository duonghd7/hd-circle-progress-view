# Android simple CircleProgressView
[![](https://jitpack.io/v/duonghd7/hd-circle-progress-view.svg)](https://jitpack.io/#duonghd7/hd-circle-progress-view)<br>

## Introduction
**`CircleProgressView`** is support for countdown process or show up process to view with util time through function **`OnPlayListener()`**

## GIF
![1](https://user-images.githubusercontent.com/18477507/36660672-94db9d82-1b0b-11e8-8710-56f863d69944.gif) ![2](https://user-images.githubusercontent.com/18477507/36660712-b6a612e4-1b0b-11e8-9f6f-049f0bfb7a75.gif) ![3](https://user-images.githubusercontent.com/18477507/36660734-c8234f00-1b0b-11e8-841e-635bbd3519ed.gif)

## Installation
- Step 1. Add the JitPack repository to your build file <br>
`Add it in your root build.gradle at the end of repositories:`
```java
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

- Step 2. Add the dependency with version `0.0.4`
```java
    dependencies {
          ...
          compile 'com.github.duonghd7:hd-circle-progress-view:0.0.4'
    }
```

## Options
![image](https://user-images.githubusercontent.com/18477507/36661723-b2b7c67a-1b0e-11e8-80f7-b234a278b80b.png)
<kbd>![image](https://user-images.githubusercontent.com/18477507/36661238-5d9e13de-1b0d-11e8-9a60-2767ab842bff.png)</kbd>
![image](https://user-images.githubusercontent.com/18477507/36662428-fa710588-1b10-11e8-8616-c2e5ac094015.png)<br><br>
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

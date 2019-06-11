# Android CircleProgressView
[![](https://jitpack.io/v/duonghd7/hd-circle-progress-view.svg)](https://jitpack.io/#duonghd7/hd-circle-progress-view)<br>

## Introduction
**`CircleProgressView`** is support for countdown process or show up process to view with util time through function **`OnPlayListener()`**

## GIF
- CircleProgressView1<br>
![1](https://user-images.githubusercontent.com/18477507/36660672-94db9d82-1b0b-11e8-8710-56f863d69944.gif) ![2](https://user-images.githubusercontent.com/18477507/36660712-b6a612e4-1b0b-11e8-9f6f-049f0bfb7a75.gif) ![3](https://user-images.githubusercontent.com/18477507/36660734-c8234f00-1b0b-11e8-841e-635bbd3519ed.gif)

- CircleProgressView2<br>
![2](https://user-images.githubusercontent.com/18477507/59265029-b3106c80-8c6e-11e9-97dd-e3568fdc3d15.gif)

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

- Step 2. Add the dependency with lastest version [![](https://jitpack.io/v/duonghd7/hd-circle-progress-view.svg)](https://jitpack.io/#duonghd7/hd-circle-progress-view)<br>
```java
    dependencies {
          ...
          implementation 'com.github.duonghd7:hd-circle-progress-view:0.6.3'
    }
```

## Options CircleProgressView1
![image](https://user-images.githubusercontent.com/18477507/36661723-b2b7c67a-1b0e-11e8-80f7-b234a278b80b.png)
<kbd><img width="444" alt="Screen Shot 2019-06-11 at 5 50 26 PM" src="https://user-images.githubusercontent.com/18477507/59266297-84e05c00-8c71-11e9-93ed-e498478cc217.png"></kbd>
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

## Options CircleProgressView2
![all](https://user-images.githubusercontent.com/18477507/59277515-8702e480-8c8a-11e9-9957-638cfd743af9.png)
<kbd><img width="423" alt="Screen Shot 2019-06-11 at 5 51 04 PM" src="https://user-images.githubusercontent.com/18477507/59266530-059f5800-8c72-11e9-9679-6d966e6cb2aa.png"></kbd>
![review](https://user-images.githubusercontent.com/18477507/59277579-a863d080-8c8a-11e9-8b1d-bbe37eb37da0.png)<br><br>
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
                .setOnPlayListener(new CircleProgressView1.OnPlayListener() {
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
                
circleProgress.cpvStart();
```

```java
circleProgress.setTotalTime(10, CircleProgressTime.SECOND)
                .setCountDownInterval(20, CircleProgressTime.MILLI_SECOND)
                .setMode(CircleProgressView2.CircleProgressMode.STROKE_BUTT_2)
                .setOnPlayListener(new CircleProgressView2.OnPlayListener() {
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
                        circleProgress.setTotalTime(10, CircleProgressTime.SECOND)
                                .setCountDownInterval(20, CircleProgressTime.MILLI_SECOND)
                                .setMode(i % 2 == 0 ? CircleProgressView2.CircleProgressMode.STROKE_BUTT_1 : CircleProgressView2.CircleProgressMode.STROKE_BUTT_2);
                        i++;
                        circleProgress.cpvStart();
                    }
                });
                
circleProgress.cpvStart();
```
    
# MIT License

Copyright (c) 2019 Hà Đại Dương

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

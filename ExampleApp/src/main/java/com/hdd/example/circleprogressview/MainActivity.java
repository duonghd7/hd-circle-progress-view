package com.hdd.example.circleprogressview;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hdd.circleprogressview.utils.CircleProgressTime;
import com.hdd.circleprogressview.v2.CircleProgressView2;

public class MainActivity extends AppCompatActivity {
    private CircleProgressView2 circleProgress;
    private TextView tvMomentTime;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleProgress = findViewById(R.id.activity_main_cpv_circle_progress);
        tvMomentTime = findViewById(R.id.activity_main_tv_moment_time);

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

        findViewById(R.id.activity_main_tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgress.cpvStart();
            }
        });

        findViewById(R.id.activity_main_tv_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgress.cpvPause();
            }
        });

        findViewById(R.id.activity_main_tv_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgress.cpvResume();
            }
        });
    }
}
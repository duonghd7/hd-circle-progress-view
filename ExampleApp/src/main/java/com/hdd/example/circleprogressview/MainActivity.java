package com.hdd.example.circleprogressview;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hdd.circleprogressview.CircleProgressTime;
import com.hdd.circleprogressview.CircleProgressView;

public class MainActivity extends AppCompatActivity {
    private CircleProgressView circleProgress;
    private TextView tvMomentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleProgress = findViewById(R.id.activity_main_cpv_circle_progress);
        TextView tvStart = findViewById(R.id.activity_main_tv_start);
        TextView tvPause = findViewById(R.id.activity_main_tv_pause);
        TextView tvResume = findViewById(R.id.activity_main_tv_resume);
        tvMomentTime = findViewById(R.id.activity_main_tv_moment_time);

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

        tvStart.setOnClickListener(v -> circleProgress.cpvStart());
        tvPause.setOnClickListener(v -> circleProgress.cpvPause());
        tvResume.setOnClickListener(v -> circleProgress.cpvResume());
    }
}
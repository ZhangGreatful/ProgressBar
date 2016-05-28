package com.example.administrator.progressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.progressbar.com.progressBar.view.HorizontalProgressBarWithProgress;
import com.example.administrator.progressbar.com.progressBar.view.RoundProgressBarWithProgress;

public class MainActivity extends AppCompatActivity {
    private HorizontalProgressBarWithProgress mHProgress;
    private RoundProgressBarWithProgress mRProgress;
    private static final int MSG_UPDATE = 0X123;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mHProgress.getProgress();
            mHProgress.setProgress(++progress);
            mRProgress.setProgress(++progress);
            if (progress >= 100) {
                handler.removeMessages(MSG_UPDATE);
            }
            handler.sendEmptyMessageDelayed(MSG_UPDATE, 100);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHProgress = (HorizontalProgressBarWithProgress) findViewById(R.id.id_progress_03);
        mRProgress= (RoundProgressBarWithProgress) findViewById(R.id.id_progress04);
        handler.sendEmptyMessage(MSG_UPDATE);
    }
}

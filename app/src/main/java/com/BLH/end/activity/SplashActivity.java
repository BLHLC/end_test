package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.BLH.end.MainActivity;
import com.BLH.end.R;

public class SplashActivity extends AppCompatActivity {

    private TextView tvSkip;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvSkip = findViewById(R.id.tv_skip);

        // 核心逻辑：使用 CountDownTimer 实现 3 秒倒计时
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新按钮上的文字：跳过 3s -> 2s -> 1s
                tvSkip.setText("跳过 " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                // 倒计时结束，自动跳转
                goToMain();
            }
        }.start();

        // 点击跳过按钮，立刻取消倒计时并跳转
        tvSkip.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // 取消倒计时
            }
            goToMain();
        });
    }

    // 提取跳转方法，防止重复写代码
    private void goToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // 销毁启动页
    }

    // 页面销毁时一定要取消倒计时，防止内存泄漏和崩溃
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
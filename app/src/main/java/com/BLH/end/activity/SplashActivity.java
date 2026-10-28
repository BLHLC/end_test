package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.BLH.end.MainActivity;
import com.BLH.end.R;
import com.BLH.end.utils.UIUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UIUtils.setStatusBarPadding(this);
        // 隐藏顶部标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 延迟 2000 毫秒（2秒）后跳转到主界面
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 跳转到 MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                // 结束当前启动页，这样用户按返回键就不会退回到启动页了
                finish();
            }
        }, 2000);
    }
}
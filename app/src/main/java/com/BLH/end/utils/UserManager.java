package com.BLH.end.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private SharedPreferences sp;

    public UserManager(Context context) {
        // 文件名为 "user_info"
        sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    // 保存当前登录的手机号
    public void saveLoginPhone(String phone) {
        sp.edit().putString("login_phone", phone).apply();
    }

    // 获取当前登录的手机号（如果没有登录，默认返回一个游客号码）
    public String getLoginPhone() {
        return sp.getString("login_phone", "13800000000");
    }
}
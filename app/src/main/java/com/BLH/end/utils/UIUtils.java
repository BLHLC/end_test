package com.BLH.end.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class UIUtils {
    // 自动为 Activity 的顶部避开状态栏（刘海屏）
    public static void setStatusBarPadding(Activity activity) {
        // 获取状态栏高度
        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }

        // 获取 Activity 的根布局
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

        if (rootView instanceof ViewGroup) {
            ViewGroup rootGroup = (ViewGroup) rootView;
            if (rootGroup.getChildCount() > 0) {
                // 获取我们写的 XML 最外层布局
                View contentView = rootGroup.getChildAt(0);
                // 给这个布局加上状态栏高度的 padding，同时保留原有的 padding
                contentView.setPadding(
                        contentView.getPaddingLeft(),
                        contentView.getPaddingTop() + statusBarHeight,
                        contentView.getPaddingRight(),
                        contentView.getPaddingBottom()
                );
            }
        }
    }
}
package com.BLH.end.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.BLH.end.R;
import com.BLH.end.activity.OrderHistoryActivity;
import com.BLH.end.utils.UserManager;

public class UserFragment extends Fragment {

    public UserFragment() {
        super(R.layout.fragment_user);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // 显示当前手机号
        TextView tvPhone = view.findViewById(R.id.tv_user_phone);
        UserManager userManager = new UserManager(getContext());
        tvPhone.setText(userManager.getLoginPhone());

        // 跳转到我的订单
        view.findViewById(R.id.layout_my_orders).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
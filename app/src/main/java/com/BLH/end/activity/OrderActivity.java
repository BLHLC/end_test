package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.MainActivity;
import com.BLH.end.R;
import com.BLH.end.adapter.OrderAdapter;
import com.BLH.end.bean.CartBean;
import com.BLH.end.db.OrderDao;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private List<CartBean> cartList;
    private double totalPrice;
    private String shopName;
    private int shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // 1. 接收店铺详情页传过来的数据
        cartList = (List<CartBean>) getIntent().getSerializableExtra("cartList");
        shopId = getIntent().getIntExtra("shopId", -1);
        shopName = getIntent().getStringExtra("shopName");

        if (cartList == null || cartList.isEmpty()) {
            finish();
            return;
        }

        // 计算总价
        totalPrice = 0;
        for (CartBean bean : cartList) {
            totalPrice += bean.getPrice() * bean.getCount();
        }

        // 2. 初始化视图
        TextView tvTotal = findViewById(R.id.tv_order_total);
        tvTotal.setText("合计：¥" + totalPrice);

        EditText etAddress = findViewById(R.id.et_address);
        EditText etPhone = findViewById(R.id.et_phone);
        TextView btnPay = findViewById(R.id.btn_pay);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new OrderAdapter(this, cartList));

        // 3. 点击去支付
        btnPay.setOnClickListener(v -> {
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (address.isEmpty()) {
                Toast.makeText(this, "请填写收货地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.isEmpty()) {
                Toast.makeText(this, "请填写联系电话", Toast.LENGTH_SHORT).show();
                return;
            }

            // 弹出支付二维码
            showPayDialog(address, phone);
        });
    } // <--- 注意这里，onCreate 方法在这里结束

    // 提取出的支付弹窗方法
    private void showPayDialog(String address, String phone) {
        // 1. 加载支付弹窗布局
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pay, null);

        // 2. 显示总金额
        TextView tvAmount = dialogView.findViewById(R.id.tv_pay_amount);
        tvAmount.setText("￥" + totalPrice);

        // 3. 创建并显示 Dialog
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        // 设置背景透明，使用我们自己画的白色圆角背景
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        // 4. 点击“模拟支付成功”按钮
        TextView btnSuccess = dialogView.findViewById(R.id.btn_pay_success);
        btnSuccess.setOnClickListener(v -> {
            // 4.1 真正将订单存入数据库
            new OrderDao(this).submitOrder(shopId, shopName, cartList, address, phone);

            Toast.makeText(this, "支付成功，下单完成！", Toast.LENGTH_LONG).show();

            // 4.2 关闭弹窗
            dialog.dismiss();

            // 4.3 跳转回首页并清空页面栈
            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

}
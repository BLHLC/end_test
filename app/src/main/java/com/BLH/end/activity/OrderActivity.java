package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
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

            // 存入数据库
            new OrderDao(this).submitOrder(shopId, shopName, cartList, address, phone);

            Toast.makeText(this, "下单成功！", Toast.LENGTH_LONG).show();

            // 核心修复：直接跳转回 MainActivity，并清空中间所有页面
            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // 关闭订单页
        });
    }
}
package com.BLH.end.activity;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.adapter.OrderHistoryAdapter;
import com.BLH.end.db.OrderDao;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OrderDao orderDao = new OrderDao(this);
        List<String> orders = orderDao.getAllOrders();

        // 注意：这里我们使用了一个带点击回调的 Adapter（需要在 Adapter 里加接口）
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(this, orders, orderInfo -> {
            // 解析出订单号 (我们的 getAllOrders 里第一行是 "订单号：xxxx")
            String orderNo = orderInfo.split("\n")[0].replace("订单号：", "").trim();

            // 查出该订单的所有商品
            List<String> items = orderDao.getOrderItemsByOrderNo(orderNo);

            // 拼接展示内容
            StringBuilder sb = new StringBuilder();
            if (items.isEmpty()) {
                sb.append("该订单暂无商品明细");
            } else {
                for (String item : items) {
                    sb.append(item).append("\n\n");
                }
            }

            // 弹出详情对话框
            new AlertDialog.Builder(this)
                    .setTitle("订单明细")
                    .setMessage(sb.toString())
                    .setPositiveButton("关闭", null)
                    .show();
        });

        recyclerView.setAdapter(adapter);
    }
}
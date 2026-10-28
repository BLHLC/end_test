package com.BLH.end.activity;

import android.os.Bundle;
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

        // 从数据库查出所有订单
        List<String> orders = new OrderDao(this).getAllOrders();
        recyclerView.setAdapter(new OrderHistoryAdapter(this, orders));
    }
}
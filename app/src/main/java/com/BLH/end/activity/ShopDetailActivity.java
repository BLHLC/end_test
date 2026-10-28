package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.adapter.MenuAdapter;
import com.BLH.end.bean.CartBean;
import com.BLH.end.bean.ShopBean;
import com.BLH.end.db.CartDao;
import java.io.Serializable;
import java.util.List;

public class ShopDetailActivity extends AppCompatActivity implements MenuAdapter.OnCartChangeListener {

    private ShopBean shopBean;
    private CartDao cartDao;
    private TextView tvTotal;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        shopBean = (ShopBean) getIntent().getSerializableExtra("shop");
        if (shopBean == null) {
            finish();
            return;
        }

        cartDao = new CartDao(this);

        TextView tvName = findViewById(R.id.tv_detail_shop_name);
        TextView tvAnnounce = findViewById(R.id.tv_detail_announcement);
        tvTotal = findViewById(R.id.tv_cart_total);
        TextView btnCheckout = findViewById(R.id.btn_checkout);

        tvName.setText(shopBean.getShopName());
        tvAnnounce.setText("公告：" + shopBean.getAnnouncement());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 注意这里，多传了 shopBean.getShopName()
        adapter = new MenuAdapter(this, shopBean.getFoods(), shopBean.getShopId(), shopBean.getShopName(), this);
        recyclerView.setAdapter(adapter);

        updateCartTotal();

        btnCheckout.setOnClickListener(v -> {
            List<CartBean> cartList = cartDao.getCartListByShop(shopBean.getShopId());
            if (cartList.isEmpty()) {
                Toast.makeText(this, "购物车是空的哦~", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            updateCartTotal();
        }
    }

    private void updateCartTotal() {
        List<CartBean> cartList = cartDao.getCartListByShop(shopBean.getShopId());
        double total = 0;
        for (CartBean bean : cartList) {
            total += bean.getPrice() * bean.getCount();
        }
        tvTotal.setText("¥" + total);
    }

    @Override
    public void onCartChanged() {
        updateCartTotal();
    }
}
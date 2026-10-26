package com.BLH.end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import java.util.List;

public class ShopDetailActivity extends AppCompatActivity implements MenuAdapter.OnCartChangeListener {

    private ShopBean shopBean;
    private CartDao cartDao;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        // 1. 接收首页传来的 ShopBean
        shopBean = (ShopBean) getIntent().getSerializableExtra("shop");
        if (shopBean == null) {
            finish();
            return;
        }

        cartDao = new CartDao(this);

        // 2. 初始化视图
        TextView tvName = findViewById(R.id.tv_detail_shop_name);
        TextView tvAnnounce = findViewById(R.id.tv_detail_announcement);
        tvTotal = findViewById(R.id.tv_cart_total);
        Button btnCheckout = findViewById(R.id.btn_checkout);

        tvName.setText(shopBean.getShopName());
        tvAnnounce.setText("公告：" + shopBean.getAnnouncement());

        // 3. 设置商品列表
        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MenuAdapter adapter = new MenuAdapter(this, shopBean.getFoods(), shopBean.getShopId(), this);
        recyclerView.setAdapter(adapter);

        // 4. 初始化总价
        updateCartTotal();

        // 5. 去结算点击事件 (暂时只做提示，Step 4 详细写)
        btnCheckout.setOnClickListener(v -> {
            List<CartBean> cartList = cartDao.getCartListByShop(shopBean.getShopId());
            if (cartList.isEmpty()) {
                Toast.makeText(this, "购物车是空的哦", Toast.LENGTH_SHORT).show();
            } else {
                // 去订单页面 (将在 Step 4 实现)
                Toast.makeText(this, "即将跳转订单页", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 更新底部总价
    private void updateCartTotal() {
        List<CartBean> cartList = cartDao.getCartListByShop(shopBean.getShopId());
        double total = 0;
        for (CartBean bean : cartList) {
            total += bean.getPrice() * bean.getCount();
        }
        tvTotal.setText("¥" + total);
    }

    // 实现接口，当点击加减号时，回调刷新总价
    @Override
    public void onCartChanged() {
        updateCartTotal();
    }
}
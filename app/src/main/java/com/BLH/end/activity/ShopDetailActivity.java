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
    private MenuAdapter adapter; // 提升为全局变量，方便在 onResume 中刷新

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
        TextView btnCheckout = findViewById(R.id.btn_checkout); // 这里最好用 TextView，因为你 XML 里可能是 TextView

        tvName.setText(shopBean.getShopName());
        tvAnnounce.setText("公告：" + shopBean.getAnnouncement());

        // 3. 设置商品列表
        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(this, shopBean.getFoods(), shopBean.getShopId(), this);
        recyclerView.setAdapter(adapter);

        // 4. 初始化总价
        updateCartTotal();

        // 5. 去结算点击事件
        btnCheckout.setOnClickListener(v -> {
            List<CartBean> cartList = cartDao.getCartListByShop(shopBean.getShopId());
            if (cartList.isEmpty()) {
                Toast.makeText(this, "购物车是空的哦", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ShopDetailActivity.this, OrderActivity.class);
                intent.putExtra("cartList", (Serializable) cartList);
                intent.putExtra("shopId", shopBean.getShopId());
                intent.putExtra("shopName", shopBean.getShopName());
                startActivity(intent);
            }
        });
    }

    // 核心修复：每次页面回到前台（比如从订单页退回来），重新刷新界面
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // 刷新列表里的加减号
            updateCartTotal(); // 刷新底部总价
        }
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

    @Override
    public void onCartChanged() {
        updateCartTotal();
    }
}
package com.BLH.end.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.activity.OrderActivity;
import com.BLH.end.adapter.CartDialogAdapter;
import com.BLH.end.adapter.ShopAdapter;
import com.BLH.end.bean.CartBean;
import com.BLH.end.bean.ShopBean;
import com.BLH.end.db.CartDao;
import com.BLH.end.utils.JsonParse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.Serializable;
import java.util.List;

public class HomeFragment extends Fragment {

    private CartDao cartDao;
    private TextView tvTotal;
    private List<CartBean> currentCartList;
    private BottomSheetDialog bottomSheetDialog;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cartDao = new CartDao(getContext());
        tvTotal = view.findViewById(R.id.tv_float_total);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_shop);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ShopBean> shopList = JsonParse.getShopList(getContext());
        recyclerView.setAdapter(new ShopAdapter(getContext(), shopList));

        updateCartBar();

        // 点击底部悬浮栏弹出购物车抽屉
        view.findViewById(R.id.layout_float_cart).setOnClickListener(v -> {
            if (currentCartList != null && !currentCartList.isEmpty()) {
                showCartBottomSheet();
            } else {
                Toast.makeText(getContext(), "购物车空空的，快去挑点好吃的吧~", Toast.LENGTH_SHORT).show();
            }
        });

        // 底部去结算按钮
        view.findViewById(R.id.btn_float_checkout).setOnClickListener(v -> {
            if (currentCartList == null || currentCartList.isEmpty()) {
                Toast.makeText(getContext(), "购物车是空的哦", Toast.LENGTH_SHORT).show();
                return;
            }

            // 如果包含多家店铺，引导用户进入【全部订单结算页】
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            // 直接传整个购物车列表过去，让订单页自己去拆分
            intent.putExtra("cartList", (Serializable) currentCartList);
            // 标记为从全局购物车进入
            intent.putExtra("isGlobal", true);
            startActivity(intent);
        });

        return view;
    }

    // 核心：展示底部购物车抽屉
    private void showCartBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_cart_sheet, null);
        bottomSheetDialog.setContentView(sheetView);

        RecyclerView rv = sheetView.findViewById(R.id.recycler_view_cart_dialog);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // 使用匿名内部类，避免方法引用报错
        CartDialogAdapter.OnCartChangedListener listener = new CartDialogAdapter.OnCartChangedListener() {
            @Override
            public void onCartChanged() {
                updateCartBar(); // 更新首页底部总价
                currentCartList = cartDao.getAllCartList();
                if (currentCartList.isEmpty()) {
                    bottomSheetDialog.dismiss();
                } else {
                    rv.setAdapter(new CartDialogAdapter(getContext(), currentCartList, this));
                }
            }
        };

        rv.setAdapter(new CartDialogAdapter(getContext(), currentCartList, listener));

        // 添加“侧滑删除”功能
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartBean bean = currentCartList.get(position);
                cartDao.deleteItem(bean.getShopId(), bean.getFoodId());

                // 删除后刷新
                updateCartBar();
                currentCartList = cartDao.getAllCartList();
                if (currentCartList.isEmpty()) {
                    bottomSheetDialog.dismiss();
                } else {
                    rv.setAdapter(new CartDialogAdapter(getContext(), currentCartList, listener));
                }
                Toast.makeText(getContext(), "已删除该商品", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rv);

        // 清空购物车
        sheetView.findViewById(R.id.tv_clear_cart).setOnClickListener(v -> {
            cartDao.clearAllCart();
            updateCartBar();
            bottomSheetDialog.dismiss();
            Toast.makeText(getContext(), "购物车已清空", Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartBar();
    }

    private void updateCartBar() {
        currentCartList = cartDao.getAllCartList();
        double total = 0;
        int count = 0;
        for (CartBean bean : currentCartList) {
            total += bean.getPrice() * bean.getCount();
            count += bean.getCount();
        }
        if (count > 0) {
            tvTotal.setText("共 " + count + " 件商品 | 合计：¥" + total);
        } else {
            tvTotal.setText("购物车是空的");
        }
    }
}
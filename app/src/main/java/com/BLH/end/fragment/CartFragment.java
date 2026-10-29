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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.activity.OrderActivity;
import com.BLH.end.adapter.CartTabAdapter;
import com.BLH.end.bean.CartBean;
import com.BLH.end.db.CartDao;
import java.io.Serializable;
import java.util.List;

public class CartFragment extends Fragment {
    private CartDao cartDao;
    private RecyclerView recyclerView;
    private TextView tvTotal;
    private List<CartBean> currentCartList;

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartDao = new CartDao(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_cart);
        tvTotal = view.findViewById(R.id.tv_cart_tab_total);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.btn_cart_tab_checkout).setOnClickListener(v -> {
            if (currentCartList == null || currentCartList.isEmpty()) {
                Toast.makeText(getContext(), "购物车空空如也", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            intent.putExtra("cartList", (Serializable) currentCartList);
            intent.putExtra("isGlobal", true);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCart();
    }

    private void refreshCart() {
        currentCartList = cartDao.getAllCartList();
        CartTabAdapter adapter = new CartTabAdapter(getContext(), currentCartList, this::refreshCart);
        recyclerView.setAdapter(adapter);

        double total = 0;
        for (CartBean bean : currentCartList) {
            total += bean.getPrice() * bean.getCount();
        }
        tvTotal.setText("合计：¥" + total);
    }
}
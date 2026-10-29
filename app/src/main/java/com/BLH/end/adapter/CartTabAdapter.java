package com.BLH.end.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.bean.CartBean;
import com.BLH.end.db.CartDao;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CartTabAdapter extends RecyclerView.Adapter<CartTabAdapter.ViewHolder> {
    private Context context;
    private List<CartBean> cartList;
    private CartDao cartDao;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartTabAdapter(Context context, List<CartBean> cartList, OnCartChangedListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.cartDao = new CartDao(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartBean bean = cartList.get(position);
        holder.tvName.setText(bean.getFoodName());
        holder.tvPrice.setText("¥" + bean.getPrice());
        holder.tvCount.setText(String.valueOf(bean.getCount()));

        String imgPath = bean.getImage().replace("file:///android_asset/", "file://");
        Picasso.get().load(imgPath).into(holder.ivImg);

        holder.btnAdd.setOnClickListener(v -> {
            // 1. 手动创建一个 FoodBean 并赋值
            com.BLH.end.bean.FoodBean food = new com.BLH.end.bean.FoodBean();
            food.setFoodId(bean.getFoodId());
            food.setFoodName(bean.getFoodName());
            food.setPrice(bean.getPrice());
            food.setImage(bean.getImage());

            // 2. 传给 addToCart
            cartDao.addToCart(bean.getShopId(), bean.getShopName(), food);

            // 3. 通知刷新
            if (listener != null) listener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            cartDao.minusCart(bean.getShopId(), bean.getFoodId());
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartList == null ? 0 : cartList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvName, tvPrice, tvCount, btnAdd, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_cart_img);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvCount = itemView.findViewById(R.id.tv_cart_count);
            btnAdd = itemView.findViewById(R.id.btn_cart_add);
            btnMinus = itemView.findViewById(R.id.btn_cart_minus);
        }
    }
}
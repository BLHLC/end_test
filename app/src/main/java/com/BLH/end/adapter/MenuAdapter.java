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
import com.BLH.end.bean.FoodBean;
import com.BLH.end.db.CartDao;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context context;
    private List<FoodBean> foodList;
    private int shopId;
    private String shopName; // 新增：用来保存店铺名字
    private CartDao cartDao;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    // 构造函数增加 shopName 参数
    public MenuAdapter(Context context, List<FoodBean> foodList, int shopId, String shopName, OnCartChangeListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.shopId = shopId;
        this.shopName = shopName;
        this.cartDao = new CartDao(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodBean food = foodList.get(position);
        holder.tvName.setText(food.getFoodName());
        holder.tvDesc.setText(food.getDescription());
        holder.tvPrice.setText("¥" + food.getPrice());
        Picasso.get().load(food.getImage()).into(holder.ivImg);

        int currentCount = 0;
        for (CartBean cb : cartDao.getCartListByShop(shopId)) {
            if (cb.getFoodId() == food.getFoodId()) {
                currentCount = cb.getCount();
                break;
            }
        }

        if (currentCount > 0) {
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.btnMinus.setVisibility(View.VISIBLE);
            holder.tvCount.setText(String.valueOf(currentCount));
        } else {
            holder.tvCount.setVisibility(View.GONE);
            holder.btnMinus.setVisibility(View.GONE);
        }

        // 点击加号
        holder.btnAdd.setOnClickListener(v -> {
            // 这里传入 this.shopName
            cartDao.addToCart(shopId, shopName, food);
            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });

        // 点击减号
        holder.btnMinus.setOnClickListener(v -> {
            cartDao.minusCart(shopId, food.getFoodId());
            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvName, tvDesc, tvPrice, tvCount;
        TextView btnAdd, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_food_img);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvDesc = itemView.findViewById(R.id.tv_food_desc);
            tvPrice = itemView.findViewById(R.id.tv_food_price);
            tvCount = itemView.findViewById(R.id.tv_count);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}
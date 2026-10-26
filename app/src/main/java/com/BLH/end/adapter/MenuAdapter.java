package com.BLH.end.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private CartDao cartDao;
    private OnCartChangeListener listener;

    // 定义接口，用于通知 Activity 更新底部的总价
    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public MenuAdapter(Context context, List<FoodBean> foodList, int shopId, OnCartChangeListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.shopId = shopId;
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

        // 实时查询当前商品在数据库里的数量
        int currentCount = 0;
        for (CartBean cb : cartDao.getCartListByShop(shopId)) {
            if (cb.getFoodId() == food.getFoodId()) {
                currentCount = cb.getCount();
                break;
            }
        }

        // 控制加号、减号、数量的显示隐藏
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
            cartDao.addToCart(shopId, food);
            notifyItemChanged(position); // 刷新当前项
            if (listener != null) listener.onCartChanged(); // 通知外层更新总价
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
        TextView  btnAdd, btnMinus;

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
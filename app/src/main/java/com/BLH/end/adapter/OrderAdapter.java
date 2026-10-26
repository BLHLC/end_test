package com.BLH.end.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.BLH.end.bean.CartBean;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<CartBean> cartList;

    public OrderAdapter(Context context, List<CartBean> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartBean bean = cartList.get(position);
        holder.tvName.setText(bean.getFoodName());
        holder.tvCount.setText("x" + bean.getCount());
        // 计算小计
        double subtotal = bean.getPrice() * bean.getCount();
        holder.tvSubtotal.setText("¥" + subtotal);
    }

    @Override
    public int getItemCount() {
        return cartList == null ? 0 : cartList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount, tvSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_order_food_name);
            tvCount = itemView.findViewById(R.id.tv_order_food_count);
            tvSubtotal = itemView.findViewById(R.id.tv_order_food_subtotal);
        }
    }
}
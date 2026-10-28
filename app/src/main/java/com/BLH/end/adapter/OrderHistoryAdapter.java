package com.BLH.end.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private Context context;
    private List<String> orderList;
    private OnItemClickListener listener;

    // 定义点击接口
    public interface OnItemClickListener {
        void onItemClick(String orderInfo);
    }

    public OrderHistoryAdapter(Context context, List<String> orderList, OnItemClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String orderInfo = orderList.get(position);
        holder.tvInfo.setText(orderInfo);

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(orderInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tv_history_info);
        }
    }
}
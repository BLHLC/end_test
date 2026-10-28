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
import com.BLH.end.bean.AddressBean;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context context;
    private List<AddressBean> list;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDelete(AddressBean bean);
        void onEdit(AddressBean bean); // 新增修改回调
    }


    public AddressAdapter(Context context, List<AddressBean> list, OnDeleteClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressBean bean = list.get(position);
        // 改为这种带前缀的格式
        holder.tvNamePhone.setText("姓名：" + bean.getName() + "  电话：" + bean.getPhone());
        holder.tvDetail.setText("地址：" + bean.getDetail());

        // 点击删除
        holder.ivDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(bean);
        });

        // 新增：点击整个卡片进行修改
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(bean);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePhone, tvDetail;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePhone = itemView.findViewById(R.id.tv_address_name_phone);
            tvDetail = itemView.findViewById(R.id.tv_address_detail);
            ivDelete = itemView.findViewById(R.id.iv_delete_address);
        }
    }
}
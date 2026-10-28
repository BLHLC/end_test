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

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.ViewHolder> {
    private Context context;
    private List<AddressBean> list;
    private OnActionListener listener;

    public interface OnActionListener {
        void onSelect(AddressBean bean);
        void onEdit(AddressBean bean);
        void onDelete(AddressBean bean);
    }

    public AddressSelectAdapter(Context context, List<AddressBean> list, OnActionListener listener) {
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
        holder.tvNamePhone.setText("姓名：" + bean.getName() + "  电话：" + bean.getPhone());
        holder.tvDetail.setText("地址：" + bean.getDetail());

        holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onSelect(bean); });
        holder.ivEdit.setOnClickListener(v -> { if (listener != null) listener.onEdit(bean); });
        holder.ivDelete.setOnClickListener(v -> { if (listener != null) listener.onDelete(bean); });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePhone, tvDetail;
        ImageView ivEdit, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePhone = itemView.findViewById(R.id.tv_address_name_phone);
            tvDetail = itemView.findViewById(R.id.tv_address_detail);
            ivEdit = itemView.findViewById(R.id.iv_edit_address);
            ivDelete = itemView.findViewById(R.id.iv_delete_address);
        }
    }
}
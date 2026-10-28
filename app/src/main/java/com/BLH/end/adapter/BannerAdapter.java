package com.BLH.end.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.BLH.end.R;
import com.squareup.picasso.Picasso;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private Context context;
    private String[] imageUrls;
    private OnBannerClickListener listener;

    // 定义点击接口
    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }

    public BannerAdapter(Context context, String[] imageUrls, OnBannerClickListener listener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(imageUrls[position]).into(holder.imageView);

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBannerClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls == null ? 0 : imageUrls.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_banner);
        }
    }
}
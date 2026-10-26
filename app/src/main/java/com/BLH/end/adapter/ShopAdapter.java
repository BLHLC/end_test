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
import com.BLH.end.bean.ShopBean;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private Context context;
    private List<ShopBean> shopList;

    // 构造函数，传入上下文和数据源
    public ShopAdapter(Context context, List<ShopBean> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载我们刚才写的 item_shop 布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取当前项的数据
        ShopBean shop = shopList.get(position);

        // 填充数据到控件
        holder.tvName.setText(shop.getShopName());
        holder.tvInfo.setText("评分 " + shop.getScore() + " | 月售 " + shop.getMonthSale());
        holder.tvFee.setText("起送 ¥" + shop.getMinPrice() + " | 配送 ¥" + shop.getDeliveryFee());

        // 用 Picasso 加载网络图片
        Picasso.get().load(shop.getLogo()).into(holder.ivLogo);
    }

    @Override
    public int getItemCount() {
        return shopList == null ? 0 : shopList.size();
    }

    // ViewHolder 用于缓存控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvName, tvInfo, tvFee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLogo = itemView.findViewById(R.id.iv_shop_logo);
            tvName = itemView.findViewById(R.id.tv_shop_name);
            tvInfo = itemView.findViewById(R.id.tv_shop_info);
            tvFee = itemView.findViewById(R.id.tv_shop_fee);
        }
    }
}
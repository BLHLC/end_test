package com.BLH.end.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.BLH.end.R;
import com.BLH.end.activity.ShopDetailActivity;
import com.BLH.end.adapter.BannerAdapter;
import com.BLH.end.adapter.ShopAdapter;
import com.BLH.end.bean.ShopBean;
import com.BLH.end.utils.JsonParse;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private Handler bannerHandler;
    private Runnable bannerRunnable;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. 初始化店铺列表
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_shop);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ShopBean> shopList = JsonParse.getShopList(getContext());
        ShopAdapter adapter = new ShopAdapter(getContext(), shopList);
        recyclerView.setAdapter(adapter);

        // 2. 初始化轮播图
        initBanner(view, shopList);

        return view;
    }

    private void initBanner(View view, List<ShopBean> shopList) {
        viewPager = view.findViewById(R.id.view_pager_banner);

        // 使用本地的 assets 图片
        String[] banners = {
                "file:///android_asset/images/strawberry.png",
                "file:///android_asset/images/matcha.png",
                "file:///android_asset/images/cake.jpg"
        };

        viewPager.setAdapter(new BannerAdapter(getContext(), banners, position -> {
            if (shopList != null && !shopList.isEmpty()) {
                int shopIndex = position % shopList.size();
                ShopBean targetShop = shopList.get(shopIndex);
                Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
                intent.putExtra("shop", targetShop);
                startActivity(intent);
            }
        }));

        // 自动滚动逻辑
        bannerHandler = new Handler(Looper.getMainLooper());
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % banners.length;
                viewPager.setCurrentItem(nextItem, true);
                bannerHandler.postDelayed(this, 3000);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    // 销毁页面时停止轮播图
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }
}
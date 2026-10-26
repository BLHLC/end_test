package com.BLH.end; // 你的包名可能略有不同，保留你自己的即可

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.BLH.end.bean.ShopBean;
import com.BLH.end.utils.JsonParse;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 定义一个 TAG，方便在 Logcat 里过滤查看
    private static final String TAG = "Step1_Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- 开始测试 ---
        Log.d(TAG, "开始解析 JSON 数据...");

        // 调用我们刚才写的工具类，读取 assets 里的 json
        List<ShopBean> shopList = JsonParse.getShopList(this);

        if (shopList != null && shopList.size() > 0) {
            Log.d(TAG, "解析成功！店铺数量：" + shopList.size());
            // 打印第一家店的名字和第一个商品的名字，验证数据是否真的取到了
            Log.d(TAG, "第一家店名：" + shopList.get(0).getShopName());
            Log.d(TAG, "第一个商品名：" + shopList.get(0).getFoods().get(0).getFoodName());
        } else {
            Log.e(TAG, "解析失败，请检查 assets 里的 JSON 文件名或格式！");
        }
    }
}
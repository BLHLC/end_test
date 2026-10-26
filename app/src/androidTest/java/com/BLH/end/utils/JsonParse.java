package com.BLH.end.utils;

import android.content.Context;
import com.BLH.end.bean.ShopBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JsonParse {
    public static List<ShopBean> getShopList(Context context) {
        try {
            // 读取 assets 里的 json 文件
            InputStream is = context.getAssets().open(Constant.JSON_FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            is.close();

            // 使用 Gson 解析
            Gson gson = new Gson();
            return gson.fromJson(sb.toString(), new TypeToken<List<ShopBean>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
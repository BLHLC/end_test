package com.BLH.end.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.BLH.end.bean.CartBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDao {
    private DbHelper helper;

    public OrderDao(Context context) {
        helper = new DbHelper(context);
    }

    // 提交订单
    public void submitOrder(int shopId, String shopName, List<CartBean> cartList, String address, String phone) {
        SQLiteDatabase db = helper.getWritableDatabase();

        // 计算总价
        double totalPrice = 0;
        for (CartBean bean : cartList) {
            totalPrice += bean.getPrice() * bean.getCount();
        }

        // 生成订单号（用时间戳）
        String orderNo = String.valueOf(System.currentTimeMillis());
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // 1. 插入订单主表 orders
        ContentValues orderValues = new ContentValues();
        orderValues.put("order_no", orderNo);
        orderValues.put("shop_name", shopName);
        orderValues.put("total_price", totalPrice);
        orderValues.put("address", address);
        orderValues.put("phone", phone);
        orderValues.put("create_time", createTime);

        // insert 方法会返回新插入行的 ID
        long orderId = db.insert("orders", null, orderValues);

        // 2. 批量插入订单明细 order_item
        for (CartBean bean : cartList) {
            ContentValues itemValues = new ContentValues();
            itemValues.put("order_id", orderId);
            itemValues.put("food_name", bean.getFoodName());
            itemValues.put("price", bean.getPrice());
            itemValues.put("count", bean.getCount());
            db.insert("order_item", null, itemValues);
        }

        // 3. 提交成功后，清空该店的购物车
        db.delete("cart", "shop_id=?", new String[]{String.valueOf(shopId)});

        db.close();
    }
}
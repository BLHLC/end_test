package com.BLH.end.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.BLH.end.bean.CartBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.database.Cursor;
import java.util.List;
import java.util.Locale;

public class OrderDao {
    private DbHelper helper;

    public OrderDao(Context context) {
        helper = new DbHelper(context);
    }
    // 查询所有历史订单（返回一个简单的字符串列表，格式：订单号 | 店名 | 金额 | 状态）
    public List<String> getAllOrders() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = db.query("orders", null, null, null, null, null, "id DESC"); // 倒序，最新的在最上面
        while (cursor.moveToNext()) {
            String orderNo = cursor.getString(cursor.getColumnIndexOrThrow("order_no"));
            String shopName = cursor.getString(cursor.getColumnIndexOrThrow("shop_name"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
            String statusStr = (status == 1) ? "已支付" : "未支付";

            list.add("订单号：" + orderNo + "\n店铺：" + shopName + "\n金额：￥" + price + " (" + statusStr + ")");
        }
        cursor.close();
        db.close();
        return list;
    }
    // 提交订单
    // 提交订单（支持多店合并，shopId 传 0 代表合并订单）
    public void submitOrder(int shopId, String shopName, List<CartBean> cartList, String address, String phone) {
        SQLiteDatabase db = helper.getWritableDatabase();

        double totalPrice = 0;
        for (CartBean bean : cartList) {
            totalPrice += bean.getPrice() * bean.getCount();
        }

        String orderNo = String.valueOf(System.currentTimeMillis());
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // 1. 插入订单主表 (只插一条！)
        ContentValues orderValues = new ContentValues();
        orderValues.put("order_no", orderNo);
        orderValues.put("shop_id", shopId); // 0代表多店合并，具体的店铺 ID 代表单店
        orderValues.put("shop_name", shopName); // 比如 "多店合并" 或真实的店名
        orderValues.put("total_price", totalPrice);
        orderValues.put("address", address);
        orderValues.put("phone", phone);
        orderValues.put("create_time", createTime);

        long orderId = db.insert("orders", null, orderValues);

        // 2. 批量插入订单明细
        for (CartBean bean : cartList) {
            ContentValues itemValues = new ContentValues();
            itemValues.put("order_id", orderId);
            itemValues.put("shop_name", bean.getShopName()); // 存下这个商品属于哪个店
            itemValues.put("food_name", bean.getFoodName());
            itemValues.put("price", bean.getPrice());
            itemValues.put("count", bean.getCount());
            db.insert("order_item", null, itemValues);
        }

        // 3. 清空相关店铺的购物车
        if (shopId == 0) {
            // 如果是合并订单，清空全部购物车
            db.delete("cart", null, null);
        } else {
            // 如果是单店，只清空这家的
            db.delete("cart", "shop_id=?", new String[]{String.valueOf(shopId)});
        }

        db.close();
    }
}
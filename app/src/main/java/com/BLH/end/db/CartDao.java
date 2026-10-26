package com.BLH.end.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.BLH.end.bean.CartBean;
import com.BLH.end.bean.FoodBean;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private DbHelper helper;

    public CartDao(Context context) {
        helper = new DbHelper(context);
    }

    // 1. 添加商品到购物车
    public void addToCart(int shopId, FoodBean food) {
        SQLiteDatabase db = helper.getWritableDatabase();
        // 先查询购物车里是否已经有这个商品了
        Cursor cursor = db.query("cart", null, "shop_id=? AND food_id=?",
                new String[]{String.valueOf(shopId), String.valueOf(food.getFoodId())}, null, null, null);

        if (cursor.moveToFirst()) {
            // 如果已经有了，数量 +1
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
            ContentValues values = new ContentValues();
            values.put("count", count + 1);
            db.update("cart", values, "shop_id=? AND food_id=?",
                    new String[]{String.valueOf(shopId), String.valueOf(food.getFoodId())});
        } else {
            // 如果没有，插入新数据
            ContentValues values = new ContentValues();
            values.put("shop_id", shopId);
            values.put("food_id", food.getFoodId());
            values.put("food_name", food.getFoodName());
            values.put("price", food.getPrice());
            values.put("count", 1);
            values.put("image", food.getImage());
            db.insert("cart", null, values);
        }
        cursor.close();
        db.close();
    }

    // 2. 减少商品数量 (如果减到0则删除)
    public void minusCart(int shopId, int foodId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("cart", null, "shop_id=? AND food_id=?",
                new String[]{String.valueOf(shopId), String.valueOf(foodId)}, null, null, null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
            if (count > 1) {
                ContentValues values = new ContentValues();
                values.put("count", count - 1);
                db.update("cart", values, "shop_id=? AND food_id=?",
                        new String[]{String.valueOf(shopId), String.valueOf(foodId)});
            } else {
                // 数量减到0，直接删除该记录
                db.delete("cart", "shop_id=? AND food_id=?",
                        new String[]{String.valueOf(shopId), String.valueOf(foodId)});
            }
        }
        cursor.close();
        db.close();
    }

    // 3. 查询某家店购物车里的所有商品
    public List<CartBean> getCartListByShop(int shopId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<CartBean> list = new ArrayList<>();
        Cursor cursor = db.query("cart", null, "shop_id=?",
                new String[]{String.valueOf(shopId)}, null, null, null);

        while (cursor.moveToNext()) {
            CartBean bean = new CartBean();
            bean.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            bean.setShopId(cursor.getInt(cursor.getColumnIndexOrThrow("shop_id")));
            bean.setFoodId(cursor.getInt(cursor.getColumnIndexOrThrow("food_id")));
            bean.setFoodName(cursor.getString(cursor.getColumnIndexOrThrow("food_name")));
            bean.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            bean.setCount(cursor.getInt(cursor.getColumnIndexOrThrow("count")));
            bean.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    // 4. 清空某家店的购物车
    public void clearCartByShop(int shopId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("cart", "shop_id=?", new String[]{String.valueOf(shopId)});
        db.close();
    }
}
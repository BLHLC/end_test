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
    // 修改方法签名，增加 shopName
    public void addToCart(int shopId, String shopName, FoodBean food) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("cart", null, "shop_id=? AND food_id=?",
                new String[]{String.valueOf(shopId), String.valueOf(food.getFoodId())}, null, null, null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
            ContentValues values = new ContentValues();
            values.put("count", count + 1);
            db.update("cart", values, "shop_id=? AND food_id=?",
                    new String[]{String.valueOf(shopId), String.valueOf(food.getFoodId())});
        } else {
            ContentValues values = new ContentValues();
            values.put("shop_id", shopId);
            values.put("shop_name", shopName); // 新增存店名
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

    // 修改 getCartListByShop 和 getAllCartList 里面解析 Cursor 的地方，加上：
    // bean.setShopName(cursor.getString(cursor.getColumnIndexOrThrow("shop_name")));

    // 2. 减少商品数量
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
                db.delete("cart", "shop_id=? AND food_id=?",
                        new String[]{String.valueOf(shopId), String.valueOf(foodId)});
            }
        }
        cursor.close();
        db.close();
    }

    // 3. 查询某家店购物车里的所有商品 (这就是报错缺少的方法)
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

    // 5. 获取全局购物车（跨店铺）用于 CartFragment
    public List<CartBean> getAllCartList() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<CartBean> list = new ArrayList<>();
        Cursor cursor = db.query("cart", null, null, null, null, null, "shop_id ASC");
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

    // 6. 获取购物车中所有不同的店铺 ID
    public List<Integer> getAllShopIdsInCart() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Integer> shopIds = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT DISTINCT shop_id FROM cart", null);
        while (cursor.moveToNext()) {
            shopIds.add(cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return shopIds;
    }

    // 7. 清空所有购物车
    public void clearAllCart() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("cart", null, null);
        db.close();
    }
    // 删除特定商品
    public void deleteItem(int shopId, int foodId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("cart", "shop_id=? AND food_id=?", new String[]{String.valueOf(shopId), String.valueOf(foodId)});
        db.close();
    }
}
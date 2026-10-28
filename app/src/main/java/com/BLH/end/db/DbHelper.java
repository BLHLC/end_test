package com.BLH.end.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        // 确保版本号是 3
        super(context, "dessert.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. 购物车表 (新增 shop_name)
        String createCart = "CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shop_id INTEGER NOT NULL, " +
                "shop_name TEXT, " +
                "food_id INTEGER NOT NULL, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL DEFAULT 1, " +
                "image TEXT)";
        db.execSQL(createCart);

        // 2. 订单主表 (新增 shop_id)
        String createOrders = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_no TEXT NOT NULL, " +
                "shop_id INTEGER DEFAULT 0, " +
                "shop_name TEXT, " +
                "total_price REAL NOT NULL, " +
                "delivery_fee REAL DEFAULT 0, " +
                "address TEXT, " +
                "phone TEXT, " +
                "status INTEGER DEFAULT 1, " +
                "create_time TEXT)";
        db.execSQL(createOrders);

        // 3. 订单明细表 (新增 shop_name)
        String createOrderItem = "CREATE TABLE order_item (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "shop_name TEXT, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL)";
        db.execSQL(createOrderItem);
    }

    // 只保留这一个 onUpgrade 方法，删掉下面重复的！
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 开发阶段，直接删表重建最省事
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_item");
        onCreate(db);
    }
}
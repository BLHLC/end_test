package com.BLH.end.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "dessert.db", null, 2); // 数据库名为 dessert.db
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. 购物车表 (之前写过的，保留)
        String createCart = "CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shop_id INTEGER NOT NULL, " +
                "food_id INTEGER NOT NULL, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL DEFAULT 1, " +
                "image TEXT)";
        db.execSQL(createCart);

        // 2. 订单主表 (新增)
        String createOrders = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_no TEXT NOT NULL, " +
                "shop_name TEXT, " +
                "total_price REAL NOT NULL, " +
                "delivery_fee REAL DEFAULT 0, " +
                "address TEXT, " +
                "phone TEXT, " +
                "status INTEGER DEFAULT 1, " + // 1待付款 2已支付
                "create_time TEXT)";
        db.execSQL(createOrders);

        // 3. 订单明细表 (新增)
        String createOrderItem = "CREATE TABLE order_item (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL)";
        db.execSQL(createOrderItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果版本变了，直接删掉旧表，重新建表（仅限开发阶段这么干）
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_item");
        onCreate(db);
    }
}
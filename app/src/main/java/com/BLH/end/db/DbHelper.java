package com.BLH.end.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        // 1. 将版本号直接改大，比如改成 5
        super(context, "dessert.db", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建购物车表
        db.execSQL("CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shop_id INTEGER NOT NULL, " +
                "shop_name TEXT, " +
                "food_id INTEGER NOT NULL, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL DEFAULT 1, " +
                "image TEXT)");

        // 创建订单主表
        db.execSQL("CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_no TEXT NOT NULL, " +
                "shop_id INTEGER DEFAULT 0, " +
                "shop_name TEXT, " +
                "total_price REAL NOT NULL, " +
                "delivery_fee REAL DEFAULT 0, " +
                "address TEXT, " +
                "phone TEXT, " +
                "status INTEGER DEFAULT 1, " +
                "create_time TEXT)");

        // 创建订单明细表
        db.execSQL("CREATE TABLE order_item (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "shop_name TEXT, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL)");

        // 创建收货地址表
        db.execSQL("CREATE TABLE address (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "detail TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 2. 只要版本号变了，直接暴力把所有的表全删了，然后重建
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_item");
        db.execSQL("DROP TABLE IF EXISTS address");
        onCreate(db);
    }
}
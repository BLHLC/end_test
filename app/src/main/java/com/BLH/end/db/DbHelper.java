package com.BLH.end.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "dessert.db", null, 1); // 数据库名为 dessert.db
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建购物车表 (完全遵循指导书的字段要求)
        String createCart = "CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shop_id INTEGER NOT NULL, " +
                "food_id INTEGER NOT NULL, " +
                "food_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "count INTEGER NOT NULL DEFAULT 1, " +
                "image TEXT)";
        db.execSQL(createCart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 暂不处理升级
    }
}
package com.BLH.end.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.BLH.end.bean.AddressBean;
import java.util.ArrayList;
import java.util.List;

public class AddressDao {
    private DbHelper helper;

    public AddressDao(Context context) {
        helper = new DbHelper(context);
    }

    public void addAddress(String name, String phone, String detail) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("detail", detail);
        db.insert("address", null, values);
        db.close();
    }

    public List<AddressBean> getAllAddress() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<AddressBean> list = new ArrayList<>();
        Cursor cursor = db.query("address", null, null, null, null, null, "id DESC");
        while (cursor.moveToNext()) {
            AddressBean bean = new AddressBean();
            bean.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            bean.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            bean.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            bean.setDetail(cursor.getString(cursor.getColumnIndexOrThrow("detail")));
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    // 更新地址
    public void updateAddress(int id, String name, String phone, String detail) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("detail", detail);
        db.update("address", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAddress(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("address", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
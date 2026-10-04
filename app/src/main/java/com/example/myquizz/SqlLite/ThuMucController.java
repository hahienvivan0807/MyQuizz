package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ThuMucController {
    private DatabaseHelper dbHelper;

    public ThuMucController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Đếm tổng số lượng Thư mục
    public int laySoLuongThuMuc() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM thu_muc", null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    // Thêm thư mục mới
    public boolean themThuMuc(String tenThuMuc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten_thu_muc", tenThuMuc);

        long result = db.insert("thu_muc", null, values);
        return result != -1;
    }
}

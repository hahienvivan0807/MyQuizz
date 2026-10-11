package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myquizz.model.ThuMuc;

import java.util.ArrayList;
import java.util.List;

public class ThuMucController {
    private DatabaseHelper dbHelper;

    public ThuMucController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Lấy danh sách tất cả Thư mục từ CSDL
    public List<ThuMuc> layDanhSachThuMuc() {
        List<ThuMuc> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM thu_muc ORDER BY ma_thu_muc DESC";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int maThuMuc = cursor.getInt(cursor.getColumnIndexOrThrow("ma_thu_muc"));
                int userId = cursor.getColumnIndex("user_id") != -1 ? cursor.getInt(cursor.getColumnIndexOrThrow("user_id")) : 1;
                String tenThuMuc = cursor.getString(cursor.getColumnIndexOrThrow("ten_thu_muc"));
                String moTaThuMuc = cursor.getString(cursor.getColumnIndexOrThrow("mo_ta_thu_muc"));

                danhSach.add(new ThuMuc(maThuMuc, userId, tenThuMuc, moTaThuMuc));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }

    // Lấy danh sách Thư mục theo ID Người dùng
    public List<ThuMuc> layDanhSachThuMucTheoUser(int userId) {
        List<ThuMuc> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM thu_muc WHERE user_id = ? ORDER BY ma_thu_muc DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int maThuMuc = cursor.getInt(cursor.getColumnIndexOrThrow("ma_thu_muc"));
                int idUser = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String tenThuMuc = cursor.getString(cursor.getColumnIndexOrThrow("ten_thu_muc"));
                String moTaThuMuc = cursor.getString(cursor.getColumnIndexOrThrow("mo_ta_thu_muc"));

                danhSach.add(new ThuMuc(maThuMuc, idUser, tenThuMuc, moTaThuMuc));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
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
    public boolean themThuMuc(ThuMuc thuMuc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", thuMuc.getUserId());
        values.put("ten_thu_muc", thuMuc.getTenThuMuc());
        values.put("mo_ta_thu_muc", thuMuc.getMoTaThuMuc());

        long result = db.insert("thu_muc", null, values);
        return result != -1;
    }
}

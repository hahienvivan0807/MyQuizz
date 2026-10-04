package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myquizz.model.BoDe;

import java.util.ArrayList;
import java.util.List;

public class BoDeController {
    private DatabaseHelper dbHelper;

    public BoDeController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm bộ đề mới
    public long themBoDe(BoDe boDe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tieu_de", boDe.getTieuDe());
        values.put("so_luong_cau", boDe.getSoLuongCau());

        return db.insert("bo_de", null, values);
    }

    // Lấy danh sách tất cả bộ đề
    public List<BoDe> layDanhSachBoDe() {
        List<BoDe> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM bo_de ORDER BY id DESC";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String tieuDe = cursor.getString(cursor.getColumnIndexOrThrow("tieu_de"));
                int soLuongCau = cursor.getInt(cursor.getColumnIndexOrThrow("so_luong_cau"));

                danhSach.add(new BoDe(id, tieuDe, soLuongCau));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }
}

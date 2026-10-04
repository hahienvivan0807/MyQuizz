package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myquizz.model.VatPham;

import java.util.ArrayList;
import java.util.List;

public class VatPhamController {
    private DatabaseHelper dbHelper;

    public VatPhamController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Lấy tất cả danh sách vật phẩm (Quiz)
    public List<VatPham> layDanhSachVatPham() {
        List<VatPham> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String cauTruyVan = "SELECT * FROM vat_pham ORDER BY ma_so DESC";

        Cursor cursor = db.rawQuery(cauTruyVan, null);
        if (cursor.moveToFirst()) {
            do {
                int maSo = cursor.getInt(cursor.getColumnIndexOrThrow("ma_so"));
                String tieuDe = cursor.getString(cursor.getColumnIndexOrThrow("tieu_de"));
                String moTa = cursor.getString(cursor.getColumnIndexOrThrow("mo_ta"));

                danhSach.add(new VatPham(maSo, tieuDe, moTa));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }

    // Đếm tổng số lượng Quiz
    public int laySoLuongQuiz() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM vat_pham", null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    // Thêm vật phẩm mới
    public boolean themVatPham(VatPham vatPham) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tieu_de", vatPham.getTieuDe());
        values.put("mo_ta", vatPham.getMoTa());

        long result = db.insert("vat_pham", null, values);
        return result != -1;
    }
}

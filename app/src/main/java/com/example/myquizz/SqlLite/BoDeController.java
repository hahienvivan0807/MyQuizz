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

    // Thêm bộ đề mới với thu_muc_id
    public long themBoDe(BoDe boDe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("thu_muc_id", boDe.getThuMucId());
        values.put("tieu_de", boDe.getTieuDe());
        values.put("so_luong_cau", boDe.getSoLuongCau());

        return db.insert("bo_de", null, values);
    }

    // Lấy danh sách tất cả bộ đề kèm lệnh đếm số lượng câu hỏi thực tế từ CSDL
    public List<BoDe> layDanhSachBoDe() {
        List<BoDe> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT b.id, b.thu_muc_id, b.tieu_de, b.so_luong_cau, "
                + "(SELECT COUNT(*) FROM cau_hoi c WHERE c.bo_de_id = b.id) AS so_luong_cau_dem "
                + "FROM bo_de b ORDER BY b.id DESC";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int thuMucId = cursor.getColumnIndex("thu_muc_id") != -1 ? cursor.getInt(cursor.getColumnIndexOrThrow("thu_muc_id")) : 0;
                String tieuDe = cursor.getString(cursor.getColumnIndexOrThrow("tieu_de"));
                int soLuongDem = cursor.getInt(cursor.getColumnIndexOrThrow("so_luong_cau_dem"));
                int soLuongCauGoc = cursor.getInt(cursor.getColumnIndexOrThrow("so_luong_cau"));

                // Ưu tiên lấy số lượng được đếm trực tiếp từ bảng câu hỏi nếu > 0
                int soLuongFinal = soLuongDem > 0 ? soLuongDem : soLuongCauGoc;

                danhSach.add(new BoDe(id, thuMucId, tieuDe, soLuongFinal));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }

    // Lấy danh sách bộ đề thuộc về một Thư Mục cụ thể, sử dụng LỆNH ĐẾM COUNT(*)
    public List<BoDe> layDanhSachBoDeTheoThuMuc(int thuMucId) {
        List<BoDe> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Lệnh SQL có subquery đếm số lượng câu hỏi thực tế trong bảng cau_hoi
        String sql = "SELECT b.id, b.thu_muc_id, b.tieu_de, b.so_luong_cau, "
                + "(SELECT COUNT(*) FROM cau_hoi c WHERE c.bo_de_id = b.id) AS so_luong_cau_dem "
                + "FROM bo_de b WHERE b.thu_muc_id = ? ORDER BY b.id DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(thuMucId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int tmId = cursor.getInt(cursor.getColumnIndexOrThrow("thu_muc_id"));
                String tieuDe = cursor.getString(cursor.getColumnIndexOrThrow("tieu_de"));
                int soLuongDem = cursor.getInt(cursor.getColumnIndexOrThrow("so_luong_cau_dem"));
                int soLuongCauGoc = cursor.getInt(cursor.getColumnIndexOrThrow("so_luong_cau"));

                int soLuongFinal = soLuongDem > 0 ? soLuongDem : soLuongCauGoc;

                danhSach.add(new BoDe(id, tmId, tieuDe, soLuongFinal));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }

    // Lệnh đếm số lượng câu hỏi trực tiếp từ bảng câu hỏi cho 1 bộ đề
    public int demSoLuongCauHoiTheoBoDe(int boDeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM cau_hoi WHERE bo_de_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(boDeId)});
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    // Cập nhật lại cột so_luong_cau trong CSDL dựa theo kết quả đếm
    public void capNhatSoLuongCauHoi(int boDeId, int soLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("so_luong_cau", soLuong);
        db.update("bo_de", values, "id = ?", new String[]{String.valueOf(boDeId)});
    }
}

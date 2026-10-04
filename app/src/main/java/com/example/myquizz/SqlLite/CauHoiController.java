package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myquizz.model.CauHoi;

import java.util.ArrayList;
import java.util.List;

public class CauHoiController {
    private DatabaseHelper dbHelper;

    public CauHoiController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm câu hỏi mới thuộc về bộ đề (boDeId)
    public boolean themCauHoi(CauHoi cauHoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bo_de_id", cauHoi.getBoDeId());
        values.put("noi_dung_cau_hoi", cauHoi.getNoiDungCauHoi());
        values.put("dap_an_dung", cauHoi.getDapAnDung());
        values.put("dap_an_sai_1", cauHoi.getDapAnSai1());
        values.put("dap_an_sai_2", cauHoi.getDapAnSai2());
        values.put("dap_an_sai_3", cauHoi.getDapAnSai3());

        long result = db.insert("cau_hoi", null, values);
        return result != -1;
    }

    // Lấy danh sách câu hỏi theo ID bộ đề (boDeId)
    public List<CauHoi> layDanhSachCauHoiTheoBoDe(int boDeId) {
        List<CauHoi> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM cau_hoi WHERE bo_de_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(boDeId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int idBoDe = cursor.getInt(cursor.getColumnIndexOrThrow("bo_de_id"));
                String noiDung = cursor.getString(cursor.getColumnIndexOrThrow("noi_dung_cau_hoi"));
                String dung = cursor.getString(cursor.getColumnIndexOrThrow("dap_an_dung"));
                String sai1 = cursor.getString(cursor.getColumnIndexOrThrow("dap_an_sai_1"));
                String sai2 = cursor.getString(cursor.getColumnIndexOrThrow("dap_an_sai_2"));
                String sai3 = cursor.getString(cursor.getColumnIndexOrThrow("dap_an_sai_3"));

                danhSach.add(new CauHoi(id, idBoDe, noiDung, dung, sai1, sai2, sai3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }
}

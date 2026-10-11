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

    // Danh sách mảng tạm lưu trữ câu hỏi trên RAM ở Controller
    private List<CauHoi> danhSachCauHoiTam = new ArrayList<>();

    public CauHoiController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * HÀM LƯU CÂU HỎI VÀO MẢNG TẠM TRÊN CONTROLLER (LuucauhoivaomangTam)
     */
    public void LuucauhoivaomangTam(CauHoi cauHoi) {
        if (cauHoi != null) {
            danhSachCauHoiTam.add(cauHoi);
        }
    }

    public List<CauHoi> getDanhSachCauHoiTam() {
        return danhSachCauHoiTam;
    }

    public void xoaDanhSachCauHoiTam() {
        danhSachCauHoiTam.clear();
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

    /**
     * HÀM THÊM HÀNG LOẠT CÂU HỎI VÀO CSDL BẰNG TRANSACTION (ThemHangLoatCauHoi)
     */
    public boolean ThemHangLoatCauHoi(List<CauHoi> danhSachCauHoi) {
        return themDanhSachCauHoi(danhSachCauHoi);
    }

    // Thêm danh sách nhiều câu hỏi cùng lúc vào CSDL bằng Transaction
    public boolean themDanhSachCauHoi(List<CauHoi> danhSachCauHoi) {
        if (danhSachCauHoi == null || danhSachCauHoi.isEmpty()) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); // Bắt đầu Transaction
        try {
            for (CauHoi cauHoi : danhSachCauHoi) {
                ContentValues values = new ContentValues();
                values.put("bo_de_id", cauHoi.getBoDeId());
                values.put("noi_dung_cau_hoi", cauHoi.getNoiDungCauHoi());
                values.put("dap_an_dung", cauHoi.getDapAnDung());
                values.put("dap_an_sai_1", cauHoi.getDapAnSai1());
                values.put("dap_an_sai_2", cauHoi.getDapAnSai2());
                values.put("dap_an_sai_3", cauHoi.getDapAnSai3());

                db.insert("cau_hoi", null, values);
            }
            db.setTransactionSuccessful(); // Đánh dấu tất cả đều thành công
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction(); // Kết thúc Transaction
        }
    }

    // Thêm câu hỏi nhanh từ 1 Đáp án đúng và Mảng/Danh sách các Đáp án sai
    public boolean themCauHoiTuDanhSach(int boDeId, String noiDungCauHoi, String dapAnDung, List<String> danhSachDapAnSai) {
        String sai1 = (danhSachDapAnSai != null && danhSachDapAnSai.size() > 0) ? danhSachDapAnSai.get(0) : "";
        String sai2 = (danhSachDapAnSai != null && danhSachDapAnSai.size() > 1) ? danhSachDapAnSai.get(1) : "";
        String sai3 = (danhSachDapAnSai != null && danhSachDapAnSai.size() > 2) ? danhSachDapAnSai.get(2) : "";

        CauHoi cauHoi = new CauHoi(boDeId, noiDungCauHoi, dapAnDung, sai1, sai2, sai3);
        return themCauHoi(cauHoi);
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

    /**
     * LỆNH ĐẾM SỐ LƯỢNG CÂU HỎI THUỘC BỘ ĐỀ TRONG CSDL (SQL COUNT)
     */
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
}

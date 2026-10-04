package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DATABASE HELPER
 * Vai trò: Khởi tạo file CSDL 'MyQuiz.db', định nghĩa cấu trúc các bảng và chèn dữ liệu mẫu ban đầu.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // =========================================================================
    // THÔNG TIN CƠ SỞ DỮ LIỆU
    // =========================================================================
    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // =========================================================================
    // KHỞI TẠO CÁC BẢNG & DỮ LIỆU MẪU (Chỉ chạy 1 lần khi cài app)
    // =========================================================================
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ---------------------------------------------------------------------
        // 1. ĐỊNH NGHĨA BẢNG NGƯỜI DÙNG (users)
        // ---------------------------------------------------------------------
        String createUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT, "
                + "password TEXT, "
                + "email TEXT"
                + ");";
        db.execSQL(createUsers);

        // Chèn tài khoản admin mẫu
        ContentValues admin = new ContentValues();
        admin.put("username", "admin");
        admin.put("password", "123");
        admin.put("email", "admin@gmail.com");
        db.insert("users", null, admin);

        // ---------------------------------------------------------------------
        // 2. ĐỊNH NGHĨA BẢNG VẬT PHẨM / QUIZ (vat_pham)
        // ---------------------------------------------------------------------
        String createVatPham = "CREATE TABLE IF NOT EXISTS vat_pham ("
                + "ma_so INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "tieu_de TEXT, "
                + "mo_ta TEXT"
                + ");";
        db.execSQL(createVatPham);

        // Chèn các Quiz mẫu ban đầu nếu bảng còn rỗng
        Cursor cVatPham = db.rawQuery("SELECT COUNT(*) FROM vat_pham", null);
        if (cVatPham != null) {
            cVatPham.moveToFirst();
            if (cVatPham.getInt(0) == 0) {
                themVatPhamMau(db, "Ôn tập Sinh học 10 · Tế bào", "15 câu hỏi · Khoa học tự nhiên");
                themVatPhamMau(db, "Tổng hợp ngữ pháp tiếng Anh", "20 câu hỏi · Ngoại ngữ");
            }
            cVatPham.close();
        }

        // ---------------------------------------------------------------------
        // 3. ĐỊNH NGHĨA BẢNG THƯ MỤC (thu_muc)
        // ---------------------------------------------------------------------
        String createThuMuc = "CREATE TABLE IF NOT EXISTS thu_muc ("
                + "ma_thu_muc INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ten_thu_muc TEXT"
                + ");";
        db.execSQL(createThuMuc);

        // Chèn các Thư mục mẫu ban đầu nếu bảng còn rỗng
        Cursor cThuMuc = db.rawQuery("SELECT COUNT(*) FROM thu_muc", null);
        if (cThuMuc != null) {
            cThuMuc.moveToFirst();
            if (cThuMuc.getInt(0) == 0) {
                themThuMucMau(db, "Tiếng Anh");
                themThuMucMau(db, "Khoa học tự nhiên");
                themThuMucMau(db, "Ôn tập học kỳ");
            }
            cThuMuc.close();
        }
    }

    // =========================================================================
    // HÀM HỖ TRỢ CHÈN DỮ LIỆU MẪU BAN ĐẦU
    // =========================================================================
    private void themVatPhamMau(SQLiteDatabase db, String tieuDe, String moTa) {
        ContentValues values = new ContentValues();
        values.put("tieu_de", tieuDe);
        values.put("mo_ta", moTa);
        db.insert("vat_pham", null, values);
    }

    private void themThuMucMau(SQLiteDatabase db, String tenThuMuc) {
        ContentValues values = new ContentValues();
        values.put("ten_thu_muc", tenThuMuc);
        db.insert("thu_muc", null, values);
    }

    // =========================================================================
    // NÂNG CẤP BẢNG KHI TĂNG DATABASE_VERSION
    // =========================================================================
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS vat_pham");
        db.execSQL("DROP TABLE IF EXISTS thu_muc");
        onCreate(db);
    }
}

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
    private static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Bật hỗ trợ Khóa ngoại trong SQLite
        db.setForeignKeyConstraintsEnabled(true);
    }

    // =========================================================================
    // KHỞI TẠO CÁC BẢNG & DỮ LIỆU MẪU (Chỉ chạy 1 lần khi cài app)
    // =========================================================================
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ---------------------------------------------------------------------
        // 1. BẢNG NGƯỜI DÙNG (users)
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
        // 2. BẢNG VẬT PHẨM (vat_pham)
        // ---------------------------------------------------------------------
        String createVatPham = "CREATE TABLE IF NOT EXISTS vat_pham ("
                + "ma_so INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "tieu_de TEXT, "
                + "mo_ta TEXT"
                + ");";
        db.execSQL(createVatPham);

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
        // 3. BẢNG THƯ MỤC (thu_muc)
        // ---------------------------------------------------------------------
        String createThuMuc = "CREATE TABLE IF NOT EXISTS thu_muc ("
                + "ma_thu_muc INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ten_thu_muc TEXT"
                + ");";
        db.execSQL(createThuMuc);

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

        // ---------------------------------------------------------------------
        // 4. BẢNG BỘ ĐỀ (bo_de) - Khóa chính ID
        // ---------------------------------------------------------------------
        String createBoDe = "CREATE TABLE IF NOT EXISTS bo_de ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "tieu_de TEXT, "
                + "so_luong_cau INTEGER"
                + ");";
        db.execSQL(createBoDe);

        // ---------------------------------------------------------------------
        // 5. BẢNG CÂU HỎI (cau_hoi) - Khóa ngoại bo_de_id nối tới bo_de(id)
        // ---------------------------------------------------------------------
        String createCauHoi = "CREATE TABLE IF NOT EXISTS cau_hoi ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "bo_de_id INTEGER, "
                + "noi_dung_cau_hoi TEXT, "
                + "dap_an_dung TEXT, "
                + "dap_an_sai_1 TEXT, "
                + "dap_an_sai_2 TEXT, "
                + "dap_an_sai_3 TEXT, "
                + "FOREIGN KEY (bo_de_id) REFERENCES bo_de(id) ON DELETE CASCADE"
                + ");";
        db.execSQL(createCauHoi);
    }

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
        db.execSQL("DROP TABLE IF EXISTS cau_hoi");
        db.execSQL("DROP TABLE IF EXISTS bo_de");
        db.execSQL("DROP TABLE IF EXISTS bo_cau_hoi");
        onCreate(db);
    }
}

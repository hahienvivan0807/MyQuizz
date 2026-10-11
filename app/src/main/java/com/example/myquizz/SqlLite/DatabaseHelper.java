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
    private static final int DATABASE_VERSION = 13; // Tăng version lên 13

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

        // Chèn tài khoản mẫu pinvutru@gmail.com (ID = 1)
        ContentValues pinvutru = new ContentValues();
        pinvutru.put("username", "pinvutru@gmail.com");
        pinvutru.put("password", "123");
        pinvutru.put("email", "pinvutru@gmail.com");
        db.insert("users", null, pinvutru);

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
        // 3. BẢNG THƯ MỤC (thu_muc) - Nối với users(id) qua Khóa ngoại user_id
        // ---------------------------------------------------------------------
        String createThuMuc = "CREATE TABLE IF NOT EXISTS thu_muc ("
                + "ma_thu_muc INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "ten_thu_muc TEXT, "
                + "mo_ta_thu_muc TEXT, "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ");";
        db.execSQL(createThuMuc);

        // ---------------------------------------------------------------------
        // 4. BẢNG BỘ ĐỀ (bo_de) - Có Khóa ngoại thu_muc_id tham chiếu thu_muc(ma_thu_muc)
        // ---------------------------------------------------------------------
        String createBoDe = "CREATE TABLE IF NOT EXISTS bo_de ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "thu_muc_id INTEGER, "
                + "tieu_de TEXT, "
                + "so_luong_cau INTEGER, "
                + "FOREIGN KEY (thu_muc_id) REFERENCES thu_muc(ma_thu_muc) ON DELETE CASCADE"
                + ");";
        db.execSQL(createBoDe);

        Cursor cBoDe = db.rawQuery("SELECT COUNT(*) FROM bo_de", null);
        if (cBoDe != null) {
            cBoDe.moveToFirst();
            if (cBoDe.getInt(0) == 0) {
                themBoDeMau(db, 1, "Từ vựng: Cuộc sống hằng ngày", 15);
                themBoDeMau(db, 1, "Ngữ pháp: Thì hiện tại đơn", 10);
            }
            cBoDe.close();
        }

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

    private void themBoDeMau(SQLiteDatabase db, int thuMucId, String tieuDe, int soLuongCau) {
        ContentValues values = new ContentValues();
        values.put("thu_muc_id", thuMucId);
        values.put("tieu_de", tieuDe);
        values.put("so_luong_cau", soLuongCau);
        db.insert("bo_de", null, values);
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

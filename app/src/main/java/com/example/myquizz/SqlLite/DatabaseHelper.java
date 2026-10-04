package com.example.myquizz.SqlLite;

import com.example.myquizz.model.VatPham;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 3; // Tăng version lên 2 để cập nhật bảng mới
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";

    // Bảng vật phẩm (Quiz)
    private static final String BANG_VAT_PHAM = "vat_pham";
    private static final String COT_MA_SO = "ma_so";
    private static final String COT_TIEU_DE = "tieu_de";
    private static final String COT_MO_TA = "mo_ta";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng users
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_EMAIL + " TEXT "
                + ");";
        db.execSQL(createTable);

        // 2. Thêm tài khoản ảo admin
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "123");
        values.put(COLUMN_EMAIL, "admin@gmail.com");

        db.insert(TABLE_NAME, null, values);

        // 3. Câu lệnh tạo bảng vật phẩm
        taoBangVatPhamNeuChuaCo(db);
    }

    private void taoBangVatPhamNeuChuaCo(SQLiteDatabase db) {
        String taoBangVatPham = "CREATE TABLE IF NOT EXISTS " + BANG_VAT_PHAM + " ("
                + COT_MA_SO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COT_TIEU_DE + " TEXT, "
                + COT_MO_TA + " TEXT"
                + ");";
        db.execSQL(taoBangVatPham);

        // Kiểm tra nếu bảng chưa có dữ liệu thì mới thêm vật phẩm mẫu
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + BANG_VAT_PHAM, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            if (count == 0) {
                themVatPhamMau(db, "Ôn tập Sinh học 10 · Tế bào", "15 câu hỏi · Khoa học tự nhiên");
                themVatPhamMau(db, "Tổng hợp ngữ pháp tiếng Anh", "20 câu hỏi · Ngoại ngữ");
            }
        }
    }

    // Hàm thêm vật phẩm mẫu
    private void themVatPhamMau(SQLiteDatabase db, String tieuDe, String moTa) {
        ContentValues values = new ContentValues();
        values.put(COT_TIEU_DE, tieuDe);
        values.put(COT_MO_TA, moTa);
        db.insert(BANG_VAT_PHAM, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BANG_VAT_PHAM);
        onCreate(db);
    }

    // Kiểm tra xem tên đăng nhập đã tồn tại chưa
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Kiểm tra đăng nhập (đúng tài khoản và mật khẩu)
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD  + " = ?", new String[]{username, password});

        boolean isMatch = cursor.getCount() > 0;
        cursor.close();

        return isMatch;
    }

    // Kiểm tra và thực hiện đăng ký tài khoản mới
    public boolean checkRegister(String username, String password) {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        // 1. Kiểm tra tài khoản đã tồn tại chưa
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        boolean isExist = cursor.getCount() > 0;
        cursor.close();

        // Nếu đã tồn tại -> không cho đăng ký
        if (isExist) {
            return false;
        }

        // 2. Nếu chưa tồn tại -> Thêm tài khoản mới vào CSDL (INSERT INTO)
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, username);

        long result = dbWrite.insert(TABLE_NAME, null, values);
        return result != -1; // Trả về true nếu chèn thành công
    }

    // Lấy tất cả danh sách vật phẩm
    public List<VatPham> layDanhSachVatPham() {
        List<VatPham> danhSach = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Đảm bảo bảng vat_pham luôn tồn tại trước khi truy vấn
        taoBangVatPhamNeuChuaCo(db);

        String cauTruyVan = "SELECT * FROM " + BANG_VAT_PHAM + " ORDER BY " + COT_MA_SO + " DESC";

        Cursor cursor = db.rawQuery(cauTruyVan, null);
        if (cursor.moveToFirst()) {
            do {
                int maSo = cursor.getInt(cursor.getColumnIndexOrThrow(COT_MA_SO));
                String tieuDe = cursor.getString(cursor.getColumnIndexOrThrow(COT_TIEU_DE));
                String moTa = cursor.getString(cursor.getColumnIndexOrThrow(COT_MO_TA));

                danhSach.add(new VatPham(maSo, tieuDe, moTa));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return danhSach;
    }
}

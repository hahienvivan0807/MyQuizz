package com.example.myquizz.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserController {
    private DatabaseHelper dbHelper;

    public UserController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Kiểm tra tài khoản đã tồn tại chưa
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Kiểm tra đăng nhập (đúng tài khoản và mật khẩu)
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean isMatch = cursor.getCount() > 0;
        cursor.close();
        return isMatch;
    }

    // Đăng ký tài khoản mới
    public boolean checkRegister(String username, String password) {
        if (checkUserExists(username)) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", username);

        long result = db.insert("users", null, values);
        return result != -1;
    }
}

package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.UserController;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    // Khai báo các ID
    TextInputEditText DN_emailInput, DN_passwordInput;
    Button btnDangNhap;
    UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userController = new UserController(this);

        AddViews();
    }

    private void AddViews() {
        // Tìm các id đăng nhập
        DN_emailInput = findViewById(R.id.emailInput);
        DN_passwordInput = findViewById(R.id.passwordInput);
        btnDangNhap = findViewById(R.id.loginButton);

        // Đăng ký sự kiện click cho nút Đăng nhập
        btnDangNhap.setOnClickListener(v -> DangNhap());

        // Chuyển sang DangKyActivity khi bấm "Đăng ký ngay"
        android.widget.TextView signUpText = findViewById(R.id.signUpText);
        if (signUpText != null) {
            signUpText.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            });
        }
    }

    private void DangNhap() {
        String taiKhoan = DN_emailInput.getText() != null ? DN_emailInput.getText().toString().trim() : "";
        String matKhau = DN_passwordInput.getText() != null ? DN_passwordInput.getText().toString().trim() : "";

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isMatch = userController.checkLogin(taiKhoan, matKhau);
        if (isMatch) {
            Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, TrangChu_Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
        }
    }
}

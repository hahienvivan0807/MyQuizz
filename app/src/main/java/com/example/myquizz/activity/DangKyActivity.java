package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.UserController;
import com.google.android.material.textfield.TextInputEditText;

public class DangKyActivity extends AppCompatActivity {

    // Khai báo các view từ giao diện activity_register.xml
    private TextInputEditText emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private TextView loginText;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Kết nối Activity với layout activity_register.xml
        setContentView(R.layout.activity_register);

        userController = new UserController(this);

        AddViews();
        AddEvents();
    }

    private void AddViews() {
        // Ánh xạ các view theo ID trong activity_register.xml
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);
    }

    private void AddEvents() {
        // Sự kiện chuyển sang màn hình Đăng nhập khi bấm "loginText"
        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Sự kiện click nút Đăng ký
        registerButton.setOnClickListener(v -> DangKy());
    }

    private void DangKy() {
        String taiKhoan = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String matKhau = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";
        String xacNhanMatKhau = confirmPasswordInput.getText() != null ? confirmPasswordInput.getText().toString().trim() : "";

        if (taiKhoan.isEmpty() || matKhau.isEmpty() || xacNhanMatKhau.isEmpty()) {
            Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            Toast.makeText(DangKyActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm checkRegister để kiểm tra trùng và thêm tài khoản mới
        boolean isSuccess = userController.checkRegister(taiKhoan, matKhau);

        if (isSuccess) {
            Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(DangKyActivity.this, "Đăng ký thất bại! Tài khoản đã tồn tại.", Toast.LENGTH_SHORT).show();
        }
    }
}

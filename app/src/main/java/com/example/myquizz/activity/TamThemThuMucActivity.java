package com.example.myquizz.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.ThuMucController;
import com.example.myquizz.model.ThuMuc;
import com.google.android.material.button.MaterialButton;

public class TamThemThuMucActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etTenThuMuc, etMoTaThuMuc;
    private MaterialButton btnLuuThuMuc;
    private ThuMucController thuMucController;

    private int currentUserId = 1; // Mặc định ID người dùng đang đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tam_them_thu_muc);

        thuMucController = new ThuMucController(this);

        initViews();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBackAddFolder);
        etTenThuMuc = findViewById(R.id.etTenThuMuc);
        etMoTaThuMuc = findViewById(R.id.etMoTaThuMuc);
        btnLuuThuMuc = findViewById(R.id.btnLuuThuMuc);
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnLuuThuMuc != null) {
            btnLuuThuMuc.setOnClickListener(v -> luuThuMucMoi());
        }
    }

    private void luuThuMucMoi() {
        String tenThuMuc = etTenThuMuc.getText().toString().trim();
        String moTaThuMuc = etMoTaThuMuc.getText().toString().trim();

        if (tenThuMuc.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên thư mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (moTaThuMuc.isEmpty()) {
            moTaThuMuc = "0 tài liệu";
        }

        ThuMuc thuMucMoi = new ThuMuc(tenThuMuc, currentUserId, moTaThuMuc);
        boolean isSuccess = thuMucController.themThuMuc(thuMucMoi);

        if (isSuccess) {
            Toast.makeText(this, "Tạo thư mục mới thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Tạo thư mục thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}

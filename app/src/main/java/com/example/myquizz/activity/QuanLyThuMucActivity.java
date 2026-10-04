package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class QuanLyThuMucActivity extends AppCompatActivity {

    private MaterialCardView cardEnglish, cardScience, cardTermExam, cardUnclassified;
    private MaterialButton btnSeeAll, btnImportDocument;
    private LinearLayout navHome, navQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thu_muc);

        initViews();
        setupEvents();
    }

    private void initViews() {
        cardEnglish = findViewById(R.id.cardFolderEnglish);
        cardScience = findViewById(R.id.cardFolderScience);
        cardTermExam = findViewById(R.id.cardFolderTermExam);
        cardUnclassified = findViewById(R.id.cardFolderUnclassified);

        btnSeeAll = findViewById(R.id.btnSeeAll);
        btnImportDocument = findViewById(R.id.btnImportDocument);

        navHome = findViewById(R.id.navFolderHome);
        navQuiz = findViewById(R.id.navFolderQuiz);
    }

    private void setupEvents() {
        // Sự kiện khi bấm vào các Thư mục
        if (cardEnglish != null) {
            cardEnglish.setOnClickListener(v -> {
                Intent intent = new Intent(QuanLyThuMucActivity.this, ChiTietThuMucActivity.class);
                startActivity(intent);
            });
        }

        if (cardScience != null) {
            cardScience.setOnClickListener(v -> 
                Toast.makeText(this, "Mở thư mục: Khoa học tự nhiên", Toast.LENGTH_SHORT).show());
        }

        if (cardTermExam != null) {
            cardTermExam.setOnClickListener(v -> 
                Toast.makeText(this, "Mở thư mục: Ôn tập học kỳ", Toast.LENGTH_SHORT).show());
        }

        if (cardUnclassified != null) {
            cardUnclassified.setOnClickListener(v -> 
                Toast.makeText(this, "Mở thư mục: Chưa phân loại", Toast.LENGTH_SHORT).show());
        }

        // Sự kiện bấm nút "Xem tất cả"
        if (btnSeeAll != null) {
            btnSeeAll.setOnClickListener(v -> 
                Toast.makeText(this, "Hiển thị tất cả thư mục", Toast.LENGTH_SHORT).show());
        }

        // Sự kiện bấm nút "+ Nhập tài liệu"
        if (btnImportDocument != null) {
            btnImportDocument.setOnClickListener(v -> 
                Toast.makeText(this, "Chức năng nhập tài liệu", Toast.LENGTH_SHORT).show());
        }

        // Chuyển về Trang chủ khi bấm Tab Trang chủ
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(QuanLyThuMucActivity.this, TrangChu_Activity.class);
                startActivity(intent);
                finish();
            });
        }

        if (navQuiz != null) {
            navQuiz.setOnClickListener(v -> 
                Toast.makeText(this, "Chuyển sang trang Quiz của tôi", Toast.LENGTH_SHORT).show());
        }
    }
}

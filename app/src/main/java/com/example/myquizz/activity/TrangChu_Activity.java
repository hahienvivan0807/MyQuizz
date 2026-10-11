package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.BoDeController;
import com.example.myquizz.SqlLite.ThuMucController;
import com.example.myquizz.adapter.BoDeAdapter;
import com.example.myquizz.model.BoDe;

import java.util.List;

public class TrangChu_Activity extends AppCompatActivity {

    private RecyclerView rvVatPham;
    private BoDeAdapter boDeAdapter;

    private BoDeController boDeController;
    private ThuMucController thuMucController;

    private TextView tvSubtitleMyQuiz, tvSubtitleFolder;
    private View btnCreateQuiz, cardHero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo các Controller
        boDeController = new BoDeController(this);
        thuMucController = new ThuMucController(this);

        // Ánh xạ các View
        rvVatPham = findViewById(R.id.recyclerViewVatPham);
        tvSubtitleMyQuiz = findViewById(R.id.tvSubtitleMyQuiz);
        tvSubtitleFolder = findViewById(R.id.tvSubtitleFolder);
        btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        cardHero = findViewById(R.id.cardHero);

        // Cập nhật thống kê số lượng từ CSDL
        capNhatThongKe();

        // Cấu hình RecyclerView hiển thị danh sách Bộ Đề
        if (rvVatPham != null) {
            rvVatPham.setLayoutManager(new LinearLayoutManager(this));

            // Lệnh gọi hàm lấy danh sách Bộ Đề từ BoDeController
            List<BoDe> danhSachBoDe = boDeController.layDanhSachBoDe();

            boDeAdapter = new BoDeAdapter(this, danhSachBoDe);
            rvVatPham.setAdapter(boDeAdapter);
        }

        // Chuyển sang trang Quản lý thư mục
        View cardFolder = findViewById(R.id.cardFolder);
        View navFolders = findViewById(R.id.navFolders);

        if (cardFolder != null) {
            cardFolder.setOnClickListener(v -> {
                Intent intent = new Intent(TrangChu_Activity.this, QuanLyThuMucActivity.class);
                startActivity(intent);
            });
        }

        if (navFolders != null) {
            navFolders.setOnClickListener(v -> {
                Intent intent = new Intent(TrangChu_Activity.this, QuanLyThuMucActivity.class);
                startActivity(intent);
            });
        }

        // Chuyển sang trang Tạo Quiz / Nhập câu hỏi
        if (btnCreateQuiz != null) {
            btnCreateQuiz.setOnClickListener(v -> {
                Intent intent = new Intent(TrangChu_Activity.this, TaoQuizActivity.class);
                startActivity(intent);
            });
        }
    }

    private void capNhatThongKe() {
        int soLuongQuiz = boDeController.layDanhSachBoDe().size();
        int soLuongThuMuc = thuMucController.laySoLuongThuMuc();

        if (tvSubtitleMyQuiz != null) {
            tvSubtitleMyQuiz.setText(soLuongQuiz + " Quiz · 0 nháp");
        }
        if (tvSubtitleFolder != null) {
            tvSubtitleFolder.setText(soLuongThuMuc + " thư mục");
        }
    }
}

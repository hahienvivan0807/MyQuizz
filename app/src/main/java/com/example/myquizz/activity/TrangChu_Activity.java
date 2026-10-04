package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.ThuMucController;
import com.example.myquizz.SqlLite.VatPhamController;
import com.example.myquizz.adapter.VatPhamAdapter;
import com.example.myquizz.model.VatPham;

import java.util.List;

public class TrangChu_Activity extends AppCompatActivity {

    private RecyclerView rvVatPham;
    private VatPhamAdapter vatPhamAdapter;

    private VatPhamController vatPhamController;
    private ThuMucController thuMucController;

    private TextView tvSubtitleMyQuiz, tvSubtitleFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo các Controller
        vatPhamController = new VatPhamController(this);
        thuMucController = new ThuMucController(this);

        // Ánh xạ các View
        rvVatPham = findViewById(R.id.recyclerViewVatPham);
        tvSubtitleMyQuiz = findViewById(R.id.tvSubtitleMyQuiz);
        tvSubtitleFolder = findViewById(R.id.tvSubtitleFolder);

        // Cập nhật thống kê số lượng từ CSDL
        capNhatThongKe();

        // Cấu hình RecyclerView
        if (rvVatPham != null) {
            rvVatPham.setLayoutManager(new LinearLayoutManager(this));

            // Lấy danh sách tất cả Quiz từ Controller
            List<VatPham> danhSachVatPham = vatPhamController.layDanhSachVatPham();

            vatPhamAdapter = new VatPhamAdapter(this, danhSachVatPham);
            rvVatPham.setAdapter(vatPhamAdapter);
        }

        // Chuyển sang trang Quản lý thư mục
        android.view.View cardFolder = findViewById(R.id.cardFolder);
        android.view.View navFolders = findViewById(R.id.navFolders);

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
        android.view.View btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        if (btnCreateQuiz != null) {
            btnCreateQuiz.setOnClickListener(v -> {
                Intent intent = new Intent(TrangChu_Activity.this, NhapCauHoiActivity.class);
                startActivity(intent);
            });
        }
    }

    private void capNhatThongKe() {
        int soLuongQuiz = vatPhamController.laySoLuongQuiz();
        int soLuongThuMuc = thuMucController.laySoLuongThuMuc();

        if (tvSubtitleMyQuiz != null) {
            tvSubtitleMyQuiz.setText(soLuongQuiz + " Quiz · 0 nháp");
        }
        if (tvSubtitleFolder != null) {
            tvSubtitleFolder.setText(soLuongThuMuc + " thư mục");
        }
    }
}

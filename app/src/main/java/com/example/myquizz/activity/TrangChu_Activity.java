package com.example.myquizz.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.DatabaseHelper;
import com.example.myquizz.adapter.VatPhamAdapter;
import com.example.myquizz.model.VatPham;

import java.util.List;

public class TrangChu_Activity extends AppCompatActivity {

    private RecyclerView rvVatPham;
    private VatPhamAdapter vatPhamAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ RecyclerView
        rvVatPham = findViewById(R.id.recyclerViewVatPham);

        // Cấu hình RecyclerView
        if (rvVatPham != null) {
            rvVatPham.setLayoutManager(new LinearLayoutManager(this));

            // Lấy danh sách tất cả Quiz từ SQLite
            List<VatPham> danhSachVatPham = dbHelper.layDanhSachVatPham();

            vatPhamAdapter = new VatPhamAdapter(this, danhSachVatPham);
            rvVatPham.setAdapter(vatPhamAdapter);
        }
    }
}

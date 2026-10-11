package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.ThuMucController;
import com.example.myquizz.adapter.ThuMucAdapter;
import com.example.myquizz.model.ThuMuc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class QuanLyThuMucActivity extends AppCompatActivity {

    private RecyclerView recyclerViewThuMuc;
    private LinearLayout layoutEmptyState;
    private ThuMucAdapter thuMucAdapter;
    private ThuMucController thuMucController;

    private FloatingActionButton fabAddFolder;
    private LinearLayout navHome, navQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thu_muc);

        thuMucController = new ThuMucController(this);

        initViews();
        setupRecyclerView();
        setupEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách khi vừa thêm thư mục mới quay lại
        setupRecyclerView();
    }

    private void initViews() {
        recyclerViewThuMuc = findViewById(R.id.recyclerViewThuMuc);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        fabAddFolder = findViewById(R.id.fabAddFolder);

        navHome = findViewById(R.id.navFolderHome);
        navQuiz = findViewById(R.id.navFolderQuiz);
    }

    private void setupRecyclerView() {
        if (recyclerViewThuMuc != null) {
            recyclerViewThuMuc.setLayoutManager(new LinearLayoutManager(this));

            // Lấy danh sách Thư mục động từ CSDL
            List<ThuMuc> danhSachThuMuc = thuMucController.layDanhSachThuMuc();

            if (danhSachThuMuc == null || danhSachThuMuc.isEmpty()) {
                // Ẩn RecyclerView và hiển thị Giao diện Trống
                recyclerViewThuMuc.setVisibility(View.GONE);
                if (layoutEmptyState != null) {
                    layoutEmptyState.setVisibility(View.VISIBLE);
                }
            } else {
                // Hiển thị RecyclerView và ẩn Giao diện Trống
                recyclerViewThuMuc.setVisibility(View.VISIBLE);
                if (layoutEmptyState != null) {
                    layoutEmptyState.setVisibility(View.GONE);
                }

                thuMucAdapter = new ThuMucAdapter(this, danhSachThuMuc);
                recyclerViewThuMuc.setAdapter(thuMucAdapter);
            }
        }
    }

    private void setupEvents() {
        // Mở màn hình Thêm Thư Mục Mới khi bấm nút +
        if (fabAddFolder != null) {
            fabAddFolder.setOnClickListener(v -> {
                Intent intent = new Intent(QuanLyThuMucActivity.this, TamThemThuMucActivity.class);
                startActivity(intent);
            });
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

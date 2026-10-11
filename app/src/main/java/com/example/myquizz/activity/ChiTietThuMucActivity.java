package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.BoDeController;
import com.example.myquizz.adapter.BoDeAdapter;
import com.example.myquizz.model.BoDe;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ChiTietThuMucActivity extends AppCompatActivity {

    private LinearLayout btnBackAllFolders;
    private TextView tvFolderName, tvFolderDocCount;
    private EditText etSearchQuestionSet;
    private MaterialButton btnCreateQuestionSet;

    private RecyclerView recyclerViewBoDe;
    private LinearLayout layoutEmptyBoDe;

    private BoDeAdapter boDeAdapter;
    private BoDeController boDeController;

    private int currentThuMucId = 1; // ID thư mục hiện tại
    private String currentThuMucName = "Tiếng Anh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thu_muc);

        // Nhận ID và Tên thư mục từ màn hình trước
        if (getIntent() != null) {
            currentThuMucId = getIntent().getIntExtra("THU_MUC_ID", 1);
            if (getIntent().hasExtra("THU_MUC_TEN")) {
                currentThuMucName = getIntent().getStringExtra("THU_MUC_TEN");
            }
        }

        boDeController = new BoDeController(this);

        initViews();
        setupRecyclerView();
        setupEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách bộ đề khi vừa tạo xong
        setupRecyclerView();
    }

    private void initViews() {
        btnBackAllFolders = findViewById(R.id.btnBackAllFolders);
        tvFolderName = findViewById(R.id.tvFolderName);
        tvFolderDocCount = findViewById(R.id.tvFolderDocCount);

        if (tvFolderName != null) {
            tvFolderName.setText(currentThuMucName);
        }

        etSearchQuestionSet = findViewById(R.id.etSearchQuestionSet);
        btnCreateQuestionSet = findViewById(R.id.btnCreateQuestionSet);

        recyclerViewBoDe = findViewById(R.id.recyclerViewBoDeInFolder);
        layoutEmptyBoDe = findViewById(R.id.layoutEmptyBoDe);
    }

    private void setupRecyclerView() {
        if (recyclerViewBoDe != null) {
            recyclerViewBoDe.setLayoutManager(new LinearLayoutManager(this));

            // Lấy danh sách bộ đề thuộc về thư mục hiện tại từ SQLite (theo Khóa ngoại thu_muc_id)
            List<BoDe> danhSachBoDe = boDeController.layDanhSachBoDeTheoThuMuc(currentThuMucId);

            int soLuong = (danhSachBoDe != null) ? danhSachBoDe.size() : 0;
            if (tvFolderDocCount != null) {
                tvFolderDocCount.setText(soLuong + " bộ đề");
            }

            if (danhSachBoDe == null || danhSachBoDe.isEmpty()) {
                // Nếu rỗng -> Hiển thị UI/UX Chưa có bộ đề nào
                recyclerViewBoDe.setVisibility(View.GONE);
                if (layoutEmptyBoDe != null) {
                    layoutEmptyBoDe.setVisibility(View.VISIBLE);
                }
            } else {
                // Nếu có dữ liệu -> Đổ mượt mà lên RecyclerView
                recyclerViewBoDe.setVisibility(View.VISIBLE);
                if (layoutEmptyBoDe != null) {
                    layoutEmptyBoDe.setVisibility(View.GONE);
                }

                boDeAdapter = new BoDeAdapter(this, danhSachBoDe);
                recyclerViewBoDe.setAdapter(boDeAdapter);
            }
        }
    }

    private void setupEvents() {
        // Nút quay lại Tất cả thư mục
        if (btnBackAllFolders != null) {
            btnBackAllFolders.setOnClickListener(v -> finish());
        }

        // Tìm kiếm bộ câu hỏi
        if (etSearchQuestionSet != null) {
            etSearchQuestionSet.setOnEditorActionListener((v, actionId, event) -> {
                String query = etSearchQuestionSet.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
                }
                return false;
            });
        }

        // Nút Tạo bộ câu hỏi -> Mở TaoQuizActivity để chọn Chế độ (Thủ công hoặc Import)
        if (btnCreateQuestionSet != null) {
            btnCreateQuestionSet.setOnClickListener(v -> {
                Intent intent = new Intent(ChiTietThuMucActivity.this, TaoQuizActivity.class);
                intent.putExtra("THU_MUC_ID", currentThuMucId);
                startActivity(intent);
            });
        }
    }
}

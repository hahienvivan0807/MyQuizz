package com.example.myquizz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ChiTietThuMucActivity extends AppCompatActivity {

    private LinearLayout btnBackAllFolders;
    private EditText etSearchQuestionSet;
    private MaterialButton btnCreateQuestionSet;
    private MaterialCardView cardQSet1, cardQSet2, cardQSet3, cardQSet4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thu_muc);

        initViews();
        setupEvents();
    }

    private void initViews() {
        btnBackAllFolders = findViewById(R.id.btnBackAllFolders);
        etSearchQuestionSet = findViewById(R.id.etSearchQuestionSet);
        btnCreateQuestionSet = findViewById(R.id.btnCreateQuestionSet);

        cardQSet1 = findViewById(R.id.cardQSet1);
        cardQSet2 = findViewById(R.id.cardQSet2);
        cardQSet3 = findViewById(R.id.cardQSet3);
        cardQSet4 = findViewById(R.id.cardQSet4);
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

        // Nút Tạo bộ câu hỏi
        if (btnCreateQuestionSet != null) {
            btnCreateQuestionSet.setOnClickListener(v -> {
                Intent intent = new Intent(ChiTietThuMucActivity.this, NhapCauHoiActivity.class);
                startActivity(intent);
            });
        }

        // Sự kiện chọn các Bộ câu hỏi
        if (cardQSet1 != null) {
            cardQSet1.setOnClickListener(v -> 
                Toast.makeText(this, "Mở: Từ vựng: Cuộc sống hằng ngày", Toast.LENGTH_SHORT).show());
        }

        if (cardQSet2 != null) {
            cardQSet2.setOnClickListener(v -> 
                Toast.makeText(this, "Mở: Ngữ pháp: Thì hiện tại đơn", Toast.LENGTH_SHORT).show());
        }

        if (cardQSet3 != null) {
            cardQSet3.setOnClickListener(v -> 
                Toast.makeText(this, "Mở: Giao tiếp: Chào hỏi cơ bản", Toast.LENGTH_SHORT).show());
        }

        if (cardQSet4 != null) {
            cardQSet4.setOnClickListener(v -> 
                Toast.makeText(this, "Mở: Ôn tập: Tiếng Anh A2", Toast.LENGTH_SHORT).show());
        }
    }
}

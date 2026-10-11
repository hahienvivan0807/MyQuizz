package com.example.myquizz.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.BoDeController;
import com.example.myquizz.SqlLite.CauHoiController;
import com.example.myquizz.adapter.DuyetCauHoiAiAdapter;
import com.example.myquizz.model.BoDe;
import com.example.myquizz.model.CauHoi;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DuyetCauHoiAiActivity extends AppCompatActivity {

    public static List<CauHoi> cauHoiAiTemporaryList = new ArrayList<>();

    private ImageView btnBackDuyetAi;
    private TextView tvAiCountSub;
    private MaterialButton btnAddManualInAi, btnSaveAiQuizSet;
    private RecyclerView rvDuyetCauHoiAi;

    private DuyetCauHoiAiAdapter duyetAdapter;
    private BoDeController boDeController;
    private CauHoiController cauHoiController;

    private int currentThuMucId = 1;
    private String boDeTitle = "Quiz từ AI Gemini";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tam_activity_duyet_cau_hoi_ai);

        if (getIntent() != null) {
            currentThuMucId = getIntent().getIntExtra("THU_MUC_ID", 1);
            if (getIntent().hasExtra("BO_DE_TITLE")) {
                boDeTitle = getIntent().getStringExtra("BO_DE_TITLE");
            }
        }

        boDeController = new BoDeController(this);
        cauHoiController = new CauHoiController(this);

        initViews();
        setupRecyclerView();
        setupEvents();
    }

    private void initViews() {
        btnBackDuyetAi = findViewById(R.id.btnBackDuyetAi);
        tvAiCountSub = findViewById(R.id.tvAiCountSub);
        btnAddManualInAi = findViewById(R.id.btnAddManualInAi);
        btnSaveAiQuizSet = findViewById(R.id.btnSaveAiQuizSet);
        rvDuyetCauHoiAi = findViewById(R.id.rvDuyetCauHoiAi);
    }

    private void setupRecyclerView() {
        List<CauHoi> currentList = cauHoiAiTemporaryList;
        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        updateCountSub(currentList.size());

        rvDuyetCauHoiAi.setLayoutManager(new LinearLayoutManager(this));
        duyetAdapter = new DuyetCauHoiAiAdapter(this, currentList, this::updateCountSub);
        rvDuyetCauHoiAi.setAdapter(duyetAdapter);
    }

    private void updateCountSub(int count) {
        if (tvAiCountSub != null) {
            tvAiCountSub.setText("AI đã tạo " + count + " câu hỏi · Bạn có thể sửa trực tiếp");
        }
    }

    private void setupEvents() {
        if (btnBackDuyetAi != null) {
            btnBackDuyetAi.setOnClickListener(v -> finish());
        }

        // Nút thêm 1 câu hỏi mới thủ công trong màn hình kiểm soát AI
        if (btnAddManualInAi != null) {
            btnAddManualInAi.setOnClickListener(v -> {
                if (duyetAdapter != null) {
                    CauHoi cauHoiMoi = new CauHoi(0, "Nội dung câu hỏi mới...", "Đáp án đúng", "Đáp án sai 1", "Đáp án sai 2", "Đáp án sai 3");
                    duyetAdapter.addNewQuestion(cauHoiMoi);
                    rvDuyetCauHoiAi.smoothScrollToPosition(duyetAdapter.getItemCount() - 1);
                }
            });
        }

        // Nút Xác Nhận & Lưu Vào CSDL SQLite
        if (btnSaveAiQuizSet != null) {
            btnSaveAiQuizSet.setOnClickListener(v -> luuBoDeVaoSqlite());
        }
    }

    private void luuBoDeVaoSqlite() {
        if (duyetAdapter == null) return;

        List<CauHoi> finalQuestions = duyetAdapter.getUpdatedList();
        if (finalQuestions == null || finalQuestions.isEmpty()) {
            Toast.makeText(this, "Danh sách câu hỏi trống! Vui lòng giữ ít nhất 1 câu hỏi.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tính hợp lệ của các câu hỏi sau khi người dùng chỉnh sửa
        for (int i = 0; i < finalQuestions.size(); i++) {
            CauHoi q = finalQuestions.get(i);
            if (q.getNoiDungCauHoi() == null || q.getNoiDungCauHoi().trim().isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung cho Câu " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }
            if (q.getDapAnDung() == null || q.getDapAnDung().trim().isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đáp án đúng cho Câu " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int totalCount = finalQuestions.size();

        // 1. Lưu Bộ đề vào SQLite
        BoDe boDeMoi = new BoDe(currentThuMucId, boDeTitle, totalCount);
        long newBoDeId = boDeController.themBoDe(boDeMoi);

        if (newBoDeId != -1) {
            int boDeId = (int) newBoDeId;

            // 2. Gán ID Bộ Đề cho từng câu hỏi
            for (CauHoi q : finalQuestions) {
                q.setBoDeId(boDeId);
            }

            // 3. Thêm hàng loạt câu hỏi vào CSDL bằng Transaction
            boolean isSaved = cauHoiController.ThemHangLoatCauHoi(finalQuestions);

            if (isSaved) {
                // 4. LỆNH ĐẾM: Đếm lại số lượng câu hỏi thực tế trong CSDL
                int soCauThucTe = cauHoiController.demSoLuongCauHoiTheoBoDe(boDeId);
                boDeController.capNhatSoLuongCauHoi(boDeId, soCauThucTe);

                Toast.makeText(this, "Lưu thành công bộ đề '" + boDeTitle + "' với " + soCauThucTe + " câu hỏi!", Toast.LENGTH_LONG).show();

                cauHoiAiTemporaryList.clear();
                finish(); // Quay lại màn hình trước
            } else {
                Toast.makeText(this, "Thêm câu hỏi vào CSDL thất bại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tạo bộ đề thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}

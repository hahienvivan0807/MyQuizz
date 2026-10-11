package com.example.myquizz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.CauHoiController;
import com.example.myquizz.model.CauHoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LamBaiActivity extends AppCompatActivity {

    private ImageView btnBack, btnMoreOptions;
    private TextView tvHeaderTitle, tvCategory, tvQuizTitle;
    private TextView tvQuestionProgress, tvAnsweredCount;
    private ProgressBar pbQuestionProgress;

    private TextView tvQuestionNumberTag, tvQuestionContent, tvInstruction;

    private LinearLayout layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD;
    private TextView tvBadgeA, tvBadgeB, tvBadgeC, tvBadgeD;
    private TextView tvOptionTextA, tvOptionTextB, tvOptionTextC, tvOptionTextD;
    private ImageView ivRadioA, ivRadioB, ivRadioC, ivRadioD;

    private LinearLayout btnPrevious, btnNext, btnSubmit;
    private TextView tvSubmitText;

    private CauHoiController cauHoiController;
    private List<CauHoi> danhSachCauHoi = new ArrayList<>();

    // Map lưu lựa chọn của người dùng cho từng câu hỏi (Key: Question Index, Value: Option Index 0,1,2,3)
    private Map<Integer, Integer> userSelections = new HashMap<>();

    // Map lưu trữ 4 lựa chọn [A, B, C, D] cố định cho từng câu hỏi
    private Map<Integer, List<String>> questionOptionsMap = new HashMap<>();

    private int currentQuestionIndex = 0;
    private int boDeId = 1;
    private String boDeTitle = "Bài Kiểm Tra Quiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SỬ DỤNG FILE LAYOUT TRẮC NGHIỆM MỚI TỪ GITHUB
        setContentView(R.layout.activity_lam_bai_trac_nghiem);

        if (getIntent() != null) {
            boDeId = getIntent().getIntExtra("BO_DE_ID", 1);
            if (getIntent().hasExtra("BO_DE_TIEU_DE")) {
                boDeTitle = getIntent().getStringExtra("BO_DE_TIEU_DE");
            }
        }

        cauHoiController = new CauHoiController(this);

        initViews();
        loadQuizData();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnMoreOptions = findViewById(R.id.btnMoreOptions);

        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvCategory = findViewById(R.id.tvCategory);
        tvQuizTitle = findViewById(R.id.tvQuizTitle);

        tvQuestionProgress = findViewById(R.id.tvQuestionProgress);
        tvAnsweredCount = findViewById(R.id.tvAnsweredCount);
        pbQuestionProgress = findViewById(R.id.pbQuestionProgress);

        tvQuestionNumberTag = findViewById(R.id.tvQuestionNumberTag);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);
        tvInstruction = findViewById(R.id.tvInstruction);

        layoutOptionA = findViewById(R.id.layoutOptionA);
        layoutOptionB = findViewById(R.id.layoutOptionB);
        layoutOptionC = findViewById(R.id.layoutOptionC);
        layoutOptionD = findViewById(R.id.layoutOptionD);

        tvBadgeA = findViewById(R.id.tvBadgeA);
        tvBadgeB = findViewById(R.id.tvBadgeB);
        tvBadgeC = findViewById(R.id.tvBadgeC);
        tvBadgeD = findViewById(R.id.tvBadgeD);

        tvOptionTextA = findViewById(R.id.tvOptionTextA);
        tvOptionTextB = findViewById(R.id.tvOptionTextB);
        tvOptionTextC = findViewById(R.id.tvOptionTextC);
        tvOptionTextD = findViewById(R.id.tvOptionTextD);

        ivRadioA = findViewById(R.id.ivRadioA);
        ivRadioB = findViewById(R.id.ivRadioB);
        ivRadioC = findViewById(R.id.ivRadioC);
        ivRadioD = findViewById(R.id.ivRadioD);

        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSubmitText = findViewById(R.id.tvSubmitText);

        if (tvQuizTitle != null) {
            tvQuizTitle.setText(boDeTitle);
        }
    }

    private void loadQuizData() {
        danhSachCauHoi = cauHoiController.layDanhSachCauHoiTheoBoDe(boDeId);

        if (danhSachCauHoi == null || danhSachCauHoi.isEmpty()) {
            Toast.makeText(this, "Bộ đề này chưa có câu hỏi nào!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Tạo danh sách 4 đáp án cho từng câu hỏi
        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            CauHoi q = danhSachCauHoi.get(i);
            List<String> opts = Arrays.asList(
                    q.getDapAnDung(),
                    q.getDapAnSai1(),
                    q.getDapAnSai2(),
                    q.getDapAnSai3()
            );
            questionOptionsMap.put(i, opts);
        }

        currentQuestionIndex = 0;
        displayQuestionAt(currentQuestionIndex);
    }

    private void displayQuestionAt(int index) {
        if (index < 0 || index >= danhSachCauHoi.size()) return;

        CauHoi q = danhSachCauHoi.get(index);
        List<String> options = questionOptionsMap.get(index);

        int total = danhSachCauHoi.size();

        // 1. Cập nhật thẻ chỉ số & tiêu đề
        if (tvQuestionProgress != null) {
            tvQuestionProgress.setText("Câu " + (index + 1) + " / " + total);
        }

        if (tvAnsweredCount != null) {
            tvAnsweredCount.setText("Đã trả lời " + userSelections.size() + " câu");
        }

        if (pbQuestionProgress != null) {
            int percent = (int) (((float) userSelections.size() / total) * 100);
            pbQuestionProgress.setProgress(percent);
        }

        if (tvQuestionNumberTag != null) {
            tvQuestionNumberTag.setText(String.format("CÂU %02d", index + 1));
        }

        if (tvQuestionContent != null) {
            tvQuestionContent.setText(q.getNoiDungCauHoi());
        }

        // 2. Hiển thị 4 lựa chọn đáp án
        if (options != null && options.size() >= 4) {
            tvOptionTextA.setText(options.get(0));
            tvOptionTextB.setText(options.get(1));
            tvOptionTextC.setText(options.get(2));
            tvOptionTextD.setText(options.get(3));
        }

        // 3. Highlight đáp án đã chọn (nếu có)
        int selectedOpt = userSelections.containsKey(index) ? userSelections.get(index) : -1;
        updateOptionUi(layoutOptionA, tvBadgeA, tvOptionTextA, ivRadioA, selectedOpt == 0);
        updateOptionUi(layoutOptionB, tvBadgeB, tvOptionTextB, ivRadioB, selectedOpt == 1);
        updateOptionUi(layoutOptionC, tvBadgeC, tvOptionTextC, ivRadioC, selectedOpt == 2);
        updateOptionUi(layoutOptionD, tvBadgeD, tvOptionTextD, ivRadioD, selectedOpt == 3);

        // 4. Cấu hình nút Câu trước / Câu tiếp theo
        if (btnPrevious != null) {
            btnPrevious.setVisibility(index == 0 ? View.INVISIBLE : View.VISIBLE);
        }

        if (btnNext != null) {
            btnNext.setVisibility(index == total - 1 ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private void selectOption(int optionIndex) {
        userSelections.put(currentQuestionIndex, optionIndex);
        displayQuestionAt(currentQuestionIndex);
    }

    private void updateOptionUi(LinearLayout layout, TextView tvBadge, TextView tvText, ImageView ivRadio, boolean isSelected) {
        if (isSelected) {
            layout.setBackgroundResource(R.drawable.bg_option_selected);
            tvBadge.setBackgroundResource(R.drawable.bg_badge_selected);
            tvBadge.setTextColor(getColor(R.color.white));
            tvText.setTextColor(getColor(R.color.blue_primary));
            ivRadio.setImageResource(R.drawable.ic_radio_selected);
        } else {
            layout.setBackgroundResource(R.drawable.bg_option_unselected);
            tvBadge.setBackgroundResource(R.drawable.bg_badge_unselected);
            tvBadge.setTextColor(getColor(R.color.text_secondary));
            tvText.setTextColor(getColor(R.color.text_primary));
            ivRadio.setImageResource(R.drawable.ic_radio_unselected);
        }
    }

    private void setupEvents() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Chọn Đáp án A
        if (layoutOptionA != null) {
            layoutOptionA.setOnClickListener(v -> selectOption(0));
        }
        // Chọn Đáp án B
        if (layoutOptionB != null) {
            layoutOptionB.setOnClickListener(v -> selectOption(1));
        }
        // Chọn Đáp án C
        if (layoutOptionC != null) {
            layoutOptionC.setOnClickListener(v -> selectOption(2));
        }
        // Chọn Đáp án D
        if (layoutOptionD != null) {
            layoutOptionD.setOnClickListener(v -> selectOption(3));
        }

        // Nút Câu trước
        if (btnPrevious != null) {
            btnPrevious.setOnClickListener(v -> {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestionAt(currentQuestionIndex);
                }
            });
        }

        // Nút Câu tiếp theo
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (currentQuestionIndex < danhSachCauHoi.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestionAt(currentQuestionIndex);
                }
            });
        }

        // CƠ CHẾ NỘP BÀI: Chỉ cho nộp bài khi đã trả lời TẤT CẢ CÂU HỎI
        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                int total = danhSachCauHoi.size();
                int answered = userSelections.size();

                if (answered < total) {
                    int remaining = total - answered;
                    Toast.makeText(this, "Bạn chưa hoàn thành tất cả câu hỏi! Còn " + remaining + " câu chưa trả lời.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Tính điểm khi đã hoàn thành đủ tất cả câu hỏi
                int correctCount = 0;
                for (int i = 0; i < total; i++) {
                    if (userSelections.containsKey(i)) {
                        int selectedOpt = userSelections.get(i);
                        List<String> options = questionOptionsMap.get(i);
                        if (options != null && selectedOpt < options.size()) {
                            String selectedText = options.get(selectedOpt);
                            String correctAnswer = danhSachCauHoi.get(i).getDapAnDung();
                            if (correctAnswer != null && correctAnswer.equals(selectedText)) {
                                correctCount++;
                            }
                        }
                    }
                }

                double diemSo = (correctCount * 10.0) / total;

                new AlertDialog.Builder(this)
                        .setTitle("Kết Quả Làm Bài")
                        .setMessage("Chúc mừng bạn đã hoàn thành bài quiz!\n\n- Số câu trả lời đúng: " + correctCount + "/" + total + " câu\n- Điểm số: " + String.format("%.1f", diemSo) + " / 10")
                        .setPositiveButton("Hoàn thành", (dialog, which) -> finish())
                        .setCancelable(false)
                        .show();
            });
        }
    }
}

package com.example.myquizz.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.google.android.material.button.MaterialButton;

public class NhapCauHoiActivity extends AppCompatActivity {

    private LinearLayout btnBackCreateSet;
    private EditText etQuestionContent, etAnswerA, etAnswerB, etAnswerC, etAnswerD;
    private CheckBox cbAnswerA, cbAnswerB, cbAnswerC, cbAnswerD;
    private MaterialButton btnAddMoreQuestion, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_cau_hoi);

        initViews();
        setupEvents();
    }

    private void initViews() {
        btnBackCreateSet = findViewById(R.id.btnBackCreateSet);

        etQuestionContent = findViewById(R.id.etQuestionContent);
        etAnswerA = findViewById(R.id.etAnswerA);
        etAnswerB = findViewById(R.id.etAnswerB);
        etAnswerC = findViewById(R.id.etAnswerC);
        etAnswerD = findViewById(R.id.etAnswerD);

        cbAnswerA = findViewById(R.id.cbAnswerA);
        cbAnswerB = findViewById(R.id.cbAnswerB);
        cbAnswerC = findViewById(R.id.cbAnswerC);
        cbAnswerD = findViewById(R.id.cbAnswerD);

        btnAddMoreQuestion = findViewById(R.id.btnAddMoreQuestion);
        btnFinish = findViewById(R.id.btnFinish);
    }

    private void setupEvents() {
        // Nút quay lại
        if (btnBackCreateSet != null) {
            btnBackCreateSet.setOnClickListener(v -> finish());
        }

        // Đảm bảo chỉ chọn 1 đáp án đúng duy nhất (Single choice)
        setupSingleChoiceAnswers();

        // Nút Tạo thêm câu hỏi
        if (btnAddMoreQuestion != null) {
            btnAddMoreQuestion.setOnClickListener(v -> 
                Toast.makeText(this, "Đã thêm câu hỏi tiếp theo!", Toast.LENGTH_SHORT).show());
        }

        // Nút Hoàn thành
        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> {
                Toast.makeText(this, "Lưu bộ câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    private void setupSingleChoiceAnswers() {
        cbAnswerA.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbAnswerB.setChecked(false);
                cbAnswerC.setChecked(false);
                cbAnswerD.setChecked(false);
            }
        });

        cbAnswerB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbAnswerA.setChecked(false);
                cbAnswerC.setChecked(false);
                cbAnswerD.setChecked(false);
            }
        });

        cbAnswerC.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbAnswerA.setChecked(false);
                cbAnswerB.setChecked(false);
                cbAnswerD.setChecked(false);
            }
        });

        cbAnswerD.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbAnswerA.setChecked(false);
                cbAnswerB.setChecked(false);
                cbAnswerC.setChecked(false);
            }
        });
    }
}

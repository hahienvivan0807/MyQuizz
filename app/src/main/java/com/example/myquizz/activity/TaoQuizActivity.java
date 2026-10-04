package com.example.myquizz.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class TaoQuizActivity extends AppCompatActivity {

    private ImageView btnBack;
    private MaterialCardView cardCreateManual, cardImportDoc;
    private RadioButton radioManual, radioImport;
    private MaterialButton btnContinue;

    private boolean isManualMode = true; // Mặc định chọn tạo thủ công

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_quiz);

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack);
        cardCreateManual = findViewById(R.id.cardCreateManual);
        cardImportDoc = findViewById(R.id.cardImportDoc);
        radioManual = findViewById(R.id.radioManual);
        radioImport = findViewById(R.id.radioImport);
        btnContinue = findViewById(R.id.btnContinue);

        // Nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Chọn chế độ Tạo thủ công
        if (cardCreateManual != null) {
            cardCreateManual.setOnClickListener(v -> selectManualMode());
        }

        // Chọn chế độ Import tài liệu
        if (cardImportDoc != null) {
            cardImportDoc.setOnClickListener(v -> selectImportMode());
        }

        // Nút Tiếp tục
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                if (isManualMode) {
                    Toast.makeText(this, "Chuyển sang màn hình Soạn Quiz Thủ Công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Chuyển sang màn hình Import Tài Liệu", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void selectManualMode() {
        isManualMode = true;
        radioManual.setChecked(true);
        radioImport.setChecked(false);

        // Đổi màu viền card
        cardCreateManual.setStrokeColor(getColor(R.color.primary));
        cardCreateManual.setStrokeWidth(6);

        cardImportDoc.setStrokeColor(getColor(R.color.border_stroke));
        cardImportDoc.setStrokeWidth(3);
    }

    private void selectImportMode() {
        isManualMode = false;
        radioManual.setChecked(false);
        radioImport.setChecked(true);

        // Đổi màu viền card
        cardImportDoc.setStrokeColor(getColor(R.color.primary));
        cardImportDoc.setStrokeWidth(6);

        cardCreateManual.setStrokeColor(getColor(R.color.border_stroke));
        cardCreateManual.setStrokeWidth(3);
    }
}

package com.example.myquizz.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.example.myquizz.helper.GeminiAiHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ImportTaiLieuActivity extends AppCompatActivity {

    private ImageView btnBackImport, btnRemoveFile, btnSettingsApiKey;
    private MaterialCardView cardUploadArea, cardSelectedFilePreview;
    private MaterialButton btnSelectFile, btnStartExtract;
    private TextView tvSelectedFileName, tvSelectedFileInfo;
    private EditText etImportTitle;
    private Chip chip10, chip15, chip20;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    private Uri selectedFileUri = null;
    private String selectedFileName = "";
    private long selectedFileSize = 0;
    private int currentThuMucId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tam_activity_import_tai_lieu);

        if (getIntent() != null) {
            currentThuMucId = getIntent().getIntExtra("THU_MUC_ID", 1);
        }

        initViews();
        registerFilePickerLauncher();
        setupEvents();
    }

    private void initViews() {
        btnBackImport = findViewById(R.id.btnBackImport);
        btnRemoveFile = findViewById(R.id.btnRemoveFile);
        btnSettingsApiKey = findViewById(R.id.btnSettingsApiKey);

        cardUploadArea = findViewById(R.id.cardUploadArea);
        cardSelectedFilePreview = findViewById(R.id.cardSelectedFilePreview);

        btnSelectFile = findViewById(R.id.btnSelectFile);
        btnStartExtract = findViewById(R.id.btnStartExtract);

        tvSelectedFileName = findViewById(R.id.tvSelectedFileName);
        tvSelectedFileInfo = findViewById(R.id.tvSelectedFileInfo);
        etImportTitle = findViewById(R.id.etImportTitle);

        chip10 = findViewById(R.id.chip10);
        chip15 = findViewById(R.id.chip15);
        chip20 = findViewById(R.id.chip20);
    }

    private void registerFilePickerLauncher() {
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            xuLyTepTaiLieuThat(uri);
                        }
                    }
                }
        );
    }

    private void setupEvents() {
        // Quay lại
        if (btnBackImport != null) {
            btnBackImport.setOnClickListener(v -> finish());
        }

        // Cài đặt Gemini API Key
        if (btnSettingsApiKey != null) {
            btnSettingsApiKey.setOnClickListener(v -> hienDialogApiKey());
        }

        // Chọn tệp
        if (btnSelectFile != null) {
            btnSelectFile.setOnClickListener(v -> moTrinhChonTepThat());
        }

        // Hủy chọn tệp
        if (btnRemoveFile != null) {
            btnRemoveFile.setOnClickListener(v -> huyChonTep());
        }

        // Trích xuất bằng AI & Chuyển sang màn hình Kiểm Soát/Sửa
        if (btnStartExtract != null) {
            btnStartExtract.setOnClickListener(v -> batDauTrichXuatVoiGeminiAi());
        }
    }

    /**
     * HIỂN THỊ DIALOG CÀI ĐẶT GEMINI API KEY
     */
    private void hienDialogApiKey() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.tam_dialog_api_key, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText etKey = dialogView.findViewById(R.id.etApiKeyInput);
        MaterialButton btnSave = dialogView.findViewById(R.id.btnSaveApiKey);

        String currentKey = GeminiAiHelper.getApiKey(this);
        if (etKey != null && !currentKey.isEmpty()) {
            etKey.setText(currentKey);
        }

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                String newKey = etKey.getText().toString().trim();
                GeminiAiHelper.saveApiKey(this, newKey);
                Toast.makeText(this, "Đã lưu Gemini API Key thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        }

        dialog.show();
    }

    private void moTrinhChonTepThat() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        String[] mimeTypes = {
                "application/pdf",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/msword",
                "text/plain"
        };
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        try {
            filePickerLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở trình chọn tệp!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyTepTaiLieuThat(Uri uri) {
        selectedFileUri = uri;
        selectedFileName = "Tài_Liệu_Soạn_Đề";
        selectedFileSize = 0;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                    if (nameIndex != -1) {
                        selectedFileName = cursor.getString(nameIndex);
                    }
                    if (sizeIndex != -1) {
                        selectedFileSize = cursor.getLong(sizeIndex);
                    }
                }
            } finally {
                cursor.close();
            }
        }

        if (cardUploadArea != null) {
            cardUploadArea.setVisibility(View.GONE);
        }
        if (cardSelectedFilePreview != null) {
            cardSelectedFilePreview.setVisibility(View.VISIBLE);
        }

        if (tvSelectedFileName != null) {
            tvSelectedFileName.setText(selectedFileName);
        }

        if (tvSelectedFileInfo != null) {
            String sizeStr = dinhDangDungLuongTep(selectedFileSize);
            tvSelectedFileInfo.setText(sizeStr + " · Sẵn sàng trích xuất");
        }

        if (etImportTitle != null && (etImportTitle.getText().toString().trim().isEmpty())) {
            String cleanTitle = selectedFileName;
            int dotIndex = cleanTitle.lastIndexOf('.');
            if (dotIndex > 0) {
                cleanTitle = cleanTitle.substring(0, dotIndex);
            }
            cleanTitle = cleanTitle.replace('_', ' ').replace('-', ' ');
            etImportTitle.setText(cleanTitle);
        }

        Toast.makeText(this, "Đã chọn tệp: " + selectedFileName, Toast.LENGTH_SHORT).show();
    }

    private void huyChonTep() {
        selectedFileUri = null;
        selectedFileName = "";
        selectedFileSize = 0;

        if (cardSelectedFilePreview != null) {
            cardSelectedFilePreview.setVisibility(View.GONE);
        }
        if (cardUploadArea != null) {
            cardUploadArea.setVisibility(View.VISIBLE);
        }

        Toast.makeText(this, "Đã hủy chọn tệp", Toast.LENGTH_SHORT).show();
    }

    /**
     * TRÍCH XUẤT CÂU HỎI BẰNG GEMINI 1.5/2.0 FLASHLITE VÀ CHUYỂN SANG TRANG KIỂM SOÁT/SỬA
     */
    private void batDauTrichXuatVoiGeminiAi() {
        if (selectedFileUri == null) {
            Toast.makeText(this, "Vui lòng chọn 1 tệp tài liệu trước khi trích xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tieuDeBoDe = etImportTitle.getText().toString().trim();
        if (tieuDeBoDe.isEmpty()) {
            tieuDeBoDe = selectedFileName.isEmpty() ? "Quiz từ tài liệu AI" : selectedFileName;
        }

        int targetQuestionCount = 10;
        if (chip15 != null && chip15.isChecked()) {
            targetQuestionCount = 15;
        } else if (chip20 != null && chip20.isChecked()) {
            targetQuestionCount = 20;
        }

        List<String> realLines = docNoiDungTextTuUri(selectedFileUri);
        StringBuilder docContentBuilder = new StringBuilder();
        for (String line : realLines) {
            docContentBuilder.append(line).append("\n");
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gemini AI đang phân tích tài liệu & khởi tạo " + targetQuestionCount + " câu hỏi...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String finalTitle = tieuDeBoDe;
        GeminiAiHelper.generateQuestionsWithGemini(this, docContentBuilder.toString(), targetQuestionCount, finalTitle, new GeminiAiHelper.AiCallback() {
            @Override
            public void onSuccess(List<com.example.myquizz.model.CauHoi> questions) {
                progressDialog.dismiss();

                // Lưu vào danh sách tạm của trang kiểm soát/sửa
                DuyetCauHoiAiActivity.cauHoiAiTemporaryList = questions;

                Intent intent = new Intent(ImportTaiLieuActivity.this, DuyetCauHoiAiActivity.class);
                intent.putExtra("THU_MUC_ID", currentThuMucId);
                intent.putExtra("BO_DE_TITLE", finalTitle);
                startActivity(intent);

                finish(); // Đóng trang Import
            }

            @Override
            public void onError(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(ImportTaiLieuActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<String> docNoiDungTextTuUri(Uri uri) {
        List<String> lines = new ArrayList<>();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        lines.add(line);
                    }
                }
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    private String dinhDangDungLuongTep(long sizeBytes) {
        if (sizeBytes <= 0) return "Tài liệu";
        double kb = sizeBytes / 1024.0;
        if (kb < 1024) {
            return String.format("%.1f KB", kb);
        }
        double mb = kb / 1024.0;
        return String.format("%.1f MB", mb);
    }
}

package com.example.myquizz.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.example.myquizz.SqlLite.BoDeController;
import com.example.myquizz.SqlLite.CauHoiController;
import com.example.myquizz.model.BoDe;
import com.example.myquizz.model.CauHoi;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class NhapCauHoiActivity extends AppCompatActivity {

    private LinearLayout btnBackCreateSet;
    private EditText etBoDeTitle;
    private LinearLayout layoutQuestionsContainer;
    private ScrollView scrollViewContent;
    private MaterialButton btnAddMoreQuestion, btnFinish;

    private CauHoiController cauHoiController;
    private BoDeController boDeController;

    // Danh sách các View khối câu hỏi đã tạo động trên giao diện
    private List<View> questionCardViews = new ArrayList<>();

    private int currentThuMucId = 1; // ID thư mục nhận từ Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_cau_hoi);

        // Nhận THU_MUC_ID từ Intent (truyền từ ChiTietThuMucActivity sang)
        if (getIntent() != null) {
            currentThuMucId = getIntent().getIntExtra("THU_MUC_ID", 1);
        }

        cauHoiController = new CauHoiController(this);
        boDeController = new BoDeController(this);

        initViews();
        setupEvents();

        // Tạo sẵn Khối Câu hỏi thứ 1 ban đầu
        themKhoiCauHoiMoi();
    }

    private void initViews() {
        btnBackCreateSet = findViewById(R.id.btnBackCreateSet);
        etBoDeTitle = findViewById(R.id.etBoDeTitle);
        layoutQuestionsContainer = findViewById(R.id.layoutQuestionsContainer);
        scrollViewContent = findViewById(R.id.scrollViewContent);

        btnAddMoreQuestion = findViewById(R.id.btnAddMoreQuestion);
        btnFinish = findViewById(R.id.btnFinish);
    }

    private void setupEvents() {
        // Nút quay lại
        if (btnBackCreateSet != null) {
            btnBackCreateSet.setOnClickListener(v -> finish());
        }

        // Nút "+ Tạo thêm câu hỏi": kiểm tra, lưu mảng tạm qua LuucauhoivaomangTam và tạo thêm khối mới
        if (btnAddMoreQuestion != null) {
            btnAddMoreQuestion.setOnClickListener(v -> {
                if (luuToanBoCauHoiVaoMangTam(false)) {
                    themKhoiCauHoiMoi();
                    Toast.makeText(this, "Đã thêm khối câu hỏi mới! (Tổng số: " + questionCardViews.size() + " câu)", Toast.LENGTH_SHORT).show();

                    // Cuộn mượt xuống khối câu hỏi mới tạo
                    if (scrollViewContent != null) {
                        scrollViewContent.post(() -> scrollViewContent.fullScroll(View.FOCUS_DOWN));
                    }
                }
            });
        }

        // Nút "Hoàn thành": Lưu toàn bộ vào SQLite
        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> hoanThanhTaoBoDe());
        }
    }

    /**
     * DỰNG ĐỘNG 1 KHỐI CÂU HỎI MỚI (CÓ RADIO BUTTONS VÀ ĐẦY ĐỦ UI/UX)
     */
    private void themKhoiCauHoiMoi() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.tam_item_question_input_card, layoutQuestionsContainer, false);

        int questionNum = questionCardViews.size() + 1;
        TextView tvQuestionNumber = cardView.findViewById(R.id.tvQuestionNumber);
        if (tvQuestionNumber != null) {
            tvQuestionNumber.setText("Câu " + questionNum);
        }

        ImageView btnRemoveQuestion = cardView.findViewById(R.id.btnRemoveQuestion);
        if (btnRemoveQuestion != null) {
            if (questionNum > 1) {
                btnRemoveQuestion.setVisibility(View.VISIBLE);
                btnRemoveQuestion.setOnClickListener(v -> {
                    layoutQuestionsContainer.removeView(cardView);
                    questionCardViews.remove(cardView);
                    capNhatLaiSoThuTuCauHoi();
                });
            } else {
                btnRemoveQuestion.setVisibility(View.GONE);
            }
        }

        RadioButton rbA = cardView.findViewById(R.id.rbAnswerA);
        RadioButton rbB = cardView.findViewById(R.id.rbAnswerB);
        RadioButton rbC = cardView.findViewById(R.id.rbAnswerC);
        RadioButton rbD = cardView.findViewById(R.id.rbAnswerD);

        // Thiết lập sự kiện chọn RadioButton duy nhất (Single Choice UI/UX đẹp mắt)
        setupRadioGroupSingleChoice(rbA, rbB, rbC, rbD);

        layoutQuestionsContainer.addView(cardView);
        questionCardViews.add(cardView);
    }

    /**
     * ĐẢM BẢO CHỈ 1 RADIO BUTTON ĐƯỢC CHỌN TRONG 1 KHỐI CÂU HỎI
     */
    private void setupRadioGroupSingleChoice(RadioButton rbA, RadioButton rbB, RadioButton rbC, RadioButton rbD) {
        rbA.setOnClickListener(v -> {
            rbB.setChecked(false);
            rbC.setChecked(false);
            rbD.setChecked(false);
        });

        rbB.setOnClickListener(v -> {
            rbA.setChecked(false);
            rbC.setChecked(false);
            rbD.setChecked(false);
        });

        rbC.setOnClickListener(v -> {
            rbA.setChecked(false);
            rbB.setChecked(false);
            rbD.setChecked(false);
        });

        rbD.setOnClickListener(v -> {
            rbA.setChecked(false);
            rbB.setChecked(false);
            rbC.setChecked(false);
        });
    }

    /**
     * CẬP NHẬT LẠI SỐ THỨ TỰ "CÂU 1", "CÂU 2"... KHI XÓA 1 KHỐI
     */
    private void capNhatLaiSoThuTuCauHoi() {
        for (int i = 0; i < questionCardViews.size(); i++) {
            View card = questionCardViews.get(i);
            TextView tvNum = card.findViewById(R.id.tvQuestionNumber);
            if (tvNum != null) {
                tvNum.setText("Câu " + (i + 1));
            }
            ImageView btnRemove = card.findViewById(R.id.btnRemoveQuestion);
            if (btnRemove != null) {
                btnRemove.setVisibility(i > 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * DUYỆT TẤT CẢ KHỐI CÂU HỎI TRÊN GIAO DIỆN VÀ LƯU VÀO MẢNG TẠM CỦA CONTROLLER VIA LuucauhoivaomangTam
     */
    private boolean luuToanBoCauHoiVaoMangTam(boolean allowIncompleteLastCard) {
        // Xóa sạch mảng tạm cũ trên Controller trước khi quét lại
        cauHoiController.xoaDanhSachCauHoiTam();

        for (int i = 0; i < questionCardViews.size(); i++) {
            View card = questionCardViews.get(i);

            EditText etNoiDung = card.findViewById(R.id.etQuestionContent);
            EditText etA = card.findViewById(R.id.etAnswerA);
            EditText etB = card.findViewById(R.id.etAnswerB);
            EditText etC = card.findViewById(R.id.etAnswerC);
            EditText etD = card.findViewById(R.id.etAnswerD);

            RadioButton rbA = card.findViewById(R.id.rbAnswerA);
            RadioButton rbB = card.findViewById(R.id.rbAnswerB);
            RadioButton rbC = card.findViewById(R.id.rbAnswerC);
            RadioButton rbD = card.findViewById(R.id.rbAnswerD);

            String noiDung = etNoiDung.getText().toString().trim();
            String ansA = etA.getText().toString().trim();
            String ansB = etB.getText().toString().trim();
            String ansC = etC.getText().toString().trim();
            String ansD = etD.getText().toString().trim();

            boolean isLast = (i == questionCardViews.size() - 1);

            // Nếu cho phép thẻ cuối rỗng (khi bấm Hoàn thành) và thẻ cuối hoàn toàn trống -> Bỏ qua thẻ cuối
            if (allowIncompleteLastCard && isLast && noiDung.isEmpty() && ansA.isEmpty() && ansB.isEmpty() && ansC.isEmpty() && ansD.isEmpty()) {
                continue;
            }

            int questionIndexDisplay = i + 1;

            if (noiDung.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung cho Câu " + questionIndexDisplay, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (ansA.isEmpty() || ansB.isEmpty() || ansC.isEmpty() || ansD.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ 4 đáp án cho Câu " + questionIndexDisplay, Toast.LENGTH_SHORT).show();
                return false;
            }

            String dapAnDung = "";
            List<String> dapAnSai = new ArrayList<>();

            if (rbA.isChecked()) {
                dapAnDung = ansA;
                dapAnSai.add(ansB);
                dapAnSai.add(ansC);
                dapAnSai.add(ansD);
            } else if (rbB.isChecked()) {
                dapAnDung = ansB;
                dapAnSai.add(ansA);
                dapAnSai.add(ansC);
                dapAnSai.add(ansD);
            } else if (rbC.isChecked()) {
                dapAnDung = ansC;
                dapAnSai.add(ansA);
                dapAnSai.add(ansB);
                dapAnSai.add(ansD);
            } else if (rbD.isChecked()) {
                dapAnDung = ansD;
                dapAnSai.add(ansA);
                dapAnSai.add(ansB);
                dapAnSai.add(ansC);
            } else {
                Toast.makeText(this, "Vui lòng chọn 1 đáp án đúng bằng Radio Button cho Câu " + questionIndexDisplay, Toast.LENGTH_SHORT).show();
                return false;
            }

            // Tạo đối tượng CauHoi
            CauHoi cauHoi = new CauHoi(0, noiDung, dapAnDung, dapAnSai.get(0), dapAnSai.get(1), dapAnSai.get(2));

            // Gọi HÀM THEO YÊU CẦU: LuucauhoivaomangTam ở controller
            cauHoiController.LuucauhoivaomangTam(cauHoi);
        }

        return true;
    }

    /**
     * HOÀN THÀNH TẠO BỘ ĐỀ: TẠO RECORD BỘ ĐỀ + LƯU TOÀN BỘ CÂU HỎI VÀO SQLITE + ĐẾM SỐ LƯỢNG
     */
    private void hoanThanhTaoBoDe() {
        String tieuDeBoDe = etBoDeTitle.getText().toString().trim();
        if (tieuDeBoDe.isEmpty()) {
            tieuDeBoDe = "Bộ câu hỏi mới";
        }

        if (!luuToanBoCauHoiVaoMangTam(true)) {
            return;
        }

        List<CauHoi> danhSachTam = cauHoiController.getDanhSachCauHoiTam();
        if (danhSachTam == null || danhSachTam.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ít nhất 1 câu hỏi hoàn chỉnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        int tongSoCau = danhSachTam.size();

        // 1. Thêm bộ đề mới vào SQLite
        BoDe boDeMoi = new BoDe(currentThuMucId, tieuDeBoDe, tongSoCau);
        long newBoDeId = boDeController.themBoDe(boDeMoi);

        if (newBoDeId != -1) {
            int boDeId = (int) newBoDeId;

            // 2. Gán ID Bộ Đề vừa tạo cho danh sách câu hỏi
            for (CauHoi q : danhSachTam) {
                q.setBoDeId(boDeId);
            }

            // 3. Lưu toàn bộ câu hỏi hàng loạt vào bảng cau_hoi
            boolean isSaved = cauHoiController.ThemHangLoatCauHoi(danhSachTam);

            if (isSaved) {
                // 4. LỆNH ĐẾM: Đếm lại số lượng câu hỏi thực tế đã lưu từ CSDL
                int soCauThucTe = cauHoiController.demSoLuongCauHoiTheoBoDe(boDeId);
                boDeController.capNhatSoLuongCauHoi(boDeId, soCauThucTe);

                Toast.makeText(this, "Tạo bộ đề thành công với " + soCauThucTe + " câu hỏi!", Toast.LENGTH_LONG).show();
                cauHoiController.xoaDanhSachCauHoiTam();
                finish(); // Trở về ChiTietThuMucActivity
            } else {
                Toast.makeText(this, "Thêm câu hỏi vào CSDL thất bại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tạo bộ đề thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}

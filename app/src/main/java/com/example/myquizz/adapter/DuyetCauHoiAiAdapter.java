package com.example.myquizz.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.model.CauHoi;

import java.util.List;

public class DuyetCauHoiAiAdapter extends RecyclerView.Adapter<DuyetCauHoiAiAdapter.AiViewHolder> {

    private Context context;
    private List<CauHoi> danhSachCauHoiAi;
    private OnQuestionCountChangeListener listener;

    public interface OnQuestionCountChangeListener {
        void onQuestionCountChanged(int currentCount);
    }

    public DuyetCauHoiAiAdapter(Context context, List<CauHoi> danhSachCauHoiAi, OnQuestionCountChangeListener listener) {
        this.context = context;
        this.danhSachCauHoiAi = danhSachCauHoiAi;
        this.listener = listener;
    }

    public List<CauHoi> getUpdatedList() {
        return danhSachCauHoiAi;
    }

    public void addNewQuestion(CauHoi newQuestion) {
        if (newQuestion != null) {
            danhSachCauHoiAi.add(newQuestion);
            notifyItemInserted(danhSachCauHoiAi.size() - 1);
            if (listener != null) {
                listener.onQuestionCountChanged(danhSachCauHoiAi.size());
            }
        }
    }

    @NonNull
    @Override
    public AiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tam_item_duyet_cau_hoi_ai, parent, false);
        return new AiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AiViewHolder holder, int position) {
        CauHoi cauHoi = danhSachCauHoiAi.get(position);

        holder.tvAiQuestionNum.setText("Câu " + (position + 1) + " (AI Tạo/Chỉnh sửa)");

        // Chỉ setText khi nội dung thực sự khác, tránh reset composing text (gây mất chữ khi gõ tiếng Việt)
        setTextIfChanged(holder.etAiQuestionContent, cauHoi.getNoiDungCauHoi());
        setTextIfChanged(holder.etAiCorrectAnswer, cauHoi.getDapAnDung());
        setTextIfChanged(holder.etAiWrong1, cauHoi.getDapAnSai1());
        setTextIfChanged(holder.etAiWrong2, cauHoi.getDapAnSai2());
        setTextIfChanged(holder.etAiWrong3, cauHoi.getDapAnSai3());

        // Lắng nghe chỉnh sửa trực tiếp từ người dùng và cập nhật tức thì vào model
        holder.etAiQuestionContent.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cauHoi.setNoiDungCauHoi(s.toString().trim());
            }
        });

        holder.etAiCorrectAnswer.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cauHoi.setDapAnDung(s.toString().trim());
            }
        });

        holder.etAiWrong1.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cauHoi.setDapAnSai1(s.toString().trim());
            }
        });

        holder.etAiWrong2.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cauHoi.setDapAnSai2(s.toString().trim());
            }
        });

        holder.etAiWrong3.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cauHoi.setDapAnSai3(s.toString().trim());
            }
        });

        // Xóa câu hỏi không mong muốn
        holder.btnDeleteAiQuestion.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && currentPos < danhSachCauHoiAi.size()) {
                danhSachCauHoiAi.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, danhSachCauHoiAi.size());
                if (listener != null) {
                    listener.onQuestionCountChanged(danhSachCauHoiAi.size());
                }
            }
        });
    }

    private void setTextIfChanged(EditText editText, String newText) {
        String current = editText.getText() != null ? editText.getText().toString() : "";
        String target = newText != null ? newText : "";
        if (!current.equals(target)) {
            editText.setText(target);
        }
    }

    @Override
    public int getItemCount() {
        return danhSachCauHoiAi != null ? danhSachCauHoiAi.size() : 0;
    }

    public static class AiViewHolder extends RecyclerView.ViewHolder {
        TextView tvAiQuestionNum;
        ImageView btnDeleteAiQuestion;
        EditText etAiQuestionContent, etAiCorrectAnswer, etAiWrong1, etAiWrong2, etAiWrong3;

        public AiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAiQuestionNum = itemView.findViewById(R.id.tvAiQuestionNum);
            btnDeleteAiQuestion = itemView.findViewById(R.id.btnDeleteAiQuestion);

            etAiQuestionContent = itemView.findViewById(R.id.etAiQuestionContent);
            etAiCorrectAnswer = itemView.findViewById(R.id.etAiCorrectAnswer);
            etAiWrong1 = itemView.findViewById(R.id.etAiWrong1);
            etAiWrong2 = itemView.findViewById(R.id.etAiWrong2);
            etAiWrong3 = itemView.findViewById(R.id.etAiWrong3);
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}

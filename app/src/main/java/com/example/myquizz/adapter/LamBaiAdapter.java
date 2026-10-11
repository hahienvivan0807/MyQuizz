package com.example.myquizz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.model.CauHoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LamBaiAdapter extends RecyclerView.Adapter<LamBaiAdapter.QuizViewHolder> {

    private Context context;
    private List<CauHoi> danhSachCauHoi;

    // Map lưu trữ vị trí đáp án được người dùng chọn cho từng câu hỏi (Key: Position, Value: Option Index 0,1,2,3)
    private Map<Integer, Integer> userSelections = new HashMap<>();

    // Map lưu trữ 4 lựa chọn [A, B, C, D] cố định cho từng câu hỏi
    private Map<Integer, List<String>> questionOptionsMap = new HashMap<>();

    private OnAnswerSelectedListener listener;

    public interface OnAnswerSelectedListener {
        void onAnswerChanged(int answeredCount, int totalCount);
    }

    public LamBaiAdapter(Context context, List<CauHoi> danhSachCauHoi, OnAnswerSelectedListener listener) {
        this.context = context;
        this.danhSachCauHoi = danhSachCauHoi;
        this.listener = listener;

        // Khởi tạo các lựa chọn đáp án cho từng câu hỏi
        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            CauHoi q = danhSachCauHoi.get(i);
            List<String> options = Arrays.asList(
                    q.getDapAnDung(),
                    q.getDapAnSai1(),
                    q.getDapAnSai2(),
                    q.getDapAnSai3()
            );
            questionOptionsMap.put(i, options);
        }
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tam_item_quiz_question_card, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        CauHoi cauHoi = danhSachCauHoi.get(position);
        List<String> options = questionOptionsMap.get(position);

        holder.tvQuestionIndex.setText("Câu " + (position + 1) + " / " + danhSachCauHoi.size());
        holder.tvQuestionText.setText(cauHoi.getNoiDungCauHoi());

        if (options != null && options.size() >= 4) {
            holder.tvTextA.setText(options.get(0));
            holder.tvTextB.setText(options.get(1));
            holder.tvTextC.setText(options.get(2));
            holder.tvTextD.setText(options.get(3));
        }

        // Lấy đáp án hiện tại đã chọn (nếu có)
        int selectedOpt = userSelections.containsKey(position) ? userSelections.get(position) : -1;

        // Cập nhật trạng thái hiển thị giao diện cho 4 lựa chọn A, B, C, D
        updateOptionUi(holder.layoutAnswerA, holder.rbAnswerA, selectedOpt == 0);
        updateOptionUi(holder.layoutAnswerB, holder.rbAnswerB, selectedOpt == 1);
        updateOptionUi(holder.layoutAnswerC, holder.rbAnswerC, selectedOpt == 2);
        updateOptionUi(holder.layoutAnswerD, holder.rbAnswerD, selectedOpt == 3);

        // Sự kiện khi người dùng click vào Đáp án A
        holder.layoutAnswerA.setOnClickListener(v -> selectOption(position, 0));
        // Sự kiện khi người dùng click vào Đáp án B
        holder.layoutAnswerB.setOnClickListener(v -> selectOption(position, 1));
        // Sự kiện khi người dùng click vào Đáp án C
        holder.layoutAnswerC.setOnClickListener(v -> selectOption(position, 2));
        // Sự kiện khi người dùng click vào Đáp án D
        holder.layoutAnswerD.setOnClickListener(v -> selectOption(position, 3));
    }

    private void selectOption(int position, int optionIndex) {
        userSelections.put(position, optionIndex);
        notifyItemChanged(position);

        if (listener != null) {
            listener.onAnswerChanged(userSelections.size(), danhSachCauHoi.size());
        }
    }

    private void updateOptionUi(LinearLayout layout, RadioButton rb, boolean isSelected) {
        if (isSelected) {
            layout.setBackgroundResource(R.drawable.tam_bg_option_selected);
            rb.setChecked(true);
        } else {
            layout.setBackgroundResource(R.drawable.tam_bg_option_unselected);
            rb.setChecked(false);
        }
    }

    public boolean isAllQuestionsAnswered() {
        return userSelections.size() == danhSachCauHoi.size();
    }

    public int getAnsweredCount() {
        return userSelections.size();
    }

    public int calculateScore() {
        int correctCount = 0;
        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            if (userSelections.containsKey(i)) {
                int selectedIndex = userSelections.get(i);
                List<String> options = questionOptionsMap.get(i);
                if (options != null && selectedIndex < options.size()) {
                    String selectedText = options.get(selectedIndex);
                    String correctAnswer = danhSachCauHoi.get(i).getDapAnDung();
                    if (correctAnswer != null && correctAnswer.equals(selectedText)) {
                        correctCount++;
                    }
                }
            }
        }
        return correctCount;
    }

    @Override
    public int getItemCount() {
        return danhSachCauHoi != null ? danhSachCauHoi.size() : 0;
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionIndex, tvQuestionText;
        TextView tvTextA, tvTextB, tvTextC, tvTextD;
        LinearLayout layoutAnswerA, layoutAnswerB, layoutAnswerC, layoutAnswerD;
        RadioButton rbAnswerA, rbAnswerB, rbAnswerC, rbAnswerD;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionIndex = itemView.findViewById(R.id.tvQuestionIndex);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);

            tvTextA = itemView.findViewById(R.id.tvTextA);
            tvTextB = itemView.findViewById(R.id.tvTextB);
            tvTextC = itemView.findViewById(R.id.tvTextC);
            tvTextD = itemView.findViewById(R.id.tvTextD);

            layoutAnswerA = itemView.findViewById(R.id.layoutAnswerA);
            layoutAnswerB = itemView.findViewById(R.id.layoutAnswerB);
            layoutAnswerC = itemView.findViewById(R.id.layoutAnswerC);
            layoutAnswerD = itemView.findViewById(R.id.layoutAnswerD);

            rbAnswerA = itemView.findViewById(R.id.rbAnswerA);
            rbAnswerB = itemView.findViewById(R.id.rbAnswerB);
            rbAnswerC = itemView.findViewById(R.id.rbAnswerC);
            rbAnswerD = itemView.findViewById(R.id.rbAnswerD);
        }
    }
}

package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;

public class FillWordFragment extends Fragment {

    private EditText edtAnswer;
    private TextView txtQuestion, txtResult, txtProgress;
    private Button btnCheck;

    private int currentIndex = 0;

    // 🔥 Dữ liệu mẫu (sau này thay bằng API / Firebase)
    private final String[] questions = {"Con mèo", "Con chó", "Quyển sách"};
    private final String[] answers   = {"cat", "dog", "book"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fill_word, container, false);

        initView(v);
        loadQuestion();

        btnCheck.setOnClickListener(view -> checkAnswer());

        return v;
    }

    // =========================
    // 🔹 INIT VIEW
    // =========================
    private void initView(View v) {
        edtAnswer = v.findViewById(R.id.edtAnswer);
        txtQuestion = v.findViewById(R.id.txtQuestion);
        txtResult = v.findViewById(R.id.txtResult);
        txtProgress = v.findViewById(R.id.txtProgress);
        btnCheck = v.findViewById(R.id.btnCheck);
    }

    // =========================
    // 📌 LOAD QUESTION
    // =========================
    private void loadQuestion() {

        txtQuestion.setText(questions[currentIndex]);
        txtProgress.setText((currentIndex + 1) + " / " + questions.length);

        edtAnswer.setText("");
        txtResult.setText("");
    }

    // =========================
    // ✅ CHECK ANSWER
    // =========================
    private void checkAnswer() {

        String userAnswer = edtAnswer.getText().toString().trim();

        if (TextUtils.isEmpty(userAnswer)) {
            edtAnswer.setError("Vui lòng nhập đáp án");
            return;
        }

        if (userAnswer.equalsIgnoreCase(answers[currentIndex])) {

            txtResult.setText("Chính xác");
            txtResult.setTextColor(0xFF2BB673);

            // 👉 tự chuyển câu sau
            edtAnswer.postDelayed(this::nextQuestion, 800);

        } else {

            txtResult.setText("Sai rồi! Đáp án: " + answers[currentIndex]);
            txtResult.setTextColor(0xFFE53935);
        }
    }

    // =========================
    // ➡️ NEXT QUESTION
    // =========================
    private void nextQuestion() {

        if (currentIndex < questions.length - 1) {
            currentIndex++;
            loadQuestion();
        } else {
            txtResult.setText("Hoàn thành");
            btnCheck.setEnabled(false);
        }
    }
}
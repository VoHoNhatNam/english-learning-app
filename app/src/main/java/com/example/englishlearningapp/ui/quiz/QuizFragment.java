package com.example.englishlearningapp.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private TextView txtQuestion, txtQuizCount;
    private Button btn1, btn2, btn3, btn4;
    private ProgressBar progressBar;

    private List<Question> questionList;
    private int currentIdx = 0;
    private int score = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        initViews(view);
        setupQuestions();
        displayQuestion();

        return view;
    }

    private void initViews(View view) {
        txtQuestion = view.findViewById(R.id.txtQuestion);
        txtQuizCount = view.findViewById(R.id.txtQuizCount);
        progressBar = view.findViewById(R.id.quizProgressBar);
        btn1 = view.findViewById(R.id.btnAnswer1);
        btn2 = view.findViewById(R.id.btnAnswer2);
        btn3 = view.findViewById(R.id.btnAnswer3);
        btn4 = view.findViewById(R.id.btnAnswer4);

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            checkAnswer(b.getText().toString());
        };

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
    }

    private void setupQuestions() {
        questionList = new ArrayList<>();
        // Mock data - Sau này có thể lấy từ Firebase Firestore
        questionList.add(new Question("Con mèo tiếng Anh là gì?", "Cat", "Dog", "Bird", "Fish", "Cat"));
        questionList.add(new Question("Số 'Mười' trong tiếng Anh là gì?", "Nine", "Eleven", "Ten", "Eight", "Ten"));
        questionList.add(new Question("'Hello' có nghĩa là gì?", "Tạm biệt", "Xin chào", "Cảm ơn", "Xin lỗi", "Xin chào"));
    }

    private void displayQuestion() {
        if (currentIdx < questionList.size()) {
            Question q = questionList.get(currentIdx);
            txtQuestion.setText(q.questionText);
            btn1.setText(q.option1);
            btn2.setText(q.option2);
            btn3.setText(q.option3);
            btn4.setText(q.option4);

            txtQuizCount.setText("Câu hỏi: " + (currentIdx + 1) + "/" + questionList.size());
            progressBar.setProgress((int) (((float) (currentIdx + 1) / questionList.size()) * 100));
        } else {
            // ĐÃ ĐIỀU CHỈNH: Gọi hàm chuyển sang màn hình kết quả
            navigateToResult();
        }
    }

    private void checkAnswer(String selected) {
        if (selected.equals(questionList.get(currentIdx).correctAnswer)) {
            score++;
            Toast.makeText(getContext(), "Chính xác!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Sai rồi!", Toast.LENGTH_SHORT).show();
        }
        currentIdx++;
        displayQuestion();
    }

    // HÀM MỚI: Chuyển sang QuizResultFragment và gửi dữ liệu điểm
    private void navigateToResult() {
        QuizResultFragment resultFragment = new QuizResultFragment();

        // Gói dữ liệu vào Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("SCORE", score);
        bundle.putInt("TOTAL", questionList.size());
        resultFragment.setArguments(bundle);

        // Thực hiện chuyển Fragment
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, resultFragment);
            // Không dùng addToBackStack ở đây để người dùng không quay lại giữa chừng bài quiz đã xong
            transaction.commit();
        }
    }

    // Class nội bộ để chứa dữ liệu câu hỏi
    static class Question {
        String questionText, option1, option2, option3, option4, correctAnswer;

        public Question(String q, String o1, String o2, String o3, String o4, String ans) {
            this.questionText = q;
            this.option1 = o1;
            this.option2 = o2;
            this.option3 = o3;
            this.option4 = o4;
            this.correctAnswer = ans;
        }
    }
}
package com.example.englishlearningapp.ui.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.englishlearningapp.data.model.TestResult;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizFragment extends Fragment {

    private TextView txtQuestion, txtQuizCount, txtTimer;
    private Button[] btnAnswers = new Button[4];
    private ProgressBar progressBar;

    private List<Vocabulary> fullVocabList = new ArrayList<>(); // Toàn bộ từ để làm đáp án nhiễu
    private List<Vocabulary> quizQuestions = new ArrayList<>(); // Danh sách câu hỏi chính
    private int currentIdx = 0;
    private int score = 0;
    private int lessonId;
    private FirebaseFirestore db;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        if (getArguments() != null) {
            lessonId = getArguments().getInt("lessonId", -1);
        }

        db = FirebaseFirestore.getInstance();
        initViews(view);
        loadVocabFromFirestore();

        return view;
    }

    private void initViews(View view) {
        txtQuestion = view.findViewById(R.id.txtQuestion);
        txtQuizCount = view.findViewById(R.id.txtQuizCount);
        txtTimer = view.findViewById(R.id.txtTimer);
        progressBar = view.findViewById(R.id.quizProgressBar);

        btnAnswers[0] = view.findViewById(R.id.btnAnswer1);
        btnAnswers[1] = view.findViewById(R.id.btnAnswer2);
        btnAnswers[2] = view.findViewById(R.id.btnAnswer3);
        btnAnswers[3] = view.findViewById(R.id.btnAnswer4);

        for (Button btn : btnAnswers) {
            btn.setOnClickListener(v -> checkAnswer((Button) v));
        }
    }

    private void loadVocabFromFirestore() {
        db.collection("vocabularies")
                .whereEqualTo("lessonId", lessonId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        fullVocabList.add(doc.toObject(Vocabulary.class));
                    }

                    if (fullVocabList.size() >= 4) {
                        quizQuestions.addAll(fullVocabList);
                        Collections.shuffle(quizQuestions); // Xáo trộn câu hỏi
                        startTimer();
                        displayQuestion();
                    } else {
                        Toast.makeText(getContext(), "Cần ít nhất 4 từ để tạo Quiz!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void displayQuestion() {
        if (currentIdx < quizQuestions.size()) {
            Vocabulary correctVocab = quizQuestions.get(currentIdx);

            // Câu hỏi học thuật: Cho từ tiếng Anh, tìm nghĩa đúng
            txtQuestion.setText("Which of the following is the correct meaning of:\n\"" + correctVocab.getWord() + "\"?");

            // Tạo danh sách đáp án nhiễu (1 đúng + 3 sai)
            List<String> options = new ArrayList<>();
            options.add(correctVocab.getMeaning());

            List<Vocabulary> distractors = new ArrayList<>(fullVocabList);
            distractors.remove(correctVocab);
            Collections.shuffle(distractors);

            for (int i = 0; i < 3; i++) {
                options.add(distractors.get(i).getMeaning());
            }
            Collections.shuffle(options); // Xáo trộn vị trí A, B, C, D

            for (int i = 0; i < 4; i++) {
                btnAnswers[i].setText(options.get(i));
                btnAnswers[i].setEnabled(true);
                btnAnswers[i].setBackgroundColor(Color.WHITE); // Reset màu
            }

            txtQuizCount.setText("QUESTION " + (currentIdx + 1) + " / " + quizQuestions.size());
            progressBar.setMax(quizQuestions.size());
            progressBar.setProgress(currentIdx + 1);
        } else {
            saveResultAndFinish();
        }
    }

    private void checkAnswer(Button selectedBtn) {
        String selected = selectedBtn.getText().toString();
        String correct = quizQuestions.get(currentIdx).getMeaning();

        if (selected.equals(correct)) {
            score++;
            selectedBtn.setBackgroundColor(Color.GREEN);
        } else {
            selectedBtn.setBackgroundColor(Color.RED);
        }

        // Vô hiệu hóa nút để tránh bấm nhiều lần
        for (Button btn : btnAnswers) btn.setEnabled(false);

        // Đợi 1 giây rồi sang câu tiếp theo
        selectedBtn.postDelayed(() -> {
            currentIdx++;
            displayQuestion();
        }, 1000);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(900000, 1000) { // 15 phút
            public void onTick(long millisUntilFinished) {
                int mins = (int) (millisUntilFinished / 60000);
                int secs = (int) (millisUntilFinished % 60000 / 1000);
                txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", mins, secs));
            }
            public void onFinish() { saveResultAndFinish(); }
        }.start();
    }

    private void saveResultAndFinish() {
        if (countDownTimer != null) countDownTimer.cancel();

        // Tính % điểm
        int finalScore = (score * 100) / quizQuestions.size();

        // Lưu kết quả vào Firestore (Bậc đại học cần lưu để theo dõi tiến độ)
        TestResult result = new TestResult(
                FirebaseAuth.getInstance().getUid(),
                lessonId,
                finalScore,
                0, // time spent logic
                new Date()
        );

        db.collection("results").add(result)
                .addOnSuccessListener(documentReference -> navigateToResult(finalScore));
    }

    private void navigateToResult(int finalScore) {
        QuizResultFragment resultFragment = new QuizResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("SCORE", finalScore);
        bundle.putInt("CORRECT", score);
        bundle.putInt("TOTAL", quizQuestions.size());
        bundle.putString("FROM_TYPE", "QUIZ");
        resultFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroy();
    }
}
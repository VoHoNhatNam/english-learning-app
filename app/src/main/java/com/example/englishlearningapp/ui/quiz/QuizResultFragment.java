package com.example.englishlearningapp.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.home.HomeFragment;
import com.example.englishlearningapp.ui.lesson.FillWordFragment; // Import thêm
import com.google.android.material.button.MaterialButton;

public class QuizResultFragment extends Fragment {

    private TextView txtFinalScore, txtScorePercent, txtPerformanceTitle, txtFeedback;
    private MaterialButton btnTryAgain, btnBackHome;

    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private int scorePercent = 0;
    private int lessonId = -1;
    private String fromType = "QUIZ"; // Mặc định là Quiz

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_result, container, false);

        // 1. Nhận dữ liệu
        if (getArguments() != null) {
            correctAnswers = getArguments().getInt("CORRECT", 0);
            totalQuestions = getArguments().getInt("TOTAL", 0);
            scorePercent = getArguments().getInt("SCORE", 0);
            lessonId = getArguments().getInt("lessonId", -1);
            // 🔥 Nhận thêm loại bài tập (Quiz hoặc Fill)
            fromType = getArguments().getString("FROM_TYPE", "QUIZ");
        }

        initViews(view);
        displayPerformance();

        // 2. Logic "Làm lại" thông minh
        btnTryAgain.setOnClickListener(v -> {
            Fragment retryFragment;

            // Nếu đến từ FillWord thì quay lại FillWord, ngược lại quay lại Quiz
            if ("FILL_WORD".equals(fromType)) {
                retryFragment = new FillWordFragment();
            } else {
                retryFragment = new QuizFragment();
            }

            Bundle bundle = new Bundle();
            bundle.putInt("lessonId", lessonId);
            retryFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, retryFragment)
                    .commit();
        });

        btnBackHome.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        });

        return view;
    }

    private void initViews(View view) {
        txtFinalScore = view.findViewById(R.id.txtFinalScore);
        txtScorePercent = view.findViewById(R.id.txtScorePercent);
        txtPerformanceTitle = view.findViewById(R.id.txtPerformanceTitle);
        txtFeedback = view.findViewById(R.id.txtFeedback);
        btnTryAgain = view.findViewById(R.id.btnTryAgain);
        btnBackHome = view.findViewById(R.id.btnBackHome);
    }

    private void displayPerformance() {
        txtFinalScore.setText(correctAnswers + " / " + totalQuestions);
        txtScorePercent.setText(scorePercent + "%");

        if (scorePercent >= 90) {
            txtPerformanceTitle.setText("EXCELLENT");
            txtPerformanceTitle.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            txtFeedback.setText("Outstanding! You have a profound grasp of this academic vocabulary set.");
        } else if (scorePercent >= 70) {
            txtPerformanceTitle.setText("GOOD");
            txtPerformanceTitle.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            txtFeedback.setText("Well done! You have mastered most of the terms. A bit more review and you'll reach perfection.");
        } else if (scorePercent >= 50) {
            txtPerformanceTitle.setText("AVERAGE");
            txtPerformanceTitle.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            txtFeedback.setText("Satisfactory. You should revisit the vocabulary list to reinforce your memory.");
        } else {
            txtPerformanceTitle.setText("NEEDS IMPROVEMENT");
            txtPerformanceTitle.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            txtFeedback.setText("Keep practicing! Academic success requires persistence. Go back to flashcards and try again.");
        }
    }
}
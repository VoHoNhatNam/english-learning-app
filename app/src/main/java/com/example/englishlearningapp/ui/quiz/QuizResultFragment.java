package com.example.englishlearningapp.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;

public class QuizResultFragment extends Fragment {

    private TextView txtFinalScore;
    private MaterialButton btnTryAgain, btnBackHome;

    // Các biến nhận dữ liệu
    private int score = 0;
    private int totalQuestions = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_result, container, false);

        // Lấy dữ liệu được truyền từ QuizFragment
        if (getArguments() != null) {
            score = getArguments().getInt("SCORE", 0);
            totalQuestions = getArguments().getInt("TOTAL", 0);
        }

        initViews(view);

        // Hiển thị điểm số
        txtFinalScore.setText(score + "/" + totalQuestions);

        // Nút làm lại (Quay lại QuizFragment)
        btnTryAgain.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuizFragment())
                    .commit();
        });

        // Nút về Home
        btnBackHome.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        });

        return view;
    }

    private void initViews(View view) {
        txtFinalScore = view.findViewById(R.id.txtFinalScore);
        btnTryAgain = view.findViewById(R.id.btnTryAgain);
        btnBackHome = view.findViewById(R.id.btnBackHome);
    }
}
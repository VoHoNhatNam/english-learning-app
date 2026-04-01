package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.google.android.material.button.MaterialButton;

public class LessonDetailFragment extends Fragment {

    private TextView txtTitle, txtPercent;
    private ProgressBar progressBar;
    private MaterialButton btnVocabulary, btnQuiz;
    private ImageButton btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện XML
        View view = inflater.inflate(R.layout.fragment_lesson_detail, container, false);

        // Ánh xạ View
        initViews(view);

        // Nhận dữ liệu (ví dụ tên bài học) truyền từ Fragment trước đó
        if (getArguments() != null) {
            String lessonName = getArguments().getString("lessonName", "Bài học mặc định");
            txtTitle.setText(lessonName);
        }

        // Xử lý sự kiện nút quay lại
        btnBack.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack(); // Quay lại Fragment trước đó
            }
        });

        // Xử lý nút Học từ vựng
        btnVocabulary.setOnClickListener(v -> {
            // Sau này bạn sẽ chuyển đến VocabularyFragment ở đây
            Toast.makeText(getContext(), "Bắt đầu học từ vựng!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút Trắc nghiệm
        btnQuiz.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Bắt đầu làm bài tập!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void initViews(View view) {
        txtTitle = view.findViewById(R.id.txtLessonDetailTitle);
        txtPercent = view.findViewById(R.id.txtProgressPercent);
        progressBar = view.findViewById(R.id.progressLesson);
        btnVocabulary = view.findViewById(R.id.btnVocabulary);
        btnQuiz = view.findViewById(R.id.btnQuiz);
        btnBack = view.findViewById(R.id.btnBack);
    }
}
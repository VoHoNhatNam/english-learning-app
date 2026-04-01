package com.example.englishlearningapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProgressFragment extends Fragment {

    private TextView txtLessonsCount, txtAvgScore, txtProgressLabel;
    private ProgressBar progressWeekly;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews(view);
        loadProgressData();

        return view;
    }

    private void initViews(View view) {
        txtLessonsCount = view.findViewById(R.id.txtLessonsCount);
        txtAvgScore = view.findViewById(R.id.txtAvgScore);
        txtProgressLabel = view.findViewById(R.id.txtProgressLabel);
        progressWeekly = view.findViewById(R.id.progressWeekly);
    }

    private void loadProgressData() {
        // Giả lập dữ liệu (Sau này bạn sẽ query từ bảng "user_progress" trong Firestore)
        String userId = mAuth.getUid();

        // Dữ liệu mẫu hiển thị ngay
        txtLessonsCount.setText("15");
        txtAvgScore.setText("92%");
        progressWeekly.setProgress(80);
        txtProgressLabel.setText("Đã hoàn thành 4/5 bài học tuần này");

        /*
        Ví dụ cấu trúc lấy từ Firestore:
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Update UI tại đây
            }
        });
        */
    }
}
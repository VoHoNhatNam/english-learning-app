package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.LearningMode;
import com.example.englishlearningapp.data.model.VocabularyLesson;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class VocabularyDetailFragment extends Fragment {

    private VocabularyLesson lesson;
    private RecyclerView rvVocabulary, rvLearningModes;
    private TextView tvCategoryTitle, tvWordCount;
    private MaterialCardView bottomActionView;
    private View viewOverlay;
    private LearningMode selectedMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_detail, container, false);

        if (getArguments() != null) {
            lesson = (VocabularyLesson) getArguments().getSerializable("lesson");
        }

        initViews(view);
        setupUI();
        setupLearningModes();

        return view;
    }

    private void initViews(View view) {
        rvVocabulary = view.findViewById(R.id.rvVocabulary);
        rvLearningModes = view.findViewById(R.id.rvLearningModes);
        tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);
        tvWordCount = view.findViewById(R.id.tvWordCount);
        bottomActionView = view.findViewById(R.id.bottomActionView);
        viewOverlay = view.findViewById(R.id.viewOverlay);
        
        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        view.findViewById(R.id.btnAdd).setOnClickListener(v -> showLearningModes());
        viewOverlay.setOnClickListener(v -> hideLearningModes());
        
        view.findViewById(R.id.btnStartLearning).setOnClickListener(v -> {
            if (selectedMode != null) {
                Toast.makeText(getContext(), "Bắt đầu: " + selectedMode.getTitle(), Toast.LENGTH_SHORT).show();
                hideLearningModes();
                // Chuyển sang màn hình học tương ứng ở đây
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn một chế độ học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLearningModes() {
        List<LearningMode> modes = new ArrayList<>();
        modes.add(new LearningMode("flashcard", "Flashcards", "Học qua thẻ ghi nhớ", R.drawable.ic_book, false, false));
        modes.add(new LearningMode("quiz", "Trắc nghiệm", "Phù hợp để nhớ mặt chữ", R.drawable.ic_quiz, false, false));
        modes.add(new LearningMode("match", "Ghép từ", "Thử thách trí nhớ ngắn hạn", R.drawable.ic_lesson, true, true));
        modes.add(new LearningMode("type", "Luyện gõ", "Nhớ mặt chữ tuyệt đối", R.drawable.ic_edit, false, false));

        selectedMode = modes.get(2); // Mặc định chọn Ghép từ (Recommended)

        LearningModeAdapter adapter = new LearningModeAdapter(modes, mode -> {
            selectedMode = mode;
        });
        
        rvLearningModes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLearningModes.setAdapter(adapter);
    }

    private void showLearningModes() {
        bottomActionView.setVisibility(View.VISIBLE);
        viewOverlay.setVisibility(View.VISIBLE);
    }

    private void hideLearningModes() {
        bottomActionView.setVisibility(View.GONE);
        viewOverlay.setVisibility(View.GONE);
    }

    private void setupUI() {
        if (lesson == null) return;

        tvCategoryTitle.setText(lesson.getTitle());
        if (lesson.getWords() != null) {
            tvWordCount.setText(lesson.getWords().size() + " Words");
            
            VocabularyCardAdapter adapter = new VocabularyCardAdapter(lesson.getWords());
            rvVocabulary.setLayoutManager(new LinearLayoutManager(getContext()));
            rvVocabulary.setAdapter(adapter);
        }
    }
}

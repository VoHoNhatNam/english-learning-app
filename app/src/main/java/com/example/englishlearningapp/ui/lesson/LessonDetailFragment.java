package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Example;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class LessonDetailFragment extends Fragment {

    private CustomLesson lesson;

    public static LessonDetailFragment newInstance(CustomLesson lesson) {
        LessonDetailFragment fragment = new LessonDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("lesson", lesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lesson = (CustomLesson) getArguments().getSerializable("lesson");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_detail, container, false);

        if (lesson != null) {
            initViews(view);
        }

        return view;
    }

    private void initViews(View view) {
        view.findViewById(R.id.toolbar).setOnClickListener(v -> getParentFragmentManager().popBackStack());

        TextView tvTitle = view.findViewById(R.id.tvLessonTitle);
        TextView tvContext = view.findViewById(R.id.tvLessonContext);
        LinearLayout layoutTheoryContent = view.findViewById(R.id.layoutTheoryContent);
        LinearProgressIndicator progressBar = view.findViewById(R.id.lessonProgressBar);

        tvTitle.setText(lesson.getTitle());
        
        // Hiển thị Context (sử dụng description làm context nếu không có trường riêng)
        if (lesson.getDescription() != null) {
            tvContext.setText(lesson.getDescription());
        }

        // Cập nhật Progress Bar (giả lập dựa trên độ dài nội dung hoặc mặc định)
        progressBar.setProgress(65);

        // Thêm nội dung giải thích (Theory)
        if (lesson.getVocabularies() != null) {
            for (Vocabulary vocab : lesson.getVocabularies()) {
                addTheoryItem(layoutTheoryContent, vocab);
            }
        }

        // Nút Thực hành với AI
        view.findViewById(R.id.btnPracticeAI).setOnClickListener(v -> {
            // Chuyển sang tab Chat AI với ngữ cảnh bài học
            Bundle bundle = new Bundle();
            bundle.putString("initial_topic", "Practice " + lesson.getTitle());
            
            ChatFragment chatFragment = new ChatFragment();
            chatFragment.setArguments(bundle);
            
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void addTheoryItem(LinearLayout container, Vocabulary vocab) {
        View theoryView = getLayoutInflater().inflate(R.layout.item_lesson_vocab, container, false);
        TextView tvWord = theoryView.findViewById(R.id.tvWord);
        TextView tvMeaning = theoryView.findViewById(R.id.tvMeaning);
        
        tvWord.setText(vocab.getWord());
        tvMeaning.setText(vocab.getMeaning());
        
        // UX Tương tác chạm: Bấm vào để lưu (giả định có chức năng này)
        theoryView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã lưu \"" + vocab.getWord() + "\" vào Kho lưu trữ", Toast.LENGTH_SHORT).show();
        });

        container.addView(theoryView);
    }
}

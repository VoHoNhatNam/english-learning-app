package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.button.MaterialButton;

public class LessonDetailFragment extends Fragment {

    private CustomLesson lesson;

    // Khai báo các view (Gộp từ cả 2 nhánh)
    private TextView tvTitle, tvContext, txtPercent;
    private LinearProgressIndicator progressBar;
    private LinearLayout layoutTheoryContent;
    private MaterialButton btnVocabulary, btnQuiz, btnPracticeAI;
    private ImageButton btnBack;

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

        initViews(view);

        if (lesson != null) {
            setupLessonData();
        } else {
            // Fallback trường hợp lỗi mất đối tượng
            tvTitle.setText("Lỗi nạp bài học");
        }

        setupButtons();

        return view;
    }

    private void initViews(View view) {
        // Ánh xạ từ nhánh HEAD
        tvTitle = view.findViewById(R.id.tvLessonTitle);
        tvContext = view.findViewById(R.id.tvLessonContext);
        layoutTheoryContent = view.findViewById(R.id.layoutTheoryContent);
        progressBar = view.findViewById(R.id.lessonProgressBar);
        btnPracticeAI = view.findViewById(R.id.btnPracticeAI);

        // Ánh xạ từ nhánh Develop (Nếu có trong XML)
        btnBack = view.findViewById(R.id.btnBack);
        btnVocabulary = view.findViewById(R.id.btnVocabulary);
        btnQuiz = view.findViewById(R.id.btnQuiz);
        txtPercent = view.findViewById(R.id.txtProgressPercent);

        // Xử lý nút Back (có thể dùng chung cho cả 2 kiểu UI)
        View toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        } else if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }
    }

    private void setupLessonData() {
        tvTitle.setText(lesson.getTitle());

        if (lesson.getDescription() != null && tvContext != null) {
            tvContext.setText(lesson.getDescription());
        }

        if (progressBar != null) {
            progressBar.setProgress(65);
        }

        if (layoutTheoryContent != null && lesson.getVocabularies() != null) {
            layoutTheoryContent.removeAllViews(); // Xoá trắng trước khi thêm
            for (Vocabulary vocab : lesson.getVocabularies()) {
                addTheoryItem(layoutTheoryContent, vocab);
            }
        }
    }

    private void setupButtons() {
        if (btnPracticeAI != null) {
            btnPracticeAI.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                // Lấy Context để truyền qua ChatFragment
                String context = (lesson != null) ? "Practice " + lesson.getTitle() : "Practice general English";
                bundle.putString("initial_topic", context);

                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, chatFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }

        // Sự kiện cho nhánh Develop
        if (btnVocabulary != null) {
            btnVocabulary.setOnClickListener(v -> Toast.makeText(getContext(), "Bắt đầu học từ vựng!", Toast.LENGTH_SHORT).show());
        }
        if (btnQuiz != null) {
            btnQuiz.setOnClickListener(v -> Toast.makeText(getContext(), "Bắt đầu làm bài tập!", Toast.LENGTH_SHORT).show());
        }
    }

    private void addTheoryItem(LinearLayout container, Vocabulary vocab) {
        View theoryView = getLayoutInflater().inflate(R.layout.item_lesson_vocab, container, false);
        TextView tvWord = theoryView.findViewById(R.id.tvWord);
        TextView tvMeaning = theoryView.findViewById(R.id.tvMeaning);

        tvWord.setText(vocab.getWord());
        tvMeaning.setText(vocab.getMeaning());

        theoryView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã lưu \"" + vocab.getWord() + "\" vào Kho lưu trữ", Toast.LENGTH_SHORT).show();
        });

        container.addView(theoryView);
    }
}
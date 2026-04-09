package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Lesson;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.example.englishlearningapp.ui.lesson.FlashcardFragment;
import com.example.englishlearningapp.ui.lesson.FillWordFragment;
import com.example.englishlearningapp.ui.lesson.VocabularyFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvLessons;
    private View lnFlashcard, lnQuiz, lnChat, lnMore;
    private ProgressBar progressBar;
    private TextView tvUserName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupProgress();
        setupLessonList();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        rvLessons = view.findViewById(R.id.rvLessons);
        lnFlashcard = view.findViewById(R.id.lnFlashcard);
        lnQuiz = view.findViewById(R.id.lnQuiz);
        lnChat = view.findViewById(R.id.lnChat);
        lnMore = view.findViewById(R.id.lnMore);
        progressBar = view.findViewById(R.id.progressBar);
        tvUserName = view.findViewById(R.id.tvUserName);
    }

    private void setupProgress() {
        // Lấy tên người dùng thực tế từ Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            tvUserName.setText((name != null && !name.isEmpty()) ? name + "!" : "Người dùng!");
        } else {
            tvUserName.setText("Người dùng!");
        }

        progressBar.setProgress(65);
    }

    private void setupLessonList() {
        List<CustomLesson> lessonData = new ArrayList<>();
        lessonData.add(new CustomLesson("Bài 1: Greetings", "Học cách chào hỏi cơ bản"));
        lessonData.add(new CustomLesson("Bài 2: Family & Friends", "Từ vựng về gia đình"));
        lessonData.add(new CustomLesson("Bài 3: Daily Activities", "Hoạt động thường ngày"));
        lessonData.add(new CustomLesson("Bài 4: Food & Drinks", "Ẩm thực và đồ uống"));

        Lesson adapter = new Lesson(lessonData, lesson -> {
            navigateToFragment(new VocabularyFragment());
        });

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLessons.setHasFixedSize(true);
        rvLessons.setAdapter(adapter);
    }

    private void setupButtons() {
        lnFlashcard.setOnClickListener(v -> navigateToFragment(new FlashcardFragment()));
        lnQuiz.setOnClickListener(v -> navigateToFragment(new QuizFragment()));
        lnChat.setOnClickListener(v -> navigateToFragment(new ChatFragment()));
        lnMore.setOnClickListener(v -> navigateToFragment(new FillWordFragment()));
    }

    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.logo_scale, android.R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
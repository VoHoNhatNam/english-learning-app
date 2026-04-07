package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Lesson;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.example.englishlearningapp.ui.lesson.VocabularyFragment;
import com.example.englishlearningapp.ui.lesson.FlashcardFragment;
import com.example.englishlearningapp.ui.lesson.FillWordFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvLessons; // Đổi từ ListView sang RecyclerView
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;
    private FirebaseFirestore db;

    private Button btnFlashcard, btnQuiz, btnFill, btnListening;
    private EditText searchInput;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        initViews(view);
        setupRecyclerView();
        loadLessonsFromFirestore();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        rvLessons = view.findViewById(R.id.listLesson); // Đảm bảo trong XML ID này là RecyclerView
        btnFlashcard = view.findViewById(R.id.btnFlashcard);
        btnQuiz = view.findViewById(R.id.btnQuiz);
        btnFill = view.findViewById(R.id.btnFill);
        btnListening = view.findViewById(R.id.btnListening);
        searchInput = view.findViewById(R.id.searchInput);
    }

    private void setupRecyclerView() {
        lessonList = new ArrayList<>();
        // Thiết lập LayoutManager (dạng danh sách dọc)
        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo Adapter với listener để xử lý click
        lessonAdapter = new LessonAdapter(lessonList, lesson -> {
            // Khi nhấn vào một bài học cụ thể
            navigateToVocabulary(lesson.getId());
        });

        rvLessons.setAdapter(lessonAdapter);
    }

    private void loadLessonsFromFirestore() {
        db.collection("lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lessonList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Lesson lesson = document.toObject(Lesson.class);
                        lessonList.add(lesson);
                    }
                    lessonAdapter.notifyDataSetChanged(); // Cập nhật giao diện khi có dữ liệu mới
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading lessons: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupButtons() {
        btnFlashcard.setOnClickListener(v -> navigateToFragment(new FlashcardFragment()));
        btnQuiz.setOnClickListener(v -> navigateToFragment(new QuizFragment()));
        btnFill.setOnClickListener(v -> navigateToFragment(new FillWordFragment()));
        btnListening.setOnClickListener(v -> navigateToVocabulary(101)); // Mặc định chuyển sang 101 nếu click nghe chung
    }

    /**
     * Hàm điều hướng sang VocabularyFragment có truyền ID bài học
     */
    private void navigateToVocabulary(int lessonId) {
        VocabularyFragment vocabFragment = new VocabularyFragment();

        // Gửi lessonId qua Bundle để VocabularyFragment biết cần tải từ bài nào
        Bundle bundle = new Bundle();
        bundle.putInt("lessonId", lessonId);
        vocabFragment.setArguments(bundle);

        navigateToFragment(vocabFragment);
    }

    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
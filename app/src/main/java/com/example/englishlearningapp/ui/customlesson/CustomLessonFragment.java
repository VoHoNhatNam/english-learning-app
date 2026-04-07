package com.example.englishlearningapp.ui.customlesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Lesson;
import com.example.englishlearningapp.ui.home.LessonAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomLessonFragment extends Fragment {

    private RecyclerView rvLessons;
    private FloatingActionButton fabAdd;
    private FirebaseFirestore db;
    private String currentUserId;

    // ĐIỀU CHỈNH 1: Đổi List sang kiểu Lesson để khớp với LessonAdapter
    private List<Lesson> lessonList;
    private LessonAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_lesson, container, false);

        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        rvLessons = view.findViewById(R.id.rvCustomLessons);
        fabAdd = view.findViewById(R.id.fabAddLesson);

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        lessonList = new ArrayList<>();

        // ĐIỀU CHỈNH 2: Khởi tạo adapter với danh sách kiểu Lesson
        adapter = new LessonAdapter(lessonList, lesson -> {
            // Ép kiểu ngược lại thành CustomLesson để lấy ID chi tiết
            if (lesson instanceof CustomLesson) {
                navigateToVocabulary((CustomLesson) lesson);
            }
        });
        rvLessons.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CreateLessonFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadCustomLessons();
        return view;
    }

    private void loadCustomLessons() {
        if (currentUserId == null) return;

        db.collection("custom_lessons")
                .whereEqualTo("userId", currentUserId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lessonList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // ĐIỀU CHỈNH 3: CustomLesson phải kế thừa Lesson (extends Lesson)
                        CustomLesson customLesson = doc.toObject(CustomLesson.class);
                        customLesson.setId(doc.getId());
                        lessonList.add(customLesson); // Thêm CustomLesson vào danh sách Lesson
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi tải bài học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToVocabulary(CustomLesson lesson) {
        CustomVocabularyFragment vocabFragment = new CustomVocabularyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LESSON_ID", lesson.getId());
        bundle.putString("LESSON_TITLE", lesson.getTitle());
        vocabFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, vocabFragment)
                .addToBackStack(null)
                .commit();
    }
}
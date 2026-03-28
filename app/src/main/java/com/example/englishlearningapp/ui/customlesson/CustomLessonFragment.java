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
import com.example.englishlearningapp.data.model.LessonAdapter; // ĐÃ ĐỔI SANG LessonAdapter
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
    private List<CustomLesson> lessonList;
    private LessonAdapter adapter; // ĐÃ ĐỔI TÊN BIẾN CHO KHỚP

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_lesson, container, false);

        // 1. Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // 2. Ánh xạ View
        rvLessons = view.findViewById(R.id.rvCustomLessons);
        fabAdd = view.findViewById(R.id.fabAddLesson);

        // 3. Cấu hình RecyclerView
        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        lessonList = new ArrayList<>();

        // Khởi tạo LessonAdapter (Dùng class LessonAdapter bạn đã viết)
        adapter = new LessonAdapter(lessonList, lesson -> {
            // Khi người dùng click vào một bài học trong danh sách
            navigateToVocabulary(lesson);
        });
        rvLessons.setAdapter(adapter);

        // 4. Sự kiện khi bấm nút FAB (Thêm bài học)
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
                        CustomLesson lesson = doc.toObject(CustomLesson.class);
                        // Gán Document ID từ Firestore vào Model
                        lesson.setId(doc.getId());
                        lessonList.add(lesson);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi tải bài học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToVocabulary(CustomLesson lesson) {
        // Chuyển sang màn hình quản lý từ vựng chi tiết
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
package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson; // Model của bạn
import com.example.englishlearningapp.data.model.Lesson;       // Adapter của bạn

import java.util.ArrayList;
import java.util.List;

public class LessonListFragment extends Fragment {

    private RecyclerView recyclerView;
    private Lesson adapter;
    private List<CustomLesson> lessonList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerLessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 1. Tạo dữ liệu mẫu bằng object CustomLesson (dùng constructor rút gọn ở Bước 1)
        lessonList = new ArrayList<>();
        lessonList.add(new CustomLesson("Bài 1: Greetings", "Chào hỏi cơ bản"));
        lessonList.add(new CustomLesson("Bài 2: Family", "Thành viên trong gia đình"));
        lessonList.add(new CustomLesson("Bài 3: School", "Đồ dùng học tập"));

        // 2. Khởi tạo Adapter
        // Biến 'lesson' ở đây tự động hiểu là kiểu CustomLesson, sẽ hết báo đỏ
        adapter = new Lesson(lessonList, lesson -> {
            openLessonDetail(lesson);
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void openLessonDetail(CustomLesson lesson) {
        LessonDetailFragment detailFragment = new LessonDetailFragment();
        Bundle bundle = new Bundle();

        // Truyền dữ liệu sang màn hình chi tiết
        bundle.putString("lessonName", lesson.getTitle());
        bundle.putString("lessonId", lesson.getId());

        detailFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
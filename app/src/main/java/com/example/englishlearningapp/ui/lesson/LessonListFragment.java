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
// QUAN TRỌNG: Import LessonAdapter từ package data.model mà bạn đã tạo
import com.example.englishlearningapp.data.model.LessonAdapter;

import java.util.ArrayList;
import java.util.List;

public class LessonListFragment extends Fragment {

    private RecyclerView recyclerView;
    private LessonAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout fragment_lesson_list
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        // Ánh xạ RecyclerView
        recyclerView = view.findViewById(R.id.recyclerLessons);

        // Thiết lập LayoutManager (dạng danh sách đứng)
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo dữ liệu mẫu cho danh sách bài học
        List<String> lessonList = new ArrayList<>();
        lessonList.add("Bài 1: Greetings");
        lessonList.add("Bài 2: Family & Friends");
        lessonList.add("Bài 3: Daily Activities");
        lessonList.add("Bài 4: Food & Drinks");
        lessonList.add("Bài 5: At School");

        // Khởi tạo Adapter với dữ liệu và sự kiện Click
        adapter = new LessonAdapter(lessonList, lessonName -> {
            // Khi click vào một bài học, gọi hàm chuyển Fragment
            openLessonDetail(lessonName);
        });

        // Kết nối Adapter với RecyclerView
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Hàm xử lý chuyển sang màn hình Chi tiết bài học
     */
    private void openLessonDetail(String lessonName) {
        LessonDetailFragment detailFragment = new LessonDetailFragment();

        // Đóng gói dữ liệu (tên bài học) để gửi sang Fragment sau
        Bundle bundle = new Bundle();
        bundle.putString("lessonName", lessonName);
        detailFragment.setArguments(bundle);

        // Thực hiện thay thế Fragment hiện tại bằng DetailFragment
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    // addToBackStack giúp người dùng nhấn nút Back để quay lại danh sách
                    .addToBackStack(null)
                    .commit();
        }
    }
}
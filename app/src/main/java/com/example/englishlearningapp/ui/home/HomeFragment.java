package com.example.englishlearningapp.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.englishlearningapp.R;

public class HomeFragment extends Fragment {

    ListView listLesson;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout fragment_home đã sửa ở bước trước
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listLesson = view.findViewById(R.id.listLesson);

        String[] lessons = {
                "Bài 1: Greetings",
                "Bài 2: Family & Friends",
                "Bài 3: Daily Activities",
                "Bài 4: Food & Drinks"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                lessons
        );

        listLesson.setAdapter(adapter);

        // Bạn có thể tìm các nút Flashcard, Quiz... ở đây
        // view.findViewById(R.id.btnFlashcard).setOnClickListener(...);

        return view;
    }
}
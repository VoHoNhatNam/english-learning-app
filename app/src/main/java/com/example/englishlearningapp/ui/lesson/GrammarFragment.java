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
import com.example.englishlearningapp.data.model.GrammarLesson;

import java.util.ArrayList;
import java.util.List;

public class GrammarFragment extends Fragment {

    private RecyclerView rvGrammarLessons;
    private GrammarAdapter adapter;
    private List<GrammarLesson> lessonList;
    private TextView tabSystem, tabMyRoadmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grammar, container, false);

        initViews(view);
        setupData();
        setupTabs();

        return view;
    }

    private void initViews(View view) {
        rvGrammarLessons = view.findViewById(R.id.rvGrammarLessons);
        tabSystem = view.findViewById(R.id.tabSystem);
        tabMyRoadmap = view.findViewById(R.id.tabMyRoadmap);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        view.findViewById(R.id.btnCreateLesson).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tính năng tạo bài học đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupData() {
        lessonList = new ArrayList<>();
        lessonList.add(new GrammarLesson("1", "Thì hiện tại hoàn thành", "Cấu trúc Have/Has + V3/ed...", 1));
        lessonList.add(new GrammarLesson("2", "Câu điều kiện loại 3", "Cấu trúc If + Past Perfect...", 2));
        lessonList.add(new GrammarLesson("3", "Mệnh đề quan hệ", "Cách sử dụng Who, Whom,...", 0));
        lessonList.add(new GrammarLesson("4", "Câu bị động", "Chuyển đổi từ chủ động sa...", 0));

        adapter = new GrammarAdapter(lessonList, lesson -> {
            // Navigate to lesson detail or show toast
            Toast.makeText(getContext(), "Bắt đầu bài học: " + lesson.getTitle(), Toast.LENGTH_SHORT).show();
        });

        rvGrammarLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGrammarLessons.setAdapter(adapter);
    }

    private void setupTabs() {
        tabSystem.setOnClickListener(v -> {
            tabSystem.setBackgroundResource(R.drawable.bg_button);
            tabSystem.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.green_primary)));
            tabSystem.setTextColor(getResources().getColor(android.R.color.white));
            tabSystem.setTypeface(null, android.graphics.Typeface.BOLD);

            tabMyRoadmap.setBackground(null);
            tabMyRoadmap.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tabMyRoadmap.setTypeface(null, android.graphics.Typeface.NORMAL);
        });

        tabMyRoadmap.setOnClickListener(v -> {
            tabMyRoadmap.setBackgroundResource(R.drawable.bg_button);
            tabMyRoadmap.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.green_primary)));
            tabMyRoadmap.setTextColor(getResources().getColor(android.R.color.white));
            tabMyRoadmap.setTypeface(null, android.graphics.Typeface.BOLD);

            tabSystem.setBackground(null);
            tabSystem.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tabSystem.setTypeface(null, android.graphics.Typeface.NORMAL);
        });
    }
}
package com.example.englishlearningapp.ui.lesson;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.GrammarLesson;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GrammarFragment extends Fragment {

    private RecyclerView rvGrammarLessons;
    private GrammarAdapter adapter;
    private List<GrammarLesson> lessonList;
    private TextView tabSystem, tabMyRoadmap;
    private TabLayout tabLayoutLevels;
    private GrammarViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grammar, container, false);

        // Khởi tạo ViewModel với scope là Activity để giữ dữ liệu khi chuyển Fragment
        viewModel = new ViewModelProvider(requireActivity()).get(GrammarViewModel.class);

        initViews(view);
        setupRecyclerView();
        setupTabs();
        observeViewModel();

        // Chỉ lấy dữ liệu nếu cần (1 lần duy nhất)
        viewModel.fetchLessonsIfNeeded();

        return view;
    }

    private void initViews(View view) {
        rvGrammarLessons = view.findViewById(R.id.rvGrammarLessons);
        tabSystem = view.findViewById(R.id.tabSystem);
        tabMyRoadmap = view.findViewById(R.id.tabMyRoadmap);
        tabLayoutLevels = view.findViewById(R.id.tabLayoutLevels);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        view.findViewById(R.id.btnCreateLesson).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang làm mới dữ liệu từ Firebase...", Toast.LENGTH_SHORT).show();
            viewModel.forceRefresh();
        });
    }

    private void setupRecyclerView() {
        lessonList = new ArrayList<>();
        adapter = new GrammarAdapter(lessonList, lesson -> {
            LessonDetailFragment detailFragment = new LessonDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("lessonName", lesson.getTitle());
            bundle.putString("lessonId", lesson.getId());
            bundle.putString("lessonDescription", lesson.getDescription());
            bundle.putString("lessonContent", lesson.getContent());
            detailFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvGrammarLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGrammarLessons.setAdapter(adapter);
    }

    private void observeViewModel() {
        // Lắng nghe danh sách bài học
        viewModel.getAllLessons().observe(getViewLifecycleOwner(), lessons -> {
            if (lessons != null) {
                filterAndDisplay(tabLayoutLevels.getSelectedTabPosition(), lessons);
            }
        });

        // Lắng nghe trạng thái loading (có thể thêm ProgressBar nếu muốn)
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Xử lý hiện/ẩn loading UI ở đây
        });
    }

    private void filterAndDisplay(int levelIndex, List<GrammarLesson> allLessons) {
        String[] levels = {"Beginner", "Intermediate", "Advanced"};
        String targetLevel = levels[levelIndex];
        
        List<GrammarLesson> filtered = new ArrayList<>();
        for (GrammarLesson lesson : allLessons) {
            if (targetLevel.equals(lesson.getLevel())) {
                filtered.add(lesson);
            }
        }
        
        lessonList.clear();
        lessonList.addAll(filtered);
        adapter.notifyDataSetChanged();
    }

    private void setupTabs() {
        tabSystem.setOnClickListener(v -> {
            updateTabStyle(tabSystem, tabMyRoadmap);
            if (viewModel.getAllLessons().getValue() != null) {
                filterAndDisplay(tabLayoutLevels.getSelectedTabPosition(), viewModel.getAllLessons().getValue());
            }
        });

        tabMyRoadmap.setOnClickListener(v -> {
            updateTabStyle(tabMyRoadmap, tabSystem);
            lessonList.clear();
            adapter.notifyDataSetChanged();
        });

        if (tabLayoutLevels != null) {
            tabLayoutLevels.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (viewModel.getAllLessons().getValue() != null) {
                        filterAndDisplay(tab.getPosition(), viewModel.getAllLessons().getValue());
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}
                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void updateTabStyle(TextView selected, TextView unselected) {
        selected.setBackgroundResource(R.drawable.bg_button);
        selected.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        selected.setTextColor(Color.parseColor("#0066FF"));
        selected.setTypeface(null, Typeface.BOLD);
        unselected.setBackground(null);
        unselected.setTextColor(Color.parseColor("#74777F"));
        unselected.setTypeface(null, Typeface.NORMAL);
    }
}

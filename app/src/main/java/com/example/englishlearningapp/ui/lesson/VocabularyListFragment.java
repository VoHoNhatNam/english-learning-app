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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class VocabularyListFragment extends Fragment {

    private RecyclerView rvVocabularyList;
    private VocabularyListAdapter adapter;
    private List<Vocabulary> vocabularyList;
    private TextView tabSystem, tabMyVocab, txtVocabCount;
    private View btnCreateVocab;
    private TabLayout tabLayoutLevels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_list, container, false);

        initViews(view);
        setupData();
        setupTabs();

        return view;
    }

    private void initViews(View view) {
        rvVocabularyList = view.findViewById(R.id.rvVocabularyList);
        tabSystem = view.findViewById(R.id.tabSystem);
        tabMyVocab = view.findViewById(R.id.tabMyVocab);
        txtVocabCount = view.findViewById(R.id.txtVocabCount);
        btnCreateVocab = view.findViewById(R.id.btnCreateVocab);
        tabLayoutLevels = view.findViewById(R.id.tabLayoutLevels);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        btnCreateVocab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tính năng tạo từ vựng mới đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupData() {
        vocabularyList = new ArrayList<>();
        loadVocabByLevel(0); // Mặc định Beginner

        adapter = new VocabularyListAdapter(vocabularyList, vocabulary -> {
            // Navigate to Detail/Flashcard view
            VocabularyFragment detailFragment = new VocabularyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("word", vocabulary.getWord());
            detailFragment.setArguments(bundle);
            
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvVocabularyList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVocabularyList.setAdapter(adapter);
        
        txtVocabCount.setText("TRỐNG");
    }

    private void setupTabs() {
        tabSystem.setOnClickListener(v -> {
            tabSystem.setBackgroundResource(R.drawable.bg_button);
            tabSystem.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            tabSystem.setTextColor(Color.parseColor("#10B981"));
            tabSystem.setTypeface(null, Typeface.BOLD);

            tabMyVocab.setBackground(null);
            tabMyVocab.setTextColor(Color.parseColor("#6B7280"));
            tabMyVocab.setTypeface(null, Typeface.NORMAL);
        });

        tabMyVocab.setOnClickListener(v -> {
            tabMyVocab.setBackgroundResource(R.drawable.bg_button);
            tabMyVocab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            tabMyVocab.setTextColor(Color.parseColor("#10B981"));
            tabMyVocab.setTypeface(null, Typeface.BOLD);

            tabSystem.setBackground(null);
            tabSystem.setTextColor(Color.parseColor("#6B7280"));
            tabSystem.setTypeface(null, Typeface.NORMAL);
        });

        if (tabLayoutLevels != null) {
            tabLayoutLevels.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    loadVocabByLevel(tab.getPosition());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void loadVocabByLevel(int levelIndex) {
        vocabularyList.clear();
        if (levelIndex == 0) { // Beginner
            vocabularyList.add(new Vocabulary("Apple", "n", "Quả táo."));
            vocabularyList.add(new Vocabulary("Book", "n", "Quyển sách."));
        } else if (levelIndex == 1) { // Intermediate
            vocabularyList.add(new Vocabulary("Ephemeral", "adj", "Phù du, chóng tàn."));
            vocabularyList.add(new Vocabulary("Serendipity", "n", "Sự may mắn bất ngờ."));
        } else { // Advanced
            vocabularyList.add(new Vocabulary("Ubiquitous", "adj", "Có mặt khắp nơi."));
            vocabularyList.add(new Vocabulary("Pernicious", "adj", "Độc hại, nguy hiểm."));
        }
    }
}
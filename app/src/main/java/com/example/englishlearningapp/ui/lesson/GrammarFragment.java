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
import com.example.englishlearningapp.data.model.GrammarLesson;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarFragment extends Fragment {

    private RecyclerView rvGrammarLessons;
    private GrammarAdapter adapter;
    private List<GrammarLesson> lessonList;
    private TextView tabSystem, tabMyRoadmap;
    private TabLayout tabLayoutLevels;
    private FirebaseFirestore db;

    // Cache to store lessons for each level to avoid redundant fetches
    private final Map<Integer, List<GrammarLesson>> lessonsCache = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grammar, container, false);

        db = FirebaseFirestore.getInstance();
        initViews(view);
        setupData();
        setupTabs();

        return view;
    }

    private void initViews(View view) {
        rvGrammarLessons = view.findViewById(R.id.rvGrammarLessons);
        tabSystem = view.findViewById(R.id.tabSystem);
        tabMyRoadmap = view.findViewById(R.id.tabMyRoadmap);
        tabLayoutLevels = view.findViewById(R.id.tabLayoutLevels);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        view.findViewById(R.id.btnCreateLesson).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tính năng tạo bài học đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupData() {
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

        loadLessonsByLevel(0); // Default to Beginner
    }

    private void setupTabs() {
        tabSystem.setOnClickListener(v -> {
            updateTabStyle(tabSystem, tabMyRoadmap);
            loadLessonsByLevel(tabLayoutLevels.getSelectedTabPosition());
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
                    loadLessonsByLevel(tab.getPosition());
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

    private void loadLessonsByLevel(int levelIndex) {
        if (lessonsCache.containsKey(levelIndex)) {
            List<GrammarLesson> cachedLessons = lessonsCache.get(levelIndex);
            if (cachedLessons != null && !cachedLessons.isEmpty()) {
                lessonList.clear();
                lessonList.addAll(cachedLessons);
                adapter.notifyDataSetChanged();
                return;
            }
        }

        String tempLevel = "Beginner";
        if (levelIndex == 1) tempLevel = "Intermediate";
        else if (levelIndex == 2) tempLevel = "Advanced";
        
        final String level = tempLevel;
        final int finalLevelIndex = levelIndex;

        db.collection("grammar_lessons")
                .whereEqualTo("level", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<GrammarLesson> fetchedLessons = new ArrayList<>();
                    if (queryDocumentSnapshots.isEmpty()) {
                        fetchedLessons = getLocalData(finalLevelIndex);
                        // Save local data to Firebase for future use
                        saveLocalDataToFirebase(fetchedLessons, level);
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            GrammarLesson lesson = document.toObject(GrammarLesson.class);
                            lesson.setId(document.getId());
                            fetchedLessons.add(lesson);
                        }
                    }
                    
                    lessonsCache.put(finalLevelIndex, fetchedLessons);
                    lessonList.clear();
                    lessonList.addAll(fetchedLessons);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    List<GrammarLesson> localLessons = getLocalData(finalLevelIndex);
                    lessonsCache.put(finalLevelIndex, localLessons);
                    lessonList.clear();
                    lessonList.addAll(localLessons);
                    adapter.notifyDataSetChanged();
                });
    }

    private void saveLocalDataToFirebase(List<GrammarLesson> lessons, String level) {
        for (GrammarLesson lesson : lessons) {
            lesson.setLevel(level);
            db.collection("grammar_lessons").document(lesson.getId()).set(lesson);
        }
    }

    private List<GrammarLesson> getLocalData(int levelIndex) {
        List<GrammarLesson> localList = new ArrayList<>();
        if (levelIndex == 0) { // Beginner
            String lesson1Content = "1. Mục tiêu\n" +
                    "Giới thiệu bản thân, nghề nghiệp, thói quen\n" +
                    "Hiểu sự khác nhau giữa động từ thường và to be\n\n" +
                    "2. Cách dùng\n\n" +
                    "Thì hiện tại đơn dùng để:\n\n" +
                    "Diễn tả sự thật hiển nhiên → The sun rises in the east\n" +
                    "Diễn tả thói quen lặp lại → I go to school every day\n" +
                    "Diễn tả trạng thái → I feel tired\n\n" +
                    "3. Cấu trúc\n" +
                    "a. Động từ “TO BE”\n" +
                    "Khẳng định: S + am/is/are + N/Adj\n" +
                    "Phủ định: S + am/is/are + not\n" +
                    "Câu hỏi: Am/Is/Are + S ?\n\n" +
                    "👉 Ví dụ:\n\n" +
                    "I am a student\n" +
                    "She is happy\n" +
                    "Are you tired?\n\n" +
                    "b. Động từ thường\n" +
                    "Khẳng định: S + V(s/es)\n" +
                    "Phủ định: S + do/does not + V\n" +
                    "Câu hỏi: Do/Does + S + V?\n\n" +
                    "👉 Ví dụ:\n\n" +
                    "She works in an office\n" +
                    "He doesn’t like coffee\n" +
                    "Do you play football?\n\n" +
                    "4. Lưu ý quan trọng\n" +
                    "He/She/It → thêm s/es\n" +
                    "go → goes, study → studies\n" +
                    "không dùng “to be” + động từ thường ❌\n\n" +
                    "5. Lỗi sai phổ biến\n\n" +
                    "❌ She go to school\n" +
                    "✅ She goes to school\n\n" +
                    "❌ I am go to work\n" +
                    "✅ I go to work";

            GrammarLesson lesson1 = new GrammarLesson("1", "BÀI 1: HIỆN TẠI ĐƠN (PRESENT SIMPLE) \u0026 “TO BE”", "Giới thiệu bản thân, nghề nghiệp, thói quen\nHiểu sự khác nhau giữa động từ thường và to be", 1);
            lesson1.setContent(lesson1Content);
            localList.add(lesson1);

            String lesson2Content = "1. Mục tiêu\n" +
                    "Diễn tả hành động đang xảy ra\n" +
                    "2. Cách dùng\n" +
                    "Hành động đang diễn ra ngay lúc nói\n" +
                    "Hành động tạm thời\n\n" +
                    "👉 Ví dụ:\n\n" +
                    "I am studying now\n" +
                    "She is working at the moment\n" +
                    "3. Cấu trúc\n\n" +
                    "S + am/is/are + V-ing\n\n" +
                    "4. Dấu hiệu nhận biết\n" +
                    "now\n" +
                    "right now\n" +
                    "at the moment\n" +
                    "5. So sánh với hiện tại đơn\n" +
                    "I work every day (thói quen)\n" +
                    "I am working now (đang diễn ra)\n" +
                    "6. Lỗi sai\n\n" +
                    "❌ I am work\n" +
                    "✅ I am working";

            GrammarLesson lesson2 = new GrammarLesson("2", "BÀI 2: HIỆN TẠI TIẾP DIỄN (PRESENT CONTINUOUS)", "Diễn tả hành động đang xảy ra", 2);
            lesson2.setContent(lesson2Content);
            localList.add(lesson2);

            String lesson3Content = "1. Mục tiêu\n" +
                    "Biết dùng số ít/số nhiều\n" +
                    "Dùng some/any\n" +
                    "2. Danh từ số nhiều\n" +
                    "book → books\n" +
                    "box → boxes\n" +
                    "child → children\n" +
                    "3. Mạo từ\n" +
                    "a/an → chưa xác định\n" +
                    "the → xác định\n" +
                    "4. Some / Any\n" +
                    "Some → câu khẳng định\n" +
                    "Any → phủ định \u0026 câu hỏi\n\n" +
                    "👉 Ví dụ:\n\n" +
                    "I have some apples\n" +
                    "I don’t have any money\n" +
                    "5. Lỗi sai\n\n" +
                    "❌ I have some money?\n" +
                    "✅ Do you have any money?";

            GrammarLesson lesson3 = new GrammarLesson("3", "BÀI 3: DANH TỪ \u0026 LƯỢNG TỪ", "Biết dùng số ít/số nhiều. Dùng some/any", 0);
            lesson3.setContent(lesson3Content);
            localList.add(lesson3);

            String lesson4Content = "1. Mục tiêu\n" +
                    "Kể chuyện quá khứ\n" +
                    "2. Cách dùng\n" +
                    "Hành động đã xảy ra và kết thúc\n" +
                    "3. Cấu trúc\n" +
                    "a. Động từ thường\n" +
                    "S + V-ed\n" +
                    "b. Bất quy tắc\n" +
                    "go → went\n" +
                    "see → saw\n" +
                    "c. Phủ định / câu hỏi\n" +
                    "S + did not + V\n" +
                    "Did + S + V?\n\n" +
                    "👉 Ví dụ:\n\n" +
                    "I visited my friend yesterday\n" +
                    "She went to school\n" +
                    "5. Lỗi sai\n\n" +
                    "❌ I did went\n" +
                    "✅ I went";

            GrammarLesson lesson4 = new GrammarLesson("4", "BÀI 4: QUÁ KHỨ ĐƠN (PAST SIMPLE)", "Kể chuyện quá khứ", 0);
            lesson4.setContent(lesson4Content);
            localList.add(lesson4);

            String lesson5Content = "1. Mục tiêu\n" +
                    "Biết dùng can, could, should\n" +
                    "2. Cấu trúc chung\n\n" +
                    "S + Modal + V (nguyên thể)\n\n" +
                    "3. Cách dùng\n" +
                    "CAN\n" +
                    "Khả năng: I can swim\n" +
                    "Xin phép: Can I go out?\n" +
                    "COULD\n" +
                    "Lịch sự hơn can\n" +
                    "Quá khứ của can\n" +
                    "SHOULD\n" +
                    "Lời khuyên\n" +
                    "👉 You should study more\n" +
                    "4. Lỗi sai\n\n" +
                    "❌ She can swims\n" +
                    "✅ She can swim";

            GrammarLesson lesson5 = new GrammarLesson("5", "BÀI 5: ĐỘNG TỪ KHUYẾT THIẾU (MODAL VERBS)", "Biết dùng can, could, should", 0);
            lesson5.setContent(lesson5Content);
            localList.add(lesson5);
        }
        return localList;
    }
}

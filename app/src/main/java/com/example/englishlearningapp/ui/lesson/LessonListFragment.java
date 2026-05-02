package com.example.englishlearningapp.ui.lesson;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Lesson;
import com.example.englishlearningapp.data.model.ReadingArticle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LessonListFragment extends Fragment {

    private RecyclerView recyclerView;
    private Lesson adapter;
    private List<CustomLesson> lessonList;
    private ProgressBar progressBar;
    private TabLayout tabLayoutLevels;
    private TextView tabSystem, tabMyRoadmap;
    private ImageButton btnBack, btnSettings;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private boolean isSystemTab = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews(view);
        setupTabs(view);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lessonList = new ArrayList<>();
        adapter = new Lesson(lessonList, lesson -> openReadingDetail(lesson));
        recyclerView.setAdapter(adapter);

        loadData();
        
        // Ẩn Bottom Navigation khi vào màn hình này
        setBottomNavigationVisibility(View.GONE);

        return view;
    }

    private void setBottomNavigationVisibility(int visibility) {
        if (getActivity() instanceof MainActivity) {
            BottomNavigationView navView = getActivity().findViewById(R.id.bottomNavigationView);
            if (navView != null) {
                navView.setVisibility(visibility);
            }
            View navContainer = getActivity().findViewById(R.id.bottomNavContainer);
            if (navContainer != null) {
                navContainer.setVisibility(visibility);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hiện lại Bottom Navigation khi thoát màn hình này
        setBottomNavigationVisibility(View.VISIBLE);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerLessons);
        progressBar = view.findViewById(R.id.progressBar);
        tabLayoutLevels = view.findViewById(R.id.tabLayoutLevels);
        tabSystem = view.findViewById(R.id.tabSystem);
        tabMyRoadmap = view.findViewById(R.id.tabMyRoadmap);
        btnBack = view.findViewById(R.id.btnBack);
        btnSettings = view.findViewById(R.id.btnSettings);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Đang đẩy dữ liệu lên Firebase...", Toast.LENGTH_SHORT).show();
                seedDataToFirebase();
            });
        }
    }

    private void setupTabs(View view) {
        tabSystem.setOnClickListener(v -> {
            if (isSystemTab) return;
            isSystemTab = true;
            updateTabUI();
            loadData();
        });

        tabMyRoadmap.setOnClickListener(v -> {
            if (!isSystemTab) return;
            isSystemTab = false;
            updateTabUI();
            loadData();
        });

        tabLayoutLevels.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadData();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void updateTabUI() {
        if (isSystemTab) {
            tabSystem.setBackgroundResource(R.drawable.bg_button);
            tabSystem.setBackgroundTintList(null);
            tabSystem.setTextColor(Color.parseColor("#10B981"));
            tabMyRoadmap.setBackground(null);
            tabMyRoadmap.setTextColor(Color.parseColor("#6B7280"));
            tabLayoutLevels.setVisibility(View.VISIBLE);
        } else {
            tabMyRoadmap.setBackgroundResource(R.drawable.bg_button);
            tabMyRoadmap.setBackgroundTintList(null);
            tabMyRoadmap.setTextColor(Color.parseColor("#10B981"));
            tabSystem.setBackground(null);
            tabSystem.setTextColor(Color.parseColor("#6B7280"));
            tabLayoutLevels.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        if (isSystemTab) {
            String level = "Beginner";
            if (tabLayoutLevels.getSelectedTabPosition() == 1) level = "Intermediate";
            else if (tabLayoutLevels.getSelectedTabPosition() == 2) level = "Advanced";
            loadLessonsByLevel(level);
        } else {
            loadMyLessons();
        }
    }

    private void loadLessonsByLevel(String levelName) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        db.collection("reading_articles")
                .whereEqualTo("level", levelName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    lessonList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ReadingArticle article = doc.toObject(ReadingArticle.class);
                        lessonList.add(new CustomLesson(article.getTitle(), article.getAiInsights(), doc.getId(), article.getContent()));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Lỗi tải: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadMyLessons() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
        if (userId.isEmpty()) return;
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        db.collection("custom_lessons")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    lessonList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String title = doc.getString("title");
                        String desc = doc.getString("description");
                        String content = doc.getString("readingContent");
                        lessonList.add(new CustomLesson(title, desc, doc.getId(), content));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Lỗi tải bài học của tôi", Toast.LENGTH_SHORT).show();
                });
    }

    private void openReadingDetail(CustomLesson lesson) {
        if (isSystemTab) {
            db.collection("reading_articles").document(lesson.getId()).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            ReadingArticle article = doc.toObject(ReadingArticle.class);
                            article.setId(doc.getId());
                            navigateToDetail(article);
                        }
                    });
        } else {
            ReadingArticle article = new ReadingArticle();
            article.setId(lesson.getId());
            article.setTitle(lesson.getTitle());
            article.setContent(lesson.getReadingContent());
            article.setAiInsights(lesson.getDescription());
            article.setAuthor("Me");
            article.setLevel("Custom");
            navigateToDetail(article);
        }
    }

    private void navigateToDetail(ReadingArticle article) {
        ReadingDetailFragment fragment = new ReadingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", article);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void seedDataToFirebase() {
        List<ReadingArticle> articles = new ArrayList<>();
        
        articles.add(new ReadingArticle("reading_01", "Reading 1: My Family and Their Jobs", "Tom", "Education", "3 min read",
                "Hello, my name is Tom. I am a student, and I study at a school in my city. I am friendly and hard-working, and I like my life.\n\nI live with my family. My father is a doctor. He works in a hospital, and he is very busy. My mother is an office worker. She works in a company. She is kind and friendly.\n\nI also have a sister. She is a salesperson. She works in a shop. She is creative and friendly.\n\nWe are a happy family. We love each other very much.",
                "Xin chào, tôi tên là Tom. Tôi là học sinh và tôi học ở một trường trong thành phố của tôi. Tôi thân thiện và chăm chỉ, và tôi thích cuộc sống của mình.\n\nTôi sống cùng gia đình. Bố tôi là bác sĩ. Ông làm việc ở bệnh viện và rất bận rộn. Mẹ tôi là nhân viên văn phòng. Bà làm việc ở công ty. Bà rất tốt bụng và thân thiện.\n\nTôi cũng có một chị gái. Chị là nhân viên bán hàng. Chị làm việc ở cửa hàng. Chị rất sáng tạo và thân thiện.\n\nChúng tôi là một gia đình hạnh phúc. Chúng tôi rất yêu thương nhau.",
                "Beginner", "Học từ vựng về gia đình và nghề nghiệp cơ bản."));

        articles.add(new ReadingArticle("reading_02", "Reading 2: My Daily Life", "Anna", "Education", "4 min read",
                "My name is Anna. Every day, I wake up at 6 a.m. I get up, brush my teeth, and take a shower. Then, I have breakfast. I eat bread and drink milk.\n\nAfter that, I go to school. I study and talk with my friends. I like reading books and listening to music.\n\nNow, I am at home. I am studying English. My brother is watching TV, and my mother is cooking dinner.\n\nI like my daily life because it is simple and good.",
                "Tôi tên là Anna. Mỗi ngày tôi thức dậy lúc 6 giờ sáng. Tôi ra khỏi giường, đánh răng và tắm. Sau đó, tôi ăn sáng. Tôi ăn bánh mì và uống sữa.\n\nSau đó, tôi đi học. Tôi học và nói chuyện với bạn bè. Tôi thích đọc sách và nghe nhạc.\n\nBây giờ tôi đang ở nhà. Tôi đang học tiếng Anh. Anh trai tôi đang xem TV và mẹ tôi đang nấu ăn.\n\nTôi thích cuộc sống hàng ngày của mình vì nó đơn giản và tốt.",
                "Beginner", "Học từ vựng về thói quen hàng ngày."));

        for (ReadingArticle article : articles) {
            db.collection("reading_articles").add(article);
        }
    }
}

package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Lesson;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LessonListFragment extends Fragment {

    private RecyclerView recyclerView;
    private Lesson adapter;
    private List<CustomLesson> lessonList;
    private ProgressBar progressBar;
    private TabLayout tabLayoutLevels;
    private ImageButton btnBack;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recyclerLessons);
        progressBar = view.findViewById(R.id.progressBar);
        tabLayoutLevels = view.findViewById(R.id.tabLayoutLevels);
        btnBack = view.findViewById(R.id.btnBack);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lessonList = new ArrayList<>();
        adapter = new Lesson(lessonList, lesson -> openLessonDetail(lesson));
        recyclerView.setAdapter(adapter);

        // Xử lý nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            });
        }

        // Xử lý sự kiện khi chuyển Tab cấp độ
        tabLayoutLevels.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadLessonsByTabIndex(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        fetchUserLevelAndLoadLessons();

        return view;
    }

    private void fetchUserLevelAndLoadLessons() {
        String uid = mAuth.getUid();
        if (uid == null) return;

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    if (documentSnapshot.exists()) {
                        String level = documentSnapshot.getString("englishLevel");
                        selectTabByLevelName(level);
                    } else {
                        // Mặc định chọn tab Beginner (Index 0)
                        if (tabLayoutLevels.getTabCount() > 0) {
                            tabLayoutLevels.getTabAt(0).select();
                            loadLessonsByTabIndex(0);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Mặc định nếu lỗi
                    if (tabLayoutLevels.getTabCount() > 0) {
                        tabLayoutLevels.getTabAt(0).select();
                    }
                });
    }

    private void selectTabByLevelName(String level) {
        if (level == null) {
            if (tabLayoutLevels.getTabCount() > 0) {
                tabLayoutLevels.getTabAt(0).select();
            }
            return;
        }

        int tabIndex = 0;
        if (level.contains("Intermediate") || level.contains("Trung bình")) {
            tabIndex = 1;
        } else if (level.contains("Advanced") || level.contains("Nâng cao")) {
            tabIndex = 2;
        }

        if (tabLayoutLevels.getTabCount() > tabIndex) {
            TabLayout.Tab tab = tabLayoutLevels.getTabAt(tabIndex);
            if (tab != null) {
                tab.select();
            }
        }
        // Gọi load dữ liệu cho tab đã chọn
        loadLessonsByTabIndex(tabIndex);
    }

    private void loadLessonsByTabIndex(int index) {
        lessonList.clear();
        switch (index) {
            case 0:
                setupBeginnerLessons();
                break;
            case 1:
                setupIntermediateLessons();
                break;
            case 2:
                setupAdvancedLessons();
                break;
            default:
                setupBeginnerLessons();
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void setupBeginnerLessons() {
        lessonList.add(createLesson("101", "Bài 1: Greetings & Introductions", "Chào hỏi, giới thiệu bản thân và động từ To-be."));
        lessonList.add(createLesson("102", "Bài 2: Alphabet & Numbers", "Bảng chữ cái, số đếm và mạo từ A/An."));
        lessonList.add(createLesson("103", "Bài 3: My Family & Friends", "Gia đình, bạn bè và tính từ sở hữu."));
        lessonList.add(createLesson("104", "Bài 4: My Daily Routine", "Hoạt động hằng ngày và thì Hiện tại đơn."));
        lessonList.add(createLesson("105", "Bài 5: Food & Favorite Drinks", "Thức ăn, đồ uống và danh từ đếm được/không đếm được."));
        lessonList.add(createLesson("106", "Bài 6: Body Parts & Health", "Bộ phận cơ thể và cách nói về sức khỏe với Have/Has."));
        lessonList.add(createLesson("107", "Bài 7: School & University Life", "Trường học và giới từ chỉ vị trí (In, On, At)."));
        lessonList.add(createLesson("108", "Bài 8: Colors & Shapes", "Màu sắc, hình dạng và vị trí của tính từ."));
        lessonList.add(createLesson("109", "Bài 9: Weather & The Four Seasons", "Thời tiết, các mùa và cấu ngữ It is..."));
        lessonList.add(createLesson("110", "Bài 10: My Hobbies & Interests", "Sở thích và cấu trúc Like/Love + V-ing."));
        lessonList.add(createLesson("111", "Bài 11: Telling Time & Dates", "Cách nói giờ, ngày tháng và giới từ thời gian."));
        lessonList.add(createLesson("112", "Bài 12: Places in My City", "Các địa điểm trong thành phố và cấu trúc There is/There are."));
    }

    private void setupIntermediateLessons() {
        lessonList.add(createLesson("201", "Bài 1: University Life & Campus", "Campus, Lecture, Semester... Ngữ pháp: Hiện tại tiếp diễn (Future arrangement)."));
        lessonList.add(createLesson("202", "Bài 2: Health & Balanced Diet", "Nutrition, Ingredient, Calorie... Ngữ pháp: Modal verbs (Should, Ought to, Had better)."));
        lessonList.add(createLesson("203", "Bài 3: Travel & Cultural Diversity", "Destination, Souvenir, Tradition... Ngữ pháp: Quá khứ đơn & Quá khứ tiếp diễn."));
        lessonList.add(createLesson("204", "Bài 4: Environment & Sustainability", "Pollution, Recycle, Renewable... Ngữ pháp: Câu điều kiện loại 1."));
        lessonList.add(createLesson("205", "Bài 5: Media & Social Networks", "Platform, Influencer, Content... Ngữ pháp: Câu so sánh hơn & so sánh nhất."));
        lessonList.add(createLesson("206", "Bài 6: Work-Life Balance", "Stress, Pressure, Relax... Ngữ pháp: Gerund (V-ing) & To-Infinitive."));
        lessonList.add(createLesson("207", "Bài 7: Inventions & Discoveries", "Invention, Discovery, Scientist... Ngữ pháp: Hiện tại hoàn thành (Trải nghiệm)."));
        lessonList.add(createLesson("208", "Bài 8: Art & Creativity", "Creative, Inspiration, Artwork... Ngữ pháp: Trạng từ chỉ cách thức."));
        lessonList.add(createLesson("209", "Bài 9: Shopping & Consumerism", "Online, Customer, Service... Ngữ pháp: Cấu trúc Too / Enough."));
        lessonList.add(createLesson("210", "Bài 10: Future Plans & Ambitions", "Goal, Career, Success... Ngữ pháp: Tương lai đơn (Will) & Tương lai gần (Be going to)."));
        lessonList.add(createLesson("211", "Bài 11: History & World Landmarks (VIP)", "Ancient, Landmark, Monument... Ngữ pháp: Cấu trúc Used to."));
        lessonList.add(createLesson("212", "Bài 12: Psychology & Emotions (VIP)", "Emotion, Feeling, Anxiety... Ngữ pháp: Câu bị động (Passive Voice)."));
        lessonList.add(createLesson("213", "Bài 13: Global Issues (VIP)", "Poverty, Education, Hunger... Ngữ pháp: Câu điều kiện loại 2 (Giả định)."));
        lessonList.add(createLesson("214", "Bài 14: Business & Entrepreneurship (VIP)", "Startup, Market, Profit... Ngữ pháp: Lời nói gián tiếp (Reported Speech)."));
        lessonList.add(createLesson("215", "Bài 15: Science & Technology (VIP)", "Experiment, Observation, Analysis... Ngữ pháp: Mệnh đề quan hệ (Who, Which, That)."));
        lessonList.add(createLesson("216", "Bài 16: Giving Opinions & Debating (VIP)", "Opinion, Agree, Disagree... Ngữ pháp: Trạng từ liên kết (However, Therefore)."));
        lessonList.add(createLesson("217", "Bài 17: Urban Life vs Rural Life (VIP)", "Modern, Facilities, Transport... Ngữ pháp: So sánh As...as & The same as."));
        lessonList.add(createLesson("218", "Bài 18: Summary & Final Review (VIP)", "Tổng kết toàn bộ kiến thức. Chuẩn bị cho Bài Test thăng cấp lên Nâng cao."));
    }

    private void setupAdvancedLessons() {
        lessonList.add(createLesson("301", "Bài 1: Computer Hardware & Architecture", "Processor, Motherboard, RAM... Ngữ pháp: Câu bị động ở các thì hoàn thành."));
        lessonList.add(createLesson("302", "Bài 2: Software Categories & Operating Systems", "Kernel, Interface, Open-source... Ngữ pháp: Mệnh đề quan hệ."));
        lessonList.add(createLesson("303", "Bài 3: Programming Languages & Logic (VIP)", "Syntax, Algorithm, Debugging... Ngữ pháp: Câu điều kiện loại 3."));
        lessonList.add(createLesson("304", "Bài 4: Web Development: Frontend & Backend (VIP)", "Responsive, API, Integration... Ngữ pháp: Danh động từ & Động từ nguyên mẫu."));
        lessonList.add(createLesson("305", "Bài 5: Database Management Systems (VIP)", "Schema, Query, NoSQL... Ngữ pháp: Wh-words + Ever."));
        lessonList.add(createLesson("306", "Bài 6: Cloud Computing & Services (VIP)", "Virtualization, SaaS, PaaS, IaaS... Ngữ pháp: Thể giả định (Subjunctive)."));
        lessonList.add(createLesson("307", "Bài 7: Artificial Intelligence & Machine Learning (VIP)", "Neural network, Dataset, Prompt... Ngữ pháp: Cấu trúc đảo ngữ (Inversion)."));
        lessonList.add(createLesson("308", "Bài 8: Cybersecurity & Data Protection (VIP)", "Encryption, Authentication, Firewall... Ngữ pháp: Lời nói gián tiếp phức tạp."));
        lessonList.add(createLesson("309", "Bài 9: IT Project Management & Agile (VIP)", "Sprint, Scrum, Backlog... Ngữ pháp: Phân từ rút gọn (Participle Clauses)."));
        lessonList.add(createLesson("310", "Bài 10: Professional Communication for IT (VIP)", "Technical report, Troubleshooting... Ôn tập tổng hợp & Kỹ năng viết Email."));
    }

    private CustomLesson createLesson(String id, String title, String description) {
        CustomLesson lesson = new CustomLesson(title, description);
        lesson.setId(id);
        return lesson;
    }

    private void openLessonDetail(CustomLesson lesson) {
        LessonDetailFragment detailFragment = new LessonDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lessonName", lesson.getTitle());
        bundle.putString("lessonId", lesson.getId());
        detailFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
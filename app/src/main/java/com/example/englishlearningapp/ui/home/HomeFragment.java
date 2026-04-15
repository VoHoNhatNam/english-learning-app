package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;
import com.example.englishlearningapp.data.model.Lesson;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.example.englishlearningapp.ui.lesson.FillWordFragment;
import com.example.englishlearningapp.ui.lesson.FlashcardFragment;
import com.example.englishlearningapp.ui.lesson.GrammarFragment;
import com.example.englishlearningapp.ui.lesson.VocabularyFragment;
import com.example.englishlearningapp.ui.mission.MissionFragment;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    // --- Biến từ nhánh phát triển Lộ trình & Lịch học (HEAD) ---
    private TextView tvUserName, tvCurrentRoadmap;
    private final TextView[] tvDays = new TextView[7];
    private final View[] bars = new View[7];
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // --- Biến từ nhánh phát triển Danh sách bài học (Develop) ---
    private RecyclerView rvLessons;
    private ProgressBar progressBar;
    private View lnFlashcard, lnQuiz, lnChat, lnMore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews(view);

        // Gộp logic hiển thị: Vừa hiển thị tên (HEAD), vừa hiển thị Progress Bar (Develop)
        setupUserDisplayAndProgress();

        // Logic của HEAD
        loadUserRoadmap();
        updateRealDatesAndBars();
        setupHeader(view);

        // Logic của Develop
        if (rvLessons != null) {
            setupLessonList();
        }

        setupButtons();

        return view;
    }

    private void initViews(View view) {
        // Ánh xạ chung
        lnFlashcard = view.findViewById(R.id.lnFlashcard);
        lnQuiz = view.findViewById(R.id.lnQuiz);
        lnChat = view.findViewById(R.id.lnChat);
        lnMore = view.findViewById(R.id.lnMore);
        tvUserName = view.findViewById(R.id.tvUserName);

        // Ánh xạ riêng của HEAD
        tvCurrentRoadmap = view.findViewById(R.id.tvCurrentRoadmap);
        tvDays[0] = view.findViewById(R.id.tvDay1);
        tvDays[1] = view.findViewById(R.id.tvDay2);
        tvDays[2] = view.findViewById(R.id.tvDay3);
        tvDays[3] = view.findViewById(R.id.tvDay4);
        tvDays[4] = view.findViewById(R.id.tvDay5);
        tvDays[5] = view.findViewById(R.id.tvDay6);
        tvDays[6] = view.findViewById(R.id.tvDay7);
        bars[0] = view.findViewById(R.id.bar1);
        bars[1] = view.findViewById(R.id.bar2);
        bars[2] = view.findViewById(R.id.bar3);
        bars[3] = view.findViewById(R.id.bar4);
        bars[4] = view.findViewById(R.id.bar5);
        bars[5] = view.findViewById(R.id.bar6);
        bars[6] = view.findViewById(R.id.bar7);

        // Ánh xạ riêng của Develop
        rvLessons = view.findViewById(R.id.rvLessons);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupHeader(View view) {
        View btnMission = view.findViewById(R.id.btnHeaderMission);
        View badgeMission = view.findViewById(R.id.viewMissionBadge);

        if (btnMission != null) {
            Animation pulse = AnimationUtils.loadAnimation(getContext(), R.anim.pulse);
            if (badgeMission != null) {
                badgeMission.startAnimation(pulse);
            }

            btnMission.setOnClickListener(v -> {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.logo_scale));
                navigateToFragment(new MissionFragment());
            });
        }
    }

    private void loadUserRoadmap() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && tvCurrentRoadmap != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String level = document.getString("englishLevel");
                                if (level != null && !level.isEmpty()) {
                                    String formattedLevel = level.substring(0, 1).toUpperCase() + level.substring(1);
                                    tvCurrentRoadmap.setText("Lộ trình: " + formattedLevel);
                                } else {
                                    tvCurrentRoadmap.setText("Lộ trình: Mới bắt đầu");
                                }
                            } else {
                                tvCurrentRoadmap.setText("Lộ trình: Mới bắt đầu");
                            }
                        } else {
                            Log.e(TAG, "Lỗi lấy lộ trình: ", task.getException());
                            tvCurrentRoadmap.setText("Lộ trình: Mới bắt đầu");
                        }
                    });
        }
    }

    private void updateRealDatesAndBars() {
        if (tvDays[0] == null || bars[0] == null) return; // Tránh lỗi nếu XML chưa cập nhật

        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int daysToSubtract = (currentDayOfWeek == Calendar.SUNDAY) ? 6 : (currentDayOfWeek - Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract);

        String[] dayNames = {"TH 2", "TH 3", "TH 4", "TH 5", "TH 6", "TH 7", "CN"};
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            tvDays[i].setText(dayNames[i] + "\n" + dayOfMonth);

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {

                tvDays[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.green_primary));
                tvDays[i].setTypeface(null, android.graphics.Typeface.BOLD);
                bars[i].setBackgroundResource(R.drawable.bg_bar_chart_active);
            } else {
                tvDays[i].setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
                tvDays[i].setTypeface(null, android.graphics.Typeface.NORMAL);
                bars[i].setBackgroundResource(R.drawable.bg_bar_chart);
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void setupUserDisplayAndProgress() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (tvUserName != null) {
            if (user != null) {
                String name = user.getDisplayName();
                tvUserName.setText((name != null && !name.isEmpty()) ? "Chào mừng trở lại,\n" + name + "!" : "Chào mừng trở lại,\nNgười dùng!");
            } else {
                tvUserName.setText("Chào mừng trở lại,\nNgười dùng!");
            }
        }

        if (progressBar != null) {
            progressBar.setProgress(65); // Tạm thời hardcode theo nhánh develop
        }
    }

    private void setupLessonList() {
        List<CustomLesson> lessonData = new ArrayList<>();
        lessonData.add(new CustomLesson("Bài 1: Greetings", "Học cách chào hỏi cơ bản"));
        lessonData.add(new CustomLesson("Bài 2: Family & Friends", "Từ vựng về gia đình"));
        lessonData.add(new CustomLesson("Bài 3: Daily Activities", "Hoạt động thường ngày"));
        lessonData.add(new CustomLesson("Bài 4: Food & Drinks", "Ẩm thực và đồ uống"));

        Lesson adapter = new Lesson(lessonData, lesson -> {
            navigateToFragment(new VocabularyFragment());
        });

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLessons.setHasFixedSize(true);
        rvLessons.setAdapter(adapter);
    }

    private void setupButtons() {
        if (lnFlashcard != null) lnFlashcard.setOnClickListener(v -> navigateToFragment(new FlashcardFragment())); // Trộn Flashcard của Develop
        if (lnQuiz != null) lnQuiz.setOnClickListener(v -> navigateToFragment(new QuizFragment()));
        if (lnChat != null) lnChat.setOnClickListener(v -> navigateToFragment(new ChatFragment()));
        if (lnMore != null) lnMore.setOnClickListener(v -> navigateToFragment(new FillWordFragment()));
    }

    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.logo_scale, android.R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
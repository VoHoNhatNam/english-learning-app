package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.englishlearningapp.ui.lesson.VocabularyFragment;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvLessons;
    private View lnFlashcard, lnQuiz, lnChat, lnMore;
    private ProgressBar progressBar;
    private TextView tvUserName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp layout fragment_home
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 2. Ánh xạ các View từ XML
        initViews(view);
        
        // 3. Thiết lập tiến độ và tên người dùng từ Firebase
        setupProgress();
        
        // 4. Thiết lập danh sách bài học (Dùng RecyclerView tối ưu hiệu năng)
        setupLessonList();
        
        // 5. Thiết lập sự kiện click cho các nút chức năng
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        rvLessons = view.findViewById(R.id.rvLessons);
        lnFlashcard = view.findViewById(R.id.lnFlashcard);
        lnQuiz = view.findViewById(R.id.lnQuiz);
        lnChat = view.findViewById(R.id.lnChat);
        lnMore = view.findViewById(R.id.lnMore);
        progressBar = view.findViewById(R.id.progressBar);
        tvUserName = view.findViewById(R.id.tvUserName);
    }

    private void setupProgress() {
        // Lấy tên người dùng thực tế từ Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            tvUserName.setText((name != null && !name.isEmpty()) ? name + "!" : "Người dùng!");
        } else {
            tvUserName.setText("Người dùng!");
        }

        progressBar.setProgress(65);
    }

    private void setupLessonList() {
        List<CustomLesson> lessonData = new ArrayList<>();
        lessonData.add(new CustomLesson("Bài 1: Greetings", "Học cách chào hỏi cơ bản"));
        lessonData.add(new CustomLesson("Bài 2: Family & Friends", "Từ vựng về gia đình"));
        lessonData.add(new CustomLesson("Bài 3: Daily Activities", "Hoạt động thường ngày"));
        lessonData.add(new CustomLesson("Bài 4: Food & Drinks", "Ẩm thực và đồ uống"));

        Lesson adapter = new Lesson(lessonData, lesson -> {
            // Khi nhấn vào bài học, dẫn thẳng tới VocabularyFragment để học từ vựng bài đó
            navigateToFragment(new VocabularyFragment());
        });

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLessons.setHasFixedSize(true);
        rvLessons.setAdapter(adapter);
    }

    private void setupButtons() {
        // Chuyển sang màn hình Flashcard (Lật thẻ)
        lnFlashcard.setOnClickListener(v -> navigateToFragment(new FlashcardFragment()));
        
        // Chuyển sang màn hình Trắc nghiệm (Quiz)
        lnQuiz.setOnClickListener(v -> navigateToFragment(new QuizFragment()));
        
        // Chuyển sang màn hình Chat AI/Giao tiếp
        lnChat.setOnClickListener(v -> navigateToFragment(new ChatFragment()));
        
        // Chuyển sang màn hình Điền từ (Gắn tạm vào nút More)
        lnMore.setOnClickListener(v -> navigateToFragment(new FillWordFragment()));
    }

    /**
     * Hàm hỗ trợ chuyển đổi Fragment mượt mà
     */
    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            
            // Hiệu ứng chuyển cảnh
            transaction.setCustomAnimations(R.anim.logo_scale, android.R.anim.fade_out);
            
            // Thay thế fragment hiện tại. Lưu ý: R.id.fragment_container phải khớp với activity_main.xml
            transaction.replace(R.id.fragment_container, fragment);
            
            // Lưu vào BackStack để người dùng nhấn nút Back có thể quay về Home thay vì thoát App
            transaction.addToBackStack(null);
            
            transaction.commit();
        }
    }
}
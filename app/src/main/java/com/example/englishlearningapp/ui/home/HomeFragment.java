package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.example.englishlearningapp.ui.lesson.VocabularyFragment;
import com.example.englishlearningapp.ui.lesson.FlashcardFragment; // Thêm import này
import com.example.englishlearningapp.ui.lesson.FillWordFragment;  // Thêm import này

public class HomeFragment extends Fragment {

    private ListView listLesson;
    private Button btnFlashcard, btnQuiz, btnFill, btnListening;
    private EditText searchInput;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Nạp layout fragment_home
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 2. Ánh xạ các View từ XML
        initViews(view);

        // 3. Thiết lập danh sách bài học mẫu
        setupLessonList();

        // 4. Thiết lập sự kiện click cho các nút chức năng
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        listLesson = view.findViewById(R.id.listLesson);
        btnFlashcard = view.findViewById(R.id.btnFlashcard);
        btnQuiz = view.findViewById(R.id.btnQuiz);
        btnFill = view.findViewById(R.id.btnFill);
        btnListening = view.findViewById(R.id.btnListening);
        searchInput = view.findViewById(R.id.searchInput);
    }

    private void setupLessonList() {
        String[] lessons = {
                "Bài 1: Greetings",
                "Bài 2: Family & Friends",
                "Bài 3: Daily Activities",
                "Bài 4: Food & Drinks",
                "Bài 5: Travel"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                lessons
        );
        listLesson.setAdapter(adapter);

        // Sự kiện khi click vào một bài học trong danh sách
        listLesson.setOnItemClickListener((parent, view, position, id) -> {
            // Khi nhấn vào bài học, có thể dẫn thẳng tới VocabularyFragment để học từ vựng bài đó
            navigateToFragment(new VocabularyFragment());
        });
    }

    private void setupButtons() {
        // 1. Chuyển sang màn hình Flashcard (Lật thẻ)
        btnFlashcard.setOnClickListener(v -> {
            navigateToFragment(new FlashcardFragment());
        });

        // 2. Chuyển sang màn hình Trắc nghiệm (Quiz)
        btnQuiz.setOnClickListener(v -> {
            navigateToFragment(new QuizFragment());
        });

        // 3. Chuyển sang màn hình Điền từ (Fill Word)
        btnFill.setOnClickListener(v -> {
            navigateToFragment(new FillWordFragment());
        });

        // 4. Chuyển sang màn hình Luyện nghe (Vocabulary/Speech)
        btnListening.setOnClickListener(v -> {
            navigateToFragment(new VocabularyFragment());
        });
    }

    /**
     * Hàm hỗ trợ chuyển đổi Fragment mượt mà
     */
    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            // Hiệu ứng chuyển cảnh (Fade in/out)
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            // Thay thế fragment hiện tại
            // Lưu ý: ID R.id.fragment_container phải khớp với FrameLayout trong activity_main.xml
            transaction.replace(R.id.fragment_container, fragment);

            // Lưu vào BackStack để người dùng nhấn nút Back có thể quay về Home thay vì thoát App
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }
}
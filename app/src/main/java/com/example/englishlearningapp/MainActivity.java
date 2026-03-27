package com.example.englishlearningapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.home.HomeFragment;
import com.example.englishlearningapp.ui.lesson.LessonListFragment;
// Import thêm các fragment khác nếu bạn đã tạo
// import com.example.englishlearningapp.ui.quiz.QuizFragment;
// import com.example.englishlearningapp.ui.profile.ProgressFragment;
// import com.example.englishlearningapp.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    // Navigation Layouts
    LinearLayout navHome, navLesson, navAdd, navProgress, navProfile;
    // Navigation Icons
    ImageView iconHome, iconLesson, iconProgress, iconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // SỬ DỤNG layout activity_main (Cái có ConstraintLayout và Logo)
        setContentView(R.layout.activity_main);

        initView();

        // 1. Mặc định hiển thị HomeFragment khi vừa mở App
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), false);
            updateUIActive(iconHome);
        }

        // 2. Xử lý sự kiện click cho thanh điều hướng dưới (Bottom Navigation)
        navHome.setOnClickListener(v -> {
            replaceFragment(new HomeFragment(), false);
            updateUIActive(iconHome);
        });

        navLesson.setOnClickListener(v -> {
            // Load danh sách bài học
            replaceFragment(new LessonListFragment(), false);
            updateUIActive(iconLesson);
        });

        navAdd.setOnClickListener(v -> {
            // Ví dụ: Mở Quiz hoặc màn hình thêm mới
            // replaceFragment(new QuizFragment(), false);
        });

        navProgress.setOnClickListener(v -> {
            // replaceFragment(new ProgressFragment(), false);
            updateUIActive(iconProgress);
        });

        navProfile.setOnClickListener(v -> {
            // replaceFragment(new ProfileFragment(), false);
            updateUIActive(iconProfile);
        });
    }

    private void initView() {
        // Ánh xạ các vùng nhấn (LinearLayout) từ bottom_navigation.xml
        navHome = findViewById(R.id.navHome);
        navLesson = findViewById(R.id.navLesson);
        navAdd = findViewById(R.id.navAdd);
        navProgress = findViewById(R.id.navProgress);
        navProfile = findViewById(R.id.navProfile);

        // Ánh xạ các Icon để đổi màu
        iconHome = findViewById(R.id.iconHome);
        iconLesson = findViewById(R.id.iconLesson);
        iconProgress = findViewById(R.id.iconProgress);
        iconProfile = findViewById(R.id.iconProfile);
    }

    /**
     * Hàm thay đổi Fragment trong fragment_container
     * @param fragment Fragment mục tiêu
     * @param addToBackStack Có lưu vào lịch sử để nhấn Back quay lại không
     */
    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        // Khi chuyển giữa các tab chính, ta nên xóa sạch lịch sử BackStack
        // để tránh việc nhấn Back bị quay vòng quanh các Tab.
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        var transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    /**
     * Hàm cập nhật màu sắc Icon để người dùng biết đang ở Tab nào
     */
    private void updateUIActive(ImageView activeIcon) {
        int defaultColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        int activeColor = ContextCompat.getColor(this, R.color.blue);

        // Reset tất cả icon về màu xám
        if (iconHome != null) iconHome.setColorFilter(defaultColor);
        if (iconLesson != null) iconLesson.setColorFilter(defaultColor);
        if (iconProgress != null) iconProgress.setColorFilter(defaultColor);
        if (iconProfile != null) iconProfile.setColorFilter(defaultColor);

        // Đổi màu xanh cho icon của tab đang chọn
        if (activeIcon != null) {
            activeIcon.setColorFilter(activeColor);
        }
    }
}
package com.example.englishlearningapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.ui.home.HomeFragment;
import com.example.englishlearningapp.ui.lesson.LessonListFragment;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.example.englishlearningapp.ui.profile.ProgressFragment;
import com.example.englishlearningapp.ui.profile.ProfileFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout navHome, navLesson, navAdd, navProgress, navProfile;
    ImageView iconHome, iconLesson, iconProgress, iconProfile;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        initView();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), false);
            updateUIActive(iconHome);
        }

        // --- CÁC SỰ KIỆN CLICK ---
        navHome.setOnClickListener(v -> {
            replaceFragment(new HomeFragment(), false);
            updateUIActive(iconHome);
        });

        navLesson.setOnClickListener(v -> {
            replaceFragment(new LessonListFragment(), false);
            updateUIActive(iconLesson);
        });

        // THAY ĐỔI Ở ĐÂY: Nhấn nút Add sẽ hiện Dialog thay vì Fragment
        navAdd.setOnClickListener(v -> showAddWordDialog());

        navProgress.setOnClickListener(v -> {
            replaceFragment(new ProgressFragment(), false);
            updateUIActive(iconProgress);
        });

        navProfile.setOnClickListener(v -> {
            replaceFragment(new ProfileFragment(), false);
            updateUIActive(iconProfile);
        });
    }

    private void showAddWordDialog() {
        // Khởi tạo Dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_vocabulary); // Tên file XML của bạn

        // Cấu hình giao diện Dialog (Full width, nền trong suốt để bo góc)
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Ánh xạ các View trong Dialog XML
        TextInputEditText edtNewWord = dialog.findViewById(R.id.edtNewWord);
        TextInputEditText edtNewMeaning = dialog.findViewById(R.id.edtNewMeaning);
        MaterialButton btnAddWord = dialog.findViewById(R.id.btnAddWord);

        btnAddWord.setOnClickListener(v -> {
            String word = edtNewWord.getText().toString().trim();
            String meaning = edtNewMeaning.getText().toString().trim();

            if (word.isEmpty() || meaning.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                saveWordToFirebase(word, meaning, dialog);
            }
        });

        dialog.show();
    }

    private void saveWordToFirebase(String word, String meaning, Dialog dialog) {
        Map<String, Object> data = new HashMap<>();
        data.put("word", word);
        data.put("meaning", meaning);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("user_vocabularies")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "Đã thêm từ mới thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void initView() {
        navHome = findViewById(R.id.navHome);
        navLesson = findViewById(R.id.navLesson);
        navAdd = findViewById(R.id.navAdd);
        navProgress = findViewById(R.id.navProgress);
        navProfile = findViewById(R.id.navProfile);

        iconHome = findViewById(R.id.iconHome);
        iconLesson = findViewById(R.id.iconLesson);
        iconProgress = findViewById(R.id.iconProgress);
        iconProfile = findViewById(R.id.iconProfile);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateUIActive(ImageView activeIcon) {
        int defaultColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        int activeColor = ContextCompat.getColor(this, R.color.blue);

        if (iconHome != null) iconHome.setColorFilter(defaultColor);
        if (iconLesson != null) iconLesson.setColorFilter(defaultColor);
        if (iconProgress != null) iconProgress.setColorFilter(defaultColor);
        if (iconProfile != null) iconProfile.setColorFilter(defaultColor);

        if (activeIcon != null) activeIcon.setColorFilter(activeColor);
    }
}
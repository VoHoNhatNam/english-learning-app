package com.example.englishlearningapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.example.englishlearningapp.ui.home.HomeFragment;
import com.example.englishlearningapp.ui.home.NotificationFragment;
import com.example.englishlearningapp.ui.lesson.GrammarFragment;
import com.example.englishlearningapp.ui.lesson.LessonDetailFragment;
import com.example.englishlearningapp.ui.lesson.LessonListFragment;
import com.example.englishlearningapp.ui.mission.MissionFragment;
import com.example.englishlearningapp.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private View bottomNavContainer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        initView();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), false);
            bottomNavigationView.setSelectedItemId(R.id.navHome);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navHome) {
                replaceFragment(new HomeFragment(), false);
                return true;
            } else if (itemId == R.id.navTutor) {
                replaceFragment(new ChatFragment(), false);
                return true;
            } else if (itemId == R.id.navStorage) {
                replaceFragment(new LessonListFragment(), false);
                return true;
            } else if (itemId == R.id.navProfile) {
                replaceFragment(new ProfileFragment(), false);
                return true;
            }
            return false;
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            updateBottomNavVisibility(currentFragment);
        });
    }

    private void showAddWordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_vocabulary);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavContainer = findViewById(R.id.bottomNavContainer);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        } else {
            // Clear back stack when switching main tabs
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        transaction.commit();

        updateBottomNavVisibility(fragment);
    }

    private void updateBottomNavVisibility(Fragment fragment) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer == null || bottomNavContainer == null) return;

        // Kiểm tra nếu là MissionFragment, NotificationFragment, LessonDetailFragment hoặc GrammarFragment thì ẩn Bottom Nav
        if (fragment instanceof MissionFragment || 
            fragment instanceof NotificationFragment || 
            fragment instanceof LessonDetailFragment || 
            fragment instanceof GrammarFragment) {
            bottomNavContainer.setVisibility(View.GONE);
            fragmentContainer.setPadding(0, 0, 0, 0); // Xóa padding để hiển thị full màn hình
        } else {
            bottomNavContainer.setVisibility(View.VISIBLE);
            int paddingBottom = (int) (80 * getResources().getDisplayMetrics().density);
            fragmentContainer.setPadding(0, 0, 0, paddingBottom);
        }
    }
}

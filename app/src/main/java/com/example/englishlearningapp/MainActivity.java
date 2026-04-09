package com.example.englishlearningapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.ui.home.HomeFragment;
import com.example.englishlearningapp.ui.lesson.LessonListFragment;
import com.example.englishlearningapp.ui.profile.ProgressFragment;
import com.example.englishlearningapp.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
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
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navHome) {
                replaceFragment(new HomeFragment(), false);
                return true;
            } else if (itemId == R.id.navLesson) {
                replaceFragment(new LessonListFragment(), false);
                return true;
            } else if (itemId == R.id.navAdd) {
                showAddWordDialog();
                return false; // Trả về false để không chọn item này trên UI (vì nó mở dialog)
            } else if (itemId == R.id.navProgress) {
                replaceFragment(new ProgressFragment(), false);
                return true;
            } else if (itemId == R.id.navProfile) {
                replaceFragment(new ProfileFragment(), false);
                return true;
            }
            return false;
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
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }
}
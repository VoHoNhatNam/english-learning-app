package com.example.englishlearningapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class OnboardingSurveyActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvStepIndicator;
    private Button btnNext, btnBack;
    private View loadingOverlay;

    private int currentStep = 1;
    private int userAge = -1; // Mặc định -1 để đánh dấu chưa hợp lệ
    private String englishLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_survey);

        initViews();
        showStep(1);

        btnNext.setOnClickListener(v -> {
            if (currentStep == 1) {
                if (userAge == -1) {
                    Toast.makeText(this, "Vui lòng nhập tuổi hợp lệ trước khi tiếp tục", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentStep = 2;
                showStep(2);
            } else {
                saveDataToFirebase();
            }
        });

        btnBack.setOnClickListener(v -> {
            if (currentStep == 2) {
                currentStep = 1;
                showStep(1);
            }
        });
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        tvStepIndicator = findViewById(R.id.tvStepIndicator);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void showStep(int step) {
        Fragment fragment;
        if (step == 1) {
            fragment = new AgeSurveyFragment();
            tvStepIndicator.setText("Bước 1/2");
            progressBar.setProgress(1);
            btnBack.setVisibility(View.GONE);
            btnNext.setText("Tiếp tục");
        } else {
            fragment = new LevelSurveyFragment();
            tvStepIndicator.setText("Bước 2/2");
            progressBar.setProgress(2);
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setText("Hoàn tất");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void setAge(int age) {
        this.userAge = age;
    }

    public void setLevel(String level) {
        this.englishLevel = level;
    }

    private void saveDataToFirebase() {
        if (englishLevel.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn trình độ của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        loadingOverlay.setVisibility(View.VISIBLE);

        Map<String, Object> data = new HashMap<>();
        data.put("age", userAge);
        data.put("englishLevel", englishLevel);
        data.put("onboardingCompleted", true);

        // Chuyển từ .update() sang .set(..., SetOptions.merge())
        // Giúp gộp dữ liệu mà không ghi đè toàn bộ document, và tạo mới nếu chưa có.
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    loadingOverlay.setVisibility(View.GONE);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    loadingOverlay.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
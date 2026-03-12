package com.example.englishlearningapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    Button btnLogin, btnRegister, btnLogout;
    TextView txtUsername;

    SharedPreferences prefs;

    // Header
    ImageView btnBack;

    // Bottom Navigation
    LinearLayout navHome, navLesson, navAdd, navProgress, navProfile;
    ImageView iconHome, iconLesson, iconProgress, iconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Header
        btnBack = findViewById(R.id.btnBack);

        // User
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogout = findViewById(R.id.btnLogout);
        txtUsername = findViewById(R.id.txtUsername);

        // Bottom Navigation
        navHome = findViewById(R.id.navHome);
        navLesson = findViewById(R.id.navLesson);
        navAdd = findViewById(R.id.navAdd);
        navProgress = findViewById(R.id.navProgress);
        navProfile = findViewById(R.id.navProfile);

        iconHome = findViewById(R.id.iconHome);
        iconLesson = findViewById(R.id.iconLesson);
        iconProgress = findViewById(R.id.iconProgress);
        iconProfile = findViewById(R.id.iconProfile);

        // Highlight icon Profile
        iconProfile.setColorFilter(getResources().getColor(R.color.blue));

        // SharedPreferences
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        checkLogin();

        // ===== BACK BUTTON =====
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        });

        // ===== LOGIN =====
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        });

        // ===== REGISTER =====
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, RegisterActivity.class));
        });

        // ===== LOGOUT =====
        btnLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            checkLogin();
        });

        // ===== BOTTOM NAVIGATION =====

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        });

        navLesson.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, LessonActivity.class));
            finish();
        });

        navAdd.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AddActivity.class));
            finish();
        });

        navProgress.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ProgressActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            // đang ở trang Profile nên không làm gì
        });
    }

    // Kiểm tra đăng nhập
    private void checkLogin() {

        boolean isLogin = prefs.getBoolean("isLogin", false);

        if (isLogin) {

            String username = prefs.getString("username", "User");

            txtUsername.setText("Xin chào, " + username);

            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

        } else {

            txtUsername.setText("Bạn chưa đăng nhập");

            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }
}
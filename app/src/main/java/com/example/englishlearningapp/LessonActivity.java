package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class LessonActivity extends AppCompatActivity {

    ListView listLesson;

    // Header
    ImageView btnBack;

    // Bottom Navigation
    LinearLayout navHome, navLesson, navAdd, navProgress, navProfile;
    ImageView iconHome, iconLesson, iconProgress, iconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // Header
        btnBack = findViewById(R.id.btnBack);

        // ListView
        listLesson = findViewById(R.id.listLesson);

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

        // Highlight icon Lesson
        iconLesson.setColorFilter(getResources().getColor(R.color.blue));

        // ===== LIST LESSON =====
        String[] lessons = {
                "Lesson 1 - Greetings",
                "Lesson 2 - Family",
                "Lesson 3 - Food",
                "Lesson 4 - Travel",
                "Lesson 5 - Work",
                "Lesson 6 - Daily Activities"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lessons
        );

        listLesson.setAdapter(adapter);

        // ===== BACK BUTTON =====
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(LessonActivity.this, MainActivity.class));
            finish();
        });

        // ===== BOTTOM NAVIGATION =====

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        navLesson.setOnClickListener(v -> {
            // đang ở trang này
        });

        navAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddActivity.class));
            finish();
        });

        navProgress.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgressActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }
}
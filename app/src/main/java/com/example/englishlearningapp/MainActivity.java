package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listLesson;

    // Navigation
    LinearLayout navHome, navLesson, navAdd, navProgress, navProfile;

    ImageView iconHome, iconLesson, iconProgress, iconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // List Lesson
        listLesson = findViewById(R.id.listLesson);

        String[] lessons = {
                "Item 1 - Sub Item 1",
                "Item 2 - Sub Item 2",
                "Item 3 - Sub Item 3",
                "Item 4 - Sub Item 4"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lessons
        );

        listLesson.setAdapter(adapter);

        // ===== Bottom Navigation =====

        navHome = findViewById(R.id.navHome);
        navLesson = findViewById(R.id.navLesson);
        navAdd = findViewById(R.id.navAdd);
        navProgress = findViewById(R.id.navProgress);
        navProfile = findViewById(R.id.navProfile);

        iconHome = findViewById(R.id.iconHome);
        iconLesson = findViewById(R.id.iconLesson);
        iconProgress = findViewById(R.id.iconProgress);
        iconProfile = findViewById(R.id.iconProfile);

        // Home đang active
        iconHome.setColorFilter(getResources().getColor(R.color.blue));

        navHome.setOnClickListener(v -> {
            // đang ở Home nên không cần chuyển
        });

        navLesson.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LessonActivity.class));
            finish();
        });

        navAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddWordActivity.class));
            finish();
        });

        navProgress.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProgressActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            finish();
        });
    }
}
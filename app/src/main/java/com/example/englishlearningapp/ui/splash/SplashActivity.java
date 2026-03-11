package com.example.englishlearningapp.ui.splash;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView sloth;
    AnimationDrawable slothAnimation;
    ProgressBar progressBar;

    TextView[] letters;
    LinearLayout logoContainer;

    int progressWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sloth = findViewById(R.id.sloth);
        progressBar = findViewById(R.id.progressBar);
        logoContainer = findViewById(R.id.logoContainer);

        slothAnimation = (AnimationDrawable) sloth.getBackground();

        letters = new TextView[]{
                findViewById(R.id.c1),
                findViewById(R.id.c2),
                findViewById(R.id.c3),
                findViewById(R.id.c4),
                findViewById(R.id.c5),
                findViewById(R.id.c6),
                findViewById(R.id.c7),
                findViewById(R.id.c8),
                findViewById(R.id.c9),
                findViewById(R.id.c10),
                findViewById(R.id.c11),
                findViewById(R.id.c12)
        };

        progressBar.post(() -> {

            progressWidth = progressBar.getWidth();

            slothAnimation.start();

            startProgress();

        });
    }

    private void startProgress() {

        new Thread(() -> {

            for (int i = 0; i <= 100; i++) {

                int progress = i;

                runOnUiThread(() -> {

                    progressBar.setProgress(progress);

                    // tính vị trí sloth theo progress
                    float x = (progress / 100f) * progressWidth;

                    sloth.setTranslationX(x - sloth.getWidth() / 2f);

                    // bounce chữ
                    int index = progress * letters.length / 100;

                    if (index < letters.length) {
                        bounce(letters[index]);
                    }

                });

                try {
                    Thread.sleep(40);
                } catch (InterruptedException ignored) {}

            }

            runOnUiThread(() -> {

                startActivity(new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                ));

                finish();

            });

        }).start();
    }

    private void bounce(TextView v) {

        ObjectAnimator scaleX =
                ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.25f, 1f);

        ObjectAnimator scaleY =
                ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.25f, 1f);

        scaleX.setDuration(200);
        scaleY.setDuration(200);

        scaleX.start();
        scaleY.start();
    }
}
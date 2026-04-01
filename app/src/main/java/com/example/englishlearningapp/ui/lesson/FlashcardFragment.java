package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;

public class FlashcardFragment extends Fragment {

    private TextView txtWord, txtMeaning, txtProgress;
    private View cardFlashcard;
    private Button btnNext, btnPrev;

    private boolean isFront = true;
    private int currentIndex = 0;

    // 🔥 Demo data (sau này thay bằng Firebase/Room)
    private final String[] words = {"Apple", "Book", "Dog", "Cat"};
    private final String[] meanings = {"Quả táo", "Quyển sách", "Con chó", "Con mèo"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flashcard, container, false);

        initView(v);
        updateCard();

        // 🔥 Flip card
        cardFlashcard.setOnClickListener(view -> flipCard());

        // 🔥 Next
        btnNext.setOnClickListener(v1 -> {
            if (currentIndex < words.length - 1) {
                currentIndex++;
                isFront = true;
                updateCard();
            }
        });

        // 🔥 Prev
        btnPrev.setOnClickListener(v12 -> {
            if (currentIndex > 0) {
                currentIndex--;
                isFront = true;
                updateCard();
            }
        });

        return v;
    }

    // =========================
    // 🔹 INIT VIEW
    // =========================
    private void initView(View v) {
        txtWord = v.findViewById(R.id.txtWord);
        txtMeaning = v.findViewById(R.id.txtMeaning);
        txtProgress = v.findViewById(R.id.txtProgress);

        cardFlashcard = v.findViewById(R.id.cardFlashcard);

        btnNext = v.findViewById(R.id.btnNext);
        btnPrev = v.findViewById(R.id.btnPrev);
    }

    // =========================
    // 🔄 UPDATE UI
    // =========================
    private void updateCard() {

        txtWord.setText(words[currentIndex]);
        txtMeaning.setText(meanings[currentIndex]);

        txtProgress.setText((currentIndex + 1) + " / " + words.length);

        // reset về mặt trước
        txtWord.setVisibility(View.VISIBLE);
        txtMeaning.setVisibility(View.GONE);

        animateFade(txtWord);
    }

    // =========================
    // 🔁 FLIP CARD
    // =========================
    private void flipCard() {

        if (isFront) {
            txtWord.setVisibility(View.GONE);
            txtMeaning.setVisibility(View.VISIBLE);
            animateFade(txtMeaning);
        } else {
            txtMeaning.setVisibility(View.GONE);
            txtWord.setVisibility(View.VISIBLE);
            animateFade(txtWord);
        }

        isFront = !isFront;
    }

    // =========================
    // ✨ ANIMATION
    // =========================
    private void animateFade(View view) {
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }
}
package com.example.englishlearningapp.ui.lesson;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections; // 🔥 Thêm xáo trộn
import java.util.List;
import java.util.Locale;

public class FlashcardFragment extends Fragment {

    private TextView txtWord, txtMeaning, txtProgress, txtExample;
    private View cardFront, cardBack, cardContainer;
    private Button btnNext, btnPrev;
    private ImageButton btnSpeak;

    private boolean isFront = true;
    private int currentIndex = 0;
    private int lessonId;

    private List<Vocabulary> vocabularyList = new ArrayList<>();
    private FirebaseFirestore db;
    private TextToSpeech tts;

    private AnimatorSet frontOut, backIn, backOut, frontIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flashcard, container, false);

        if (getArguments() != null) {
            lessonId = getArguments().getInt("lessonId", -1);
        }

        db = FirebaseFirestore.getInstance();
        initView(v);
        loadAnimations();
        initTTS();

        loadVocabFromFirestore();

        cardContainer.setOnClickListener(view -> flipCard());
        btnNext.setOnClickListener(v1 -> navigateCard(1));
        btnPrev.setOnClickListener(v1 -> navigateCard(-1));
        btnSpeak.setOnClickListener(v1 -> speakOut());

        return v;
    }

    private void initView(View v) {
        txtWord = v.findViewById(R.id.txtWord);
        txtMeaning = v.findViewById(R.id.txtMeaning);
        txtExample = v.findViewById(R.id.txtExample);
        txtProgress = v.findViewById(R.id.txtProgress);
        cardFront = v.findViewById(R.id.cardFront);
        cardBack = v.findViewById(R.id.cardBack);
        cardContainer = v.findViewById(R.id.cardFlashcard);
        btnNext = v.findViewById(R.id.btnNext);
        btnPrev = v.findViewById(R.id.btnPrev);
        btnSpeak = v.findViewById(R.id.btnSpeak);

        float scale = getResources().getDisplayMetrics().density;
        float cameraDistance = 8000 * scale;
        cardFront.setCameraDistance(cameraDistance);
        cardBack.setCameraDistance(cameraDistance);
    }

    private void loadAnimations() {
        frontOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_left_out);
        backIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_in);
        backOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_out);
        frontIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_left_in);
    }

    private void flipCard() {
        if (vocabularyList.isEmpty()) return;

        vibrateDevice(); // 🔥 Rung nhẹ khi lật thẻ

        if (isFront) {
            frontOut.setTarget(cardFront);
            backIn.setTarget(cardBack);
            frontOut.start();
            backIn.start();
            isFront = false;
        } else {
            backOut.setTarget(cardBack);
            frontIn.setTarget(cardFront);
            backOut.start();
            frontIn.start();
            isFront = true;
        }
    }

    private void navigateCard(int step) {
        int nextIndex = currentIndex + step;
        if (nextIndex >= 0 && nextIndex < vocabularyList.size()) {
            currentIndex = nextIndex;
            isFront = true;

            // Reset UI
            cardFront.setVisibility(View.VISIBLE);
            cardFront.setAlpha(1f);
            cardFront.setRotationY(0);
            cardBack.setVisibility(View.GONE);
            cardBack.setAlpha(0f);
            cardBack.setRotationY(0);

            updateCard();
        } else if (nextIndex >= vocabularyList.size()) {
            // 🔥 Xử lý khi xem hết từ vựng
            showFinishedDialog();
        }
    }

    private void showFinishedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Hoàn thành!")
                .setMessage("Bạn đã xem hết từ vựng của bài học này. Bắt đầu luyện tập ngay nhé?")
                .setPositiveButton("Luyện tập (Fill Word)", (dialog, which) -> {
                    FillWordFragment fillWordFragment = new FillWordFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("lessonId", lessonId);
                    fillWordFragment.setArguments(bundle);

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fillWordFragment)
                            .commit();
                })
                .setNegativeButton("Học lại", (dialog, which) -> {
                    currentIndex = 0;
                    updateCard();
                })
                .show();
    }

    private void updateCard() {
        if (vocabularyList.isEmpty()) return;
        Vocabulary current = vocabularyList.get(currentIndex);
        txtWord.setText(current.getWord());
        txtMeaning.setText(current.getMeaning());
        txtExample.setText(current.getExample() != null ? "Example: " + current.getExample() : "No example available.");
        txtProgress.setText((currentIndex + 1) + " / " + vocabularyList.size());
    }

    private void initTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });
    }

    private void loadVocabFromFirestore() {
        db.collection("vocabularies")
                .whereEqualTo("lessonId", lessonId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    vocabularyList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        vocabularyList.add(doc.toObject(Vocabulary.class));
                    }
                    if (!vocabularyList.isEmpty()) {
                        Collections.shuffle(vocabularyList); // 🔥 Xáo trộn bộ thẻ
                        updateCard();
                    } else {
                        Toast.makeText(getContext(), "Không có dữ liệu từ vựng!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show());
    }

    private void vibrateDevice() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(50);
            }
        }
    }

    private void speakOut() {
        if (vocabularyList.isEmpty()) return;
        String text = vocabularyList.get(currentIndex).getWord();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}

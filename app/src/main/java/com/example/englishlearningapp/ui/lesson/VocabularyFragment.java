package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar; // Thêm import
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.google.firebase.firestore.FirebaseFirestore; // Thêm import Firebase
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VocabularyFragment extends Fragment {

    private TextView txtWord, txtPhonetic, txtMeaning, txtProgress, txtExample;
    private ImageButton btnSpeak, btnBack;
    private Button btnPrev, btnNext;
    private ProgressBar vocabProgressBar; // Thêm ProgressBar

    private TextToSpeech tts;
    private List<Vocabulary> vocabularyList;
    private int currentIndex = 0;
    private int lessonId; // Biến lưu ID bài học được truyền sang
    private FirebaseFirestore db; // Biến Firestore

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);

        // Nhận lessonId từ Bundle (truyền từ màn hình Lesson Detail)
        if (getArguments() != null) {
            lessonId = getArguments().getInt("lessonId", -1);
        }

        db = FirebaseFirestore.getInstance();
        initViews(view);
        setupTTS();

        // Bước quan trọng: Tải dữ liệu thật từ Firebase
        loadVocabularyFromFirestore();

        // Các sự kiện OnClick giữ nguyên logic cũ nhưng thêm cập nhật ProgressBar
        btnNext.setOnClickListener(v -> {
            if (currentIndex < vocabularyList.size() - 1) {
                currentIndex++;
                updateUI();
            } else {
                Toast.makeText(getContext(), "You've finished this lesson!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateUI();
            }
        });

        btnSpeak.setOnClickListener(v -> {
            if (!vocabularyList.isEmpty()) {
                String word = vocabularyList.get(currentIndex).getWord();
                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void initViews(View view) {
        txtWord = view.findViewById(R.id.txtWord);
        txtPhonetic = view.findViewById(R.id.txtPhonetic);
        txtMeaning = view.findViewById(R.id.txtMeaning);
        txtExample = view.findViewById(R.id.txtExample);
        txtProgress = view.findViewById(R.id.txtProgressIndex);
        vocabProgressBar = view.findViewById(R.id.vocabProgressBar); // Ánh xạ Progress Bar
        btnSpeak = view.findViewById(R.id.btnSpeak);
        btnBack = view.findViewById(R.id.btnBackVocab);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);

        vocabularyList = new ArrayList<>();
    }

    private void loadVocabularyFromFirestore() {
        // Truy vấn: Lấy tất cả từ vựng có lessonId khớp với bài học hiện tại
        db.collection("vocabularies")
                .whereEqualTo("lessonId", lessonId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    vocabularyList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Vocabulary vocab = document.toObject(Vocabulary.class);
                        vocabularyList.add(vocab);
                    }

                    if (!vocabularyList.isEmpty()) {
                        // Cấu hình ProgressBar tối đa theo số lượng từ
                        vocabProgressBar.setMax(vocabularyList.size());
                        updateUI();
                    } else {
                        Toast.makeText(getContext(), "No data found for this lesson.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUI() {
        if (vocabularyList == null || vocabularyList.isEmpty()) return;

        Vocabulary current = vocabularyList.get(currentIndex);

        txtWord.setText(current.getWord());
        txtPhonetic.setText(current.getPhonetic());
        txtMeaning.setText(current.getMeaning());

        if (current.getExample() != null && !current.getExample().isEmpty()) {
            txtExample.setText("Example: " + current.getExample());
            txtExample.setVisibility(View.VISIBLE);
        } else {
            txtExample.setVisibility(View.GONE);
        }

        // Cập nhật text tiến độ: ví dụ "1 / 10"
        txtProgress.setText((currentIndex + 1) + " / " + vocabularyList.size());

        // Cập nhật thanh ProgressBar ngang
        vocabProgressBar.setProgress(currentIndex + 1);
    }

    private void setupTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });
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
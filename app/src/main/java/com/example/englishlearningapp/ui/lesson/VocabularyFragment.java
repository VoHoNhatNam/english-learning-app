package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VocabularyFragment extends Fragment {

    private TextView txtWord, txtPhonetic, txtMeaning, txtProgress;
    private ImageButton btnSpeak, btnBack;
    private Button btnPrev, btnNext;

    private TextToSpeech tts;
    private List<WordModel> vocabularyList;
    private int currentIndex = 0;
    private String lessonName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);

        if (getArguments() != null) {
            lessonName = getArguments().getString("lessonName");
        }

        initViews(view);
        setupData();
        setupTTS();

        updateUI();

        // Xử lý sự kiện
        btnNext.setOnClickListener(v -> {
            if (currentIndex < vocabularyList.size() - 1) {
                currentIndex++;
                updateUI();
            } else {
                Toast.makeText(getContext(), "Đã hết bài học!", Toast.LENGTH_SHORT).show();
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
        txtProgress = view.findViewById(R.id.txtProgressIndex);
        btnSpeak = view.findViewById(R.id.btnSpeak);
        btnBack = view.findViewById(R.id.btnBackVocab);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);
    }

    private void setupData() {
        vocabularyList = new ArrayList<>();

        if (lessonName != null && lessonName.contains("Bài 1") && lessonName.contains("Greetings")) {
            // Nội dung Bài 1: Greetings & Introductions
            vocabularyList.add(new WordModel("Hello", "/həˈləʊ/", "Xin chào"));
            vocabularyList.add(new WordModel("Goodbye", "/ˌɡʊdˈbaɪ/", "Tạm biệt"));
            vocabularyList.add(new WordModel("Name", "/neɪm/", "Tên"));
            vocabularyList.add(new WordModel("Student", "/ˈstjuːdnt/", "Học sinh/Sinh viên"));
            vocabularyList.add(new WordModel("Teacher", "/ˈtiːtʃə(r)/", "Giáo viên"));
            vocabularyList.add(new WordModel("Friend", "/frend/", "Người bạn"));
            vocabularyList.add(new WordModel("Nice to meet you", "/naɪs tu miːt ju/", "Rất vui được gặp bạn"));
        } else {
            // Dữ liệu mặc định nếu không khớp bài học
            vocabularyList.add(new WordModel("Apple", "/ˈæpl/", "Quả táo"));
            vocabularyList.add(new WordModel("Banana", "/bəˈnænə/", "Quả chuối"));
            vocabularyList.add(new WordModel("Orange", "/ˈɔːrɪndʒ/", "Quả cam"));
        }
    }

    private void setupTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });
    }

    private void updateUI() {
        if (vocabularyList.isEmpty()) return;

        WordModel currentWord = vocabularyList.get(currentIndex);
        txtWord.setText(currentWord.getWord());
        txtPhonetic.setText(currentWord.getPhonetic());
        txtMeaning.setText(currentWord.getMeaning());
        txtProgress.setText((currentIndex + 1) + " / " + vocabularyList.size());
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // Class nội bộ để quản lý dữ liệu từ vựng
    static class WordModel {
        private String word, phonetic, meaning;
        public WordModel(String word, String phonetic, String meaning) {
            this.word = word;
            this.phonetic = phonetic;
            this.meaning = meaning;
        }
        public String getWord() { return word; }
        public String getPhonetic() { return phonetic; }
        public String getMeaning() { return meaning; }
    }
}
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
import com.example.englishlearningapp.data.model.Vocabulary;
import com.example.englishlearningapp.data.model.VocabularyLesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VocabularyFragment extends Fragment {

    private TextView txtWord, txtPhonetic, txtMeaning, txtProgress;
    private ImageButton btnSpeak, btnBack;
    private Button btnPrev, btnNext;

    private TextToSpeech tts;
    private List<Vocabulary> vocabularyList = new ArrayList<>();
    private int currentIndex = 0;
    private VocabularyLesson lesson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);

        if (getArguments() != null) {
            lesson = (VocabularyLesson) getArguments().getSerializable("lesson");
            if (lesson != null && lesson.getWords() != null) {
                vocabularyList = lesson.getWords();
            }
        }

        initViews(view);
        setupTTS();
        updateUI();

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

    private void setupTTS() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });
    }

    private void updateUI() {
        if (vocabularyList.isEmpty()) {
            txtWord.setText("No data");
            return;
        }
        
        Vocabulary currentWord = vocabularyList.get(currentIndex);
        txtWord.setText(currentWord.getWord());
        txtPhonetic.setText(currentWord.getPhonetic() != null ? currentWord.getPhonetic() : "");
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
}

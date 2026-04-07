package com.example.englishlearningapp.ui.lesson;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator; // Thêm rung
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation; // Thêm hiệu ứng
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.example.englishlearningapp.ui.quiz.QuizResultFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillWordFragment extends Fragment {

    private EditText edtAnswer;
    private TextView txtQuestion, txtResult, txtProgress;
    private MaterialButton btnCheck;
    private ProgressBar progressBar;

    private List<Vocabulary> vocabularyList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int lessonId;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fill_word, container, false);

        if (getArguments() != null) {
            lessonId = getArguments().getInt("lessonId", -1);
        }

        db = FirebaseFirestore.getInstance();
        initView(v);
        loadVocabFromFirestore();

        btnCheck.setOnClickListener(view -> checkAnswer());

        edtAnswer.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer();
                return true;
            }
            return false;
        });

        return v;
    }

    private void initView(View v) {
        edtAnswer = v.findViewById(R.id.edtAnswer);
        txtQuestion = v.findViewById(R.id.txtQuestion);
        txtResult = v.findViewById(R.id.txtResult);
        txtProgress = v.findViewById(R.id.txtProgress);
        btnCheck = v.findViewById(R.id.btnCheck);
        progressBar = v.findViewById(R.id.fillWordProgressBar);
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
                        Collections.shuffle(vocabularyList);
                        loadQuestion();
                    } else {
                        Toast.makeText(getContext(), "No data found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadQuestion() {
        if (currentIndex < vocabularyList.size()) {
            Vocabulary current = vocabularyList.get(currentIndex);

            // Hiệu ứng mờ dần khi hiện câu hỏi mới
            AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(500);
            txtQuestion.startAnimation(fadeIn);

            txtQuestion.setText(current.getMeaning());

            // Gợi ý ký tự đầu (Hint học thuật)
            String word = current.getWord();
            if (word != null && word.length() > 0) {
                edtAnswer.setHint("Starts with '" + word.charAt(0) + "'...");
            }

            txtProgress.setText((currentIndex + 1) + " / " + vocabularyList.size());
            if (progressBar != null) {
                progressBar.setMax(vocabularyList.size());
                progressBar.setProgress(currentIndex + 1);
            }

            edtAnswer.setText("");
            txtResult.setText("");
            btnCheck.setEnabled(true);
            showKeyboard();
        }
    }

    private void checkAnswer() {
        String userAnswer = edtAnswer.getText().toString().trim();
        String correctAnswer = vocabularyList.get(currentIndex).getWord().trim();

        if (TextUtils.isEmpty(userAnswer)) {
            edtAnswer.setError("Type something...");
            return;
        }

        btnCheck.setEnabled(false);

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            score++;
            txtResult.setText("Correct! ✨");
            txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            // Rung khi sai
            vibrateDevice();
            txtResult.setText("Incorrect! Ans: " + correctAnswer);
            txtResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        edtAnswer.postDelayed(this::nextQuestion, 1500);
    }

    private void vibrateDevice() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // Cho Android 8.0 trở lên
                v.vibrate(android.os.VibrationEffect.createOneShot(200, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Cho các dòng máy cũ hơn
                v.vibrate(200);
            }
        }
    }

    private void nextQuestion() {
        if (currentIndex < vocabularyList.size() - 1) {
            currentIndex++;
            loadQuestion();
        } else {
            hideKeyboard(); // Ẩn bàn phím trước khi chuyển màn hình
            navigateToResult();
        }
    }

    private void showKeyboard() {
        edtAnswer.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(edtAnswer, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void navigateToResult() {
        int finalPercent = (score * 100) / vocabularyList.size();
        QuizResultFragment resultFragment = new QuizResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("CORRECT", score);
        bundle.putInt("TOTAL", vocabularyList.size());
        bundle.putInt("SCORE", finalPercent);
        bundle.putInt("lessonId", lessonId);
        bundle.putString("FROM_TYPE", "FILL_WORD");
        resultFragment.setArguments(bundle);

        if (getActivity() != null) {
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, resultFragment)
                    .commit();
        }
    }
}
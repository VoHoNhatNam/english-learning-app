package com.example.englishlearningapp.ui.customlesson;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomVocabularyFragment extends Fragment {

    private String lessonId, lessonTitle;
    private FirebaseFirestore db;
    private TextView txtTitle;
    private FloatingActionButton fabAdd;
    private RecyclerView rvVocab;
    // Lưu ý: Bạn cần tạo VocabAdapter tương tự LessonAdapter
    // private VocabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_vocabulary, container, false);

        // 1. Lấy dữ liệu truyền từ Fragment trước
        if (getArguments() != null) {
            lessonId = getArguments().getString("LESSON_ID");
            lessonTitle = getArguments().getString("LESSON_TITLE");
        }

        db = FirebaseFirestore.getInstance();
        txtTitle = view.findViewById(R.id.txtVocabLessonTitle);
        fabAdd = view.findViewById(R.id.fabAddVocab);
        rvVocab = view.findViewById(R.id.rvVocabList);

        txtTitle.setText(lessonTitle);

        fabAdd.setOnClickListener(v -> showAddVocabDialog());

        loadVocabularies();

        return view;
    }

    private void showAddVocabDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_vocabulary, null);
        EditText edtWord = dialogView.findViewById(R.id.edtNewWord);
        EditText edtMeaning = dialogView.findViewById(R.id.edtNewMeaning);

        new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String word = edtWord.getText().toString().trim();
                    String meaning = edtMeaning.getText().toString().trim();
                    if (!word.isEmpty() && !meaning.isEmpty()) {
                        saveVocabToFirestore(word, meaning);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void saveVocabToFirestore(String word, String meaning) {
        Map<String, Object> vocab = new HashMap<>();
        vocab.put("word", word);
        vocab.put("meaning", meaning);
        vocab.put("createdAt", System.currentTimeMillis());

        // Lưu vào sub-collection 'vocabularies' bên trong document của bài học
        db.collection("custom_lessons").document(lessonId)
                .collection("vocabularies")
                .add(vocab)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Đã thêm từ!", Toast.LENGTH_SHORT).show();
                    loadVocabularies(); // Tải lại danh sách
                });
    }

    private void loadVocabularies() {
        // Code lấy danh sách từ vựng từ Firestore sub-collection
        db.collection("custom_lessons").document(lessonId)
                .collection("vocabularies")
                .orderBy("createdAt")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Xử lý dữ liệu và đổ vào Adapter tại đây
                });
    }
}
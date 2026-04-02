package com.example.englishlearningapp.ui.customlesson;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateLessonFragment extends Fragment {

    private TextInputEditText edtName, edtDesc;
    private MaterialButton btnSave;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_lesson, container, false);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ View
        edtName = view.findViewById(R.id.edtLessonName);
        edtDesc = view.findViewById(R.id.edtLessonDesc);
        btnSave = view.findViewById(R.id.btnSaveLesson);

        btnSave.setOnClickListener(v -> saveLessonToFirestore());

        return view;
    }

    private void saveLessonToFirestore() {
        String name = edtName.getText().toString().trim();
        String desc = edtDesc.getText().toString().trim();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Vui lòng nhập tên bài học");
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(getContext(), "Lỗi: Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo ID duy nhất cho bài học
        String lessonId = UUID.randomUUID().toString();

        // Chuẩn bị dữ liệu bài học
        Map<String, Object> lesson = new HashMap<>();
        lesson.put("lessonId", lessonId);
        lesson.put("userId", userId);
        lesson.put("title", name);
        lesson.put("description", desc);
        lesson.put("createdAt", System.currentTimeMillis());

        // Lưu vào Firestore
        db.collection("custom_lessons").document(lessonId)
                .set(lesson)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Tạo bài học thành công!", Toast.LENGTH_SHORT).show();

                    // Sau khi lưu xong, chuyển sang màn hình thêm từ vựng chi tiết
                    navigateToVocabulary(lessonId, name);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void navigateToVocabulary(String lessonId, String lessonName) {
        // Chuyển sang CustomVocabularyFragment và truyền lessonId qua Bundle
        CustomVocabularyFragment vocabFragment = new CustomVocabularyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LESSON_ID", lessonId);
        bundle.putString("LESSON_NAME", lessonName);
        vocabFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, vocabFragment)
                .addToBackStack(null)
                .commit();
    }
}
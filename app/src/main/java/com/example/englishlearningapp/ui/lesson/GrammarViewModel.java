package com.example.englishlearningapp.ui.lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.englishlearningapp.data.model.GrammarLesson;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GrammarViewModel extends ViewModel {
    private final MutableLiveData<List<GrammarLesson>> allLessons = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<GrammarLesson>> getAllLessons() {
        return allLessons;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchLessonsIfNeeded() {
        // Chỉ fetch nếu chưa có dữ liệu (Chỉ lấy 1 lần)
        if (allLessons.getValue() != null && !allLessons.getValue().isEmpty()) {
            return;
        }
        forceRefresh();
    }

    public void forceRefresh() {
        isLoading.setValue(true);
        db.collection("grammar_lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<GrammarLesson> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        GrammarLesson lesson = document.toObject(GrammarLesson.class);
                        lesson.setId(document.getId());
                        list.add(lesson);
                    }
                    allLessons.setValue(list);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                });
    }
    
    public void seedInitialData(List<GrammarLesson> initialLessons) {
        isLoading.setValue(true);
        for (GrammarLesson l : initialLessons) {
            db.collection("grammar_lessons").document(l.getId()).set(l);
        }
        // Sau khi seed xong thì tải lại
        forceRefresh();
    }
}

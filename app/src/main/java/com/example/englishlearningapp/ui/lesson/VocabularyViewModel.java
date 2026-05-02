package com.example.englishlearningapp.ui.lesson;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.englishlearningapp.data.model.VocabularyLesson;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VocabularyViewModel extends ViewModel {
    private static final String TAG = "VocabularyViewModel";
    private final MutableLiveData<List<VocabularyLesson>> allLessons = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<VocabularyLesson>> getAllLessons() {
        return allLessons;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchLessonsIfNeeded() {
        if (allLessons.getValue() != null && !allLessons.getValue().isEmpty()) {
            return;
        }
        forceRefresh();
    }

    public void forceRefresh() {
        isLoading.setValue(true);
        db.collection("vocabulary_lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<VocabularyLesson> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        VocabularyLesson lesson = document.toObject(VocabularyLesson.class);
                        lesson.setId(document.getId());
                        list.add(lesson);
                    }
                    Log.d(TAG, "Fetched " + list.size() + " lessons from Firestore");
                    allLessons.setValue(list);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching lessons", e);
                    isLoading.setValue(false);
                });
    }

    public void seedInitialData(List<VocabularyLesson> initialLessons) {
        isLoading.setValue(true);
        Log.d(TAG, "Seeding initial data to Firestore...");
        for (VocabularyLesson l : initialLessons) {
            db.collection("vocabulary_lessons").document(l.getId()).set(l)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Seeded lesson: " + l.getTitle()))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to seed lesson: " + l.getTitle(), e));
        }
        // Delay a bit to allow Firestore to process before refreshing
        new android.os.Handler().postDelayed(this::forceRefresh, 2000);
    }
}

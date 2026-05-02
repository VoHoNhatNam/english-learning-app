package com.example.englishlearningapp.ui.lesson;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.englishlearningapp.data.model.ReadingArticle;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ReadingViewModel extends ViewModel {
    private static final String TAG = "ReadingViewModel";
    private final MutableLiveData<List<ReadingArticle>> allArticles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<ReadingArticle>> getAllArticles() {
        return allArticles;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchArticlesIfNeeded() {
        if (allArticles.getValue() != null && !allArticles.getValue().isEmpty()) {
            return;
        }
        forceRefresh();
    }

    public void forceRefresh() {
        isLoading.setValue(true);
        db.collection("reading_articles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ReadingArticle> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ReadingArticle article = document.toObject(ReadingArticle.class);
                        article.setId(document.getId());
                        list.add(article);
                    }
                    allArticles.setValue(list);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching articles", e);
                    isLoading.setValue(false);
                });
    }

    public void seedInitialData(List<ReadingArticle> initialArticles) {
        isLoading.setValue(true);
        for (ReadingArticle article : initialArticles) {
            db.collection("reading_articles").document(article.getId()).set(article)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Seeded article: " + article.getTitle()))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to seed article: " + article.getTitle(), e));
        }
        // Refresh after seeding
        new android.os.Handler().postDelayed(this::forceRefresh, 2000);
    }
}

package com.example.englishlearningapp.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Global State (Singleton) - Trạm trung chuyển dữ liệu cấp độ người dùng
 */
public class UserStateManager {
    private static UserStateManager instance;
    private final MutableLiveData<String> currentLevel = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private UserStateManager() {}

    public static synchronized UserStateManager getInstance() {
        if (instance == null) {
            instance = new UserStateManager();
        }
        return instance;
    }

    public LiveData<String> getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Khởi tạo 1 lần (Gọi tại Trang Chủ)
     * Chỉ fetch Firebase nếu currentLevel chưa có dữ liệu.
     */
    public void initLevel() {
        if (currentLevel.getValue() != null) return; // Đã có dữ liệu cục bộ, không gọi lại Firebase

        String userId = auth.getUid();
        if (userId == null) return;

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String level = documentSnapshot.getString("englishLevel");
                        currentLevel.setValue(level != null ? level : "Mới bắt đầu");
                    } else {
                        currentLevel.setValue("Mới bắt đầu");
                    }
                })
                .addOnFailureListener(e -> {
                    if (currentLevel.getValue() == null) {
                        currentLevel.setValue("Lỗi tải dữ liệu");
                    }
                });
    }

    /**
     * Hàm Cập nhật Toàn cục (Global Update)
     */
    public void updateLevel(String newLevel) {
        String userId = auth.getUid();
        if (userId == null) return;

        // 1. Cập nhật lên Firebase
        Map<String, Object> data = new HashMap<>();
        data.put("englishLevel", newLevel);

        db.collection("users").document(userId)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    // 2. Cập nhật cục bộ - Tự động phát tín hiệu đến các màn hình đang Observe
                    currentLevel.setValue(newLevel);
                });
    }
}

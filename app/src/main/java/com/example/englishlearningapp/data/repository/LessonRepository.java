package com.example.englishlearningapp.data.repository;

import android.util.Log;
import com.example.englishlearningapp.data.model.LessonBlock;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonRepository {
    private static final String TAG = "LessonRepository";
    private final FirebaseFirestore db;

    public interface OnLessonFetchListener {
        void onSuccess(String title, List<LessonBlock> blocks);
        void onError(Exception e);
    }

    public LessonRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Lấy nội dung bài học từ Firestore theo lessonId
     */
    public void getLessonContent(String lessonId, OnLessonFetchListener listener) {
        db.collection("lessons").document(lessonId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        List<Map<String, Object>> blocksRaw = (List<Map<String, Object>>) documentSnapshot.get("content_blocks");
                        List<LessonBlock> blocks = new ArrayList<>();
                        
                        if (blocksRaw != null) {
                            for (Map<String, Object> map : blocksRaw) {
                                LessonBlock block = new LessonBlock();
                                block.setType((String) map.get("type"));
                                block.setContent((String) map.get("content"));
                                block.setWrong((String) map.get("wrong"));
                                block.setCorrect((String) map.get("correct"));
                                blocks.add(block);
                            }
                        }
                        listener.onSuccess(title, blocks);
                    } else {
                        listener.onError(new Exception("Lesson not found"));
                    }
                })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Khởi tạo dữ liệu mẫu cho Bài 1 trên Firestore.
     */
    public void initializeDefaultLessons() {
        // 1. Tạo danh sách các khối nội dung (blocks) cho Bài 1
        List<LessonBlock> contentBlocks = new ArrayList<>();

        contentBlocks.add(new LessonBlock("header", "BÀI 1: HIỆN TẠI ĐƠN (PRESENT SIMPLE) & “TO BE”"));
        contentBlocks.add(new LessonBlock("section_title", "1. Mục tiêu"));
        contentBlocks.add(new LessonBlock("body", "• Giới thiệu bản thân, nghề nghiệp, thói quen\n• Hiểu sự khác nhau giữa động từ thường và to be"));

        contentBlocks.add(new LessonBlock("section_title", "2. Cách dùng"));
        contentBlocks.add(new LessonBlock("body", "Thì hiện tại đơn dùng để:"));
        contentBlocks.add(new LessonBlock("bullet", "Diễn tả sự thật hiển nhiên → The sun rises in the east"));
        contentBlocks.add(new LessonBlock("bullet", "Diễn tả thói quen lặp lại → I go to school every day"));
        contentBlocks.add(new LessonBlock("bullet", "Diễn tả trạng thái → I feel tired"));

        contentBlocks.add(new LessonBlock("section_title", "3. Cấu trúc"));
        contentBlocks.add(new LessonBlock("sub_title", "a. Động từ “TO BE”"));
        contentBlocks.add(new LessonBlock("formula", "Khẳng định: S + am/is/are + N/Adj\nPhủ định: S + am/is/are + not\nCâu hỏi: Am/Is/Are + S ?"));
        contentBlocks.add(new LessonBlock("example", "• I am a student\n• She is happy\n• Are you tired?"));

        contentBlocks.add(new LessonBlock("sub_title", "b. Động từ thường"));
        contentBlocks.add(new LessonBlock("formula", "Khẳng định: S + V(s/es)\nPhủ định: S + do/does not + V\nCâu hỏi: Do/Does + S + V?"));
        contentBlocks.add(new LessonBlock("example", "• She works in an office\n• He doesn’t like coffee\n• Do you play football?"));

        contentBlocks.add(new LessonBlock("section_title", "4. Lưu ý quan trọng"));
        contentBlocks.add(new LessonBlock("note", "• He/She/It → thêm s/es\n• go → goes, study → studies\n• Không dùng “to be” + động từ thường ❌"));

        contentBlocks.add(new LessonBlock("section_title", "5. Lỗi sai phổ biến"));
        contentBlocks.add(new LessonBlock("error_correction", null, "She go to school", "She goes to school"));
        contentBlocks.add(new LessonBlock("error_correction", null, "I am go to work", "I go to work"));

        // 2. Đóng gói thành Document
        Map<String, Object> lessonData = new HashMap<>();
        lessonData.put("title", "BÀI 1: HIỆN TẠI ĐƠN");
        lessonData.put("content_blocks", contentBlocks);
        lessonData.put("level", "Beginner");
        lessonData.put("order", 1);

        // 3. Đẩy lên Firestore vào Collection "lessons" với ID "lesson_01"
        db.collection("lessons").document("lesson_01")
                .set(lessonData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Đã đẩy dữ liệu Bài 1 lên thành công!");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi đẩy dữ liệu: ", e);
                });
    }
}

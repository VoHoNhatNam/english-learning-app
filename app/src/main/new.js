service cloud.firestore {
  match /databases/{database}/documents {// Phần Từ vựng
    match /vocabulary_lessons/{document=**} {
      allow read, write: if true;
    }

    // Phần Ngữ pháp
    match /grammar_lessons/{document=**} {
      allow read, write: if true;
    }
    
    // QUAN TRỌNG: Thêm phần này để cho phép lưu bài đọc hiểu
    match /reading_articles/{document=**} {
      allow read, write: if true;
    }
    
    // Phần Người dùng
    match /users/{userId} {
      allow read, write: if true;
    }
  }
}
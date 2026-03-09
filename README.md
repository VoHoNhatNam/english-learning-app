# English Learning App 📚🤖

## 1. Giới thiệu dự án

English Learning App là ứng dụng học tiếng Anh trên nền tảng Android giúp người dùng học từ vựng, luyện tập và theo dõi tiến độ học tập.

Ứng dụng tích hợp **AI** để phân tích dữ liệu học tập của người dùng và đưa ra **gợi ý ôn tập cá nhân hóa**, giúp cải thiện hiệu quả học tập.

Ngoài ra, người dùng còn có thể **tự tạo bài học riêng** để học các từ vựng quan trọng đối với mình.

---

# 2. Mục tiêu của dự án

* Xây dựng ứng dụng học tiếng Anh thông minh
* Cá nhân hóa trải nghiệm học tập
* Phân tích dữ liệu học tập của người dùng bằng AI
* Đề xuất nội dung ôn tập phù hợp
* Tạo trải nghiệm học tập hiệu quả và thân thiện

---

# 3. Chức năng chính

## Authentication

* Đăng ký tài khoản
* Đăng nhập

---

## Learning

Ứng dụng cung cấp các chức năng học tập chính:

* Xem danh sách bài học
* Học từ vựng
* Làm bài kiểm tra (Quiz)
* Ôn tập lại các bài học đã học

Ngoài ra người dùng còn có thể:

### Custom Lesson (Tạo bài học cá nhân)

Người dùng có thể tự tạo bài học riêng:

* Thêm từ vựng mới
* Thêm nghĩa và ví dụ
* Tạo danh sách từ cần học

Các bài học do người dùng tạo có thể:

* lưu vào tài khoản cá nhân
* dùng để luyện tập
* dùng để tạo bài kiểm tra riêng

---

## Progress Tracking

Ứng dụng lưu dữ liệu học tập của người dùng:

* tiến độ học tập
* kết quả bài kiểm tra
* thời gian học
* số lần làm bài

---

## AI Features

AI hỗ trợ học tập thông minh:

* Chat với AI
* Phân tích kết quả học tập
* Phân tích thời gian học
* Phân tích câu hỏi người dùng thường sai
* Đề xuất nội dung ôn tập
* Sinh câu hỏi luyện tập

---

# 4. AI Learning Flow

Ứng dụng sử dụng dữ liệu học tập để phân tích và hỗ trợ người dùng.

```
User học bài
     ↓
Làm Quiz
     ↓
Lưu kết quả (TestResult)
     ↓
Phân tích dữ liệu học tập
     ↓
AI Recommendation
     ↓
Gợi ý nội dung ôn tập
```

AI có thể phân tích:

* điểm số bài kiểm tra
* thời gian học
* thời gian làm bài
* số lần sai câu hỏi
* chủ đề người dùng còn yếu

---

# 5. Monetization (Kiếm tiền)

Ứng dụng sử dụng hai phương thức kiếm tiền.

---

## Quảng cáo (Ads)

Ứng dụng tích hợp **Google AdMob**.

Các loại quảng cáo:

* Banner Ads
* Interstitial Ads
* Reward Ads

Quảng cáo có thể xuất hiện:

* sau khi hoàn thành bài học
* sau khi hoàn thành quiz

---

## VIP Subscription

Người dùng có thể mua **VIP subscription** để nâng cao trải nghiệm sử dụng ứng dụng.

Khi mua VIP:

* Tắt toàn bộ quảng cáo
* Truy cập các tính năng AI nâng cao
* Phân tích học tập chi tiết hơn
* Nhận gợi ý học tập thông minh hơn

Lưu ý:

**Tất cả người dùng đều có thể truy cập toàn bộ bài học.**

VIP chỉ giúp **nâng cao trải nghiệm và tính năng**, không giới hạn nội dung học.

Thanh toán sử dụng **Google Play Billing**.

---

# 6. Công nghệ sử dụng

* Android (Java)
* MVVM Architecture
* Repository Pattern
* Firebase / API
* AI API (GPT / Gemini)
* Google AdMob
* Google Play Billing

---

# 7. Cấu trúc project

```
com.example.englishlearningapp

ads
billing

data
   model
   repository
   local
   remote
   firebase

ui
   auth
   home
   lesson
   quiz
   chat
   profile

viewmodel
utils
```

---

# 8. Model chính

```
User
Lesson
Vocabulary
QuizQuestion
TestResult
UserProgress
ChatMessage
AIRecommendation
```

---

# 9. Kiến trúc hệ thống

Ứng dụng sử dụng **MVVM Architecture**

```
UI (Activity / Fragment)
        ↓
ViewModel
        ↓
Repository
        ↓
Data Source (API / Firebase / Local DB)
```

---


# 10. Thành viên dự án

Leader
Võ Hồ Nhật Nam

Members

* Trương Vĩnh Hòa 
* Phan Thanh Hải 

---

# 11. Roadmap phát triển

* Hoàn thiện UI các màn hình
* Xây dựng hệ thống bài học
* Xây dựng hệ thống Quiz
* Tích hợp AI phân tích dữ liệu học tập
* Tích hợp quảng cáo AdMob
* Xây dựng hệ thống VIP Subscription
* Publish ứng dụng lên Google Play

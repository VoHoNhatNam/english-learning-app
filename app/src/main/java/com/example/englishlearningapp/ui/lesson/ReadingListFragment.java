package com.example.englishlearningapp.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ReadingArticle;

import java.util.ArrayList;
import java.util.List;

public class ReadingListFragment extends Fragment {

    private RecyclerView rvReadingList;
    private ReadingViewModel viewModel;
    private ReadingListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading_list, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReadingViewModel.class);

        rvReadingList = view.findViewById(R.id.rvReadingList);
        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        view.findViewById(R.id.btnSeedData).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang cập nhật dữ liệu đọc hiểu lên Firebase...", Toast.LENGTH_SHORT).show();
            seedData();
        });

        setupRecyclerView();
        observeViewModel();

        viewModel.fetchArticlesIfNeeded();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new ReadingListAdapter(new ArrayList<>(), article -> {
            ReadingDetailFragment fragment = new ReadingDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("article", article);
            fragment.setArguments(bundle);
            
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvReadingList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReadingList.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getAllArticles().observe(getViewLifecycleOwner(), articles -> {
            if (articles != null) {
                adapter.setArticles(articles);
            }
        });
    }

    private void seedData() {
        List<ReadingArticle> articles = new ArrayList<>();

        // Bài 1: MY FAMILY AND THEIR JOBS
        articles.add(new ReadingArticle("reading_01", "Reading 1: My Family and Their Jobs", "Tom", "Student", "3 min read",
                "Hello, my name is Tom. I am a student, and I study at a school in my city. I am friendly and hard-working, and I like my life.\n\n" +
                "I live with my family. My father is a doctor. He works in a hospital, and he is very busy. My mother is an office worker. She works in a company. She is kind and friendly.\n\n" +
                "I also have a sister. She is a salesperson. She works in a shop. She is creative and friendly.\n\n" +
                "We are a happy family. We love each other very much.",
                "Xin chào, tôi tên là Tom. Tôi là học sinh và tôi học ở một trường trong thành phố của tôi. Tôi thân thiện và chăm chỉ, và tôi thích cuộc sống của mình.\n\n" +
                "Tôi sống cùng gia đình. Bố tôi là bác sĩ. Ông làm việc ở bệnh viện và rất bận rộn. Mẹ tôi là nhân viên văn phòng. Bà làm việc ở công ty. Bà rất tốt bụng và thân thiện.\n\n" +
                "Tôi cũng có một chị gái. Chị là nhân viên bán hàng. Chị làm việc ở cửa hàng. Chị rất sáng tạo và thân thiện.\n\n" +
                "Chúng tôi là một gia đình hạnh phúc. Chúng tôi rất yêu thương nhau.",
                "Beginner", "Học từ vựng về gia đình và nghề nghiệp cơ bản."));

        // Bài 2: MY DAILY LIFE
        articles.add(new ReadingArticle("reading_02", "Reading 2: My Daily Life", "Anna", "Student", "4 min read",
                "My name is Anna. Every day, I wake up at 6 a.m. I get up, brush my teeth, and take a shower. Then, I have breakfast. I eat bread and drink milk.\n\n" +
                "After that, I go to school. I study and talk with my friends. I like reading books and listening to music.\n\n" +
                "Now, I am at home. I am studying English. My brother is watching TV, and my mother is cooking dinner.\n\n" +
                "I like my daily life because it is simple and good.",
                "Tôi tên là Anna. Mỗi ngày tôi thức dậy lúc 6 giờ sáng. Tôi ra khỏi giường, đánh răng và tắm. Sau đó, tôi ăn sáng. Tôi ăn bánh mì và uống sữa.\n\n" +
                "Sau đó, tôi đi học. Tôi học và nói chuyện với bạn bè. Tôi thích đọc sách và nghe nhạc.\n\n" +
                "Bây giờ tôi đang ở nhà. Tôi đang học tiếng Anh. Anh trai tôi đang xem TV và mẹ tôi đang nấu ăn.\n\n" +
                "Tôi thích cuộc sống hàng ngày của mình vì nó đơn giản và tốt.",
                "Beginner", "Luyện tập các động từ chỉ hoạt động hàng ngày và thì hiện tại tiếp diễn."));

        // Bài 3: FOOD AND MY HOME
        articles.add(new ReadingArticle("reading_03", "Reading 3: Food and My Home", "Aura", "Education", "3 min read",
                "I live in a small house with my family. In the house, there is a table, some chairs, and a fridge.\n\n" +
                "Every day, I eat rice, vegetables, and meat. In the morning, I eat bread and eggs. I drink a glass of milk or water.\n\n" +
                "In the kitchen, there is a bottle of water and some fruit. My mother cooks every day. The food is very good.\n\n" +
                "I like eating with my family because it is happy.",
                "Tôi sống trong một ngôi nhà nhỏ với gia đình. Trong nhà có một cái bàn, vài cái ghế và một cái tủ lạnh.\n\n" +
                "Mỗi ngày tôi ăn cơm, rau và thịt. Buổi sáng tôi ăn bánh mì và trứng. Tôi uống một ly sữa hoặc nước.\n\n" +
                "Trong bếp có một chai nước và một ít trái cây. Mẹ tôi nấu ăn mỗi ngày. Đồ ăn rất ngon.\n\n" +
                "Tôi thích ăn cùng gia đình vì rất vui.",
                "Beginner", "Mở rộng vốn từ về thực phẩm và đồ dùng trong nhà."));

        // Bài 4: LAST WEEK
        articles.add(new ReadingArticle("reading_04", "Reading 4: Last Week", "Narrator", "Storyteller", "4 min read",
                "Last week, I was very happy. I visited my friend. We talked and played games.\n\n" +
                "Yesterday, I went to the park with my family. We walked and talked together. It was a nice day.\n\n" +
                "Two days ago, I stayed at home. I watched TV and listened to music.\n\n" +
                "Last year, I traveled with my family. It was a great time.",
                "Tuần trước tôi rất vui. Tôi đã đến thăm bạn. Chúng tôi nói chuyện và chơi game.\n\n" +
                "Hôm qua tôi đi công viên với gia đình. Chúng tôi đi dạo và trò chuyện cùng nhau. Đó là một ngày đẹp.\n\n" +
                "Hai ngày trước, tôi ở nhà. Tôi xem TV và nghe nhạc.\n\n" +
                "Năm ngoái, tôi đã đi du lịch với gia đình. Đó là khoảng thời gian tuyệt vời.",
                "Beginner", "Ôn tập các trạng từ chỉ thời gian quá khứ và động từ quá khứ đơn."));

        // Bài 5: MY HEALTH
        articles.add(new ReadingArticle("reading_05", "Reading 5: My Health", "Health Advisor", "Education", "5 min read",
                "Today, I feel tired because I study a lot. Sometimes, I have a headache or a sore throat.\n\n" +
                "When I feel sick, I drink water and take a rest. My mother says I should go to bed early.\n\n" +
                "If I have a fever, I see a doctor and take medicine.\n\n" +
                "I eat healthy food and drink water every day. I want to be healthy.",
                "Hôm nay tôi cảm thấy mệt vì học nhiều. Đôi khi tôi bị đau đầu hoặc đau họng.\n\n" +
                "Khi tôi bị ốm, tôi uống nước và nghỉ ngơi. Mẹ tôi nói tôi nên đi ngủ sớm.\n\n" +
                "Nếu tôi bị sốt, tôi đi khám bác sĩ và uống thuốc.\n\n" +
                "Tôi ăn đồ ăn lành mạnh và uống nước mỗi ngày. I want to be healthy.",
                "Beginner", "Từ vựng về sức khỏe và các lời khuyên y tế cơ bản."));

        viewModel.seedInitialData(articles);
    }
}

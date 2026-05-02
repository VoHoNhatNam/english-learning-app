package com.example.englishlearningapp.ui.lesson;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;
import com.example.englishlearningapp.data.model.VocabularyLesson;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VocabularyListFragment extends Fragment {

    private RecyclerView rvVocabularyList;
    private VocabularyListAdapter adapter;
    private List<VocabularyLesson> displayList = new ArrayList<>();
    private TextView tabSystem, tabMyVocab, txtVocabCount;
    private View btnCreateVocab;
    private TabLayout tabLayoutLevels;
    private VocabularyViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_list, container, false);

        // Sử dụng requireActivity() để ViewModel được giữ lại trong suốt vòng đời của Activity
        viewModel = new ViewModelProvider(requireActivity()).get(VocabularyViewModel.class);

        initViews(view);
        setupRecyclerView();
        setupTabs();
        observeViewModel();

        // Chỉ fetch nếu chưa có dữ liệu trong ViewModel
        viewModel.fetchLessonsIfNeeded();
        
        // Ẩn Bottom Navigation khi vào màn hình này
        setBottomNavigationVisibility(View.GONE);

        return view;
    }

    private void setBottomNavigationVisibility(int visibility) {
        if (getActivity() instanceof MainActivity) {
            BottomNavigationView navView = getActivity().findViewById(R.id.bottomNavigationView);
            if (navView != null) {
                navView.setVisibility(visibility);
            }
            View navContainer = getActivity().findViewById(R.id.bottomNavContainer);
            if (navContainer != null) {
                navContainer.setVisibility(visibility);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hiện lại Bottom Navigation khi thoát màn hình này
        setBottomNavigationVisibility(View.VISIBLE);
    }

    private void initViews(View view) {
        rvVocabularyList = view.findViewById(R.id.rvVocabularyList);
        tabSystem = view.findViewById(R.id.tabSystem);
        tabMyVocab = view.findViewById(R.id.tabMyVocab);
        txtVocabCount = view.findViewById(R.id.txtVocabCount);
        btnCreateVocab = view.findViewById(R.id.btnCreateVocab);
        tabLayoutLevels = view.findViewById(R.id.tabLayoutLevels);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        btnCreateVocab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang cập nhật dữ liệu mới lên Firebase...", Toast.LENGTH_SHORT).show();
            seedInitialData();
        });
    }

    private void setupRecyclerView() {
        adapter = new VocabularyListAdapter(displayList, lesson -> {
            VocabularyDetailFragment fragment = new VocabularyDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("lesson", lesson);
            fragment.setArguments(bundle);
            
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvVocabularyList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVocabularyList.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getAllLessons().observe(getViewLifecycleOwner(), lessons -> {
            if (lessons != null) {
                updateDisplayList();
            }
        });

        // Bạn có thể quan sát viewModel.getIsLoading() để hiện ProgressBar nếu cần
    }

    private void updateDisplayList() {
        if (viewModel.getAllLessons().getValue() == null) {
            displayList.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        int levelIndex = tabLayoutLevels.getSelectedTabPosition();
        String level = "Beginner";
        if (levelIndex == 1) level = "Intermediate";
        else if (levelIndex == 2) level = "Advanced";

        String finalLevel = level;
        List<VocabularyLesson> filtered = viewModel.getAllLessons().getValue().stream()
                .filter(l -> l.getLevel().equals(finalLevel))
                .collect(Collectors.toList());

        displayList.clear();
        displayList.addAll(filtered);
        adapter.notifyDataSetChanged();
        
        if (displayList.isEmpty()) {
            txtVocabCount.setText("TRỐNG");
        } else {
            txtVocabCount.setText(displayList.size() + " BÀI HỌC");
        }
    }

    private void setupTabs() {
        tabSystem.setOnClickListener(v -> {
            tabSystem.setBackgroundResource(R.drawable.bg_button);
            tabSystem.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            tabSystem.setTextColor(Color.parseColor("#10B981"));
            tabSystem.setTypeface(null, Typeface.BOLD);

            tabMyVocab.setBackground(null);
            tabMyVocab.setTextColor(Color.parseColor("#6B7280"));
            tabMyVocab.setTypeface(null, Typeface.NORMAL);
        });

        tabMyVocab.setOnClickListener(v -> {
            tabMyVocab.setBackgroundResource(R.drawable.bg_button);
            tabMyVocab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            tabMyVocab.setTextColor(Color.parseColor("#10B981"));
            tabMyVocab.setTypeface(null, Typeface.BOLD);

            tabSystem.setBackground(null);
            tabSystem.setTextColor(Color.parseColor("#6B7280"));
            tabSystem.setTypeface(null, Typeface.NORMAL);
        });

        if (tabLayoutLevels != null) {
            tabLayoutLevels.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    updateDisplayList();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void seedInitialData() {
        List<VocabularyLesson> lessons = new ArrayList<>();

        // BÀI 1: THÔNG TIN CÁ NHÂN & NGHỀ NGHIỆP
        List<Vocabulary> words1 = new ArrayList<>();
        words1.add(new Vocabulary("Teacher", "n", "Giáo viên", "/ˈtiː.tʃər/", "She is a teacher.", "Cô ấy là giáo viên."));
        words1.add(new Vocabulary("Engineer", "n", "Kỹ sư", "/ˌen.dʒɪˈnɪr/", "He is an engineer.", "Anh ấy là kỹ sư."));
        words1.add(new Vocabulary("Doctor", "n", "Bác sĩ", "/ˈdɒk.tər/", "My father is a doctor.", "Bố tôi là bác sĩ."));
        words1.add(new Vocabulary("Salesperson", "n", "Nhân viên bán hàng", "/ˈseɪlz.pɜː.sən/", "She works as a salesperson.", "Cô ấy làm nhân viên bán hàng."));
        words1.add(new Vocabulary("Student", "n", "Học sinh / sinh viên", "/ˈstjuː.dənt/", "I am a student.", "Tôi là học sinh."));
        words1.add(new Vocabulary("Friendly", "adj", "Thân thiện", "/ˈfrend.li/", "She is very friendly.", "Cô ấy rất thân thiện."));
        words1.add(new Vocabulary("Hard-working", "adj", "Chăm chỉ", "/ˌhɑːdˈwɜː.kɪŋ/", "He is hard-working.", "Anh ấy chăm chỉ."));
        words1.add(new Vocabulary("Office", "n", "Văn phòng", "/ˈɒf.ɪs/", "I work in an office.", "Tôi làm việc ở văn phòng."));
        words1.add(new Vocabulary("Hospital", "n", "Bệnh viện", "/ˈhɒs.pɪ.təl/", "She works in a hospital.", "Cô ấy làm việc ở bệnh viện."));
        lessons.add(new VocabularyLesson("v_lesson_01", "Bài 1: Thông tin cá nhân & Nghề nghiệp", "Nắm các từ vựng cơ bản để nói về bản thân, công việc và môi trường làm việc.", "Beginner", 0, words1));

        // BÀI 2: HÀNH ĐỘNG HẰNG NGÀY & SỞ THÍCH
        List<Vocabulary> words2 = new ArrayList<>();
        words2.add(new Vocabulary("Cook", "v", "Nấu ăn", "/kʊk/", "I am cooking dinner.", "Tôi đang nấu bữa tối."));
        words2.add(new Vocabulary("Clean", "v", "Lau dọn", "/kliːn/", "She is cleaning the room.", "Cô ấy đang dọn phòng."));
        words2.add(new Vocabulary("Run", "v", "Chạy", "/rʌn/", "He is running.", "Anh ấy đang chạy."));
        words2.add(new Vocabulary("Listen to music", "phrase", "Nghe nhạc", "/ˈlɪs.ən/", "I am listening to music.", "Tôi đang nghe nhạc."));
        words2.add(new Vocabulary("Watch TV", "phrase", "Xem TV", "/wɒtʃ/", "They are watching TV.", "Họ đang xem TV."));
        words2.add(new Vocabulary("Study", "v", "Học", "/ˈstʌd.i/", "She is studying.", "Cô ấy đang học."));
        words2.add(new Vocabulary("Wake up", "phrase", "Thức dậy", "/weɪk ʌp/", "I wake up at 6 a.m.", "Tôi thức dậy lúc 6 giờ."));
        words2.add(new Vocabulary("Play football", "phrase", "Chơi bóng đá", "/pleɪ/", "I play football on weekends.", "Tôi chơi bóng vào cuối tuần."));
        lessons.add(new VocabularyLesson("v_lesson_02", "Bài 2: Hành động hằng ngày & Sở thích", "Diễn tả các hoạt động hàng ngày và sở thích cá nhân.", "Beginner", 0, words2));

        // BÀI 3: ĐỒ ĂN, ĐỒ UỐNG & VẬT DỤNG
        List<Vocabulary> words3 = new ArrayList<>();
        words3.add(new Vocabulary("Bread", "n", "Bánh mì", "/bred/", "I eat bread for breakfast.", "Tôi ăn bánh mì buổi sáng."));
        words3.add(new Vocabulary("Rice", "n", "Cơm / gạo", "/raɪs/", "I eat rice every day.", "Tôi ăn cơm mỗi ngày."));
        words3.add(new Vocabulary("Vegetable", "n", "Rau", "/ˈvedʒ.tə.bəl/", "Vegetables are healthy.", "Rau rất tốt cho sức khỏe."));
        words3.add(new Vocabulary("Milk", "n", "Sữa", "/mɪlk/", "I drink milk every morning.", "Tôi uống sữa mỗi sáng."));
        words3.add(new Vocabulary("Coffee", "n", "Cà phê", "/ˈkɒf.i/", "He drinks coffee.", "Anh ấy uống cà phê."));
        words3.add(new Vocabulary("A bottle of water", "phrase", "Một chai nước", "/ˈbɒt.əl/", "I buy a bottle of water.", "Tôi mua một chai nước."));
        words3.add(new Vocabulary("Table", "n", "Bàn", "/ˈteɪ.bəl/", "The book is on the table.", "Cuốn sách ở trên bàn."));
        words3.add(new Vocabulary("Fridge", "n", "Tủ lạnh", "/frɪdʒ/", "The milk is in the fridge.", "Sữa ở trong tủ lạnh."));
        lessons.add(new VocabularyLesson("v_lesson_03", "Bài 3: Đồ ăn, đồ uống & Vật dụng", "Giao tiếp trong các tình huống mua sắm, ăn uống hoặc sinh hoạt gia đình.", "Beginner", 0, words3));

        // BÀI 4: THỜI GIAN & HOẠT ĐỘNG
        List<Vocabulary> words4 = new ArrayList<>();
        words4.add(new Vocabulary("Yesterday", "n", "Hôm qua", "/ˈjes.tə.deɪ/", "I met him yesterday.", "Tôi gặp anh ấy hôm qua."));
        words4.add(new Vocabulary("Last week", "phrase", "Tuần trước", "/lɑːst wiːk/", "I visited my friend last week.", "Tôi đã thăm bạn tuần trước."));
        words4.add(new Vocabulary("Travel", "v", "Du lịch", "/ˈtræv.əl/", "I travel every year.", "Tôi đi du lịch mỗi năm."));
        words4.add(new Vocabulary("Visit", "v", "Thăm", "/ˈvɪz.ɪt/", "I visited my parents.", "Tôi đã thăm bố mẹ."));
        words4.add(new Vocabulary("Watch a movie", "phrase", "Xem phim", "/wɒtʃ/", "We watched a movie.", "Chúng tôi đã xem phim."));
        words4.add(new Vocabulary("Go camping", "phrase", "Đi cắm trại", "/ˈkæm.pɪŋ/", "They went camping.", "Họ đã đi cắm trại."));
        lessons.add(new VocabularyLesson("v_lesson_04", "Bài 4: Thời gian & Hoạt động giải trí", "Nói về các hoạt động đã xảy ra và các trải nghiệm trong cuộc sống.", "Beginner", 0, words4));

        // BÀI 5: SỨC KHỎE & LỜI KHUYÊN
        List<Vocabulary> words5 = new ArrayList<>();
        words5.add(new Vocabulary("Headache", "n", "Đau đầu", "/ˈhed.eɪk/", "I have a headache.", "Tôi bị đau đầu."));
        words5.add(new Vocabulary("Sore throat", "n", "Đau họng", "/sɔːr θrəʊt/", "She has a sore throat.", "Cô ấy bị đau họng."));
        words5.add(new Vocabulary("Tired", "adj", "Mệt", "/ˈtaɪəd/", "I feel tired.", "Tôi cảm thấy mệt."));
        words5.add(new Vocabulary("Fever", "n", "Sốt", "/ˈfiː.vər/", "He has a fever.", "Anh ấy bị sốt."));
        words5.add(new Vocabulary("Take medicine", "phrase", "Uống thuốc", "/ˈmed.ɪ.sən/", "You should take medicine.", "Bạn nên uống thuốc."));
        words5.add(new Vocabulary("Drink water", "phrase", "Uống nước", "/drɪŋk/", "Drink more water.", "Hãy uống nhiều nước hơn."));
        lessons.add(new VocabularyLesson("v_lesson_05", "Bài 5: Sức khỏe & Lời khuyên", "Giao tiếp khi không khỏe hoặc khi cần giúp đỡ trong các tình huống thực tế.", "Beginner", 0, words5));

        viewModel.seedInitialData(lessons);
    }
}

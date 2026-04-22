package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.example.englishlearningapp.ui.lesson.GrammarFragment;
import com.example.englishlearningapp.ui.lesson.LessonListFragment;
import com.example.englishlearningapp.ui.lesson.VocabularyListFragment;
import com.example.englishlearningapp.ui.mission.MissionFragment;
import com.example.englishlearningapp.utils.UserStateManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView tvUserName, tvCurrentRoadmap;
    private View[] bars = new View[7];
    private TextView[] tvDays = new TextView[7];
    private View lnChat, lnFlashcard, lnQuiz, lnMore;
    private View btnHeaderMission, btnHeaderNotification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        mAuth = FirebaseAuth.getInstance();
        tvUserName = view.findViewById(R.id.tvUserName);
        tvCurrentRoadmap = view.findViewById(R.id.tvCurrentRoadmap);
        
        // Initialize bars and tvDays arrays
        for (int i = 0; i < 7; i++) {
            int barId = getResources().getIdentifier("bar" + (i + 1), "id", getContext().getPackageName());
            int dayId = getResources().getIdentifier("tvDay" + (i + 1), "id", getContext().getPackageName());
            bars[i] = view.findViewById(barId);
            tvDays[i] = view.findViewById(dayId);
        }

        lnChat = view.findViewById(R.id.lnChat);
        lnFlashcard = view.findViewById(R.id.lnFlashcard);
        lnQuiz = view.findViewById(R.id.lnQuiz);
        lnMore = view.findViewById(R.id.lnMore);
        
        // Find header buttons from included header
        btnHeaderMission = view.findViewById(R.id.btnHeaderMission);
        btnHeaderNotification = view.findViewById(R.id.btnHeaderNotification);

        setupChart();
        setupUserDisplay();
        setupGlobalStateObserver();
        setupClickListeners();
        
        return view;
    }

    private void setupGlobalStateObserver() {
        // KHỞI TẠO 1 LẦN: Lấy dữ liệu từ Firebase nếu chưa có
        UserStateManager.getInstance().initLevel();

        // LẮNG NGHE THAY ĐỔI: Tự động cập nhật UI khi Level thay đổi ở bất cứ đâu
        UserStateManager.getInstance().getCurrentLevel().observe(getViewLifecycleOwner(), level -> {
            if (tvCurrentRoadmap != null) {
                tvCurrentRoadmap.setText("Lộ trình: " + level);
            }
        });
    }

    private void setupClickListeners() {
        lnChat.setOnClickListener(v -> replaceFragment(new ChatFragment()));
        
        lnFlashcard.setOnClickListener(v -> replaceFragment(new GrammarFragment()));
        
        lnQuiz.setOnClickListener(v -> replaceFragment(new VocabularyListFragment()));
        
        lnMore.setOnClickListener(v -> replaceFragment(new LessonListFragment()));

        if (btnHeaderMission != null) {
            btnHeaderMission.setOnClickListener(v -> replaceFragment(new MissionFragment()));
        }

        if (btnHeaderNotification != null) {
            btnHeaderNotification.setOnClickListener(v -> replaceFragment(new NotificationFragment()));
        }

        View btnTalkAI = getView() != null ? getView().findViewById(R.id.btnTalkAI) : null;
        if (btnTalkAI != null) {
            btnTalkAI.setOnClickListener(v -> replaceFragment(new ChatFragment()));
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                
                if (tvDays[i] != null) {
                    tvDays[i].setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
                    tvDays[i].setTypeface(null, android.graphics.Typeface.BOLD);
                }
                if (bars[i] != null) {
                    bars[i].setBackgroundResource(R.drawable.bg_bar_chart_active);
                }
            } else {
                if (tvDays[i] != null) {
                    tvDays[i].setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
                    tvDays[i].setTypeface(null, android.graphics.Typeface.NORMAL);
                }
                if (bars[i] != null) {
                    bars[i].setBackgroundResource(R.drawable.bg_bar_chart);
                }
            }
            
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void setupUserDisplay() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && tvUserName != null) {
            String name = user.getDisplayName();
            tvUserName.setText((name != null && !name.isEmpty()) ? "Chào mừng trở lại,\n" + name + "." : "Chào mừng trở lại,\nNgười dùng.");
        }
    }
}

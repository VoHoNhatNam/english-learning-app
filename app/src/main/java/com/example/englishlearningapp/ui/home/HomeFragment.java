package com.example.englishlearningapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.chat.ChatFragment;
import com.example.englishlearningapp.ui.quiz.QuizFragment;
import com.example.englishlearningapp.ui.lesson.FlashcardFragment;
import com.example.englishlearningapp.ui.lesson.FillWordFragment;
import com.example.englishlearningapp.ui.lesson.GrammarFragment;
import com.example.englishlearningapp.ui.mission.MissionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private View lnFlashcard, lnQuiz, lnChat, lnMore;
    private TextView tvUserName, tvCurrentRoadmap;
    private TextView[] tvDays = new TextView[7];
    private View[] bars = new View[7];
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews(view);
        setupUserDisplay();
        loadUserRoadmap();
        setupButtons();
        updateRealDatesAndBars();
        setupHeader(view);

        return view;
    }

    private void initViews(View view) {
        lnFlashcard = view.findViewById(R.id.lnFlashcard);
        lnQuiz = view.findViewById(R.id.lnQuiz);
        lnChat = view.findViewById(R.id.lnChat);
        lnMore = view.findViewById(R.id.lnMore);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvCurrentRoadmap = view.findViewById(R.id.tvCurrentRoadmap);

        tvDays[0] = view.findViewById(R.id.tvDay1);
        tvDays[1] = view.findViewById(R.id.tvDay2);
        tvDays[2] = view.findViewById(R.id.tvDay3);
        tvDays[3] = view.findViewById(R.id.tvDay4);
        tvDays[4] = view.findViewById(R.id.tvDay5);
        tvDays[5] = view.findViewById(R.id.tvDay6);
        tvDays[6] = view.findViewById(R.id.tvDay7);

        bars[0] = view.findViewById(R.id.bar1);
        bars[1] = view.findViewById(R.id.bar2);
        bars[2] = view.findViewById(R.id.bar3);
        bars[3] = view.findViewById(R.id.bar4);
        bars[4] = view.findViewById(R.id.bar5);
        bars[5] = view.findViewById(R.id.bar6);
        bars[6] = view.findViewById(R.id.bar7);
    }

    private void setupHeader(View view) {
        View btnMission = view.findViewById(R.id.btnHeaderMission);
        View badgeMission = view.findViewById(R.id.viewMissionBadge);
        
        if (btnMission != null) {
            Animation pulse = AnimationUtils.loadAnimation(getContext(), R.anim.pulse);
            if (badgeMission != null) {
                badgeMission.startAnimation(pulse);
            }
            
            btnMission.setOnClickListener(v -> {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.logo_scale));
                navigateToFragment(new MissionFragment());
            });
        }
    }

    private void loadUserRoadmap() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Lấy dữ liệu từ trường englishLevel theo yêu cầu
                                String level = document.getString("englishLevel");
                                
                                if (level != null && !level.isEmpty()) {
                                    // Định dạng lại: viết hoa chữ cái đầu (ví dụ: "mới bắt đầu" -> "Mới bắt đầu")
                                    String formattedLevel = level.substring(0, 1).toUpperCase() + level.substring(1);
                                    tvCurrentRoadmap.setText("Lộ trình: " + formattedLevel);
                                } else {
                                    tvCurrentRoadmap.setText("Lộ trình: Mới bắt đầu");
                                }
                            } else {
                                tvCurrentRoadmap.setText("Lộ trình: Mới bắt đầu");
                            }
                        } else {
                            Log.e(TAG, "Lỗi lấy lộ trình: ", task.getException());
                            tvCurrentRoadmap.setText("Lộ trình: Mới bắt đầu");
                        }
                    });
        }
    }

    private void updateRealDatesAndBars() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        int daysToSubtract = (currentDayOfWeek == Calendar.SUNDAY) ? 6 : (currentDayOfWeek - Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract);

        String[] dayNames = {"TH 2", "TH 3", "TH 4", "TH 5", "TH 6", "TH 7", "CN"};
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            tvDays[i].setText(dayNames[i] + "\n" + dayOfMonth);
            
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                
                tvDays[i].setTextColor(ContextCompat.getColor(getContext(), R.color.green_primary));
                tvDays[i].setTypeface(null, android.graphics.Typeface.BOLD);
                bars[i].setBackgroundResource(R.drawable.bg_bar_chart_active);
            } else {
                tvDays[i].setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
                tvDays[i].setTypeface(null, android.graphics.Typeface.NORMAL);
                bars[i].setBackgroundResource(R.drawable.bg_bar_chart);
            }
            
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void setupUserDisplay() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            tvUserName.setText((name != null && !name.isEmpty()) ? "Chào mừng trở lại,\n" + name + "." : "Chào mừng trở lại,\nNgười dùng.");
        } else {
            tvUserName.setText("Chào mừng trở lại,\nNgười dùng.");
        }
    }

    private void setupButtons() {
        lnFlashcard.setOnClickListener(v -> navigateToFragment(new GrammarFragment()));
        lnQuiz.setOnClickListener(v -> navigateToFragment(new QuizFragment()));
        lnChat.setOnClickListener(v -> navigateToFragment(new ChatFragment()));
        lnMore.setOnClickListener(v -> navigateToFragment(new FillWordFragment()));
    }

    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.logo_scale, android.R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
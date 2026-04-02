package com.example.englishlearningapp.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;
import com.google.android.material.button.MaterialButton;

public class OnboardingFragment extends Fragment {

    private MaterialButton btnGetStarted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện XML
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        btnGetStarted = view.findViewById(R.id.btnGetStarted);

        btnGetStarted.setOnClickListener(v -> {
            // Chuyển sang màn hình hỏi Độ tuổi (AgeFragment)
            navigateToAgeFragment();
        });

        return view;
    }

    private void navigateToAgeFragment() {
        AgeFragment ageFragment = new AgeFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Hiệu ứng chuyển cảnh mượt mà (Tùy chọn)
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

        transaction.replace(R.id.fragment_container, ageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
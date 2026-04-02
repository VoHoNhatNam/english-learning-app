package com.example.englishlearningapp.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;
import com.google.android.material.button.MaterialButton;

public class AgeFragment extends Fragment {

    private MaterialButton btnKids, btnTeens, btnAdults, btnSenior, btnNext;
    private String selectedAge = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age, container, false);

        initViews(view);
        setupSelectionLogic();

        btnNext.setOnClickListener(v -> {
            // Chuyển sang SchoolFragment
            navigateToSchoolFragment();
        });

        return view;
    }

    private void initViews(View view) {
        btnKids = view.findViewById(R.id.btnAgeKids);
        btnTeens = view.findViewById(R.id.btnAgeTeens);
        btnAdults = view.findViewById(R.id.btnAgeAdults);
        btnSenior = view.findViewById(R.id.btnAgeSenior);
        btnNext = view.findViewById(R.id.btnAgeNext);
    }

    private void setupSelectionLogic() {
        View.OnClickListener ageClickListener = v -> {
            // Reset tất cả nút về màu xám (mặc định)
            resetButtons();

            // Highlight nút được chọn (màu xanh)
            MaterialButton clickedButton = (MaterialButton) v;
            clickedButton.setStrokeColorResource(R.color.blue); // Bạn cần định nghĩa màu này
            clickedButton.setStrokeWidth(4);

            selectedAge = clickedButton.getText().toString();
            btnNext.setEnabled(true); // Bật nút Tiếp tục
        };

        btnKids.setOnClickListener(ageClickListener);
        btnTeens.setOnClickListener(ageClickListener);
        btnAdults.setOnClickListener(ageClickListener);
        btnSenior.setOnClickListener(ageClickListener);
    }

    private void resetButtons() {
        int grayColor = android.graphics.Color.parseColor("#E0E0E0");
        btnKids.setStrokeColor(android.content.res.ColorStateList.valueOf(grayColor));
        btnTeens.setStrokeColor(android.content.res.ColorStateList.valueOf(grayColor));
        btnAdults.setStrokeColor(android.content.res.ColorStateList.valueOf(grayColor));
        btnSenior.setStrokeColor(android.content.res.ColorStateList.valueOf(grayColor));

        btnKids.setStrokeWidth(2);
        btnTeens.setStrokeWidth(2);
        btnAdults.setStrokeWidth(2);
        btnSenior.setStrokeWidth(2);
    }

    private void navigateToSchoolFragment() {
        SchoolFragment schoolFragment = new SchoolFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Thêm hiệu ứng slide sang trái
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

        transaction.replace(R.id.fragment_container, schoolFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
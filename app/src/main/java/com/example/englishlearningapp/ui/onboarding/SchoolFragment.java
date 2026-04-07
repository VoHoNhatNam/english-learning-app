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

public class SchoolFragment extends Fragment {

    private MaterialButton btnStudent, btnUni, btnWorker, btnOther, btnNext;
    private String selectedGoal = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school, container, false);

        initViews(view);
        setupSelectionLogic();

        btnNext.setOnClickListener(v -> {
            // Sau khi chọn môi trường học, chuyển sang LevelFragment
            navigateToLevelFragment();
        });

        return view;
    }

    private void initViews(View view) {
        btnUni = view.findViewById(R.id.btnSchoolUni);
        btnWorker = view.findViewById(R.id.btnSchoolWorker);
        btnOther = view.findViewById(R.id.btnSchoolOther);
        btnNext = view.findViewById(R.id.btnSchoolNext);
    }

    private void setupSelectionLogic() {
        View.OnClickListener schoolClickListener = v -> {
            resetButtons();

            MaterialButton clickedButton = (MaterialButton) v;
            // Highlight nút được chọn (Sử dụng màu xanh chính của app)
            clickedButton.setStrokeColorResource(R.color.blue);
            clickedButton.setStrokeWidth(4);

            selectedGoal = clickedButton.getText().toString();
            btnNext.setEnabled(true);
        };

        btnStudent.setOnClickListener(schoolClickListener);
        btnUni.setOnClickListener(schoolClickListener);
        btnWorker.setOnClickListener(schoolClickListener);
        btnOther.setOnClickListener(schoolClickListener);
    }

    private void resetButtons() {
        int grayColor = android.graphics.Color.parseColor("#E0E0E0");
        MaterialButton[] buttons = {btnStudent, btnUni, btnWorker, btnOther};
        for (MaterialButton btn : buttons) {
            btn.setStrokeColor(android.content.res.ColorStateList.valueOf(grayColor));
            btn.setStrokeWidth(2);
        }
    }

    private void navigateToLevelFragment() {
        LevelFragment levelFragment = new LevelFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Hiệu ứng slide mượt mà
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

        transaction.replace(R.id.fragment_container, levelFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
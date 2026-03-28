package com.example.englishlearningapp.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.google.android.material.button.MaterialButton;

public class LevelFragment extends Fragment {

    private MaterialButton btnBeginner, btnInter, btnAdvanced, btnFinish;
    private String selectedLevel = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        initViews(view);
        setupSelectionLogic();

        btnFinish.setOnClickListener(v -> {
            // Lưu thông tin người dùng vào Firebase Firestore tại đây nếu cần

            // Kết thúc Onboarding, chuyển vào màn hình chính (MainActivity)
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void initViews(View view) {
        btnBeginner = view.findViewById(R.id.btnLevelBeginner);
        btnInter = view.findViewById(R.id.btnLevelInter);
        btnAdvanced = view.findViewById(R.id.btnLevelAdvanced);
        btnFinish = view.findViewById(R.id.btnLevelFinish);
    }

    private void setupSelectionLogic() {
        View.OnClickListener levelClickListener = v -> {
            resetButtons();

            MaterialButton clickedButton = (MaterialButton) v;
            // Highlight nút được chọn (Màu xanh chủ đạo)
            clickedButton.setStrokeColorResource(R.color.blue);
            clickedButton.setStrokeWidth(4);

            selectedLevel = clickedButton.getText().toString();
            btnFinish.setEnabled(true);
        };

        btnBeginner.setOnClickListener(levelClickListener);
        btnInter.setOnClickListener(levelClickListener);
        btnAdvanced.setOnClickListener(levelClickListener);
    }

    private void resetButtons() {
        int grayColor = android.graphics.Color.parseColor("#E0E0E0");
        MaterialButton[] buttons = {btnBeginner, btnInter, btnAdvanced};
        for (MaterialButton btn : buttons) {
            btn.setStrokeColor(android.content.res.ColorStateList.valueOf(grayColor));
            btn.setStrokeWidth(2);
        }
    }
}
package com.example.englishlearningapp.ui.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;

public class AgeSurveyFragment extends Fragment {

    private EditText etAge;
    private TextView tvAgeError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age_survey, container, false);

        etAge = view.findViewById(R.id.etAge);
        tvAgeError = view.findViewById(R.id.tvAgeError);

        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateAge(s.toString());
            }
        });

        return view;
    }

    private void validateAge(String input) {
        if (input.isEmpty()) {
            showError("Vui lòng nhập tuổi");
            updateAgeInActivity(-1);
            return;
        }

        // Loại bỏ khoảng trắng
        String cleanInput = input.trim();
        if (cleanInput.isEmpty()) {
            showError("Vui lòng không chỉ nhập khoảng trắng");
            updateAgeInActivity(-1);
            return;
        }

        // Kiểm tra số bắt đầu bằng 0 (ví dụ: 05, 018...)
        if (cleanInput.length() > 1 && cleanInput.startsWith("0")) {
            showError("Số tuổi không được bắt đầu bằng số 0");
            updateAgeInActivity(-1);
            return;
        }

        try {
            int age = Integer.parseInt(cleanInput);

            if (age < 16) {
                showError("Ứng dụng dành cho bậc Đại học (tối thiểu 16 tuổi)");
                updateAgeInActivity(-1);
            } else if (age > 120) {
                showError("Độ tuổi tối đa là 120 tuổi");
                updateAgeInActivity(-1);
            } else {
                hideError();
                updateAgeInActivity(age);
            }
        } catch (NumberFormatException e) {
            showError("Định dạng tuổi không hợp lệ");
            updateAgeInActivity(-1);
        }
    }

    private void showError(String message) {
        tvAgeError.setText(message);
        tvAgeError.setVisibility(View.VISIBLE);
        etAge.setBackgroundResource(R.drawable.bg_input_error);
    }

    private void hideError() {
        tvAgeError.setVisibility(View.INVISIBLE);
        etAge.setBackgroundResource(R.drawable.bg_input);
    }

    private void updateAgeInActivity(int age) {
        if (getActivity() instanceof OnboardingSurveyActivity) {
            ((OnboardingSurveyActivity) getActivity()).setAge(age);
        }
    }
}
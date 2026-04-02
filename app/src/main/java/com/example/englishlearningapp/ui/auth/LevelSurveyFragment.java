package com.example.englishlearningapp.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearningapp.R;

public class LevelSurveyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_survey, container, false);

        RadioGroup rg = view.findViewById(R.id.rgLevel);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = view.findViewById(checkedId);
            if (rb != null && getActivity() instanceof OnboardingSurveyActivity) {
                ((OnboardingSurveyActivity) getActivity()).setLevel(rb.getText().toString());
            }
        });

        return view;
    }
}
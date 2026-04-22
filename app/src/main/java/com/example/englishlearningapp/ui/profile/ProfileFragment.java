package com.example.englishlearningapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.ui.auth.LoginActivity;
import com.example.englishlearningapp.utils.UserStateManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private TextView txtName, txtEmail, tvUserLevelTag;
    private View btnLogout, btnAdjustGoals, btnShowLevelSheet, btnGoVip;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews(view);
        setupGlobalStateObserver();
        loadUserInfo();

        // Mở Bottom Sheet Nâng cấp VIP
        if (btnGoVip != null) {
            btnGoVip.setOnClickListener(v -> {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new VipFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            });
        }

        // Mở Bottom Sheet điều chỉnh mục tiêu
        if (btnAdjustGoals != null) {
            btnAdjustGoals.setOnClickListener(v -> showAdjustGoalsBottomSheet());
        }

        // MỞ BOTTOM SHEET QUY ĐỔI TRÌNH ĐỘ (NEW)
        if (btnShowLevelSheet != null) {
            btnShowLevelSheet.setOnClickListener(v -> showLevelConversionBottomSheet());
        }

        // Xử lý đăng xuất
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                mAuth.signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                
                GoogleSignIn.getClient(requireActivity(), gso).signOut().addOnCompleteListener(task -> {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
            });
        }

        return view;
    }

    private void initViews(View view) {
        txtName = view.findViewById(R.id.txtProfileName);
        txtEmail = view.findViewById(R.id.txtProfileEmail);
        tvUserLevelTag = view.findViewById(R.id.tvUserLevelTag);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAdjustGoals = view.findViewById(R.id.btnAdjustGoals);
        btnShowLevelSheet = view.findViewById(R.id.btnShowLevelSheet);
        btnGoVip = view.findViewById(R.id.btnGoVip);
    }

    private void setupGlobalStateObserver() {
        UserStateManager.getInstance().getCurrentLevel().observe(getViewLifecycleOwner(), level -> {
            if (tvUserLevelTag != null) {
                tvUserLevelTag.setText(level);
            }
        });
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (txtName != null) txtName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Người dùng");
            if (txtEmail != null) txtEmail.setText(user.getEmail());
            UserStateManager.getInstance().initLevel();
        }
    }

    private void showLevelConversionBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_level_conversion, null);
        dialog.setContentView(view);

        View rowAdvanced = view.findViewById(R.id.rowAdvanced);
        View rowIntermediate = view.findViewById(R.id.rowIntermediate);
        View rowBeginner = view.findViewById(R.id.rowBeginner);
        MaterialButton btnClose = view.findViewById(R.id.btnCloseSheet);

        // Highlight hàng dựa trên trình độ hiện tại
        String currentLevel = tvUserLevelTag.getText().toString();
        int highlightColor = 0xFFFFB800; // Secondary Yellow

        if (currentLevel.contains("Cao cấp")) {
            rowAdvanced.setBackgroundColor(highlightColor);
        } else if (currentLevel.contains("Trung bình")) {
            rowIntermediate.setBackgroundColor(highlightColor);
        } else if (currentLevel.contains("Mới bắt đầu")) {
            rowBeginner.setBackgroundColor(highlightColor);
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showAdjustGoalsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_adjust_goals, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        AutoCompleteTextView spinnerLevel = bottomSheetView.findViewById(R.id.spinnerLevel);
        Slider sliderVocab = bottomSheetView.findViewById(R.id.sliderVocab);
        Slider sliderTime = bottomSheetView.findViewById(R.id.sliderTime);
        MaterialButton btnSaveGoals = bottomSheetView.findViewById(R.id.btnSaveGoals);

        String[] levels = {"Mới bắt đầu", "Trung bình", "Cao cấp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, levels);
        spinnerLevel.setAdapter(adapter);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(doc -> {
                if (doc.exists()) {
                    if (doc.contains("englishLevel")) spinnerLevel.setText(doc.getString("englishLevel"), false);
                    if (doc.contains("dailyVocabGoal")) sliderVocab.setValue(doc.getLong("dailyVocabGoal").floatValue());
                    if (doc.contains("dailyTimeGoal")) sliderTime.setValue(doc.getLong("dailyTimeGoal").floatValue());
                }
            });
        }

        btnSaveGoals.setOnClickListener(v -> {
            String selectedLevel = spinnerLevel.getText().toString();
            UserStateManager.getInstance().updateLevel(selectedLevel);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}

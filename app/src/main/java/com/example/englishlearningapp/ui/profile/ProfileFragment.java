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
    private View btnLogout, btnEditProfile, btnChangePassword, btnGoVip, cvUserLevel, btnAdjustGoals;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews(view);
        loadUserInfo();

        // Mở Bottom Sheet Nâng cấp VIP (Thay vì chuyển Fragment như trước)
        if (btnGoVip != null) {
            btnGoVip.setOnClickListener(v -> showVipUpgradeBottomSheet());
        }

        // Mở Bottom Sheet điều chỉnh mục tiêu & trình độ
        View.OnClickListener adjustGoalsListener = v -> showAdjustGoalsBottomSheet();
        if (btnAdjustGoals != null) btnAdjustGoals.setOnClickListener(adjustGoalsListener);
        if (cvUserLevel != null) cvUserLevel.setOnClickListener(adjustGoalsListener);
        if (btnEditProfile != null) btnEditProfile.setOnClickListener(adjustGoalsListener);

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
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnGoVip = view.findViewById(R.id.btnGoVip);
        cvUserLevel = view.findViewById(R.id.cvUserLevel);
        btnAdjustGoals = view.findViewById(R.id.btnAdjustGoals);
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (txtName != null) txtName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Người dùng");
            if (txtEmail != null) txtEmail.setText(user.getEmail());

            db.collection("users").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String level = documentSnapshot.getString("englishLevel");
                    if (level != null && tvUserLevelTag != null) {
                        tvUserLevelTag.setText(level);
                    }
                }
            });
        }
    }

    private void showAdjustGoalsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_adjust_goals, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        AutoCompleteTextView spinnerLevel = bottomSheetView.findViewById(R.id.spinnerLevel);
        Slider sliderVocab = bottomSheetView.findViewById(R.id.sliderVocab);
        Slider sliderTime = bottomSheetView.findViewById(R.id.sliderTime);
        MaterialButton btnSaveGoals = bottomSheetView.findViewById(R.id.btnSaveGoals);

        // Setup Spinner Level
        String[] levels = {"Mới bắt đầu", "Trung bình", "Nâng cao"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, levels);
        spinnerLevel.setAdapter(adapter);

        // Load current data to Bottom Sheet
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
            int vocabGoal = (int) sliderVocab.getValue();
            int timeGoal = (int) sliderTime.getValue();

            if (user != null) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("englishLevel", selectedLevel);
                updates.put("dailyVocabGoal", vocabGoal);
                updates.put("dailyTimeGoal", timeGoal);

                db.collection("users").document(user.getUid()).update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            tvUserLevelTag.setText(selectedLevel);
                            bottomSheetDialog.dismiss();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        bottomSheetDialog.show();
    }

    private void showVipUpgradeBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_vip_upgrade, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        MaterialButton btnUnlockVip = bottomSheetView.findViewById(R.id.btnUnlockVip);
        btnUnlockVip.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new VipFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        bottomSheetDialog.show();
    }
}

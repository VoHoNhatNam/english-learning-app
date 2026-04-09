package com.example.englishlearningapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView txtName, txtEmail;
    private LinearLayout btnLogout, btnEditProfile, btnChangePassword, btnGoVip;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        initViews(view);
        loadUserInfo();

        // Chuyển sang màn hình VIP
        btnGoVip.setOnClickListener(v -> {
            Fragment vipFragment = new VipFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, vipFragment);
            transaction.addToBackStack(null); // Để khi nhấn Back thì quay lại trang Profile
            transaction.commit();
        });

        // Xử lý đăng xuất (Đã cập nhật để đăng xuất cả Google)
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            
            // Đăng xuất Google để lần sau có thể chọn tài khoản khác
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

        return view;
    }

    private void initViews(View view) {
        txtName = view.findViewById(R.id.txtProfileName);
        txtEmail = view.findViewById(R.id.txtProfileEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnGoVip = view.findViewById(R.id.btnGoVip); // Ánh xạ nút VIP mới
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            txtName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Người dùng");
            txtEmail.setText(user.getEmail());
        }
    }
}
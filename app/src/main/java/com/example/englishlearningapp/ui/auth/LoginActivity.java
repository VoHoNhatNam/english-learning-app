package com.example.englishlearningapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishlearningapp.MainActivity;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.viewmodel.AuthViewModel;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_DEBUG";
    private static final int RC_SIGN_IN = 100;

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private LinearLayout btnGoogleLogin, loadingOverlay;
    private TextView tvRegister, tvForgot;

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Kiểm tra nếu đã login thì vào luôn Main
        if (authViewModel.getCurrentUser() != null) {
            navigateToMain();
        }

        // Cấu hình Google Sign-In - Kiểm tra ID Token để tránh Crash
        try {
            String webClientId = getString(R.string.default_web_client_id);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(webClientId)
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi cấu hình Google Sign-In: " + e.getMessage());
        }

        // Sự kiện Click
        btnLogin.setOnClickListener(v -> loginWithEmail());
        btnGoogleLogin.setOnClickListener(v -> signInGoogle());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        if (tvForgot != null) {
            tvForgot.setOnClickListener(v ->
                    Toast.makeText(this, "Tính năng lấy lại mật khẩu đang được cập nhật", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgot = findViewById(R.id.tvForgot);
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void loginWithEmail() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng không để trống Email/Mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        // Gọi hàm login từ ViewModel (đã sửa lỗi callback ở đây)
        authViewModel.login(email, password, task -> {
            showLoading(false);
            if (task.isSuccessful()) {
                Log.d(TAG, "Đăng nhập Email thành công");
                navigateToMain();
            } else {
                String errorMsg = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                Log.e(TAG, "Đăng nhập thất bại: " + errorMsg);
                Toast.makeText(this, "Đăng nhập thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signInGoogle() {
        if (googleSignInClient == null) {
            Toast.makeText(this, "Dịch vụ Google chưa sẵn sàng", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading(true);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                showLoading(false);
                Log.e(TAG, "Google Sign-In lỗi (Code: " + e.getStatusCode() + ")");
                // Lỗi 10: Thường do SHA-1 chưa đăng ký trên Firebase
                // Lỗi 12500: Lỗi file google-services.json hoặc Play Services
                Toast.makeText(this, "Lỗi Google (Code: " + e.getStatusCode() + ")", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        authViewModel.loginWithGoogle(credential, task -> {
            showLoading(false);
            if (task.isSuccessful()) {
                navigateToMain();
            } else {
                Log.e(TAG, "Firebase Google Auth Failed", task.getException());
                Toast.makeText(this, "Lỗi xác thực Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isShow) {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
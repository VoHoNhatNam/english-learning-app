package com.example.englishlearningapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.viewmodel.AuthViewModel;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private LinearLayout btnGoogleLogin;
    private TextView tvRegister;
    private View loadingOverlay;

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initViewModel();
        initGoogleSignIn();
        initListeners();
        checkAutoLogin();
    }

    // =========================
    // 🔹 INIT
    // =========================
    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        tvRegister = findViewById(R.id.tvRegister);
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void initGoogleSignIn() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleGoogleResult(result.getResultCode(), result.getData())
        );
    }

    private void initListeners() {
        btnLogin.setOnClickListener(v -> handleEmailLogin());
        btnGoogleLogin.setOnClickListener(v -> signInGoogle());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void checkAutoLogin() {
        if (authViewModel.getCurrentUser() != null) {
            goToHome();
        }
    }

    // =========================
    // 🔐 EMAIL LOGIN
    // =========================
    private void handleEmailLogin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInput(email, password)) return;

        showLoading(true);

        authViewModel.login(email, password, task -> {
            showLoading(false);

            if (task.isSuccessful()) {
                goToHome();
            } else {
                showError(task.getException());
            }
        });
    }

    private boolean validateInput(String email, String password) {

        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Mật khẩu không được để trống");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Ít nhất 6 ký tự");
            return false;
        }

        return true;
    }

    // =========================
    // 🔐 GOOGLE LOGIN
    // =========================
    private void signInGoogle() {
        showLoading(true);
        signInLauncher.launch(googleSignInClient.getSignInIntent());
    }

    private void handleGoogleResult(int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            showLoading(false);
            return;
        }

        Task<GoogleSignInAccount> task =
                GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null && account.getIdToken() != null) {
                firebaseAuthWithGoogle(account.getIdToken());
            } else {
                showToast("Không lấy được thông tin Google");
                showLoading(false);
            }

        } catch (ApiException e) {
            showToast("Google Sign-In thất bại");
            showLoading(false);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        authViewModel.loginWithGoogle(credential, task -> {
            showLoading(false);

            if (task.isSuccessful()) {
                goToHome();
            } else {
                showToast("Đăng nhập Google thất bại");
            }
        });
    }

    // =========================
    // 🔧 UTILS
    // =========================
    private void goToHome() {
        startActivity(new Intent(this,
                com.example.englishlearningapp.MainActivity.class));
        finish();
    }

    private void showLoading(boolean isLoading) {
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        btnLogin.setEnabled(!isLoading);
        btnGoogleLogin.setEnabled(!isLoading);
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
    }

    private void showError(Exception e) {
        if (e != null && e.getMessage() != null) {
            showToast(e.getMessage());
        } else {
            showToast("Đã xảy ra lỗi");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
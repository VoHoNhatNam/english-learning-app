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

    // 🔥 dùng overlay thay vì progressBar
    private View loadingOverlay;

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔹 Bind view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        tvRegister = findViewById(R.id.tvRegister);
        loadingOverlay = findViewById(R.id.loadingOverlay);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // 🔹 Auto login
        if (authViewModel.getCurrentUser() != null) {
            goToHome();
            return;
        }

        // 🔹 Google config
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // 🔹 Google launcher
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        try {
                            GoogleSignInAccount account =
                                    task.getResult(ApiException.class);

                            firebaseAuthWithGoogle(account.getIdToken());

                        } catch (ApiException e) {
                            showToast("Google Sign-In Failed");
                            showLoading(false);
                        }
                    } else {
                        showLoading(false);
                    }
                }
        );

        // 🔹 Click events
        btnLogin.setOnClickListener(v -> handleEmailLogin());
        btnGoogleLogin.setOnClickListener(v -> signInGoogle());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    // =========================
    // 🔐 Email Login
    // =========================
    private void handleEmailLogin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInput(email, password)) return;

        showLoading(true);

        authViewModel.login(email, password, task -> {

            showLoading(false);

            if (task.isSuccessful()) {
                showToast("Login Success");
                goToHome();
            } else {
                showToast("Login Failed: " + task.getException().getMessage());
            }

        });
    }

    private boolean validateInput(String email, String password) {

        if (email.isEmpty()) {
            etEmail.setError("Email required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password required");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Min 6 characters");
            return false;
        }

        return true;
    }

    // =========================
    // 🔐 Google Login
    // =========================
    private void signInGoogle() {
        showLoading(true);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        authViewModel.loginWithGoogle(credential, task -> {

            showLoading(false);

            if (task.isSuccessful()) {
                showToast("Login Success");
                goToHome();
            } else {
                showToast("Google Login Failed");
            }

        });
    }

    // =========================
    // 🔧 Utils
    // =========================
    private void goToHome() {
        startActivity(new Intent(this,
                com.example.englishlearningapp.ui.home.HomeActivity.class));
        finish();
    }

    private void showLoading(boolean isLoading) {
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        // disable click khi loading
        btnLogin.setEnabled(!isLoading);
        btnGoogleLogin.setEnabled(!isLoading);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
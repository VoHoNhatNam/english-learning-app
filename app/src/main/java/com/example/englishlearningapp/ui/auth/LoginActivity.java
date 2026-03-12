package com.example.englishlearningapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.viewmodel.AuthViewModel;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout btnGoogleLogin;
    private TextView tvRegister;

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        tvRegister = findViewById(R.id.tvRegister);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Nếu user đã login rồi → vào Home luôn
        if (authViewModel.getCurrentUser() != null) {

            startActivity(new Intent(
                    LoginActivity.this,
                    com.example.englishlearningapp.ui.home.HomeActivity.class
            ));

            finish();
        }

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleLogin.setOnClickListener(v -> signInGoogle());

        tvRegister.setOnClickListener(v -> {

            startActivity(new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            ));

        });
    }

    private void signInGoogle() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account =
                        task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {

                Toast.makeText(this,
                        "Google Sign-In Failed",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        authViewModel.loginWithGoogle(credential, task -> {

            if (task.isSuccessful()) {

                Toast.makeText(this,
                        "Login Success",
                        Toast.LENGTH_SHORT).show();

                // Chuyển sang HomeActivity
                Intent intent = new Intent(
                        LoginActivity.this,
                        com.example.englishlearningapp.ui.home.HomeActivity.class
                );

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(this,
                        "Login Failed: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}
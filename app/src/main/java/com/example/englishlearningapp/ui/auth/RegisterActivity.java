package com.example.englishlearningapp.ui.auth;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword;
    private Button btnRegister;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Clear errors
        etEmail.setError(null);
        etUsername.setError(null);
        etPassword.setError(null);

        // Validate fields
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        btnRegister.setEnabled(false);


        authViewModel.register(email, password, task -> {

            btnRegister.setEnabled(true);

            if (task.isSuccessful()) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this,
                            "User authentication failed",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getUid();

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> user = new HashMap<>();
                user.put("uid", uid);
                user.put("email", email);
                user.put("username", username);
                user.put("createdAt", System.currentTimeMillis());

                db.collection("users")
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener(aVoid -> {

                            Toast.makeText(this,
                                    "Register Success",
                                    Toast.LENGTH_SHORT).show();

                            finish();
                        })
                        .addOnFailureListener(e -> {

                            Toast.makeText(this,
                                    "Save user failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });

            } else {

                Toast.makeText(this,
                        "Register Failed: " +
                                task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}
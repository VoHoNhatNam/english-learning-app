package com.example.englishlearningapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.englishlearningapp.data.repository.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private AuthRepository repository;

    public AuthViewModel() {
        repository = new AuthRepository();
    }

    // 🔹 Get current user
    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }

    // 🔹 Login Email + Password (THIẾU → thêm)
    public void login(String email, String password,
                      OnCompleteListener<AuthResult> listener) {

        repository.login(email, password, listener);
    }

    // 🔹 Login Google
    public void loginWithGoogle(AuthCredential credential,
                                OnCompleteListener<AuthResult> listener) {

        repository.loginWithCredential(credential, listener);
    }

    // 🔹 Register
    public void register(String email, String password,
                         OnCompleteListener<AuthResult> listener) {

        repository.register(email, password, listener);
    }

    // 🔹 Logout
    public void logout() {
        repository.logout();
    }
}
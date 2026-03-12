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

    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }

    // Login Google
    public void loginWithGoogle(AuthCredential credential,
                                OnCompleteListener<AuthResult> listener) {

        repository.loginWithCredential(credential, listener);
    }

    // Register Email + Password
    public void register(String email, String password,
                         OnCompleteListener<AuthResult> listener) {

        repository.register(email, password, listener);
    }
}
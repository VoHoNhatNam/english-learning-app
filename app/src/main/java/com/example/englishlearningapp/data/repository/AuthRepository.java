package com.example.englishlearningapp.data.repository;

import com.example.englishlearningapp.data.firebase.FirebaseAuthManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    private FirebaseAuthManager authManager;

    public AuthRepository() {
        authManager = new FirebaseAuthManager();
    }

    public FirebaseUser getCurrentUser() {
        return authManager.getCurrentUser();
    }

    // Login bằng Google
    public void loginWithCredential(AuthCredential credential,
                                    OnCompleteListener<AuthResult> listener) {

        authManager.getAuth()
                .signInWithCredential(credential)
                .addOnCompleteListener(listener);
    }

    // Register bằng Email + Password
    public void register(String email, String password,
                         OnCompleteListener<AuthResult> listener) {

        authManager.getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

}
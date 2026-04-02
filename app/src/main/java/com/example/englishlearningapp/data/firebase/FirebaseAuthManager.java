package com.example.englishlearningapp.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {

    private FirebaseAuth firebaseAuth;

    public FirebaseAuthManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void logout() {
        firebaseAuth.signOut();
    }
}
package com.example.englishlearningapp.data.repository;

import com.example.englishlearningapp.data.firebase.FirebaseAuthManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {

    private FirebaseAuthManager authManager;
    private FirebaseFirestore db;

    public interface OnAdminCheckListener {
        void onResult(boolean isAdmin);
        void onError(Exception e);
    }

    public AuthRepository() {
        authManager = new FirebaseAuthManager();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return authManager.getCurrentUser();
    }

    /**
     * Kiểm tra xem người dùng hiện tại có phải là Admin không
     */
    public void checkIsAdmin(String uid, OnAdminCheckListener listener) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        listener.onResult("admin".equals(role));
                    } else {
                        listener.onResult(false);
                    }
                })
                .addOnFailureListener(listener::onError);
    }

    // 🔐 Login Email + Password
    public void login(String email, String password,
                      OnCompleteListener<AuthResult> listener) {

        authManager.getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    // 🔐 Login Google
    public void loginWithCredential(AuthCredential credential,
                                    OnCompleteListener<AuthResult> listener) {

        authManager.getAuth()
                .signInWithCredential(credential)
                .addOnCompleteListener(listener);
    }

    // 📝 Register
    public void register(String email, String password,
                         OnCompleteListener<AuthResult> listener) {

        authManager.getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    // 🚪 Logout
    public void logout() {
        authManager.getAuth().signOut();
    }
}
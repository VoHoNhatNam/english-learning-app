package com.example.englishlearningapp.ui.auth;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin, tvPasswordRequirements;
    private ImageView imgTogglePassword, imgToggleConfirm;
    private View loadingOverlay, layoutPasswordStrength;

    // Password strength UI
    private ProgressBar progressPassword;
    private TextView tvStrength, tvRuleLength, tvRuleUpper, tvRuleNumber, tvRuleSpecial;

    private AuthViewModel authViewModel;
    private View currentTopMessage;

    private int currentPasswordScore = 0;
    private boolean isPasswordVisible = false;
    private boolean isConfirmVisible = false;

    // Lưu lại thông tin để truyền ngược về trang đăng nhập
    private String registeredEmail = "";
    private String registeredPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();
        setupPasswordWatcher();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        tvPasswordRequirements = findViewById(R.id.tvPasswordRequirements);
        imgTogglePassword = findViewById(R.id.imgTogglePassword);
        imgToggleConfirm = findViewById(R.id.imgToggleConfirm);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        layoutPasswordStrength = findViewById(R.id.layoutPasswordStrength);

        progressPassword = findViewById(R.id.progressPassword);
        tvStrength = findViewById(R.id.tvStrength);
        tvRuleLength = findViewById(R.id.tvRuleLength);
        tvRuleUpper = findViewById(R.id.tvRuleUpper);
        tvRuleNumber = findViewById(R.id.tvRuleNumber);
        tvRuleSpecial = findViewById(R.id.tvRuleSpecial);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> {
            registerUser();
        });

        tvLogin.setOnClickListener(v -> finish());

        imgTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        imgToggleConfirm.setOnClickListener(v -> toggleConfirmVisibility());

        clearErrorOnFocus();
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgTogglePassword.setImageResource(R.drawable.ic_visibility_off);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgTogglePassword.setImageResource(R.drawable.ic_visibility);
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void toggleConfirmVisibility() {
        isConfirmVisible = !isConfirmVisible;
        if (isConfirmVisible) {
            etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgToggleConfirm.setImageResource(R.drawable.ic_visibility_off);
        } else {
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgToggleConfirm.setImageResource(R.drawable.ic_visibility);
        }
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
    }

    private void setupPasswordWatcher() {
        etPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (password.isEmpty()) {
                    layoutPasswordStrength.setVisibility(View.GONE);
                    tvPasswordRequirements.setVisibility(View.VISIBLE);
                } else {
                    layoutPasswordStrength.setVisibility(View.VISIBLE);
                    tvPasswordRequirements.setVisibility(View.GONE);
                    updatePasswordStrength(password);
                }
            }
        });
    }

    private void updatePasswordStrength(String password) {
        int score = 0;
        boolean hasLength = password.length() >= 8;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[@#$%^&+=!].*");

        if (hasLength) score += 25;
        if (hasUpper) score += 25;
        if (hasNumber) score += 25;
        if (hasSpecial) score += 25;

        currentPasswordScore = score;
        progressPassword.setProgress(score);

        if (score <= 25) {
            tvStrength.setText(R.string.password_weak);
            tvStrength.setTextColor(0xFFF44336);
            progressPassword.setProgressTintList(ColorStateList.valueOf(0xFFF44336));
        } else if (score <= 50) {
            tvStrength.setText(R.string.password_medium);
            tvStrength.setTextColor(0xFFFF9800);
            progressPassword.setProgressTintList(ColorStateList.valueOf(0xFFFF9800));
        } else if (score <= 75) {
            tvStrength.setText(R.string.password_strong);
            tvStrength.setTextColor(0xFF03A9F4);
            progressPassword.setProgressTintList(ColorStateList.valueOf(0xFF03A9F4));
        } else {
            tvStrength.setText(R.string.password_very_strong);
            tvStrength.setTextColor(0xFF4CAF50);
            progressPassword.setProgressTintList(ColorStateList.valueOf(0xFF4CAF50));
        }

        setRule(tvRuleLength, hasLength);
        setRule(tvRuleUpper, hasUpper);
        setRule(tvRuleNumber, hasNumber);
        setRule(tvRuleSpecial, hasSpecial);

        btnRegister.setEnabled(true);
        btnRegister.setAlpha(score >= 75 ? 1f : 0.6f);
    }

    private void setRule(TextView tv, boolean ok) {
        tv.setTextColor(ok ? 0xFF4CAF50 : 0xFF9E9E9E);
    }

    private void registerUser() {
        hideKeyboard();
        clearFocusAll();

        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInput(email, username, password, confirmPassword)) return;

        registeredEmail = email;
        registeredPassword = password;

        showLoading(true);

        authViewModel.register(email, password, task -> {
            if (task.isSuccessful()) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                saveUserToFirestore(uid, email, username);
            } else {
                showLoading(false);
                String error = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                showTopMessage(mapFirebaseError(error), false);
            }
        });
    }

    private boolean validateInput(String email, String username, String password, String confirmPassword) {
        if (email.isEmpty()) {
            showTopMessage("Vui lòng nhập Email", false);
            etEmail.setError("Không được để trống");
            etEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showTopMessage("Email không hợp lệ", false);
            etEmail.setError("Sai định dạng");
            etEmail.requestFocus();
            return false;
        }

        if (username.isEmpty()) {
            showTopMessage("Vui lòng nhập Username", false);
            etUsername.setError("Không được để trống");
            etUsername.requestFocus();
            return false;
        }
        if (username.length() < 3) {
            showTopMessage("Username quá ngắn", false);
            etUsername.setError("Tối thiểu 3 ký tự");
            etUsername.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            showTopMessage("Vui lòng nhập mật khẩu", false);
            etPassword.requestFocus();
            return false;
        }
        if (currentPasswordScore < 75) {
            showTopMessage("Mật khẩu chưa đủ độ mạnh", false);
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            showTopMessage("Vui lòng xác nhận mật khẩu", false);
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showTopMessage("Mật khẩu xác nhận không khớp", false);
            etConfirmPassword.setError("Không khớp");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void saveUserToFirestore(String uid, String email, String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("email", email);
        user.put("username", username);
        user.put("isVip", false);
        user.put("onboardingCompleted", false);
        user.put("createdAt", System.currentTimeMillis());

        db.collection("users")
                .document(uid)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    showSuccessScreen();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    showTopMessage("Lỗi lưu dữ liệu: " + e.getMessage(), false);
                });
    }

    private void showSuccessScreen() {
        FrameLayout root = findViewById(android.R.id.content);
        View successView = getLayoutInflater().inflate(R.layout.layout_success, root, false);
        root.addView(successView);
        successView.setAlpha(0f);
        successView.animate().alpha(1f).setDuration(250).start();
        successView.postDelayed(() -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("email", registeredEmail);
            resultIntent.putExtra("password", registeredPassword);
            setResult(RESULT_OK, resultIntent);
            finish();
        }, 1800);
    }

    private void showTopMessage(String message, boolean isSuccess) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        FrameLayout root = findViewById(android.R.id.content);
        if (currentTopMessage != null) {
            root.removeView(currentTopMessage);
        }

        View view = getLayoutInflater().inflate(R.layout.layout_top_message, root, false);
        TextView tv = view.findViewById(R.id.tvMessage);
        ImageView img = view.findViewById(R.id.imgIcon);

        tv.setText(message);
        view.setBackgroundResource(R.drawable.bg_top_message);

        if (isSuccess) {
            view.setBackgroundTintList(ColorStateList.valueOf(0xFF4CAF50));
            img.setImageResource(R.drawable.ic_check);
        } else {
            view.setBackgroundTintList(ColorStateList.valueOf(0xFFF44336));
            img.setImageResource(R.drawable.ic_error);
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = getStatusBarHeight() + 20;
        view.setLayoutParams(params);
        view.setElevation(1000f);

        root.addView(view);
        view.bringToFront();
        currentTopMessage = view;

        view.setTranslationY(-500f);
        view.setAlpha(0f);
        view.animate().translationY(0).alpha(1).setDuration(300).start();

        view.postDelayed(() -> {
            if (currentTopMessage == view) {
                view.animate().translationY(-500f).alpha(0).setDuration(300)
                        .withEndAction(() -> {
                            root.removeView(view);
                            if (currentTopMessage == view) currentTopMessage = null;
                        }).start();
            }
        }, 3500);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clearFocusAll() {
        etEmail.clearFocus();
        etUsername.clearFocus();
        etPassword.clearFocus();
        etConfirmPassword.clearFocus();
    }

    private int getStatusBarHeight() {
        int id = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return id > 0 ? getResources().getDimensionPixelSize(id) : 0;
    }

    private String mapFirebaseError(String error) {
        if (error == null) return "Có lỗi xảy ra";
        String lowercaseError = error.toLowerCase();
        if (lowercaseError.contains("already") || lowercaseError.contains("collision")) return "Email này đã được sử dụng";
        if (lowercaseError.contains("badly") || lowercaseError.contains("format")) return "Định dạng Email không hợp lệ";
        if (lowercaseError.contains("network")) return "Lỗi kết nối mạng, vui lòng kiểm tra internet";
        if (lowercaseError.contains("weak-password")) return "Mật khẩu quá yếu (tối thiểu 6 ký tự)";
        return "Lỗi đăng ký: " + error;
    }

    private void clearErrorOnFocus() {
        etEmail.setOnFocusChangeListener((v, f) -> {
            if (f) {
                hideTopMessage();
                etEmail.setError(null);
            }
        });
        etUsername.setOnFocusChangeListener((v, f) -> {
            if (f) {
                hideTopMessage();
                etUsername.setError(null);
            }
        });
        etPassword.setOnFocusChangeListener((v, f) -> { if (f) hideTopMessage(); });
        etConfirmPassword.setOnFocusChangeListener((v, f) -> {
            if (f) {
                hideTopMessage();
                etConfirmPassword.setError(null);
            }
        });
    }

    private void hideTopMessage() {
        if (currentTopMessage != null) {
            FrameLayout root = findViewById(android.R.id.content);
            View view = currentTopMessage;
            view.animate().translationY(-500f).alpha(0).setDuration(250)
                    .withEndAction(() -> {
                        root.removeView(view);
                        if (currentTopMessage == view) currentTopMessage = null;
                    }).start();
        }
    }

    private void showLoading(boolean isLoading) {
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
    }
}

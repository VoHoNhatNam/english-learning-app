package com.example.englishlearningapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    ImageView btnShowPass;
    Button btnLogin, btnRegister;
    TextView txtForgot, txtRegisterNow;

    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ View
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnShowPass = findViewById(R.id.btnShowPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtForgot = findViewById(R.id.txtForgot);
        txtRegisterNow = findViewById(R.id.txtRegisterNow);

        // Hiện / ẩn mật khẩu
        btnShowPass.setOnClickListener(v -> {

            if (isPasswordVisible) {

                edtPassword.setTransformationMethod(
                        PasswordTransformationMethod.getInstance());
                btnShowPass.setImageResource(R.drawable.ic_eye);

                isPasswordVisible = false;

            } else {

                edtPassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance());
                btnShowPass.setImageResource(R.drawable.ic_eye_off);

                isPasswordVisible = true;
            }

            edtPassword.setSelection(edtPassword.getText().length());
        });

        // LOGIN
        btnLogin.setOnClickListener(v -> {

            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(LoginActivity.this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT).show();

            } else {

                // Demo login
                if (username.equals("admin") && password.equals("123456")) {

                    Toast.makeText(LoginActivity.this,
                            "Đăng nhập thành công",
                            Toast.LENGTH_SHORT).show();

                    // Lưu trạng thái login
                    SharedPreferences prefs =
                            getSharedPreferences("UserData", MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putBoolean("isLogin", true);
                    editor.putString("username", username);

                    editor.apply();

                    Intent intent =
                            new Intent(LoginActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(LoginActivity.this,
                            "Sai tài khoản hoặc mật khẩu",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // REGISTER
        btnRegister.setOnClickListener(v -> {

            Intent intent =
                    new Intent(LoginActivity.this, RegisterActivity.class);

            startActivity(intent);
        });

        // REGISTER TEXT
        txtRegisterNow.setOnClickListener(v -> {

            Intent intent =
                    new Intent(LoginActivity.this, RegisterActivity.class);

            startActivity(intent);
        });

        // FORGOT PASSWORD
        txtForgot.setOnClickListener(v -> {

            Toast.makeText(LoginActivity.this,
                    "Chức năng đang phát triển",
                    Toast.LENGTH_SHORT).show();
        });
    }
}
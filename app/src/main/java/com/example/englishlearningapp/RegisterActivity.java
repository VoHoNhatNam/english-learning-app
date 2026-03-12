package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUser, edtEmail, edtPhone, edtPass, edtPass2;
    Button btnRegister;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ View
        edtUser = findViewById(R.id.edtUser);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPass = findViewById(R.id.edtPass);
        edtPass2 = findViewById(R.id.edtPass2);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        // Sự kiện đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = edtUser.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String pass2 = edtPass2.getText().toString().trim();

                // Kiểm tra rỗng
                if (TextUtils.isEmpty(user) ||
                        TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(phone) ||
                        TextUtils.isEmpty(pass) ||
                        TextUtils.isEmpty(pass2)) {

                    Toast.makeText(RegisterActivity.this,
                            "Vui lòng nhập đầy đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra mật khẩu
                if (!pass.equals(pass2)) {
                    Toast.makeText(RegisterActivity.this,
                            "Mật khẩu xác nhận không đúng",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thành công
                Toast.makeText(RegisterActivity.this,
                        "Đăng ký thành công",
                        Toast.LENGTH_SHORT).show();

                // Chuyển sang Login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Chuyển sang Login
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
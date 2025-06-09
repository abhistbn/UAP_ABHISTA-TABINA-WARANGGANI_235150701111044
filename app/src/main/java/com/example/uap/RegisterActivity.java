package com.example.uap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etPassword2;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            if (validateForm()) {
                signUp(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
            }
        });
    }

    private boolean validateForm() {
        String email = etEmail.getText().toString().trim();
        String pass1 = etPassword.getText().toString().trim();
        String pass2 = etPassword2.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            return false;
        }
        if (TextUtils.isEmpty(pass1) || pass1.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            return false;
        }
        if (!pass1.equals(pass2)) {
            etPassword2.setError("Password tidak cocok");
            return false;
        }
        return true;
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil. Silakan login.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

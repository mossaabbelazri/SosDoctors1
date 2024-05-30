package com.example.sosdoctors;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Declaration
    Button btnReset, btnBack;
    EditText edtEmail;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initialization
        btnBack = findViewById(R.id.btnForgotPasswordBack);
        btnReset = findViewById(R.id.btnReset);
        edtEmail = findViewById(R.id.edtForgotPasswordEmail);
        progressBar = findViewById(R.id.forgetPasswordProgressbar);

        mAuth = FirebaseAuth.getInstance();

        //Reset Button Listener
        btnReset.setOnClickListener(v -> {
            strEmail = edtEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(strEmail)) {
                ResetPassword();
            } else {
                edtEmail.setError("Email field can't be empty");
            }
        });

        // Handling back button press using OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom back press handling
                finish(); // Example: finishing the activity
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Back Button Code
        btnBack.setOnClickListener(v -> callback.handleOnBackPressed());
    }

    private void ResetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Reset Password link has been sent to your registered Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                });
    }
}

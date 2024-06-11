package com.example.pocketnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAccount extends AppCompatActivity {

    EditText etEmail,etPassword;
    Button btnLogin;
    ProgressBar pbProgressBar;
    TextView tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnLogin.setOnClickListener((v)-> loginUser());
        tvSignUp.setOnClickListener((v) -> startActivity(new Intent(this,CreateAccount.class)));

    }

    private void loginUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();


        boolean isValidated = validateData(email,password);

        if(!isValidated)
            return;
        loginAccountInFirebase(email,password);

    }

    private void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        // login plus email also verified then login success
                        startActivity(new Intent(LoginAccount.this,Home.class));
                        finish();
                    } else{
                        Utility.Toast(LoginAccount.this, "Please verify your email");
                        firebaseAuth.getCurrentUser().sendEmailVerification();

                    }
                }else{
                    // login failed
                    Utility.Toast(LoginAccount.this, task.getException().getLocalizedMessage());
                }

            }
        });


    }

    private void changeInProgress(boolean inProgress){
        if(inProgress){
            btnLogin.setVisibility(View.GONE);
            pbProgressBar.setVisibility(View.VISIBLE);

        }else{
            btnLogin.setVisibility(View.VISIBLE);
            pbProgressBar.setVisibility(View.GONE);

        }

    }

    boolean validateData(String email,String password){
        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;

    }

    private void init(){
        etEmail = findViewById(R.id.tvEmailLogin);
        etPassword = findViewById(R.id.tvPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        pbProgressBar = findViewById(R.id.progress_barLoginAccount);
        tvSignUp = findViewById(R.id.tvSignUp);

    }
}
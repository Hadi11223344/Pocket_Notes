package com.example.pocketnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreateAccount extends AppCompatActivity {


    EditText etEmail,etPassword,etConfirmPassword;
    Button btnCreateAccount;
    ProgressBar pbProgressBar;
    TextView tvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        btnCreateAccount.setOnClickListener(v -> createAccount());
        tvLogin.setOnClickListener(v -> startActivity(new Intent(CreateAccount.this, LoginAccount.class)));

    }

    private void createAccount(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);

        if(!isValidated)
            return;
         createAccountInFirebase(email,password);

    }

    private void createAccountInFirebase(String email,String password) {
        changeInProgress(true);
        //create account in firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            // account created successfully
                            Utility.Toast(CreateAccount.this,"Successfully created account");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{

                            // account creation failed
                            Utility.Toast(CreateAccount.this,task.getException().getLocalizedMessage());
                        }

                    }
                });
    }

    private void changeInProgress(boolean inProgress){
        if(inProgress){
            btnCreateAccount.setVisibility(View.GONE);
            pbProgressBar.setVisibility(View.VISIBLE);

        }else{
            btnCreateAccount.setVisibility(View.VISIBLE);
            pbProgressBar.setVisibility(View.GONE);

        }

    }

    boolean validateData(String email,String password,String confirmPassword){
        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        if(!password.equals(confirmPassword)){
            etConfirmPassword.setError("Password does not match");
            return false;
        }

        return true;

    }



    private void init(){
        etEmail = findViewById(R.id.email_edit_text);
        etPassword = findViewById(R.id.password_edit_text);
        etConfirmPassword = findViewById(R.id.confirm_password_edit_text);
        btnCreateAccount = findViewById(R.id.create_account_btn);
        pbProgressBar = findViewById(R.id.progress_bar);
        tvLogin = findViewById(R.id.login_text_view_btn);
    }

    }



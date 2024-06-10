package com.example.pocketnotes;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccount extends AppCompatActivity {


    EditText etemail,etpassword,etconfirmpassword;
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
        tvLogin.setOnClickListener(v -> finish());
    }

    private void createAccount(){
        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString();
        String confirmPassword = etconfirmpassword.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
    }
    boolean validateData(String email,String password,String confirmPassword){
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etemail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            etpassword.setError("Password must be at least 6 characters");
            return false;
        }
        if(!password.equals(confirmPassword)){
            etconfirmpassword.setError("Password does not match");
            return false;
        }

        return true;

    }



    private void init(){
        etemail = findViewById(R.id.email_edit_text);
        etpassword = findViewById(R.id.password_edit_text);
        etconfirmpassword = findViewById(R.id.confirm_password_edit_text);
        btnCreateAccount = findViewById(R.id.create_account_btn);
        pbProgressBar = findViewById(R.id.progress_bar);
        tvLogin = findViewById(R.id.login_text_view_btn);
    }

    }



package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextView login;
    EditText mFullName,mEmail,mPassword,mPhone,mConfirm;
    Button mRegisterBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.inputEmail);
        mPassword = findViewById(R.id.inputPassword);
        mConfirm = findViewById(R.id.inputConfirmPassword);
        mPhone = findViewById(R.id.phoneNumber);
        mRegisterBtn = findViewById(R.id.btnRegister);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirm = mConfirm.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email field is empty.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password field is empty.");
                    return;
                }

                if(!password.equals(confirm)){
                    mConfirm.setError("Passwords did not match.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password length must be greater or equal to 6.");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Your account has been created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        login = (TextView) findViewById(R.id.alreadyHave);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

    }
    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
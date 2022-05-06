package com.example.myproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.myproject.User.md5;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RegisterActivity extends AppCompatActivity implements Serializable {

    TextView login;
    EditText fullName;
    EditText email;
    EditText phoneNumber;
    EditText password;
    EditText confirmPassword;
    Button register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (TextView) findViewById(R.id.alreadyHave);

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.inputEmail);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.inputPassword);
        confirmPassword = findViewById(R.id.inputConfirmPassword);
        register = findViewById(R.id.btnRegister);

        confBtn();
        if (getIntent().getExtras() != null) {
            email.setText(getIntent().getExtras().getString("emailAddress", ""));
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });


    }

    public void mailSend(){
        String mailName = fullName.getText().toString().trim();
        String mailMail = email.getText().toString().trim();
        String mailPhone = phoneNumber.getText().toString().trim();
        String mailPassword = password.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailMail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your registration information.");
        intent.putExtra(Intent.EXTRA_TEXT,"Full Name: " + mailName + " Phone Number: " + mailPhone + " Password: " + mailPassword);
        intent.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(intent, "Choose an e-mail provider."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RegisterActivity.this, "Error occur to sending mail..", Toast.LENGTH_SHORT).show();
        }
    }

    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void confBtn() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterAttempt(v);
            }
        });

        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }



    private void onRegisterAttempt(View view) {
        if (!userCheck()) {
            if (validation()) {


                User user = new User(fullName.getText().toString(), email.getText().toString(), phoneNumber.getText().toString(), md5(password.getText().toString()));
                mailSend();
                if(addUser(user)){
                    Toast.makeText(RegisterActivity.this, "Your account has been created.", Toast.LENGTH_SHORT).show();
                    LoginActivity.users.add(user);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean addUser(User user){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(getApplicationContext().getFilesDir() + "/users", true);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private boolean validation() {
        if (fullName.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Name field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Email field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phoneNumber.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Phone number field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (confirmPassword.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Re-type password field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            Toast.makeText(RegisterActivity.this, "Passwords did not match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean userCheck() {
        for (User user : LoginActivity.users) {
            if (user.getEmail().equals(email.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "User with this email address already exist.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        for(User user : LoginActivity.users){
            if(user.getPhoneNumber().equals(phoneNumber.getText().toString())){
                Toast.makeText(RegisterActivity.this, "User with this number already exist.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}
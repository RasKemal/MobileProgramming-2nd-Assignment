package com.example.myproject;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    TextView register1;
    TextView register2;
    EditText email;
    EditText password;
    Button login;
    int loginAttempt = 0;
    int maxLoginAttempt = 3;
    static ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputLoginPassword);
        login = findViewById(R.id.btnlogin);

        confBtn();
        users.clear();
        readUsersFromFile();
        if(users == null || users.size() == 0){
            users = createUsers();
        }

        register1 = (TextView) findViewById(R.id.loginRegister);
        register2 = (TextView) findViewById(R.id.loginRegis);

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
    }
    public void openRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void readUsersFromFile() {
        FileInputStream fis = null;
        try {
            File usersFile = new File(getApplicationContext().getFilesDir() + "/users");
            usersFile.createNewFile();
            fis = new FileInputStream(getApplicationContext().getFilesDir() + "/users");
            ObjectInputStream is = null;
            try{
                while(true){
                    is = new ObjectInputStream(fis);
                    User user = (User) is.readObject();
                    users.add(user);
                }
            }catch (EOFException e){
                e.printStackTrace();
                if (is != null) {
                    is.close();
                }
                fis.close();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }


    private void confBtn(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginAttempt(v);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(loginAttempt >= maxLoginAttempt){
                    login.setEnabled(false);
                }
                else if(s.toString().equals("")){
                    login.setEnabled(false);
                }
                else if(!password.getText().toString().equals("")){
                    login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(loginAttempt >= maxLoginAttempt){
                    login.setEnabled(false);
                }
                else if(s.toString().equals("")){
                    login.setEnabled(false);
                }
                else if(!email.getText().toString().equals("")){
                    login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onLoginAttempt(View view){
        int userIndex = getUserIndex();
        if(getUserIndex() != -1){
            loginAttempt = 0;
            Toast.makeText(LoginActivity.this, "You have logged in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("userIndex", userIndex);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Check your email or password and try again", Toast.LENGTH_SHORT).show();
            loginAttempt++;
            if(loginAttempt >= maxLoginAttempt){
                login.setEnabled(false);
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                intent.putExtra("emailAddress", email.getText().toString());
                startActivity(intent);
            }
        }
    }

    private int getUserIndex(){
        for (User user : users) {
            if(user.getEmail().equals(email.getText().toString()) && user.getPassword().equals(User.md5(password.getText().toString()))){
                return users.indexOf(user);
            }
        }
        return -1;
    }

    public ArrayList<User> createUsers() {

        User newUser = new User("Feyyaz Yiğit", "feyyazyigit@gmail.com", "5431234567", User.md5("sevgi"));
        users.add(newUser);
        addUser(newUser);

        newUser = new User("Ece İmre", "ece.imre@gmail.com", "3265002545", User.md5("scamand1992"));
        users.add(newUser);
        addUser(newUser);

        newUser = new User("Atreus the Demigod", "atreusthearcher@gmail.com", "5963211236", User.md5("whymydadhatesme"));
        users.add(newUser);
        addUser(newUser);

        newUser = new User("Kratos the god", "feyyazyigit@gmail.com", "5431234567", User.md5("sevgi"));
        users.add(newUser);
        addUser(newUser);


        return users;
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

}
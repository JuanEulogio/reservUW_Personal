package com.cs407.reservuw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);

        Button buttonLogin = findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick(view);
            }
        });
    }

    public void onLoginClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        //TODO: checks authorization. for now we will just go to the next wireframe activity
        /** Check if username and password are correct (this is a basic example)
        if (username.equals(username) && password.equals(password)) {
            // Login successful, you can navigate to the next screen or perform other actions

        } else {
            // Login failed

        }**/


        goToMainMenu();
    }

    public void goToMainMenu(){
        Intent intent= new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
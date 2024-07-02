package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs407.reservuw.roomDB.User;
import com.cs407.reservuw.roomDB.uwRoomDatabase;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    //TODO: dont let it make paragraphs on enter key
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView alertTextView;


    uwRoomDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //before login render, it checks if user has a logged in session still
        SharedPreferences sharedPreferences = getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt ( "uid", -1) != -1) {
            //if sesson exists, we go main menu

            //TODO: why do we pass uid when we can get it in sharedPreference???
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("uid", sharedPreferences.getInt("uid", -1));
            startActivity(intent);
        }
        //initializing database to prebuild our data
        myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .createFromAsset("database/rooms.db")
                .allowMainThreadQueries()
                .build();


        //render login screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        alertTextView = findViewById(R.id.alertTextView);

        Button buttonLogin = findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick(view);
            }
        });

        Button buttonCreateAccount = findViewById(R.id.createNewAccount);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewAccountClick(view);
            }
        });
    }



    public void onLoginClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        //queries to our SQL_Room database to check if user account exists
        User user= myDatabase.userDAO().findByUser(username, password);

        if (user == null ) {
            alertTextView.setText("User Not Registered. Please Create a New Account");

        }else{
            //if user exists, we log them in and send them to the main menu
            //TODO: This is where we should save to our sharedPreference
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("uid", user.getUid());
            startActivity(intent);
        }
    }


    public void onNewAccountClick(View view){
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            alertTextView.setText("Please type your wished username and password");
        }else{
            //on successful registration attempt
            alertTextView.setText("New User. Adding you to the database");

            User newUser = new User(0, username, password);
            myDatabase.userDAO().insertUser(newUser);

            //TODO: other place where we should store to sharedStorage and send no intent
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("uid", myDatabase.userDAO().findByUser(username, password).getUid());
            startActivity(intent);
        }



    }
}
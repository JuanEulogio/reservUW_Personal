package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs407.reservuw.roomDB.User;
import com.cs407.reservuw.roomDB.uwRoomDatabase;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    private TextView alertTextView;

    uwRoomDatabase myDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

        if (sharedPreferences.getInt ( "uid", -1) != -1) {
            // Implies that the uid exists in SharedPreferences. The user did not log out when closing
            // the application
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("uid", sharedPreferences.getInt("uid", -1));
            startActivity(intent);
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //initializing database to prebuild our data
        myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .createFromAsset("database/rooms.db")
                .allowMainThreadQueries()
                .build();


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
    }



    public void onLoginClick(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        User user= myDatabase.userDAO().findByUser(username, password);


        //if user doest exist we make a new user, insert to db, and go to main menu
        if (user == null ) {
            //user not in the database so we make it for them
            alertTextView.setText("New User. Adding you to the database");
            //user not in the database so we make it for them
            try {
                //timer for user to see alertTextView prompt
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            User newUser = new User(0, username, password);
            myDatabase.userDAO().insertUser(newUser);

            Log.i(TAG, "new user just inserted. Its (new) uid= " + myDatabase.userDAO().findByUser(username, password).getUid());

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("uid", myDatabase.userDAO().findByUser(username, password).getUid());
            Log.i(TAG, "new user: " + myDatabase.userDAO().findByUser(username, password).getUid());
            startActivity(intent);

        }else{

            //user exist and no need to create user
            Log.i(TAG, "user exist: " + user.getUid());

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("uid", user.getUid());
            startActivity(intent);
        }


    }


}
package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cs407.reservuw.roomDB.uwRoomDatabase;

import org.json.JSONArray;

public class loginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;


    //callback to initialize database
    /**
    RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate (SupportSQLiteDatabase db) {
            try {
                 user = uwRoomDatabase.getDatabase(getApplicationContext()).userDAO();

                JSONArray list =
                        context.resources.openRawResource(R.raw.users).bufferedReader().use {
                    JSONArray(it.readText());
                }

                list.takeIf { it.length() > 0 }?.let { list ->
                    for (index in 0 until list.length()) {
                        val userObj = list.getJSONObject(index)
                        userDao.insertUser(
                                User(
                                        userObj.getInt("userId"),
                                        userObj.getString("userName")
                                )
                        )

                    }
                    Log.e("User App", "successfully pre-populated users into database")
                }
            } catch (Exception e) {
                Log.e("User App", e + ": failed to pre-populate users into database");
            }
        }
        //public void onOpen (SupportSQLiteDatabase db) {
            // do something every time database is open
        //}
    };
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //initializing database to prebuild our data
        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .createFromAsset("database/myapp.db")
                .build();

        Log.i(TAG, "Rooms in building 1 " + myDatabase.roomDAO().getRoomsByBuilding("ChIJrdw6sMusB4gR4jw375UJDZY").toString());

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
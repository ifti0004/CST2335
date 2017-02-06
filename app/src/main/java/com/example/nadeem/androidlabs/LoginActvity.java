package com.example.nadeem.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActvity extends AppCompatActivity {

    //For Log Purposes
    protected static final String LoginActivity = "LoginActivity";





    //onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        Log.i(LoginActivity, "In onCreate()");

        //Create SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


        //Set default value for the email address in the first time the activity runs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("DefaultEmail","email@domain.com");
        editor.commit();
    }


    protected void onStart(){
        super.onStart();
        Log.i(LoginActivity, "In onStart()");

        //Store a reference to the button
        Button loginButton = (Button) findViewById(R.id.loginButton);

        //Store a reference to the emailTextEdit widget
        final EditText emailInput = (EditText) findViewById(R.id.emailInput);

        //Create SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        //Read from SharedPreferences
        String email = prefs.getString("userEmail","");


        //Check if email is null, we will retreive the default value of the email
        if(email == null){
            email = prefs.getString("DefaultEmail","email@domain.com");
            emailInput.setText(email, TextView.BufferType.EDITABLE);
        }
        else
            emailInput.setText(email, TextView.BufferType.EDITABLE);


        //Store email into sharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", emailInput.getText().toString());
        editor.commit();


        //Callback function for the loginButton to store the text in the emailTextEdit to sharedPreferences
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Create SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userEmail", emailInput.getText().toString());
                editor.commit();

                //Calling StartActivity() as the next activity to start
                Intent intent = new Intent(LoginActvity.this, StartActivity.class);
                startActivity(intent);
            }
        });


    }



    protected void onResume(){
        super.onResume();
        Log.i(LoginActivity, "In onResume()");
    }


    protected void onPause(){
        super.onPause();
        Log.i(LoginActivity, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(LoginActivity, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(LoginActivity, "In onDestroy()");
    }
}
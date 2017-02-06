package com.example.nadeem.androidlabs;


        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    protected static final String StartActivity = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(StartActivity, "In onCreate()");


        Button startChat = (Button) findViewById(R.id.startChatID);
        startChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(StartActivity,"User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });



        //Store a reference to the button
        Button imButton = (Button) findViewById(R.id.imButtonID);
        imButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this,ListItemsActivity.class);
                // startActivity(intent);
                startActivityForResult(intent , 5);

            }
        });
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        // check if the request code is same as what is passed, here it is 5
        if(requestCode==5)
        {
            Log.i(StartActivity, "Returned to StartActivity.onActivityResult");
        }
        String messagePassed = data.getStringExtra("Response");
        if(messagePassed == data.getStringExtra("Response")){
            CharSequence text = messagePassed;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext() , text, duration);
            toast.show();
        }


    }


    protected void onResume(){
        super.onResume();
        Log.i(StartActivity, "In onResume()");
    }

    protected void onStart(){
        super.onStart();
        Log.i(StartActivity, "In onStart()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(StartActivity, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(StartActivity, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(StartActivity, "In onDestroy()");
    }
}

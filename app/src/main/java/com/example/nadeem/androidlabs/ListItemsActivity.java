package com.example.nadeem.androidlabs;


        import android.app.Activity;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.provider.MediaStore;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.Switch;
        import android.widget.Toast;

        import static android.R.attr.duration;

public class ListItemsActivity extends AppCompatActivity {
    protected static final String ListItemActivity = "ListItemActivity";
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ListItemActivity, "In onCreate()");

        imgButton =(ImageButton)findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        //Switch onCheckedChanged event handler
        final Switch enableSomthing = (Switch) findViewById(R.id.enableSomethingID);

        enableSomthing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked){
                    CharSequence text = "Switch is On";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext() , text, duration);
                    toast.show();
                }
                else
                {
                    CharSequence text = "Switch is Off";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext() , text, duration);
                    toast.show();
                }
            }
        });


        //=====================================================================
        //CheckBox onChanged event listener
        final CheckBox checkBox = (CheckBox)findViewById(R.id.checkBoxID);

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                builder.setMessage(R.string.dialog_message);
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "My information to share");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                builder.show();
            }

        });

    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgButton.setImageBitmap(imageBitmap);
        }
    }

    protected void onResume(){
        super.onResume();
        Log.i(ListItemActivity, "In onResume()");
    }

    protected void onStart(){
        super.onStart();
        Log.i(ListItemActivity, "In onStart()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(ListItemActivity, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(ListItemActivity, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(ListItemActivity, "In onDestroy()");
    }

}

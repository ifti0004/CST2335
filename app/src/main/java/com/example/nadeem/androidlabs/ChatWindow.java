package com.example.nadeem.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class ChatWindow extends AppCompatActivity {
    final ArrayList<String> chatArray = new ArrayList<>();
    protected ChatDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Resources resources = getResources();
        final ListView listViewChat = (ListView) findViewById(R.id.listViewChat);
        //in this case, "this" is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listViewChat.setAdapter(messageAdapter);
        final EditText editTextChat = (EditText) findViewById(R.id.editTextChat);
        Button buttonSend = (Button) findViewById(R.id.buttonSend);

        //Database
        dbHelper = new ChatDatabaseHelper(this);//Create a temporary ChatDatabaseHelper object
        final SQLiteDatabase db = dbHelper.getWritableDatabase();//dbHelper gets a writeable database and stores that as an instance variable db
        //db.isOpen();



        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatArray.add(editTextChat.getText().toString());
                messageAdapter.notifyDataSetChanged();//this restarts the process of getCount()/getView()
                editTextChat.setText("");
                ContentValues newValues = new ContentValues();
                newValues.put(ChatDatabaseHelper.MESSAGES_COLUMN, chatArray.get(chatArray.size()-1));
                db.insert(ChatDatabaseHelper.databaseName, "", newValues);
            }

        });

        //Reading from Database
        Cursor cursor = db.query(ChatDatabaseHelper.databaseName,
                new String[] { ChatDatabaseHelper.ID_COLUMN, ChatDatabaseHelper.MESSAGES_COLUMN}, null, null, null, null,null);

        cursor.moveToFirst();//move the cursor to the first row

        //Adding data from database to the arrayList
        while(!cursor.isAfterLast()) {
            //Add the data into the ArrayList
            int msg_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.MESSAGES_COLUMN); //find the index of the MESSAGES_COLUMN
            int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.ID_COLUMN);//find the index of the ID_COLUMN

            chatArray.add(cursor.getString(msg_columnIndex));//add the messages from the screen to the ArrayList



            Log.i("ChatWindow:1st ColName:",cursor.getColumnName(id_columnIndex));
            Log.i("ChatWindow", "SQL MESSAGE:" +  cursor.getString(id_columnIndex));//Display the id to the console

            Log.i("ChatWindow:2nd ColName:",cursor.getColumnName(msg_columnIndex));
            Log.i("ChatWindow", "SQL MESSAGE:" +  cursor.getString(msg_columnIndex));//Display the message to the console

            messageAdapter.notifyDataSetChanged();//this restarts the process of getCount()/getView()

            cursor.moveToNext();
        }

        Log.i("ChatWindow", "Cursor's column count=" + cursor.getColumnCount());
        cursor.close();

    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return chatArray.size();
        }

        public String getItem(int position) {
            return chatArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById((R.id.message_text));
            message.setText(getItem(position));//get the string at position
            return result;
        }
    }

    public void onDestroy() {

        super.onDestroy();
        dbHelper.close();

    }

}
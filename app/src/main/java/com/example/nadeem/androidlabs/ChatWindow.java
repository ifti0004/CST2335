package com.example.nadeem.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class ChatWindow extends AppCompatActivity {
    final ArrayList<String> chatArray = new ArrayList<>();
    ChatAdapter messageAdapter;
    protected ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
    //dbHelper gets a writeable database and stores that as an instance variable db
    //final protected SQLiteDatabase db = dbHelper.getWritableDatabase();

    protected Cursor cursor;
    int clickedPosition;
    protected boolean falg_FrameLayoutExsist=false;

    protected Bundle extras;
    String str2;
    MessageFragment frag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //Lab7 - Check if FrameLayout has been loaded
        falg_FrameLayoutExsist = (findViewById(R.id.put_fragment_here) != null); //find out if this is a phone or tablet

        Resources resources = getResources();
        final ListView listViewChat = (ListView) findViewById(R.id.listViewID);


        //in this case, "this" is the ChatWindow, which is-A Context object
        messageAdapter = new ChatAdapter(this);
        listViewChat.setAdapter(messageAdapter);
        final EditText editTextChat = (EditText) findViewById(R.id.chatEditTextID);
        Button buttonSend = (Button) findViewById(R.id.sendButtonID);

        //Database
        //  dbHelper = new ChatDatabaseHelper(this);//Create a temporary ChatDatabaseHelper object

        //dbHelper gets a writeable database and stores that as an instance variable db
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
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
                cursor = db.query( ChatDatabaseHelper.databaseName,
                        new String[] { ChatDatabaseHelper.ID_COLUMN, ChatDatabaseHelper.MESSAGES_COLUMN}, null, null, null, null,null);

                cursor.moveToFirst();//move the cursor to the first row

            }

        });

        //Reading from Database
        cursor = db.query( ChatDatabaseHelper.databaseName,
                new String[] { ChatDatabaseHelper.ID_COLUMN, ChatDatabaseHelper.MESSAGES_COLUMN}, null, null, null, null,null);

        cursor.moveToFirst();//move the cursor to the first row

        //Adding data from database to the arrayList
        while(!cursor.isAfterLast()) {
            //Add the data into the ArrayList
            int msg_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.MESSAGES_COLUMN); //find the index of the MESSAGES_COLUMN
            int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.ID_COLUMN);//find the index of the ID_COLUMN

            chatArray.add(cursor.getString(msg_columnIndex));//add the messages from the DB to the ArrayList

            Log.i("ChatWindow:1st ColName:",cursor.getColumnName(id_columnIndex));
            Log.i("ChatWindow", "SQL MESSAGE:" +  cursor.getString(id_columnIndex));//Display the id to the console

            Log.i("ChatWindow:2nd ColName:",cursor.getColumnName(msg_columnIndex));
            Log.i("ChatWindow", "SQL MESSAGE:" +  cursor.getString(msg_columnIndex));//Display the message to the console

            cursor.moveToNext();
        }

        Log.i("ChatWindow", "Cursor's column count=" + cursor.getColumnCount());
        // cursor.close();

        messageAdapter.notifyDataSetChanged();//this restarts the process of getCount()/getView()

        //Lab7 - Add onItemClickListener
        listViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                Bundle bun = new Bundle();
                cursor.moveToPosition(position);
                bun.putLong("ID", l );
                str2 = chatArray.get(position);
                bun.putString("msgTxt",str2);
                clickedPosition = position;
                if(falg_FrameLayoutExsist) {
                    frag = new MessageFragment(ChatWindow.this);

                    frag.setArguments(bun);

                    //getFragmentManager().beginTransaction().add(R.id.FragmentHolderLayout, frag).commit();
                    getFragmentManager( ).beginTransaction().replace(R.id.put_fragment_here, frag).commit();
                }
                //step 3 if a phone, transition to empty Activity that has FrameLayout
                else //isPhone
                {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(bun);
                    startActivityForResult(intent, 5);
                }
            }

        });

    }
    public
    void  deleteID(long id)
    {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        //  String id = returnedIntent.getStringExtra("ID");
        //   int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.ID_COLUMN);
        String str = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.MESSAGES_COLUMN));
        Log.i("the msg",str);
        // int position = messageAdapter.getPosition(str);
        Log.i("position is",String.valueOf(chatArray.indexOf(str)) );

        chatArray.remove(clickedPosition);
        db.delete(ChatDatabaseHelper.databaseName, ChatDatabaseHelper.ID_COLUMN +"=?", new String[]{Long.toString(id)});

        cursor = db.query( ChatDatabaseHelper.databaseName,
                new String[] { ChatDatabaseHelper.ID_COLUMN, ChatDatabaseHelper.MESSAGES_COLUMN}, null, null, null, null,null);

        cursor.moveToFirst();//move the cursor to the first row
        // messageAdapter.remove(messageAdapter.getItem(position));
        messageAdapter.notifyDataSetChanged();
        //  db.delete(ChatDatabaseHelper.databaseName, ChatDatabaseHelper.ID_COLUMN +"=?", new String[]{id});
        getFragmentManager( ).beginTransaction().remove(frag).commit();

    }



    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Activity context) {
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

        //For lab7
        public long getItemId(int position){
            Log.i("this is the position", String.valueOf(position));

            cursor.moveToPosition(position);
            Log.i("c moved to position", String.valueOf(position));

            int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.ID_COLUMN);
            String str = cursor.getString(id_columnIndex);
            Log.i("c read this item", str);

            id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.MESSAGES_COLUMN);
            str2 = cursor.getString(id_columnIndex);

            int value = Integer.parseInt(str);


            return value;

        }
    }



    //onActivityResult of lab7
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i("onActivityResult","");

        if (resultCode == 1) {
            String id = returnedIntent.getStringExtra("ID");
            //   int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.ID_COLUMN);
            String str = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.MESSAGES_COLUMN));
            Log.i("the msg",str);
            // int position = messageAdapter.getPosition(str);
            Log.i("position is",String.valueOf(chatArray.indexOf(str)) );

            chatArray.remove(clickedPosition);
            db.delete(ChatDatabaseHelper.databaseName, ChatDatabaseHelper.ID_COLUMN +"=?", new String[]{id});
            // messageAdapter.remove(messageAdapter.getItem(position));
            cursor = db.query( ChatDatabaseHelper.databaseName,
                    new String[] { ChatDatabaseHelper.ID_COLUMN, ChatDatabaseHelper.MESSAGES_COLUMN}, null, null, null, null,null);

            messageAdapter.notifyDataSetChanged();
            //  db.delete(ChatDatabaseHelper.databaseName, ChatDatabaseHelper.ID_COLUMN +"=?", new String[]{id});


        }
    }


    public void onDestroy() {

        super.onDestroy();
        dbHelper.close();

    }

}
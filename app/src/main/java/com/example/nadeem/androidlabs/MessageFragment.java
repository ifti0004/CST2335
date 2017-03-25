package com.example.nadeem.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Nadeem on 3/20/2017.
 */

public class MessageFragment extends Fragment {
    TextView textViewIdNumber, textViewMsg;

    Button btnDelMsg;
    Bundle bun;

    Context parent;
    long id;
    ChatWindow chatWindow;

    public  MessageFragment(ChatWindow cw)
    {
        chatWindow = cw;
    }
    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        bun = getArguments();
        id = bun.getLong("ID");

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        parent = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_message_details, null);
        // setContentView(R.layout.activity_message_details);
        // TextView textViewMsg = (TextView)gui.findViewById(R.id.textViewMsg);
        //     textViewMsg.setText("");//




        btnDelMsg = (Button)view.findViewById(R.id.btnDeleteMsg);
        textViewIdNumber = (TextView)view. findViewById(R.id.textViewIdNumber);
        textViewMsg = (TextView)view.findViewById(R.id.textViewMsg);

        //Step 3, create fragment onCreation, pass data from Intent Extras to FragmentTransction

        //  bun = chatWindow.getIntent().getExtras();
        //  Log.i("id came from chat", Long.toString(bun.getLong("ID")));




        textViewIdNumber.setText(Long.toString(bun.getLong("ID")));
        textViewMsg.setText(bun.getString("msgTxt"));

        btnDelMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                // getActivity().setResult(int resultCode, Intent data){
                if(chatWindow== null) {
                    Intent returnIntent = new Intent();
                    String str = Long.toString(bun.getLong("ID"));
                    returnIntent.putExtra("ID", str);///
                    // setResult(1, returnIntent);
                    getActivity().setResult(1, returnIntent);
                    getActivity().finish();
                }
                else chatWindow.deleteID(bun.getLong("ID"));
            }


        });

        return view;


    }


}


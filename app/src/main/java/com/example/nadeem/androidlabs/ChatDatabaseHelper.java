package com.example.nadeem.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nadeem on 2/11/2017.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION_NUM = 5;
    public static final String databaseName = "Chats";//Chats.db
    public static final String ID_COLUMN = "_id";
    public static final String MESSAGES_COLUMN = "KEY_MESSAGES";

    public ChatDatabaseHelper(Context ctx)
    {
        super(ctx, databaseName, null, VERSION_NUM);

    }

    public void onCreate(SQLiteDatabase db) //only called if not yet created
    {
        db.execSQL("CREATE TABLE " + databaseName + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY autoincrement, "+
                MESSAGES_COLUMN + " TEXT);"
        );

        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + databaseName);
        onCreate(db);

        Log.i("ChatDatabaseHelper", "Calling onUpgrade, old vesrion=" + oldVersion + "newVersion=" + newVersion);
    }


}

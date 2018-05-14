package com.example.user.ekg;

/**
 * Created by user on 14.11.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SmsDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmsDatenBank";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "SmsDaten";
    public static final String NAME_COLUMN = "Name";
    public static final String NUMBER_COLUMN = "Number";
    public static final String MESSAGE_COLUMN = "Message";
    public static final String COLUMN_ID = "_id";
    private final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COLUMN
            + " TEXT, " + NUMBER_COLUMN + " TEXT, " + MESSAGE_COLUMN + " TEXT);";




    public SmsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void insertSmsDB(SQLiteDatabase db, String name, String nummer, String message){
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, name);
        values.put(NUMBER_COLUMN, nummer);
        values.put(MESSAGE_COLUMN, message);
        db.insert("SmsDaten", null, values);
    }

}




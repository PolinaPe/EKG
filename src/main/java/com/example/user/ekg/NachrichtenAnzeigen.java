package com.example.user.ekg;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NachrichtenAnzeigen extends Activity {
    SmsDB dbSms;
    SQLiteDatabase db;
    ListView list;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nachrichten_anzeigen);
        list = (ListView) findViewById(R.id.list);
        dbSms = new SmsDB(this);
        db = dbSms.getReadableDatabase();
        cursor = db.query("SmsDaten", new String[]{ "_id", "Number"}, null, null, null, null, null);
        CursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"Number"},
                new int[] {android.R.id.text1}, 0);
        list.setAdapter(adapter);

    }
}

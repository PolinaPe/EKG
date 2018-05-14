package com.example.user.ekg;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import static com.example.user.ekg.SmsDB.*;


public class SmsService extends IntentService {


    public SmsService() {
        super("SmsService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle smsIntent = intent.getExtras();
        String message2 = smsIntent.getString("message");
        String sender2 = smsIntent.getString("sender");
        showText(message2, sender2);
        SmsDB mDatabaseHelper = new SmsDB(getApplicationContext());
        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        String s ="contact";
        mDatabaseHelper.insertSmsDB(mSqLiteDatabase, s, sender2, message2);
    }

  private void showText(String message2, String sender2){
      Log.v("SmsService", "Message " + message2 + " " + sender2);
  }

}

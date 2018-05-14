package com.example.user.ekg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    //BT Adapter bekommen
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_DISCOVERABLE = 10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void click(View view)
    {

        _bluetooth.enable();

        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE); // Make device discoverable
        startActivityForResult(enabler, REQUEST_DISCOVERABLE); // Make device discoverable


    }
    public void weiter(View view) {
        Intent start = new Intent(this, BTClientActivity.class);
        startActivity(start);
    }
    protected void onDestroy() {
        super.onDestroy();
        if (_bluetooth != null) {
            try {
                // Socket schliessen
                _bluetooth.disable();
            } catch (Exception e) {
                Toast.makeText(this, "BT kann nicht ausgeschaltet werden", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
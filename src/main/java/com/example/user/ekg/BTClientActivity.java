package com.example.user.ekg;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


/* Datenbank ist nicht eingeschlossen. Datenbank muss man in andere Thread öffnen und die Daten reinchmeissen.
* Danach wird die Tabelle umbennannt( Name und Vorname vom Patient). Es wird einfach als neue Tabelle zu erzeugen.
* CleintSocket und BT wird auf Back Knopf zugeschlossen*/

public class BTClientActivity extends AppCompatActivity {
    private static final int codeRequest = 0x1;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter(); // Adapter bekommen
    private BluetoothSocket socket = null; // Client BT auf null
    private GraphicalView mChart; // Graphic

    private XYSeries visitsSeries; // Behälter für 2 XYValues
    private XYMultipleSeriesDataset dataset; // Behälter für alle XY Values

    private XYSeriesRenderer visitsRenderer; // Aussehen von Paar XYValues
    private XYMultipleSeriesRenderer multiRenderer; // Aussehen von allen XYValues
    private LinearLayout chartContainer;
   private ImageView imageView;
   private Handler mHandler;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btclient);
        mHandler = new Handler();
        imageView = (ImageView) findViewById(R.id.imageView);
        registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("See your Sms"))
                {
                    Intent intentSms = new Intent(getApplicationContext(), NachrichtenAnzeigen.class);
                    startActivity(intentSms);
                }
                if(selectedItem.equals("Send SMS"))
                {
                   FragmentManager manager = getSupportFragmentManager();
                    SmsDialog smsDialog = new SmsDialog();
                    smsDialog.show(manager, "SmsDialog");
                }
                if(selectedItem.equals("Send E-Mail"))
                {
                    FragmentManager manager = getSupportFragmentManager();
                    MailDialog mailDialog = new MailDialog();
                    mailDialog.show(manager, "mailDialog");
                }


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        if (!_bluetooth.isEnabled()) {
            finish();
            return;
        }

        Intent intent = new Intent(this, DiscoveryActivity.class);

        //Handlungsnachricht
        Toast.makeText(this, "select device to connect", Toast.LENGTH_SHORT).show();

        //starten DiscoveryActivity
        startActivityForResult(intent, codeRequest);
        // Setting up chart
        setupChart();



    }
    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "You have new massage", Toast.LENGTH_SHORT).show();



        }
    };

    // Information über das Gerät bekommen
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != codeRequest) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); // Device aus Intent bekommen
        Toast.makeText(this, "select device to connect", Toast.LENGTH_SHORT).show();
        new Thread() {
            public void run() {


                connect(device);
            }


        }.start();
    }

    // Verbinden das Gerät als Client

    protected void connect(BluetoothDevice device) {

        try {
            // schaffen Rfcomm BluetoothSocket
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
            // Verbindung von Socket initiate
            socket.connect();
           Log.d("ee", "connection");

            if (socket != null) {
                try {


                    InputStream inputStream;

                    inputStream = socket.getInputStream();

                    final byte[] bytes = new byte[2048];
                    int read;


                    for (int i = 1; (read = inputStream.read(bytes)) > -1; i++) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run () {
                                final Animation animation = new AlphaAnimation(1, 0);
                                animation.setRepeatCount(1);
                                imageView.startAnimation(animation);

                            }
                        });
                        String s = new String(bytes, 0, read);
                        String[] values = new String[2];
                        values[0] = Integer.toString(i);
                        values[1] = s;
                        visitsSeries.add(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                        mChart.repaint();


                    }} catch (IOException e) {
                    Log.d("ee", "no date");
                }}
        } catch (IOException e) {
            Log.e("Connection failed", "", e);
        }
    }

    private void setupChart(){

        // schaffen Series für zwei xy daten
        visitsSeries = new XYSeries("Dein Herz");

        // schaffen dataset für alle Series
        dataset = new XYMultipleSeriesDataset();
        // Adding Visits Series to the dataset
        dataset.addSeries(visitsSeries);

        // schaffen Render für eine Series, bestimmt wie sie aussieht
        visitsRenderer = new XYSeriesRenderer();
        visitsRenderer.setColor(Color.RED);
        visitsRenderer.setFillPoints(true);
        visitsRenderer.setLineWidth(1);


        // schaffen Aussehen (Render) von ganzen Chart
        multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle("EKG");
        multiRenderer.setXTitle("Seconds");
        multiRenderer.setYTitle("Herzschlag");
        multiRenderer.setZoomButtonsVisible(true);

        multiRenderer.setXAxisMin(0);
        multiRenderer.setXAxisMax(150);

        multiRenderer.setYAxisMin(-10);
        multiRenderer.setYAxisMax(250);


        // Adding visitsRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(visitsRenderer);

        // Getting a reference to LinearLayout of the MainActivity Layout
        chartContainer = (LinearLayout) findViewById(R.id.linearLayout);

        mChart = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);

        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(mChart);
    }
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            try {
                // Socket schliessen
                socket.close();
            } catch (IOException e) {
                Toast.makeText(this, "Socket kann nicht geschlossen werden", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
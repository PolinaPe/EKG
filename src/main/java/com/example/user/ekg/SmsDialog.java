package com.example.user.ekg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

/**
 * Created by user on 08.11.2016.
 */

public class SmsDialog  extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View form=null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form= getActivity().getLayoutInflater().inflate(R.layout.sms_dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Sms senden").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {


        EditText editTextText = (EditText) form.findViewById(R.id.text);
        String text 	= editTextText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);



        startActivity(Intent.createChooser(intent, "Select an Sms Client:"));
    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }

}

package com.example.user.ekg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

/**
 * Created by user on 08.11.2016.
 */

public class MailDialog  extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View form=null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form= getActivity().getLayoutInflater().inflate(R.layout.mail_dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setTitle("E-Mail senden").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

        EditText editTextEailTo = (EditText) form.findViewById(R.id.editText1);
        EditText editTextEmailSubject = (EditText) form.findViewById(R.id.editText2);
        EditText editTextEmailContent = (EditText) form.findViewById(R.id.editText3);
//get to, subject and content from the user input and store as string.
        String emailTo 		= editTextEailTo.getText().toString();
        String emailSubject 	= editTextEmailSubject.getText().toString();
        String emailContent 	= editTextEmailContent.getText().toString();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailTo});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
        /// use below 2 commented lines if need to use BCC an CC feature in email
        //emailIntent.putExtra(Intent.EXTRA_CC, new String[]{ to});
        //emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{to});
        ////use below 3 commented lines if need to send attachment
        //emailIntent .setType("image/jpeg");
       // emailIntent .putExtra(Intent.EXTRA_SUBJECT, "My Picture");
        //emailIntent .putExtra(Intent.EXTRA_STREAM, Uri.parse("file://sdcard/captureimage.png"));

        //need this to prompts email client only
        emailIntent.setType("message/rfc822");

        startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));



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

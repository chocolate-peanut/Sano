package com.example.sano;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Feedback extends AppCompatActivity {

    private EditText txt_email, txt_subject, txt_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        txt_email = findViewById(R.id.txt_email);
        txt_subject = findViewById(R.id.txt_subject);
        txt_message = findViewById(R.id.txt_message);
        Button bt_sendEmail = findViewById(R.id.bt_send);
        Button bt_back = findViewById(R.id.bt_back);

        //Button to send an email
        bt_sendEmail.setOnClickListener(view -> {
            String fb_subject= txt_subject.getText().toString();
            String fb_message = txt_message.getText().toString();
            String fb_email = txt_email.getText().toString();
            //divide the emails
            String [] emails = fb_email.split(",");

            //ACTION_SEND to send data from one activity to another
            Intent bt_send = new Intent(Intent.ACTION_SEND);
            //Add extended data to the intent.
            bt_send.putExtra(Intent.EXTRA_SUBJECT, fb_subject);
            bt_send.putExtra(Intent.EXTRA_TEXT, fb_message);
            bt_send.putExtra(Intent.EXTRA_EMAIL, emails);

            //"message/rfc822" expression to open emails client
            bt_send.setType("message/rfc822");
            //Set the package type to open gmail
            bt_send.setPackage("com.google.android.gm");

            //start activity to send an email
            startActivity(bt_send);
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
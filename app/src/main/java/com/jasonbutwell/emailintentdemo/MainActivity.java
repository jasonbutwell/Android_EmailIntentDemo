package com.jasonbutwell.emailintentdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText name, phone, subject, message;

    Intent emailIntent, chooserIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.Name);
        phone = (EditText)findViewById(R.id.PhoneNo);
        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.Message);
    }

    public void sendText( View view ) {
        
    }

    public void sendEmail(View view) {

        boolean successful = true;

        if ( name.length() < 1 || phone.length() < 1 || subject.length() < 1 || message.length() < 1 )
            successful = false;

        if ( !successful )
            Toast.makeText(getApplicationContext(),"Please fill out all required fields",Toast.LENGTH_LONG).show();
        else {
            emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));

            String[] to = {"jasonbutwell@gmail.com"};

            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());

            String bodyTextEmail = "Email from: " + name.getText().toString() + ", Phone number: " + phone.getText().toString() + "\n\n"+ message.getText().toString();

            emailIntent.putExtra(Intent.EXTRA_TEXT, bodyTextEmail);
            emailIntent.setType("message/rfc822");

            chooserIntent = Intent.createChooser(emailIntent, "Send Email");
            startActivity(chooserIntent);
        }
    }
}

package com.jasonbutwell.emailintentdemo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE_LOAD_IMAGE = 101;
    String picturePath;
    Uri imagepath;

    private EditText name, phone, subject, message;

    Intent smsIntent, emailIntent, chooserIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.Name);
        phone = (EditText)findViewById(R.id.PhoneNo);
        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.Message);
    }

    public void openGallery(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data",true);
        startActivityForResult(Intent.createChooser(intent,"Compete Action Using"),REQUEST_CODE_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            imagepath = selectedImage;

            Log.i("file",selectedImage.toString());

        }
    }

    public void openWebPage(View view) {
        String url="http://www.google.com";

        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        startActivity(webIntent);
    }

    public void sendText( View view ) {
        smsIntent = new Intent(Intent.ACTION_VIEW);

        String bodyTextSMS = "SMS From: " + name.getText().toString() + ", Phone number: " + phone.getText().toString() + "\n\n"+ message.getText().toString();

        smsIntent.putExtra("sms_body",bodyTextSMS);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setData(Uri.parse("sms:07475653985"));

        startActivity(smsIntent);
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

            if ( imagepath != null ) {
                File file = new File(this.getFilesDir().getAbsolutePath()+picturePath);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                emailIntent.setType("image/jpeg");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath())); //Your image file
            }

            chooserIntent = Intent.createChooser(emailIntent, "Send Email");
            startActivity(chooserIntent);
        }
    }

}

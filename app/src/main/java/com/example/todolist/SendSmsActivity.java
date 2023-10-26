package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSmsActivity extends AppCompatActivity {
    EditText messageEdt;
    Button messageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        messageEdt = findViewById(R.id.messageEdt);
        messageButton = findViewById(R.id.messageButton);

        private static final int PERMISSION_REQUEST_SEND_SMS = 1;

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEdt.getText().toString();
                if (TextUtils.isEmpty(message)){
                    Toast.makeText(SendSmsActivity.this, "Message is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(SendSmsActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
                } else {
                    // Permission is already granted, send SMS
                    sendSMS();
                }
            }
        });

    }

    private void sendSMS() {
        String msg = messageEdt.getText().toString();
        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage("9510360874",null,msg,null,null);
            Toast.makeText(getApplicationContext(),"Message Sent!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Some Error Occurred!",Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.send_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.Manifest;
import android.widget.Toast;

import com.example.send_s.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    //Declaration des variables
    EditText editTextPhone, editTextMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialisation des variables
        editTextMessage = findViewById(R.id.editTextMessage);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnSend = findViewById(R.id.btnSend);

        Button btnLogout = findViewById(R.id.btnLogout);

        //Listener pour le boutton d'envoie
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verification des conditions pour les permissions
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){
                    //Quand la permission est 'granted' faut envoyer le sms
                    sendSMS();
                }else{
                    //Quand la permission est 'not granted' faut demander la permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Verifier la condition
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //Dans ce cas la permission est verifier faut appeler sendSMS()
            sendSMS();
        }else{
            //Dans le cas contraire afficher un petit message
            Toast.makeText(this, "Permission refusé !", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS() {
        //Recuperer la valeur de editText
        String phone = editTextPhone.getText().toString();
        String message = editTextMessage.getText().toString();

        //Verifier si on a bien recuperer des valeurs (non vide) et initialiser SMS Manager
        if(!phone.isEmpty() && !message.isEmpty()){
            SmsManager smsManager = SmsManager
                    .getDefault();

            //Envoie du SMS
            smsManager.sendTextMessage(phone,null,message,null,null);
            Toast.makeText(this,"SMS envoyer avec succés", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Merci de tapez un numero de telephone valide et un message",Toast.LENGTH_SHORT).show();
        }
    }
}
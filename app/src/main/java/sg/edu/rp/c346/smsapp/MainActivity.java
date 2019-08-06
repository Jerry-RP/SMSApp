package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText ed1;
EditText ed2;
Button button2;
    Button button;
    private BroadcastReceiver br;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 9999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        button2=findViewById(R.id.button2);
        br=new MessageReciever();
        ed1=findViewById(R.id.editText);
        ed2=findViewById(R.id.editText2);
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br,filter);
        checkPermission();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                String [] split=ed1.getText().toString().split(",");
                for(int i=0;i<split.length;i++){
                    smsManager.sendTextMessage(split[i], null, ed2.getText().toString(), null, null);

                }
                Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra("sms_body", ed2.getText().toString());
                    intent.setData(Uri.parse("sms:"+ed1.getText().toString()));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Log.d("Error" , "Error");
                }
                }
        });
    }


    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        int permissionReadSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED && permissionReadSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},MY_PERMISSIONS_REQUEST_SMS_RECEIVE;
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
}

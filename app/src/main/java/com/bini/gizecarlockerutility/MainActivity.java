package com.bini.gizecarlockerutility;
/* Binishare (Gize Trading PLC) */
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.marozzi.roundbutton.RoundButton;
import com.pixplicity.easyprefs.library.Prefs;
import com.victor.loading.rotate.RotateLoading;

public class MainActivity extends AppCompatActivity {
    LoginActivity loginActivity = new LoginActivity();
    public RotateLoading rotateLoading;
    public TextView smsStatus;
    public TextView deliveryStatus;
    public String LAsimno;
    public RoundButton lockBtn;
    public RoundButton unlockBtn;
    public static final String SMS_SENT_ACTION = "com.bini.gizecarlockerutility.SMS_SENT_ACTION";
    public static final String SMS_DELIVERED_ACTION = "com.bini.gizecarlockerutility.SMS_DELIVERED_ACTION";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize yderegal
        LAsimno = Prefs.getString("simnumberkey", "no num set");
        //animation eziga ygebal initiate yderegal
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        final String Lock = "dy123456";
        final String Unlock = "ty123456";
        lockBtn = (RoundButton) findViewById(R.id.lock_btn);
        unlockBtn = (RoundButton) findViewById(R.id.unlock_btn);
        smsStatus = (TextView) findViewById(R.id.message_status);
        deliveryStatus = (TextView) findViewById(R.id.delivery_msg);
//======================================================================================================================
///lock click sareg yalew aaction
lockBtn.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   ///permission meteyek
                                   if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                           Manifest.permission.SEND_SMS) !=
                                           PackageManager.PERMISSION_GRANTED) {
                                       ActivityCompat.requestPermissions(MainActivity.this,
                                               new String[]{Manifest.permission.SEND_SMS},
                                               MY_PERMISSIONS_REQUEST_SEND_SMS);
                                   } else {
                                       // Permission already granted. ahun melak ychlalu

                                       sendLockSMS(LAsimno, Lock);
                                       Toast.makeText(getApplicationContext(), "Sending...., please wait for delivery", Toast.LENGTH_LONG).show();
                                       ///////////////////meshkerker/////////////////////////
                                       rotateLoading.start();
                                       final RoundButton rndbtn = (RoundButton) findViewById(R.id.lock_btn);

                                       rndbtn.startAnimation();
                                       final Handler handler = new Handler();
                                       handler.postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               rotateLoading.stop();
                                               rndbtn.revertAnimation();
                                           }
                                       }, 5000);
                                       /////////////////////////////////////
                                   }
                               }
                           }
);


//======================================================================================================================

//======================================================================================================================
        ///unlock click sareg yalew action
unlockBtn.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   ///permission meteyek
                                   if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                           Manifest.permission.SEND_SMS) !=
                                           PackageManager.PERMISSION_GRANTED) {
                                       ActivityCompat.requestPermissions(MainActivity.this,
                                               new String[]{Manifest.permission.SEND_SMS},
                                               MY_PERMISSIONS_REQUEST_SEND_SMS);
                                   } else {
                                       // Permission already granted. ahun melak ychlalu
                                       sendUnLockSMS(LAsimno, Unlock);

                                       ///////////////////meshkerker/////////////////////////
                                       rotateLoading.start();
                                       final RoundButton rndbtn = (RoundButton) findViewById(R.id.unlock_btn);

                                       rndbtn.startAnimation();

                                       final Handler handler = new Handler();
                                       handler.postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               rotateLoading.stop();
                                               rndbtn.revertAnimation();
                                           }
                                       }, 5000);
                                       //////////////////////////////////////////////////////
                                       Toast.makeText(getApplicationContext(), "Sending.....,please wait for delivery", Toast.LENGTH_LONG).show();
                                   }
                               }
                           }
);

     }
//==================================================================================================================================================

                                        //*******************************************************
                                        //Methods to send sms
                                        //*******************************************************

//==================================================================================================================================================
    public void sendLockSMS(String LAsimno, String Lock) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(LAsimno, null, "dy123456", PendingIntent.getBroadcast(
                MainActivity.this, 0, new Intent(SMS_SENT_ACTION), 0), PendingIntent.getBroadcast(MainActivity.this, 1, new Intent(SMS_DELIVERED_ACTION), 0));
        ////////
        /*
         * Sent Receiver
         * */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = null;
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message = "Message Sent Successfully !";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = "Error.";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = "Error: No service.";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = "Error: Null PDU.";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        message = "Error: Radio off.";
                        break;
                }
                smsStatus.setText(message);
                smsStatus.setTextColor(Color.parseColor("#013220"));
            }
        }, new IntentFilter(SMS_SENT_ACTION));

        /*
         * Delivery Receiver
         * */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//
                deliveryStatus.setText("SMS Delivered");
                lockBtn.setText("Car Locked, Check to ensure");
                lockBtn.setTextColor(Color.parseColor("#013220"));
                lockBtn.setTextSize(15);
                deliveryStatus.setTextColor(Color.parseColor("#013220"));
                deliveryStatus.setTextSize(15);
                unlockBtn.setText("unLock");
                unlockBtn.setTextColor(Color.parseColor("#FFA000"));
                Toast.makeText(getApplicationContext(), "Delivered SMS", Toast.LENGTH_SHORT).show();
                abortBroadcast(); //message begeba kutr endaychekachek
            }
        }, new IntentFilter(SMS_DELIVERED_ACTION));
    }
//===========================================================================================================================================
    public void sendUnLockSMS(String LAsimno, String Unlock) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(LAsimno, null, "ty123456", PendingIntent.getBroadcast(
                MainActivity.this, 0, new Intent(SMS_SENT_ACTION), 0), PendingIntent.getBroadcast(MainActivity.this, 1, new Intent(SMS_DELIVERED_ACTION), 0));
        /*
         * Sent Receiver
         * */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = null;
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message = "Message Sent Successfully !";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = "Error.";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = "Error: No service.";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = "Error: Null PDU.";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        message = "Error: Radio off.";
                        break;
                }
                smsStatus.setText(message);
                smsStatus.setTextColor(Color.parseColor("#013220"));
            }
        }, new IntentFilter(SMS_SENT_ACTION));
        /*
         * Delivery Receiver
         * */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                deliveryStatus.setText("SMS Delivered");
                deliveryStatus.setTextSize(15);
                deliveryStatus.setTextColor(Color.parseColor("#013220"));
                unlockBtn.setText("Unlocked");
                unlockBtn.setTextColor(Color.parseColor("#013220"));
                unlockBtn.setTextSize(15);
                Toast.makeText(getApplicationContext(), "Delivered SMS", Toast.LENGTH_SHORT).show();
                lockBtn.setText("Lock");
                lockBtn.setTextColor(Color.parseColor("#FFA000"));
                abortBroadcast(); //message begeba kutr endaychekachek
            }
        }, new IntentFilter(SMS_DELIVERED_ACTION));
     }
}
//============================================================================================================================================

        //wedefit implement
///////////////////////////////////////////////////////////////////////////////////////
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//         // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }
/////////////////////////////////////////////////////////////////////////////////////////
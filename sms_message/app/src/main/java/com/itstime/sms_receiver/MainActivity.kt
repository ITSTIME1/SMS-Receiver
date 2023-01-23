package com.itstime.sms_receiver

import android.Manifest.permission.RECEIVE_SMS
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText


// This Project is for getting sms message
// Many program want to know someone who are you so you have to input the specify number.
// but it's not auto if you want to use this program.
// you know below step.

// 1. you have to install this app.
// 2. Input specify number ( to - to )

private const val TAG = "SmsBroadCastReceiver"
class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SMS_PERMISSION = 1;
    private val number : String = "010-9775-4185"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initial()
        requestSmsPermission()
        val intent = intent
        onNewIntent(intent)

    }
    // initial
    private fun initial() {
        val br : BroadcastReceiver = SmsBroadCaster()
        val filter = IntentFilter().apply{
            addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        }
        registerReceiver(br, filter)
    }


    // request permission
    private fun requestSmsPermission() {
        val receiveSMSPermission = RECEIVE_SMS
        val sendSMSPermission = android.Manifest.permission.SEND_SMS

        val receiveSMS = ContextCompat.checkSelfPermission(this, receiveSMSPermission)
        val sendSMS = ContextCompat.checkSelfPermission(this, sendSMSPermission)

        if (sendSMS and receiveSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(receiveSMSPermission, sendSMSPermission), REQUEST_CODE_SMS_PERMISSION)
        }
    }
    @SuppressLint("SetTextI18n", "ObsoleteSdkInt")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val data = intent?.getStringExtra("certifinum")
        // 성공적으로 가지고 온다.
        Log.d("data+set", data.toString())
        if (data != null) {
            val showText: TextView = findViewById(R.id.message_id)
            val inputText: TextView = findViewById(R.id.phone_number_input)
            inputText.text = number
            showText.text = data.toString()

            Toast.makeText(this, "성공적으로 반영", Toast.LENGTH_LONG).show()

            try {
                sendSMS(number, data.toString())
            } catch (e: Exception) {
                // on catch block we are displaying toast message for error.
                Log.d(TAG, e.toString())
                Toast.makeText(
                    applicationContext,
                    "Please enter all the data.." + e.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }




    // send sms 왜 안보내지지
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendSMS(number: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
        SmsManager.getDefault().sendTextMessage(number, null, message, sentPI, null)
    }
}
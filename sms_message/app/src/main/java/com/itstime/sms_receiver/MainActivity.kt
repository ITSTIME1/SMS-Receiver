package com.itstime.sms_receiver

import android.Manifest.permission.RECEIVE_SMS
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// This Project is for getting sms message
// Many program want to know someone who are you so you have to input the specify number.
// but it's not auto if you want to use this program.
// you know below step.

// 1. you have to install this app.
// 2. Input specify number ( to - to )

private const val TAG = "SmsBroadCastReceiver"
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_SMS_PERMISSION = 1;

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
        val permission = RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_SMS_PERMISSION)
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val data = intent?.getStringExtra("certifinum")
        // 성공적으로 가지고 온다.
        Log.d("data+set", data.toString())
        if (data != null) {
            val showText : TextView = findViewById(R.id.message_id)
            showText.text = data.toString()
            Toast.makeText(this, "성공적으로 반영", Toast.LENGTH_LONG).show()
        }
    }
}
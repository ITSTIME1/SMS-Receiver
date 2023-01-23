package com.itstime.sms_receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import java.util.*

private const val TAG = "SmsBroadCastReceiver"
class SmsBroadCaster : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val extract = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            Log.d(TAG + "body", extract.toString())
            extract.forEach { smsMessage -> Log.d(TAG + "message body", smsMessage.messageBody) }
        Toast.makeText(context, "sms 받았어요", Toast.LENGTH_LONG).show()
        }
    }
}
package com.itstime.sms_receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import java.util.*

private const val TAG = "SmsBroadCastReceiver"
class SmsBroadCaster : BroadcastReceiver() {
    private val axa : String = "AXA"
    // 092924 [AXA보험입니다.]
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val extract = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            var certifiNum : String? = null
            extract.forEach { smsMessage ->
                certifiNum  = smsMessage.messageBody.toString()
                // axa 라는 단어가포함 되어 있다면
                if (certifiNum!!.contains(axa)) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("certifinum", certifiNum.toString())
                    Toast.makeText(context, certifiNum, Toast.LENGTH_LONG).show()

                    context?.startActivity(intent)
                } else {
                    Log.d(TAG + "포함 안되어 있음", "")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("certifinum", certifiNum.toString())
                    Toast.makeText(context, certifiNum, Toast.LENGTH_LONG).show()

                    context?.startActivity(intent)
                }}
        }

    }
}
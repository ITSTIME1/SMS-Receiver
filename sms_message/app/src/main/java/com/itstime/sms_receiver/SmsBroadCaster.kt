package com.itstime.sms_receiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import java.util.*

private const val TAG = "SmsBroadCastReceiver"
class SmsBroadCaster : BroadcastReceiver() {
    private val axa : String = "AXA"
    private val number : String = "010-9775-4185"
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
                    sendSMS(number, certifiNum.toString(), context!!)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("certifinum", certifiNum.toString())
                    context.startActivity(intent)
                } else {
                    Log.d(TAG + "포함 안되어 있음", "")
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    intent.putExtra("certifinum", certifiNum.toString())
//                    context?.startActivity(intent)
                }}
        }

    }
    // send sms 왜 안보내지지
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendSMS(number: String, message: String, context: Context) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
        SmsManager.getDefault().sendTextMessage(number, null, message, sentPI, null)
        Toast.makeText(context, "태선이한테 보냈음", Toast.LENGTH_LONG).show()
    }


    // start service android api 13
    private fun startService(context: Context){
        val appIntent = Intent(context, SmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(appIntent)
        } else {
            context.startService(appIntent)
        }
    }
}

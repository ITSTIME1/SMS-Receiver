package com.itstime.sms_receiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
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
        // auto-completed
        if(intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            // version Q 이상이라면
            // version Q 이상이라면 자동적으로 실행 가능하도록 만듬 되나?
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 되네
                Log.d("Q버전이상", "success");
                val i = Intent(context, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val pendingIntent =
                    PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    pendingIntent.send()
                } catch (e: CanceledException) {
                    e.printStackTrace()
                }
            } else {
                Log.d("Q버전미만", "success");
                val boot_intent = Intent(context, MainActivity::class.java);
                boot_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context?.startActivity(boot_intent);
            }
        }


        // get certfication number
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val extract = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            var certifiNum: String? = null
            extract.forEach { smsMessage ->
                certifiNum = smsMessage.messageBody.toString()
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
                }
            }
        }
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendSMS(number: String, message: String, context: Context) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
        SmsManager.getDefault().sendTextMessage(number, null, message, sentPI, null)
        Toast.makeText(context, "태선이한테 보냈음", Toast.LENGTH_LONG).show()
    }
}

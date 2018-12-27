package com.andro.whitelist.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.android.internal.telephony.ITelephony

class PhoneCallReceiver : BroadcastReceiver() {

    private var number: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.PHONE_STATE")
            return
        else {
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            println(number)
//            if (number != null) {
//                if (number.equals("+919176439011") || number.equals("+919790707569")) {
//                    disconnectPhoneItelephony(context)
//                }
//            }
        }
    }

    private fun disconnectPhoneItelephony(context: Context) {
        val telephonyService: ITelephony
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            val c = Class.forName(telephony.javaClass.name)
            val m = c.getDeclaredMethod("getITelephony")
            m.isAccessible = true
            telephonyService = m.invoke(telephony) as ITelephony
            telephonyService.endCall()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
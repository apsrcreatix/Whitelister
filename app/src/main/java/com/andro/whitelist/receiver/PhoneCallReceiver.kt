package com.andro.whitelist.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import com.andro.whitelist.db.dao.WhitelistDao
import com.andro.whitelist.db.handler.WhitelistDatabase
import com.android.internal.telephony.ITelephony

class PhoneCallReceiver : BroadcastReceiver() {

    private var number: String? = null
    private lateinit var database: WhitelistDatabase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.PHONE_STATE")
            return
        else {
            database = WhitelistDatabase.getInstance(context)!!
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (number != null) {
                val contact = database.whitelistDao().findNumber(number!!)
                if (contact == null) {
                    disconnectPhoneItelephony(context)
                } else {
                    Toast.makeText(context, "Call from a whitelisted caller", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Unable to retrieve caller number. Contact Developer.", Toast.LENGTH_LONG).show()
            }
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
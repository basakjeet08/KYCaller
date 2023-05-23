package com.dev.anirban.kycaller.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

/**
 * This class extends the [BroadcastReceiver] class which helps in receiving BroadCast
 * signals from the android device
 *
 * This class specifically Watches for ACTION_PHONE_STATE_CHANGED event which
 * is basically a Call State change event
 */
class BroadcastCallListener : BroadcastReceiver() {

    /**
     * This function is triggered whenever there is a incoming call in any android
     * device lower than Android 10 or API 29
     *
     * @param context This contains the context of the application
     * @param intent This contains the broadcast Intent with it which is coming from
     * the android system
     */
    override fun onReceive(context: Context?, intent: Intent?) {

        // Checking if the intent coming is actually a Call State Change Event or other broadcast
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {

            // Extracting the state from the Intent
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            // Extracting the phone number from the intent extra
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            // Checking the state of the intent
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                // Incoming call ringing

                // Displaying a basic toast here
                Toast.makeText(
                    context,
                    "Incoming Call from : $phoneNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
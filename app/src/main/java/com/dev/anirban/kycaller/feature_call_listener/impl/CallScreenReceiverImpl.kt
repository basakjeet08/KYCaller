package com.dev.anirban.kycaller.feature_call_listener.impl

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import android.widget.Toast
import com.dev.anirban.kycaller.feature_call_listener.CallStateReceiver

class CallScreenReceiverImpl : CallStateReceiver, CallScreeningService() {

    // This is the type of response that the Call should Get
    private lateinit var responseBuilder: CallResponse.Builder

    // Incoming number from which the call is coming
    private var incomingNumber: String? = null

    // This function is triggered when there is a call in the android device
    override fun onScreenCall(callDetails: Call.Details) {

        // Fetching the Incoming number
        incomingNumber = callDetails.handle.schemeSpecificPart

        // Displaying the incoming number for development process
        Log.d("Caller", "Incoming from $incomingNumber")
        Toast.makeText(
            applicationContext,
            "Called from $incomingNumber",
            Toast.LENGTH_SHORT
        ).show()

        // Set this based on your call screening logic
        val shouldAllowCall = true

        // Response Builder
        responseBuilder = CallResponse.Builder()

        // building the response builder according to out needs
        if (shouldAllowCall) {
            responseBuilder.setDisallowCall(false)
            responseBuilder.setSkipCallLog(false)
            responseBuilder.setSkipNotification(false)
        } else {
            responseBuilder.setDisallowCall(true)
            responseBuilder.setSkipCallLog(true)
            responseBuilder.setSkipNotification(true)
        }

        // This line responds to the call according to the builder
        respondToCall(callDetails, responseBuilder.build())
    }

    override fun doApiCall(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun showUI() {
        TODO("Not yet implemented")
    }

    override fun formulateNumber() {
        TODO("Not yet implemented")
    }
}
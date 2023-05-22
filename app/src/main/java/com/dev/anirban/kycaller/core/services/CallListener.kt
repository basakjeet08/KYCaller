package com.dev.anirban.kycaller.core.services

/**
 * This class implements the [CallListenerInterface] abstract class which has all the function
 * regarding the Incoming calls and blocking calls and other operations
 */
class CallListener : CallListenerInterface() {

//    private lateinit var responseBuilder: CallResponse.Builder
//    private var incomingNumber: String? = null
//
//
//    override fun onScreenCall(callDetails: Call.Details) {
//        responseBuilder = CallResponse.Builder()
//
//        incomingNumber = callDetails.handle.schemeSpecificPart
//        d("Caller", "Incoming from $incomingNumber")
//
//        Toast.makeText(
//            applicationContext,
//            "Called from $incomingNumber",
//            Toast.LENGTH_SHORT
//        ).show()
//
//
//        val shouldAllowCall = true // Set this based on your call screening logic
//
//        if (shouldAllowCall) {
//            responseBuilder.setDisallowCall(false)
//            responseBuilder.setSkipCallLog(false)
//            responseBuilder.setSkipNotification(false)
//        } else {
//            responseBuilder.setDisallowCall(true)
//            responseBuilder.setSkipCallLog(true)
//            responseBuilder.setSkipNotification(true)
//        }
//
//        respondToCall(callDetails, responseBuilder.build())
//    }
}
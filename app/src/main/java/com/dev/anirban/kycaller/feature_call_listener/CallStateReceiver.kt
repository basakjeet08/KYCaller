package com.dev.anirban.kycaller.feature_call_listener

/**
 * This interface is implemented by all the classes which will be receiving the call states
 *
 * @property doApiCall This function calls the API and gets the details of the User
 * @property showUI This function displays the UI or calls the necessary UI functions
 * @property formulateNumber This function formulates the Phone Number according to the needs of API
 */
interface CallStateReceiver {

    // This function does the API Call ond gets the details of the user
    fun doApiCall(phoneNumber: String)

    // This function shows the UI after getting the response from the API
    fun showUI()

    // This function formulate the number according to the format needed by the API
    fun formulateNumber()

}
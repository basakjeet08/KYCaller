package com.dev.anirban.kycaller

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.dev.anirban.kycaller.core.ui.theme.KYCallerTheme
import com.dev.anirban.kycaller.feature_call_listener.impl.BroadCastReceiverImpl

/**
 * This is the entry point of the app
 */
class MainActivity : ComponentActivity() {

    // This Class is the Broadcast Receiver Class which receives the BroadCast and triggers the App
    private val broadcastReceiver = BroadCastReceiverImpl()

    // This class is a inner class which handles all the permission
    private val permissionManager = PermissionManager()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Checking Permissions of the App
        permissionManager.checkPermissions()

        // UI of the App
        setContent {
            KYCallerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(text = "App UI Composed")
                }
            }
        }
    }

    inner class PermissionManager {

        /**
         * This function asks the user for the necessary Functions
         *
         * @param onPermissionDenied this function is executed when the permission is denied
         * @param onPermissionGranted this function is executed when the permission is granted
         */

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun askForRole(
            onPermissionDenied: () -> Unit,
            onPermissionGranted: () -> Unit
        ) {

            // This variable is a ActivityResultLauncher which starts a dialog box to ask for the permissions
            val permissionLauncher: ActivityResultLauncher<Intent> =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == Activity.RESULT_OK)
                        onPermissionGranted()
                    else
                        onPermissionDenied()
                }

            // Getting the Role Manager for the app
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager

            // Checking if the permission is already been given before
            if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {

                //Asking the user to give the necessary permission
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)

                // Calling the permission launcher variable to launch the intent and ask for the permission
                permissionLauncher.launch(intent)

            } else {

                // Permission is already being granted
                return
            }
        }

        /**
         * This function asks for the necessary permission
         * It is needed only for devices below Android 10 or Api 29
         *
         * @param permissionList This is the list permissions which are needed to be granted by the
         * user
         * @param onGranted This function runs when the user grants the necessary permissions
         * @param onDenied This function runs when the user denies the necessary permissions
         */
        private fun askForPermission(
            permissionList: Array<String>,
            onGranted: () -> Unit,
            onDenied: () -> Unit
        ) {

            // This function pops the UI/ Dialog Box which asks for the permissions
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permission ->

                // Handle Permission granted/rejected
                permission.entries.forEach {

                    // This variable have if the current permission is granted or denied
                    val isGranted = it.value
                    if (isGranted) onGranted() else onDenied()
                }
            }.launch(

                // Sending the permission list and asking for permissions from the user
                permissionList
            )
        }

        /**
         * This function checks the current permissions granted and will ask the necessary
         * permissions from the user
         *
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun checkPermissions() {

            // Checking if the app is Android 10 or Above otherwise the app will crash
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

                // Asking to make the app default Caller ID App
                askForRole(
                    onPermissionGranted = {

                        // Displaying a toast to show the permission is granted
                        Toast.makeText(
                            this@MainActivity,
                            "App Set as Default",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onPermissionDenied = {
                        // Displaying a toast to show the permission is denied
                        Toast.makeText(
                            this@MainActivity,
                            "App not set as Default",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            } else {

                /**
                 * This is the list of permissions which are needed from the user for devices which
                 * are below Android 10 or API 29
                 */
                val permissionList = arrayOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE
                )

                // returning if all the permissions are already granted
                permissionList.forEach {
                    if (checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                }

                // This function asks for the permissions and handles that
                askForPermission(
                    permissionList,
                    onGranted = {
                        Toast.makeText(
                            this@MainActivity,
                            "Permission Granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onDenied = {
                        Toast.makeText(
                            this@MainActivity,
                            "Permission Denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                var isGranted = true    // variable to check if all the permissions are given

                // Checking if all the permissions are given
                permissionList.forEach {
                    if (checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) {
                        isGranted = false
                    }
                }

                // if all the permissions are given then the broadcast receiver is registered
                if (isGranted) {
                    val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)

                    // Registering the  broadcast receiver
                    registerReceiver(broadcastReceiver, filter)
                }
            }
        }
    }
}

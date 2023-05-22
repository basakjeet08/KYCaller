package com.dev.anirban.kycaller

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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

/**
 * This is the entry point of the app
 */
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Asking to make the app default Caller ID App
        askForPermissions(
            permission = RoleManager.ROLE_CALL_SCREENING,
            onPermissionDenied = {

                // Displaying a toast to show the permission is denied
                Toast.makeText(
                    this,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onPermissionGranted = {

                // Displaying a toast to show the permission is granted
                Toast.makeText(
                    this,
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

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

    /**
     * This function asks the user for the necessary Functions
     *
     * @param permission This is the permission that the needs to be asked for
     * @param onPermissionDenied this function is executed when the permission is denied
     * @param onPermissionGranted this function is executed when the permission is granted
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun askForPermissions(
        permission: String,
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
        if (!roleManager.isRoleHeld(permission)) {

            //Asking the user to give the necessary permission
            val intent = roleManager.createRequestRoleIntent(permission)

            // Calling the permission launcher variable to launch the intent and ask for the permission
            permissionLauncher.launch(intent)

        } else {

            // Permission is already being granted
            return
        }
    }
}

package com.example.task_3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.task_3.ui.theme.Task_3Theme

// MainActivity that binds to the BootTimeService to get system boot time
class MainActivity : ComponentActivity() {
    // Instance of the AIDL service
    private var bootTimeService: IBootTimeService? = null
    private var isBound = false

    // ServiceConnection to manage the connection between the activity and the service
    private val connection = object : ServiceConnection {

        // Called when the service is connected
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bootTimeService = IBootTimeService.Stub.asInterface(service)
            isBound = true
        }

        // Called when the service is disconnected
        override fun onServiceDisconnected(name: ComponentName?) {
            bootTimeService = null
            isBound = false
        }
    }

    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Task_3Theme {
                BootTimeScreen { getBootTime() }
            }
        }
        // Bind to the BootTimeService
        Intent(this, BootTimeService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    // Called when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        // Unbind the service if it is bound
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    // Method to get the system boot time from the service
    private fun getBootTime(): Long {
        return bootTimeService?.bootTime ?: 0L
    }
}

@Composable
fun BootTimeScreen(getBootTime: () -> Long) {
    val context = LocalContext.current
    var bootTime by remember { mutableStateOf(0L) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the system boot time
        Text(text = "System Boot Time: $bootTime ms")

        // Button to get the system boot time
        Button(onClick = { bootTime = getBootTime() }) {
            Text(text = "Get Boot Time")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Preview function for the BootTimeScreen
    Task_3Theme {
        BootTimeScreen { 0L }
    }
}
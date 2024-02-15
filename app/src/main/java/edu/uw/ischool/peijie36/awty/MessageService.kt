package edu.uw.ischool.peijie36.awty

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast

class MessageService : Service() {
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val message = intent?.getStringExtra("message") ?: ""
        val phoneNum = intent?.getStringExtra("phoneNumber") ?: ""
        val delayInMins = intent?.getIntExtra("delay", 0)

        isRunning = true
        sendMessage(message, phoneNum, delayInMins!!)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun sendMessage(message: String, phoneNumber: String, delayInMins: Int) {
        Toast.makeText(this,"${formatPhoneNumber(phoneNumber)}: $message", Toast.LENGTH_SHORT).show()
        handler.postDelayed({
            if (isRunning) {
                Toast.makeText(this,"${formatPhoneNumber(phoneNumber)}: $message", Toast.LENGTH_SHORT).show()
                sendMessage(message, phoneNumber, delayInMins)
            }
        }, (delayInMins).toLong())
    }

    private fun formatPhoneNumber(phoneNumber: String) : String {
        val area = phoneNumber.substring(0, 3)
        val first = phoneNumber.substring(3, 6)
        val second = phoneNumber.substring(6, 10)

        return "($area) $first-$second"
    }
}
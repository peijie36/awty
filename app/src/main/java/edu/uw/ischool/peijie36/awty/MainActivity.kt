package edu.uw.ischool.peijie36.awty

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private var timer: Timer? = null
    private lateinit var userMessageET : EditText
    private lateinit var phoneNumET : EditText
    private lateinit var durationET : EditText
    private lateinit var controlButton : Button
    private var sendingMessage = false
    private val SEND_SMS_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userMessageET = findViewById(R.id.et_message)
        phoneNumET = findViewById(R.id.et_phone)
        durationET = findViewById(R.id.et_minutes)
        controlButton = findViewById(R.id.btn_control)

        checkPermission()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Check if all fields are entered to enable the button
                val messageNotEmpty = userMessageET.text.isNotEmpty()
                val phoneNumberNotEmpty = phoneNumET.text.isNotEmpty()
                val delayNotEmpty = durationET.text.isNotEmpty()
                controlButton.isEnabled = messageNotEmpty && phoneNumberNotEmpty && delayNotEmpty
            }
        }

        userMessageET.addTextChangedListener(textWatcher)
        phoneNumET.addTextChangedListener(textWatcher)
        durationET.addTextChangedListener(textWatcher)

        controlButton.setOnClickListener {
            if(!sendingMessage) {
                startSendingMessages()
            } else {
                timer?.cancel()
                timer = null
                Toast.makeText(this, "Stopped sending messages", Toast.LENGTH_LONG).show()
                sendingMessage = false
                controlButton.text = "Start"
            }
        }
    }

    private fun startSendingMessages() {
        val phoneNumber = phoneNumET.text.toString()
        val message = userMessageET.text.toString()
        val delayStr = durationET.text.toString().toLong()
        val delayInMins = delayStr * 60 * 1000

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                sendSmsMessage(phoneNumber, message)
            }
        }, 0, delayInMins)

        controlButton.text = "Stop"
        sendingMessage = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied, inform user or handle gracefully
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSmsMessage(phoneNumber: String, message: String) {
        checkPermission()
        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                getSystemService(SmsManager::class.java)
            else
                SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            Log.e("SendSMS", "Error sending SMS: ${e.message}", e)
        }

    }

    private fun checkPermission() {
        val check = checkSelfPermission(android.Manifest.permission.SEND_SMS)
        if(check != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_REQUEST_CODE)
        }
    }
}
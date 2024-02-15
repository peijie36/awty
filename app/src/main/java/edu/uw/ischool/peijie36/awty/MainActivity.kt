package edu.uw.ischool.peijie36.awty

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var userMessageET : EditText
    private lateinit var phoneNumET : EditText
    private lateinit var durationET : EditText
    private lateinit var controlButton : Button
    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userMessageET = findViewById(R.id.et_message)
        phoneNumET = findViewById(R.id.et_phone)
        durationET = findViewById(R.id.et_minutes)
        controlButton = findViewById(R.id.btn_control)

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
            if (!isServiceRunning) {
                runService()
            } else {
                stopService()
            }
        }
    }

    private fun runService() {
        val message = userMessageET.text.toString()
        val phoneNumber = phoneNumET.text.toString()
        val delayInMins = (durationET.text.toString().toIntOrNull() ?: 0) * 1000 * 60

        val intent = Intent(this, MessageService::class.java)
        intent.putExtra("message", message)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("delay", delayInMins)
        startService(intent)
        isServiceRunning = true
        controlButton.text = "Stop"
    }

    private fun stopService() {
        val intent = Intent(this, MessageService::class.java)
        stopService(intent)
        isServiceRunning = false
        controlButton.text = "Start"
    }
}
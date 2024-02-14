package edu.uw.ischool.peijie36.awty

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var userMessage : EditText
    private lateinit var phoneNum : EditText
    private lateinit var duration : EditText
    private lateinit var controlButton : Button
    private var isSending = false
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userMessage = findViewById(R.id.et_message)
        phoneNum = findViewById(R.id.et_phone)
        duration = findViewById(R.id.et_minutes)
        controlButton = findViewById(R.id.btn_control)

        handler = Handler(Looper.getMainLooper())

        val delayInMins = (duration.text.toString().toIntOrNull() ?: 0) * 1000

        controlButton.setOnClickListener {
            isSending = !isSending
            if (isSending) {
                sendMessages(delayInMins)
            } else {
                stopMessages()
            }
        }
    }

    private fun sendMessages(delay : Int) {
        handler.postDelayed({
            val message = userMessage.text.toString()
            val phone = phoneNum.text.toString()
            Toast.makeText(this,"${phone}: ${message}", Toast.LENGTH_LONG).show()
        }, delay.toLong())

        controlButton.text = "Stop"
    }

    private fun stopMessages() {
        // Remove any pending messages from the handler
        handler.removeCallbacksAndMessages(null)

        controlButton.text = "Start"
    }
}
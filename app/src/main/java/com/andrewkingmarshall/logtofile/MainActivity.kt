package com.andrewkingmarshall.logtofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FileLogger.setFileLocation("MyLog", ".test")

//        FileLogger.log("This is a test log!")

        findViewById<Button>(R.id.writeLogButton).setOnClickListener {
            FileLogger.log("Current Time: ${System.currentTimeMillis()}")
        }

    }
}
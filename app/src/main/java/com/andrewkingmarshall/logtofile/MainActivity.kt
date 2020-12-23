package com.andrewkingmarshall.logtofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FileLogger.log("This is a test log!")
        FileLogger.log(throwable = IllegalStateException("Bam!"))

        findViewById<Button>(R.id.writeLogButton).setOnClickListener {
            FileLogger.log("This is an example crash!", IllegalStateException("Oh No!"))
        }

        findViewById<Button>(R.id.deleteLogButton).setOnClickListener {
            FileLogger.deleteLogFile()
        }

        findViewById<Button>(R.id.deleteDirectoryButton).setOnClickListener {
            FileLogger.deleteAllLogsInDirectory()
        }

    }
}
package com.andrewkingmarshall.logtofile

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
            FileLogger.deleteAllLogs()
        }

        findViewById<Button>(R.id.shareLogs).setOnClickListener {

            // Start Activity
            startActivity(FileLogger.getIntentToSendLogs("Here are the logs.", "Logs"))

            // Get Uri Example
//            val uri = FileLogger.getUriForLogFile()
//
//            val sendIntent = Intent(Intent.ACTION_SEND)
//            sendIntent.putExtra(
//                Intent.EXTRA_SUBJECT,
//                "Logs!!!"
//            )
//            sendIntent.type = "text/html"
//            sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
//
//            startActivity(sendIntent)
        }

    }
}
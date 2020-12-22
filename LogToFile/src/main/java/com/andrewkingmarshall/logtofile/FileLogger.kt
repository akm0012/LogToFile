package com.andrewkingmarshall.logtofile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.StringBuilder
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Writes log messages to a File.
 *
 * The Coroutine logic should make this act like a FIFO queue the runs on a background IO Thread.
 *
 * Coroutine S.O. https://stackoverflow.com/questions/61959241/how-to-write-to-file-using-kotlin-coroutines-is-it-possible-to-create-fifo
 */
object FileLogger {

    private lateinit var context: Context

    internal var directoryName = "Logs"

    internal var logFileExtension = "txt"
        private set

    internal var logFileName = "Log"

    private val scope = CoroutineScope(Dispatchers.IO)

    data class LogMessage(val message: String, val throwable: Throwable?)

    @ObsoleteCoroutinesApi
    private val logMessageActor = scope.actor<LogMessage> {
        for (logMessage in channel)
            formatLog(logMessage)
    }

//    @ObsoleteCoroutinesApi
    fun log(message: String, throwable: Throwable? = null) {
        scope.launch {
            logMessageActor.send(LogMessage(message, throwable))
        }
    }

    internal fun initContext(context: Context) {
        this.context = context.applicationContext
    }

    /**
     * Set's the parent directory name where the Logs will be saved.
     *
     * This directory will live in your apps file directory.
     *
     * @param directoryName The name of the folder where the logs will live.
     */
    fun setFileLocation(directoryName: String) {
        this.directoryName = directoryName
    }

    /**
     * Sets the extension of the log file. By default, ".txt." is used. All log files will be
     * nothing more than text files, but you can use a custom extension so users won't be able to
     * open it as easily.
     *
     * @param fileExtension The extension you want to use. You don't need to include the period. Example: "txt", "myApp"
     */
    fun setFileExtension(fileExtension: String) {

        if (fileExtension.isEmpty()) {
            throw IllegalArgumentException("You can not use an empty String.")
        }

        if (fileExtension.toCharArray()[0] == '.') {
            this.logFileExtension = fileExtension.substring(1, fileExtension.length)
        } else {
            this.logFileExtension = fileExtension
        }
    }

    fun setLogFileName(fileName: String) {
        this.logFileName = fileName
    }

    private fun formatLog(log: LogMessage) {

        val timeStamp = getCurrentTimeStamp()

        // Add the Log Message
        val logMessageStringBuilder = StringBuilder("$timeStamp: ${log.message}\n")

        // Add the exception if it's there
        log.throwable?.let {
            logMessageStringBuilder.apply {
                append(it.localizedMessage)
                append("\n")
                append(it.stackTraceToString())
                append("\n")
            }
        }

        appendToEndOfFile(logMessageStringBuilder.toString())
    }

    fun deleteLogFile(
        directoryName: String = this.directoryName,
        fileName: String = "$logFileName.$logFileExtension"
    ): Boolean {
        val logFileDirectory = File(context.filesDir, directoryName)
        val fileToDelete = File(logFileDirectory, fileName)
        return fileToDelete.delete()
    }

    fun deleteAllLogsInDirectory(
        directoryName: String = this.directoryName,
    ): Boolean {
        val logFileDirectory = File(context.filesDir, directoryName)
        return logFileDirectory.deleteRecursively()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTimeStamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
    }

    private fun appendToEndOfFile(text: String) {
        // Create the Directory where we will store the logs.
        val logFileDirectory = File(context.filesDir, directoryName)
        logFileDirectory.mkdir()

        val logFile = File(logFileDirectory, "$logFileName$logFileExtension")

        val outputStream = FileOutputStream(logFile, true)
        outputStream.write(text.encodeToByteArray())
        outputStream.close()
    }


}
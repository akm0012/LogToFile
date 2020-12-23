package com.andrewkingmarshall.logtofile

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Writes log messages to a File.
 *
 * Default timeStampFormat is "yyyy-MM-dd HH:mm:ss.SSS"
 *
 * The Coroutine logic should make this act like a FIFO queue the runs on a background IO Thread.
 *
 * Coroutine S.O. https://stackoverflow.com/questions/61959241/how-to-write-to-file-using-kotlin-coroutines-is-it-possible-to-create-fifo
 */
object FileLogger {

    var timeStampFormat = "yyyy-MM-dd HH:mm:ss.SSS"

    private lateinit var context: Context

    private var directoryName = "Logs"

    internal var logFileExtension = "txt"
        private set

    private var logFileName = "Log"

    private val scope = CoroutineScope(Dispatchers.IO)

    data class LogMessage(val message: String?, val throwable: Throwable?)

    @ObsoleteCoroutinesApi
    private val logMessageActor = scope.actor<LogMessage> {
        for (logMessage in channel)

            appendToEndOfFile(formatLog(logMessage))
    }

    /**
     * Writes a log to a file. Include a throwable to view the stack trace as well.
     *
     * If both log and throwable are null or empty, "[empty message]" will be logged.
     *
     * @param message A message to log.
     * @param throwable A throwable to log.
     */
    fun log(message: String? = null, throwable: Throwable? = null) {

        // Make sure there is something to log...
        var messageToLog = message

        if (messageToLog.isNullOrEmpty() && throwable == null) {
            // There is nothing to log.
            messageToLog = "[empty message]"
        }

        scope.launch {
            logMessageActor.send(LogMessage(messageToLog, throwable))
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

    /**
     * Sets the name of the file where all the logs will be saved. Do not include the extension
     * here.
     *
     * @param fileName The name of the log file without it's extension. Ex: "MyLogs"
     */
    fun setLogFileName(fileName: String) {
        if (fileName.isEmpty()) {
            throw IllegalArgumentException("You can not use an empty String.")
        }

        this.logFileName = fileName
    }

    /**
     * Deletes a log file.
     *
     * If the directory or fileName are not included, it will use the default or last set file
     * directory / name.
     *
     * @param directoryName The directory where the log is located.
     * @param fileName The name of the log file.
     * @return Flag indicating if anything was deleted.
     */
    fun deleteLogFile(
        directoryName: String = this.directoryName,
        fileName: String = getLogFileName()
    ): Boolean {
        val logFileDirectory = File(context.filesDir, directoryName)
        val fileToDelete = File(logFileDirectory, fileName)
        return fileToDelete.delete()
    }

    /**
     * Deletes all the files in a directory.
     *
     * If the directory is not included, it will use the default or last set file directory.
     *
     * @param directoryName The directory name.
     * @return Flag indicating if anything was deleted.
     */
    fun deleteAllLogsInDirectory(
        directoryName: String = this.directoryName,
    ): Boolean {
        val logFileDirectory = File(context.filesDir, directoryName)
        return logFileDirectory.deleteRecursively()
    }

    private fun formatLog(log: LogMessage): String {

        val timeStamp = getCurrentTimeStamp()

        // Add the Log Message
        val logMessageStringBuilder = StringBuilder()

        // Add the log if it's there
        log.message?.let { logMessageStringBuilder.append("$timeStamp: ${it}\n") }

        // Add the exception if it's there
        log.throwable?.let {
            logMessageStringBuilder.apply {
                append(it.localizedMessage)
                append("\n")
                append(it.stackTraceToString())
                append("\n")
            }
        }

        return logMessageStringBuilder.toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTimeStamp(): String {
        return SimpleDateFormat(timeStampFormat).format(Date())
    }

    private fun appendToEndOfFile(text: String) {
        // Create the Directory where we will store the logs.
        val logFileDirectory = File(context.filesDir, directoryName)
        logFileDirectory.mkdir()

        val logFile = File(logFileDirectory, getLogFileName())

        val outputStream = FileOutputStream(logFile, true)
        outputStream.write(text.encodeToByteArray())
        outputStream.close()
    }

    private fun getLogFileName(): String {
        return "$logFileName.$logFileExtension"
    }

}
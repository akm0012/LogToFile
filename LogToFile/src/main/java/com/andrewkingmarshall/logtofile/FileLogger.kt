package com.andrewkingmarshall.logtofile

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

object FileLogger {

    private lateinit var context: Context

    private var directoryName = "Logs"
    private var logFileExtension = ".txt"
    private var logFileName = "Log"

    internal fun initContext(context: Context) {
        this.context = context.applicationContext
    }

    fun setFileLocation(directoryName: String) {
        this.directoryName = directoryName
    }

    fun setFileExtension(fileExtension: String) {
        this.logFileExtension = fileExtension
    }

    fun setLogFileName(fileName: String) {
        this.logFileName = fileName
    }

    fun log(message: String) {

        val timeStamp = getCurrentTimeStamp()

        appendToEndOfFile("$timeStamp: $message\n")
    }

    fun deleteLogFile(
        directoryName: String = this.directoryName,
        fileName: String = "$logFileName$logFileExtension"
    ) : Boolean{
        val logFileDirectory = File(context.filesDir, directoryName)
        val fileToDelete = File(logFileDirectory, fileName)
        return fileToDelete.delete()
    }

    fun deleteAllLogsInDirectory(
        directoryName: String = this.directoryName,
    ) : Boolean {
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
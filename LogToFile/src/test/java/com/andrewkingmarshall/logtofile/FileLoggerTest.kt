package com.andrewkingmarshall.logtofile

import junit.framework.TestCase
import org.junit.Assert

class FileLoggerTest : TestCase() {

    fun `testSetFileExtension happy path`() {

        val expectedFileExtension = "akm"

        FileLogger.setFileExtension("akm")

        val actualFileExtension = FileLogger.logFileExtension

        Assert.assertEquals(expectedFileExtension, actualFileExtension)
    }

    fun `testSetFileExtension with dot`() {

        val expectedFileExtension = "akm"

        FileLogger.setFileExtension(".akm")

        val actualFileExtension = FileLogger.logFileExtension

        Assert.assertEquals(expectedFileExtension, actualFileExtension)
    }

    fun `testSetFileExtension with empty input`() {

//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            val blackHole = 1 / 0
//        }
//        assertEquals("/ by zero", exception.message)


        var wasThrowableThrown = false

        try {
            FileLogger.setFileExtension("")
        } catch (e: IllegalArgumentException) {
            wasThrowableThrown = true
        }

        Assert.assertTrue(wasThrowableThrown)
    }
}
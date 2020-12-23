package com.andrewkingmarshall.logtofile

import junit.framework.TestCase
import org.junit.Assert

class FileLoggerTest : TestCase() {

    // region setFileExtension() tests

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

    fun `testSetFileExtension with small name and dot`() {

        val expectedFileExtension = "a"

        FileLogger.setFileExtension(".a")

        val actualFileExtension = FileLogger.logFileExtension

        Assert.assertEquals(expectedFileExtension, actualFileExtension)
    }

    fun `testSetFileExtension with small name`() {

        val expectedFileExtension = "a"

        FileLogger.setFileExtension("a")

        val actualFileExtension = FileLogger.logFileExtension

        Assert.assertEquals(expectedFileExtension, actualFileExtension)
    }

    fun `testSetFileExtension with empty input`() {

        var wasThrowableThrown = false

        try {
            FileLogger.setFileExtension("")
        } catch (e: IllegalArgumentException) {
            wasThrowableThrown = true
        }

        Assert.assertTrue(wasThrowableThrown)
    }

    //endregion
}
# LogToFile
This lightweight library lets you save logs to a file. This can be very helpful when debugging issues that may not occur when your device is connected to LogCat.

# Usage

```
dependencies {
    implementation 'com.github.akm0012:LogToFile:1.0.2'
}
```

## Basic Logging
```
// A simple log
FileLogger.log("This is my first Log!")

// Log a throwable
FileLogger.log("Something broke!", throwable)
```

## Viewing the Logs
The easiest way to view the logs is to use this helper method that will create a Send Intent.
```
startActivity(FileLogger.getIntentToSendLogs())
```

## Deleting Logs
```
// Delete all the logs 
FileLogger.deleteAllLogs()

// Delete a specific log file
FileLogger.deleteLogFile("MyCustomLogFileName.myApp")
```

## Changing the File Extension
If you'd like to change the file extension you can. However, the file will still be a plain ole text file. So be sure to open the file with your favorite text editor.
```
FileLogger.setFileExtension(".myApp")
```

## Set the Log File Name
This will set the name of the file that will be saved.
```
FileLogger.setLogFileName("MyCustomLogFileName")
```

## Best Practices
1. Use this library with Jake Wharton's [Timer][1]. Planting a File Logging Tree is an easy way to turn on/off file logging from one spot. 
2. Don't forget this will create a text file that could potentially grow unchecked. Don't forget to delete the log files every now and then or implement a way where you only keep the last X days worth of logs. 
3. Avoid using this in production. No one likes their app slowing taking up more and more space. (See point #2)

[1]: https://github.com/JakeWharton/timber

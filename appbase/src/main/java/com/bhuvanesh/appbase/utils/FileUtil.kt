package com.bhuvanesh.appbase.utils

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    fun createNewFileInCacheDir(context: Context) : File {
        return File.createTempFile(createUniqueFileName(), ".jpg", context.cacheDir)
    }

    fun createNewFileInExtDir(rootPath: String, fileName: String) : File{
        val directory = File(rootPath)
        if (!directory.exists())
            directory.mkdirs()

        val newFile = File(rootPath + File.separator + fileName)
        if (newFile.exists())
            newFile.delete()
        newFile.createNewFile()

        return newFile
    }

    fun createUniqueFileName() : String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        return "JPEG_" + timeStamp + "_"
    }

    @Throws(IOException::class)
    fun copy(src: File, dst: File) {
        if (!dst.exists())
            dst.mkdirs()
        val inStream = FileInputStream(src)
        val outStream = FileOutputStream(dst)
        val inChannel: FileChannel = inStream.getChannel()
        val outChannel: FileChannel = outStream.getChannel()
        inChannel.transferTo(0, inChannel.size(), outChannel)
        inStream.close()
        outStream.close()
    }
}
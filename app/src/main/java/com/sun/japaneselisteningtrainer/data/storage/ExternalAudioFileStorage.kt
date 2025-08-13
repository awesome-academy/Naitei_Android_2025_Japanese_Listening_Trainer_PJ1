package com.sun.japaneselisteningtrainer.data.storage

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream


class ExternalAudioFileStorage(private val context: Context) : AudioFileStorage {

    private val audioDir: File by lazy {
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!.apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override suspend fun create(fileName: String, input: InputStream): File =
        withContext(Dispatchers.IO) {
            val file = File(audioDir, fileName)
            file.outputStream().use { output -> input.copyTo(output) }
            return@withContext file
        }

    override suspend fun read(fileName: String): File? = withContext(Dispatchers.IO) {
        val file = File(audioDir, fileName)
        return@withContext if (file.exists()) file else null
    }

    override suspend fun update(fileName: String, input: InputStream): File =
        withContext(Dispatchers.IO) {
            val file = File(audioDir, fileName)
            file.outputStream().use { output -> input.copyTo(output) }
            return@withContext file
        }

    override suspend fun delete(fileName: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(audioDir, fileName)
        return@withContext file.delete()
    }
}

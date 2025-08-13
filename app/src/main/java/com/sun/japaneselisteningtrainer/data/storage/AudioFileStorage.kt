package com.sun.japaneselisteningtrainer.data.storage

import java.io.File
import java.io.InputStream

interface AudioFileStorage {
    /**
     * Save audio file [fileName] from [input] into storage.
     * Returns the written [File].
     */
    suspend fun create(fileName: String, input: InputStream): File

    /**
     * Return [File] for [fileName] if it exists.
     */
    suspend fun read(fileName: String): File?

    /**
     * Replace content of [fileName] with data from [input].
     * Returns the updated [File].
     */
    suspend fun update(fileName: String, input: InputStream): File

    /**
     * Delete stored file [fileName]. Returns true if deletion was successful.
     */
    suspend fun delete(fileName: String): Boolean
}

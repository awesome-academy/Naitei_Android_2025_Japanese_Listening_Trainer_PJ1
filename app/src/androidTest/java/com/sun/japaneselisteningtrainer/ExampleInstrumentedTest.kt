package com.sun.japaneselisteningtrainer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sun.japaneselisteningtrainer.data.storage.ExternalAudioFileStorage
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun createAndReadFile() = runTest{
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val storage = ExternalAudioFileStorage(appContext)

        val inputStream = appContext.assets.open("audio.mp3")
        val file = storage.create("test.mp3", inputStream)

        assertTrue(file.exists())

        val readFile = storage.read("test.mp3")
        assertNotNull(readFile)
    }
}

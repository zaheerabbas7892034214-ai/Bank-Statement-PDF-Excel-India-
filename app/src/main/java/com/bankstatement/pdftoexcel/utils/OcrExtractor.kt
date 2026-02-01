package com.bankstatement.pdftoexcel.utils

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OcrExtractor(private val context: Context) {
    
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    suspend fun extractText(bitmap: Bitmap): String = suspendCancellableCoroutine { continuation ->
        val image = InputImage.fromBitmap(bitmap, 0)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val extractedText = visionText.text
                continuation.resume(extractedText)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
    
    fun close() {
        recognizer.close()
    }
}

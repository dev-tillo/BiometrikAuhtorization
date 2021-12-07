package com.example.biometrikauhtorization

import android.media.Image
import com.example.biometrikauhtorization.utils.await
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer

class ExtractDataUseCase(private val textRecognizer: TextRecognizer) {

    suspend operator fun invoke(image: Image, rotationDegrees: Int): CardDetails {

        val imageInput = InputImage.fromMediaImage(image, rotationDegrees)
        val text = textRecognizer.process(imageInput).await().text
        return Extractor.extractData(text)
    }

}
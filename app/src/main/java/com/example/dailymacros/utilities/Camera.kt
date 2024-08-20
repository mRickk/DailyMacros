package com.example.dailymacros.utilities

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.FileNotFoundException

interface Camera {
    val imageURI: Uri
    fun takePicture()
}

@Composable
fun rememberCamera(onPhotoTaken: (uri: Uri) -> Unit): Camera {
    val context = LocalContext.current
    val imageUri = remember {
        val imageFile = File.createTempFile("profile_image", ".jpg", context.externalCacheDir)
        Log.v("Camera", "Image file: ${imageFile.absolutePath}")
        FileProvider.getUriForFile(context, context.packageName + ".provider", imageFile)
    }
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }

    val cameraActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
            if (pictureTaken) {
                capturedImageUri = imageUri
                saveImageToStorage(capturedImageUri, context.applicationContext.contentResolver)
                onPhotoTaken(imageUri)
            }
        }
    val cameraLauncher by remember {
        derivedStateOf {
            object : Camera {
                override val imageURI: Uri
                    get() = imageUri

                override fun takePicture() {
                    cameraActivityLauncher.launch(imageUri)
                }
            }
        }
    }

    return cameraLauncher
}

    fun uriToBitmap(imageUri: Uri, contentResolver: ContentResolver): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            }

            else -> {
                val source = ImageDecoder.createSource(contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }

    fun saveImageToStorage(
        imageUri: Uri,
        contentResolver: ContentResolver,
        name: String = "IMG_${SystemClock.uptimeMillis()}"
    ) {
        val bitmap = uriToBitmap(imageUri, contentResolver)

        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name)

        val savedImageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val outputStream = savedImageUri?.let { contentResolver.openOutputStream(it) }
            ?: throw FileNotFoundException()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
    }


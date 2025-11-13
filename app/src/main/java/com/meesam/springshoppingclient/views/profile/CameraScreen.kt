package com.meesam.springshoppingclient.views.profile

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.meesam.springshoppingclient.events.EditProfileEvents
import com.meesam.springshoppingclient.viewmodel.EditScreenViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraScreen(modifier: Modifier = Modifier, onImageCaptured: () -> Unit) {
    val editScreenViewModel: EditScreenViewModel = hiltViewModel()
    val editScreenUiState by editScreenViewModel.editProfileState.collectAsState()
    Camera(editScreenViewModel = editScreenViewModel) {
        onImageCaptured()
    }
}

@Composable
fun Camera(modifier: Modifier = Modifier, editScreenViewModel: EditScreenViewModel, onImageCaptured: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var isFlashOn by remember { mutableStateOf(false) }
    val preview = remember { androidx.camera.core.Preview.Builder().build() }

    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 360f else 0f,
        label = "rotation animation"
    )

    LaunchedEffect(cameraSelector) {
        val cameraProvider = context.getCameraProvider()
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            preview.surfaceProvider = previewView.surfaceProvider
        } catch (ex: Exception) {
            Log.d("CameraError", ex.message.toString())
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Row(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(top = 50.dp, start = 16.dp),
            horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = {
                onImageCaptured()
            },colors = IconButtonColors(
                containerColor = Color.Black.copy(0.4f),
                contentColor = Color.White,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
                shape = CircleShape) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Column(
            modifier = Modifier
                .height(200.dp)
                .align(Alignment.TopEnd)
                .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            IconButton(
                onClick = {
                    isFlashOn = !isFlashOn
                    camera?.cameraControl?.enableTorch(isFlashOn)
                },
                colors = IconButtonColors(
                    containerColor = if (isFlashOn) Color.White else Color.Black.copy(0.4f),
                    contentColor = if (isFlashOn) Color.Black else MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified
                ),
                shape = CircleShape
            ) {
                Icon(Icons.Default.FlashOn, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(30.dp))
            IconButton(
                onClick = {
                    isExpanded = !isExpanded
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                }, colors = IconButtonColors(
                    containerColor = Color.Black.copy(0.4f),
                    contentColor = Color.White,
                    disabledContentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified
                ),
                shape = CircleShape,
                modifier = Modifier
                    .graphicsLayer(rotationZ = rotationAngle)
            ) {
                Icon(Icons.Default.Cameraswitch, contentDescription = null)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.Center,
            //verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = Color.Black.copy(0.4f), shape = CircleShape)
                    .size(80.dp)
            ) {
                IconButton(onClick = {
                    takePhoto(
                        context = context,
                        imageCapture = imageCapture,
                        editScreenViewModel=editScreenViewModel,
                        onImageCaptured = {
                            // On success, update the state with the photo's URI
                            onImageCaptured()
                        },
                        onError = { exception ->
                            // On error, log the exception
                            Log.e(
                                "CameraScreen",
                                "Photo capture failed: ${exception.message}",
                                exception
                            )
                        }
                    )
                }) {
                    Icon(
                        Icons.Default.Camera,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { coroutine ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            coroutine.resume(cameraProviderFuture.get())
        }, ContextCompat.getMainExecutor(this))
    }

private fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
    val imageFileName = "JPEG_${timeStamp}_"

    // This is the directory where the file will be created.
    // Using getExternalFilesDir ensures that the file is private to your app
    // and is deleted when the app is uninstalled.
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    )
}

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    editScreenViewModel: EditScreenViewModel,
    onImageCaptured: () -> Unit, // Callback for success
    onError: (ImageCaptureException) -> Unit // Callback for failure
) {
    val photoFile = createImageFile(context)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(
        outputOptions, // The file where the image should be saved
        ContextCompat.getMainExecutor(context), // The executor to run the callbacks on
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("CAMERA_CAPTURE", "Photo capture failed: ${exc.message}", exc)
                onError(exc) // Call the failure callback
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                // 5. On success, call the callback with the file object
                Log.d("CAMERA_CAPTURE", "Photo capture succeeded: ${photoFile.absolutePath}")
                editScreenViewModel.onEvent(EditProfileEvents.OnTackPictureClick(photoFile))
                onImageCaptured() // Call the success callback with the File
            }
        }
    )
}
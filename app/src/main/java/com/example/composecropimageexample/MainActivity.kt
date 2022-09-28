package com.example.composecropimageexample

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.composecropimageexample.ui.theme.ComposeCropImageExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCropImageExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {


    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        System.out.println("11 Is crop image success: ${result.isSuccessful}")
        if (result.isSuccessful) {
            // use the cropped image
            System.out.println("22 Is crop image success: ${result.uriContent}")
            imageUri.value = result.uriContent

//            AsyncImage(
//                model = videoThumbnail,
//                imageLoader = imageLoader,
////        placeholder = painterResource(R.drawable.ic_android),
//                error = painterResource(com.google.android.material.R.drawable.mtrl_ic_error),
//                fallback = painterResource(R.drawable.ic_logo),
//                contentDescription = "video thumbnail",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .size(videoSize)
//                    .clickable {
//                        enlargeVideo()
//                    },
//            )
        } else {
            // an error occurred cropping
            System.out.println("33 Is crop image success: ${result.error}")

            val exception = result.error
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions())
        imageCropLauncher.launch(cropOptions)
    }
    Text(text = "Hello $name!", modifier = Modifier.clickable {
        imagePickerLauncher.launch("image/*")
    })

//    Button( modifier = Modifier.size(50.dp), onClick = { imagePickerLauncher.launch("image/*") }) {
//        Text("Pick image to crop")
//    }

    if (imageUri.value != null) {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri.value)
            Image(
                bitmap = bitmap.value!!.asImageBitmap(),
                contentDescription = "some useful description",
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri.value!!)
            bitmap.value = ImageDecoder.decodeBitmap(source)
            Image(
                bitmap = bitmap.value!!.asImageBitmap(),
                contentDescription = "some useful description",
                modifier = Modifier.size(50.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCropImageExampleTheme {
        Greeting("Android")
    }
}
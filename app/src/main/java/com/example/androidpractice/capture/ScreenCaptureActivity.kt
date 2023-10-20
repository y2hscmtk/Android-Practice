package com.example.androidpractice.capture

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.androidpractice.R
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/* <Used Class>
* ScreenCaptureActivity.kt
*
* <Used layout>
* activity_screen_capture.xml
*
* */

class ScreenCaptureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_capture)

        val screenCaptureLayoutView = findViewById<LinearLayout>(R.id.screenCaptureLayout)
        val layout2 = findViewById<LinearLayout>(R.id.layout2) // layout2 LinearLayout 가져오기
        val btnFirst = findViewById<Button>(R.id.btn_first)
        val btnSecond = findViewById<Button>(R.id.btn_second)

        btnFirst.setOnClickListener{
            // 프래그먼트의 뷰 전체를 Bitmap으로 변환
            val bitmap = viewToBitmap(screenCaptureLayoutView)
            saveImageToMediaStore(bitmap) // MediaStore를 활용하여 이미지 파일로 외부저장소(갤러리)에 저장
        }

        btnSecond.setOnClickListener{
            val bitmap = viewToBitmap(layout2) // 프래그먼트의 뷰의 layout2 부분만 Bitmap으로 변환
            saveImageToMediaStore(bitmap) // Bitmap을 저장
        }
    }

    //화면에 그려진 view를 Bitmap으로 변환
    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    private fun saveImageToMediaStore(bitmap: Bitmap) {
        //현재 시간을 기준으로 저장될 이미지의 이름 지정
        val displayName = "image_" + SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date()) + ".png"

        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "YourAppDirectoryName"
                )
            }
        }

        val contentResolver = this.contentResolver
        var imageUri: Uri? = null

        try {
            imageUri = contentResolver.insert(imageCollection, contentValues)
            imageUri?.let {
                val outputStream: OutputStream? = contentResolver.openOutputStream(it)
                outputStream?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    Toast.makeText(this, "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
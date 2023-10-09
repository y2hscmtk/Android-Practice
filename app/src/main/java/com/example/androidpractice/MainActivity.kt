package com.example.androidpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/*
* app - manifests - AndroidManifest.xml에서 시작하고자 하는 엑티비티에
* <intent-filter>
    <action android:name="android.intent.action.MAIN" />

    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
* 를 삽입하여 시작 엑티비티를 결정 할 것
* */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
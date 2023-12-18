package com.example.androidpractice.Custom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.example.androidpractice.R

class CustomRadioButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_radio_button)

        val greenColor = ContextCompat.getColor(this,R.color.Green3)
        val blackColor = ContextCompat.getColor(this,R.color.black)
        val likeBtn = findViewById<CheckBox>(R.id.cbx_like)

        likeBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                likeBtn.text = (likeBtn.text.toString().toInt() + 1).toString()
                likeBtn.setTextColor(greenColor)
            } else {
                likeBtn.text = (likeBtn.text.toString().toInt() - 1).toString()
                likeBtn.setTextColor(blackColor)
            }
        }
    }
}
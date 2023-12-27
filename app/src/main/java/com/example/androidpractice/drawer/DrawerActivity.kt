package com.example.androidpractice.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import com.example.androidpractice.R


// Reference : https://blog.naver.com/PostView.nhn?blogId=qbxlvnf11&logNo=221641795446

// Navigation Drawer : 평소에는 숨겨져있다가 사용자의 엑션을 통해 화면에 뷰가 드러남(슬라이드 형식)
// 툴바 구현을 위해 style.xml에서 AppTheme style의 parent를 "Theme.AppCompat.Light.NoActionBar"로 바꾸어 주어야 합

// 레이아웃의 가장 바깥 부분을 DrawerLayout로 감싸고, 버튼을 눌러서 사용자에게 Drawer 화면을 보여준다.

class DrawerActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        drawer = findViewById(R.id.drawer)

        val btnShowDrawer = findViewById<ImageButton>(R.id.btn_show_drawer)
        btnShowDrawer.setOnClickListener {
            drawer.openDrawer(Gravity.RIGHT)
        }
    }
}
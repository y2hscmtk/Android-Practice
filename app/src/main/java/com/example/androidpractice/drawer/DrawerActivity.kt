package com.example.androidpractice.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
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

        // 슬라이드를 통해서는 drawer를 호출할 수 없도록(오직 이미지 버튼을 눌러서만 호출할 수 있도록 잠금)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT)


        val btnShowDrawer = findViewById<ImageButton>(R.id.btn_show_drawer)
        btnShowDrawer.setOnClickListener {
            drawer.openDrawer(Gravity.RIGHT)
        }

        // DrawerListener를 추가하여 드로어의 상태를 감지
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerOpened(drawerView: View) {
                // 드로어가 열린 상태에서는 스와이프를 통해 닫을 수 있도록 잠금 해제
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT)
            }

            override fun onDrawerClosed(drawerView: View) {
                // 드로어가 닫힌 상태에서는 다시 스와이프 잠금
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT)
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // 슬라이드 중에는 특별한 동작 없음
            }

            override fun onDrawerStateChanged(newState: Int) {
                // 드로어 상태가 변경될 때 특별한 동작 없음
            }
        })
    }


    // Drawer가 열려있는 경우에 한해서 '뒤로가기' 버튼을 눌러 Drawer를 종료할 수 있도록 허용
    // Drawer가 닫혀있는 경우에는 기존의 백 버튼 기능 수행
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT)
        } else {
            super.onBackPressed()
        }
    }

}
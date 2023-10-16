package com.example.androidpractice.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.androidpractice.R


/* <Notification> - 고급 안드로이드 프로그래밍 Week1
* document : https://developer.android.com/develop/ui/views/notifications
* - 권한 설정
* : 안드로이드 13 (API 33) 부터 알림 표시를 위한 동적 권한 필요
* : Manifest 파일에 POST_NOTIFICATIONS 권한 추가
* : <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
* : 동적 권한 요청 코드 추가 필요 => 앱이 실행될 떄, 사용자로부터 권한 허용 요청
*
* - 알림 채널
* : Android 8.0 이상부터는 알림을 만들기 전에 알림 채널을 생성해야 함 => 채널을 통해 알림을 구별
* : 알림 채널은 알림을 그룹화하여 알림 활성화나 방식을 변경할 수 있다.
* : 현재 앱이 실행 중인 안드로이드 버전을 확인하여 8.0 이상인 경우에만 채널을 생성 Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
*
* - 알림 생성
* : NotificationCompat.Builder 알림에 대한 UI 정보와 작업 지정(setSmallIcon(),setContentTitle,setContentText)
* : NotificationCompat.Builder.build() 호출 : Notification 객체를 반환
* : NotificationManagerCompat.notify() 호출 : 시스템에 Notification 객체 전달
*
* - 알림 중요도
* : NotificationChannel(Android 8.0)
* : NotificaitionCompat.Builder - setPriority(under Android 7.1)
*
* - 알림
* : 긴 텍스트, 그림 등을 포함한 확장 뷰를 넣을 수 있음
* : 알림에 버튼 추가
* : - 알림에 버튼을 추가하고 버튼을 누르면 Intent를 통해 Activity나 Broadcast를 시작함
* : - 앱이 종료된 이후에 알림이 도착해도 Intent가 동작해야하므로, pendingIntent를 사용하여 intent를 감싸줘야 함
* : 알림에 프로그래스 표시 가능
*
* - 알림에 엑티비티 연결
* : PendingIntent 사용
* : 연결된 엑티비티가 일반 엑티비티, 알림 전용 엑티비티인지에 따라 백스택 관리가 달라짐
*
* - 태스크와 백 스택
* : 태스크(Task) : 어떤 작업을 하기 위한 엑티비티 그룹 => 태스크마다 자신의 백스탭을 가지고 있음(동일한 프로그램을 여러개 실행시켰다고 생각?)
* : 엑티비티를 시작할때 플래그에 따라 다르게 동작 가능
* : - FLAG_ACTIVITY_NEW_TASK : 이미 실행중인 A의 인스턴스가 있다면 새로 생성하지 않고, A의 인스턴스가 포함된 태스크를
* 앞(foreground)로 가져오고 A의 onNewIntent() 호출
* : - FLAG_ACTIVITY_CLEAR_TASK : A의 인스턴스와 관련된 모든 기존 태스크를 제거하고, 새로 생성
* ...
*
* - 알림에 액티비티 연결 - 일반 액티비티
* : 알림을 터치하면 일반 액티비티인 SecondActivity가 시작, 이때 MainActivity위에 SecondActivity가 있는 백스택을 생성
* : AndroidManifest.xml의 SecondActivity 정의 부분
* : <activity android:name=".SecondActivity" android:parentActivityName=".MainActivity"/>
* : val intent = Intent(this, SecondActivity::class.java) //SecondActivity로 화면 전환을 위한 엑티비티
* : val pendingIntent = with(TaskStackBuilder.create(this)) {
*     addNewIntentWithParentStack(intent)
*     getPendingIntent(0,PendingIntent.FLAG_IMUUTABLE)
* }
* : 알림 클릭시 - SecondActivity 실행, 뒤로 가기 클릭 시 - MainActivity 호출됨
* */


class NotificationActivity : AppCompatActivity() {
    //channelName
    private val channelName1 = "channel1"
    private val channelName2 = "channel2"
    //채널끼리 서로 구별할 수 있는 아이디
    // => 아이디 값을 다르게 한다면 새로운 알림,
    // => 아이디 값이 같다면 기존 알람을 고쳐서 사용
    private var channelID1 = 1
    private var channelID2 = 2

    private var count = 0 //몇번 클릭했는지 알 수 있도록


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        //앱이 실행될 때, 알림 전송 허용 권한 확인
        reqeustSinglePermssion(Manifest.permission.POST_NOTIFICATIONS)
        //알림 채널 생성
        createNotificationChannel()


        //Show Notification - Channel1
        //알림을 누른 횟수를 알림에 출력한다. 알림 아이디를 공유하므로 새로운 알림이 생성되지 않는다.
        findViewById<Button>(R.id.notify1).setOnClickListener{
            Log.d("notification","channel1 notification called")
            showChannel1Notification()
        }


        //Notify - Channel2
        //EditTextView의 내용을 포함하여 알림을 보낸다.
        //버튼을 누를때마다 기존 알림을 지우고 새로 보낸다.(알림 아이디(channelID1)를 공유하므로 새로운 알림이 생성되지 않는다.)
        //=> 알림 아이디 값이 다르다면 새로운 알림을 띄운다.
        findViewById<Button>(R.id.notify2).setOnClickListener{
            Log.d("notification","channel2 notification called")
            showChannel2Notification()
        }

        //Notification settings information
        findViewById<Button>(R.id.settings).setOnClickListener{
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
            startActivity(intent)
        }
    }


    //channel1에 대한 알림 생성
    private fun  showChannel1Notification() {
        count++
        val builder = NotificationCompat.Builder(this,channelName1) //채널 이름 설정
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Notification Lab.")
        builder.setContentText("Notification # $count")

        //중요도 설정
        builder.priority = NotificationCompat.PRIORITY_DEFAULT

        //권한이 허가 되어있을때만 알림 생성
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //channelID1 알림 아이디 값을 바꾼다면, 별도의 알림에서 생성됨
            NotificationManagerCompat.from(this).notify(channelID1,builder.build())
        }else{
            Log.d("notification","do not have permmision")
        }
    }

    //channel2에 대한 알림 생성
    private fun showChannel2Notification() {
        val builder = NotificationCompat.Builder(this,channelName2) //채널 이름 설정
        builder.setSmallIcon(R.mipmap.ic_launcher)

        builder.setContentTitle("Notification Lab2.")

        //사용자가 EditText에 입력한 내용을 알림 내용으로 설정
        val message = findViewById<EditText>(R.id.editTextNotification).text
        builder.setContentText(message)

        //권한이 허가 되어있을때만 알림 생성
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //channelID2 알림 아이디 값을 바꾼다면, 별도의 알림에서 생성됨
            NotificationManagerCompat.from(this).notify(channelID2,builder.build())
        } else{
            Log.d("notification","do not have permmision")
        }
    }


    // 알림 권한 허용 요청(사용자에게)
    private fun reqeustSinglePermssion(permission: String){
        if(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
            return //해당 권한에 대한 요청이 있다면 리턴
        //아직 앱에 알림 권한이 없다면 경고 메시지 띄우기
        val requestPermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it==false){ //permission is not granted!
                AlertDialog.Builder(this).apply {
                    setTitle("Warning")
                    setMessage("권한이 허용되지 않았습니다.")
                }.show()
            }
        }

        //해당 권한에 대해 요청이 필요한 경우
        if(shouldShowRequestPermissionRationale(permission)){
            // you should explain the reason why this app needs the permission.
            AlertDialog.Builder(this).apply {
                setTitle("권한이 필요한 이유")
                setMessage("알림을 보내기 위해 권한 허용이 필요합니다.")
                //허용 버튼 클릭시
                setPositiveButton("Allow") {_,_ ->requestPermLauncher.launch(permission)}
                //거부 버튼 클릭시
                setNegativeButton("Deny"){_,_ ->}
            }.show()
        } else {
            // should be called in onCreate() // activity 최초 호출시 권한 요청
            requestPermLauncher.launch(permission)
        }
    }



    // 안드로이드 8.0 이상인지 확인하고, 8.0 이상이면 알림 채널을 생성해야함
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //Android 8.0
            //알림 채널 생성

            //채널 1 생성
            val channel1 = NotificationChannel(
                channelName1, "channel1",
                NotificationManager.IMPORTANCE_DEFAULT
            )  // 알림의 우선순위 설정 가능
            channel1.description = "this is desrciption about notification channel 1" //채널에 대한 설명 기술

            //채널 2 생성
            val channel2 = NotificationChannel(
                channelName2, "channel2",
                NotificationManager.IMPORTANCE_DEFAULT
            ) // 알림의 우선순위 설정 가능
            channel2.description = "this is desrciption about notification channel 2" //채널에 대한 설명 기술

            //생성한 알림 채널 NotificationManager에 등록
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1) //알림 채널 등록
            notificationManager.createNotificationChannel(channel2) //알림 채널 등록
        }
    }

}
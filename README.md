# Android-Practice

### 1. RecyclerView example Code
> 리사이클러뷰 사용법 및 클릭 이벤트 정의

### Code
  	- recyclerview - RecyclerViewPracticeActivity.kt : Activity
  	- item - Group.kt : DTO

### Layout
	- layout/activity_recyclerview_practice.xml
	- layout/item_group.xml
	- drawalbe/shape_round.xml

---

### 2. ItemHelper example Code
> 리사이클러뷰의 뷰를 조작하기 위한 ItemHelper.Callback 예시 코드

### Code
	- recyclerview - ItemHelperPracticeActivity.kt : Activity
	- recyclerview - GroupRecyclerViewAdapterWithItemHelper.kt : Adapter with ItemHelper Callback
	- item - Group.kt : DTO
 	- recyclerview - ItemTouchHelperCallback.kt : ItemTouchHelperCallback, ItemTouchHelperListner(interface)

### Layout
	- activity_item_helper_practice.xml
	- item_group.xml
	- drawalbe/shape_round.xml


 ---

### 3. CustomView
> 고급 안드로이드 프로그래밍 Week1 : View를 상속받아 사용자가 원하는 CustomView를 보여주기 위한 예시 코드, 사용자의 선택에 따라 사각형과 원을 클릭한 위치에 그려준다.

### Code
	- customview - CustomViewActivity.kt : Container about CustomView, Activity
	- customview - MyView.kt : CutomView

### Layout
	- activity_custom_view.xml : using 'view' tag

 ---

### 4. Notification
> 고급 안드로이드 프로그래밍 Week2 : NotificationManager를 사용하여 채널을 생성하고, NotificationCompat을 사용하여 알림 생성

### Code
	- notification - NotificationActivity.kt : Activity
 
### Layout
	- activity_notification.xml

 ---

### 5. Animation
> 고급 안드로이드 프로그래밍 Week3 : Transition을 활용한 씬 변환

### Code
	- AnimationActivity.kt : Activity, Transition Root View
 
### Layout
	- activity_animation.xml
	- layout_student.xml : scene1
	- layout_staff.xml : scene2

  ---

### 6. Room
> 고급 안드로이드 프로그래밍 Week4 : Room을 활용한 데이터 저장

### Code
	- RoomActivity.kt : Activity
	- MyDao.kt : DAO Class
	- MyDatabase : database abstract define
	- Schema.kt : database table define
 
### Layout
	- activity_room.xml

### Plugin
	- id("com.google.devtools.ksp") //room dependency plugin

### Dependencies
	implementation("androidx.room:room-runtime:$roomVersion")
	annotationProcessor("androidx.room:room-compiler:$roomVersion")
	ksp("androidx.room:room-compiler:$roomVersion")
	implementation("androidx.room:room-ktx:$roomVersion")
	testImplementation("androidx.room:room-testing:$roomVersion")

   ---

### 7. Screen Capture
> View를 Bitmap으로 변환하여 이미지 파일로 저장

### Code
	- ScreenCaptureActivity.kt
 
### Layout
	- activity_screen_capture.xml

   ---


### 8. RecyclerView Swipe Menu
> RecyclerView Item의 Swipe 엑션을 통해 숨겨진 삭제 버튼이 드러나도록 함

### Code
	- ItemViewHolder.kt : RecyclerView Item View Holder
 	- SwipeRecyclerView2Adapter.kt : RecyclerView Adapter
  	- SwipeRecyclerViewWithMenuActivity.kt
 
### Layout
	- activity_swipe_recycler_view_with_menu.xml : RecyclerView Container
 	- item_swipe2.xml : RecyclerView Item

### Dependencies
	// 당겨서 새로고침(Pull to Refresh) 기능 => 아래로 당겨서 리스트 목록 새로고침
	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") 



### 8. Navigation Drawer 
> 평소에는 숨겨져 있다가 사용자가 햄버거 메뉴 버튼 클릭시 화면의 구석에서 스와이프 애니메이션과 함께 숨겨진 화면이 등장

### Code
	- DrawerActivity.kt
 
### Layout
	- activity_drawer.xml : main
 	- navi_drawer.xml : drawer menu


 


 


 

# Android-Practice

### 1. RecyclerView example Code
> 리사이클러뷰 사용법 및 클릭 이벤트 정의

### Code
  	- RecyclerViewPracticeActivity.kt : Activity
  	- Group.kt : DTO

### Layout
	- layout/activity_recyclerview_practice.xml
	- layout/item_group.xml
	- drawalbe/shape_round.xml

---

### 2. ItemHelper example Code
> 리사이클러뷰의 뷰를 조작하기 위한 ItemHelper.Callback 예시 코드

### Code
	- GroupRecyclerViewAdapterWithItemHelper.kt : Adapter with ItemHelper Callback
	- Group.kt : DTO
 	- ItemTouchHelperCallback.kt : ItemTouchHelperCallback, ItemTouchHelperListner(interface)

### Layout
	- activity_item_helper_practice.xml
	- item_group.xml
	- drawalbe/shape_round.xml


 ---

### 3. CustomView
> View를 상속받아 사용자가 원하는 CustomView를 보여주기 위한 예시 코드, 사용자의 선택에 따라 사각형과 원을 클릭한 위치에 그려준다.

### Code
	- CustomViewActivity.kt : Container about CustomView
	- MyView.kt : CutomView

### Layout
	- activity_custom_view.xml : using 'view' tag

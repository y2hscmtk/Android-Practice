package com.example.androidpractice.recyclerview

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R
import kotlin.math.*

//ItemTouchHelper는 사용자가 리사이클러뷰에 가하는 엑션을 중계해준다.
//ItemTouchHelper.Callback()을 상속받으면 아래 함수들을 구현해야한다.
//getMovementFlags => 이동 가능한 방향을 정의한다.
//onMove => 리사이클러뷰 아이템의 각 움직임에 대한 이벤트를 정의한다.(드래그 될 경우 호출된다.)
//onSwipe => 리사이클러뷰 아이템이 스와이프 될 경우 호출된다.

//Reference
//https://velog.io/@nimok97/RecyclerView-%EC%97%90%EC%84%9C-item-Swipe-%ED%95%98%EA%B8%B0-feat.-ItemTouchHelper-ItemTouchUIUtil
//https://velog.io/@trycatch98/Android-RecyclerView-Swipe-Menu
//https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchHelper.Callback#getSwipeEscapeVelocity(float

class SwipeHelper : ItemTouchHelper.Callback()  {

    //드래그 이후, 손을 뗐을때 영역을 남길지 지울지 결정하기 위해
    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        val view = getView(viewHolder) //스와이프 영역에 대한 뷰를 받아온다.
        clamp = view.width.toFloat() / 10 * 3   // 아이템뷰 가로 길이의 비율로 clamp영역 설정
        //clamp는 오른쪽에 보여줄 삭제 버튼이 존재하는 영역

        // Drag와 Swipe 가능 방향을 결정한다.
        // Drag는 사용하지 않을 것이므로 0으로 둔다.
        // 양방향 모두 하고 싶다면 'ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT' 로 설정한다.
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    // 아이템이 드래그 될 경우 호출된다.(위-아래)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }


    // 아이템이 스와이프 될 경우 호출된다.(왼쪽-오른쪽)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    // https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchUIUtil
    // ItemTouchHelper.Callback에는 public static ItemTouchUIUtil getDefaultUIUtil()가 구현되어 있으며,
    // ItemTouchUIUtil 에서도 기존 ItemTouchHelper.Callback에서 대응되는 메서드를 제공한다
    // ItemTouchUIUTil의 함수를 재정의하여 특정 뷰만 스와이프 시킬 수 있다.

    // Called by the ItemTouchHelper when the user interaction with an element is over and it also completed its animation.
    // 엘리먼트와 사용자 상호작용이 끝나고, 애니메이션까지 완료되었을 때 ItemTouchHelper에 의해 호출됨
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        //super.clearView(recyclerView, viewHolder)
        currentDx = 0f  // clamp 관련(공부 필요)
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.adapterPosition // clamp 관련(공부 필요)
    }

    // Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed =
    // 뷰홀더가 ItemTouchHelper에 의해 스와이프 되거나,드래그되면 호출됨
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        //super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition  // 현재 스와이프 된 아이템 위치
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    //리사이클러뷰 아이템의 모든 자식 요소에 대해 작동?
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val view = getView(viewHolder)
        val isClamped = getClamped(viewHolder as SwipeListAdapter.SwipeViewHolder)

        val maxSwipeDistance = -view.width.toFloat() / 3  // 예시로 1/3 지점을 고정 지점으로 설정

        if (dX < maxSwipeDistance && !isClamped) {
            setClamped(viewHolder, true)  // 스와이프 거리가 임계값을 넘어서면 상태 고정
        }

        val x = if (isCurrentlyActive) {
            if (isClamped) dX.coerceAtLeast(maxSwipeDistance) else dX
        } else {
            if (isClamped) maxSwipeDistance else 0f  // 스와이프 상태가 고정된 경우, 고정 위치로 설정
        }

        // 뷰의 위치를 업데이트할 필요가 있을 때만 실행
        if (view.translationX != x) {
            view.translationX = x
            getDefaultUIUtil().onDraw(c, recyclerView, view, x, dY, actionState, isCurrentlyActive)
        }
    }


    //우리의 목표는 swipe_view 부분만 스와이프 시키는 것임
    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        // 아이템뷰에서 스와이프 영역에 해당하는 뷰 가져오기
        return (viewHolder as SwipeListAdapter.SwipeViewHolder).itemView.findViewById(R.id.swipe_view)
    }

    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,  //
        isClamped: Boolean,
        isCurrentlyActive: Boolean // 스와이프 중인지 , 손 떼면 false 된다
    ) : Float {
        // View의 가로 길이의 30% 만 스와이프 되도록
        val maxSwipe: Float = -view.width.toFloat() / 10 * 3 // 음수 값!!,  xml 상에서 삭제 영역이 아이템뷰 width의 0.3 만큼 차지하도록 설정한 것과 맞추기 위함

        // RIGHT 방향으로 swipe 막기
        val right: Float = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
            // 스와이프 된 고정 상태에서 dX - clamp 가 maxSwipe 이하인 상태에서 터치 유지 해제(왼쪽 스와이프 시도 중) -> maxSwipe 리턴
            // 스와이프 된 고정 상태에서 dx - clamp 가 maxSwipe 이상인 상태에서 터치 유지 해제(오른쪽 스와이프 시도 중) -> isClamped 에 false 값이 들어올 것임 -> currentDx가 -clamp 보다 커진다!
        } else {
            dX   // maxSwipe보다 더 많이 왼쪽으로 스와이프 해도 maxSwipe 까지만 스와이프 된다(이때 isClamped는 true 된다) / 밑에서 maxSwipe & x 중에 큰 값을 사용하기 때문
        }

        return min(max(maxSwipe, x), right) // right = 항상 0 이므로  min 함수에서 최대는 항상 0 값이 나온다 -> 스와이프를 통해 오른쪽으로 밀리는 일은 없다!!
    }


    // 일정속도 이상으로 스와이프 할 경우 아이템 뷰 Escape되도록 구현되어있다.
    // 기본 속도 * 20 을 넘어서지 않는 이상 탈출하지 않도록 고정시킨다.
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        //return super.getSwipeEscapeVelocity(defaultValue)
        return defaultValue * 20
    }

    // Default value is .5f , which means, to swipe a View,
    // user must move the View at least half of RecyclerView's width or height,
    // depending on the swipe direction.
    // 반 이상 스와이프 할 경우 뷰가 사라진다. 따라서 2f를 리턴? => 좀 더 공부가 필요
    // 사용자 스와이프하다가 손을 떼는 시점에 호출됨
    // 여기서 isClamped 가 true 가 될지, false가 될 지를 정하여 고정시키거나, 복구시킨다.
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        //return super.getSwipeThreshold(viewHolder)

        Log.d("AppTest", "getSwipeThreshold")
        // 현재 View가 고정되어있지 않고 사용자가 -clamp 이상 swipe시 isClamped true로 변경 아닐시 false로 변경 처리 할 것!!!

        Log.d("AppTest", "isClamped = ${currentDx <= -clamp}")
        //setTag(viewHolder, currentDx <= -clamp)  // 스와이프 되고 오른쪽 스와이프 시에만 닫히도록 하게하기 위해  '!isClamped && ' 조건 제거
        setClamped(viewHolder as SwipeListAdapter.SwipeViewHolder, currentDx <= -clamp)
        return 2f // 절반 이상 넘어갈 수 없음?
    }

    private fun setClamped(viewHolder: SwipeListAdapter.SwipeViewHolder, isClamped: Boolean){
        viewHolder.setClamped(isClamped)

        //필요하다면 삭제버튼의 가시성 설정
    }

    private fun getClamped(viewHolder: SwipeListAdapter.SwipeViewHolder): Boolean{
        return viewHolder.getClamped()
    }

    fun setClamp(clamp: Float) {  // activity or fragment에서 clamp 값을 설정할 수도 있다
        this.clamp = clamp
    }

    // 스와이프가 되었는지를 tag 값으로 판단했으나, 뷰홀더 재활용 과정에서 혼란이 발생할 수 있음 -> 리사이클러뷰 데이터 클래스에 swipe가 되었는지를 판단하는 data 추가
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }
}
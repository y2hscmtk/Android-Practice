package com.example.androidpractice


// 3.아이템 값 DTO
// Data Transfer Object : 계층 간 데이터 전송을 위한 도메인 모델 대신 사용되는 객체
data class Group(
    var groupName: String, // 그룹 이름
    var groupMemberCount : Int // 그룹에 포함된 전체 인원 수
){} //{} : 클래스 본문에 해당, 현재 추가적인 로직이 없기 때문에 비워져 있음
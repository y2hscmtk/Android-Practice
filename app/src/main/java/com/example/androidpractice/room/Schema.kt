package com.example.androidpractice.room

import androidx.room.*

//학생 테이블
@Entity(tableName = "student_table") //테이블 이름을 student_table로 지정
data class Student (
    @PrimaryKey @ColumnInfo(name = "student_id") val id: Int, //테이블에서의 속성 이름을 student_id로 설정
    val name: String
)

//수업 테이블
@Entity(tableName = "class_table")
data class ClassInfo(
    @PrimaryKey val id: Int,
    val name: String,
    val day_time: String,
    val room: String,
    val teacher_id: Int
)

//학생 - 수강 - 수업
//수강 테이블(관계 테이블)
//어떤 학생이 어떤 수업을 수강하는지 알아야하므로, 학생id와 수업id를 복합키로 가져야함
@Entity(tableName = "enrollment",
    primaryKeys = ["sid","cid"],//(학생id,수업id)
    foreignKeys = [
        //student 테이블의 기본키를 외래키로 받아와서 sid라는 이름으로 사용
        ForeignKey(entity = Student::class, parentColumns = ["student_id"], childColumns = ["sid"],onDelete = ForeignKey.CASCADE),
        //classs 테이블의 기본키를 외래키로 받아와서 cid라는 이름으로 사용
        ForeignKey(entity = ClassInfo::class, parentColumns = ["id"], childColumns = ["cid"])
    ],
    indices = [Index(value=["sid", "cid"])]
    )
data class Enrollment ( //학생들의 수업 수강 정보를 관리하기 위한 수강 테이블
    val sid: Int,
    val cid: Int,
    val grade: String? = null // 어떤 과목을 수강하는지?
)


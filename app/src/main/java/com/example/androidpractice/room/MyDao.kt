package com.example.androidpractice.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction

data class StudentWithEnrollments(  // 1:N 관계
    @Embedded val student: Student,
    @Relation(
        parentColumn = "student_id",
        entityColumn = "sid"
    )
    val enrollments: List<Enrollment>
)

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)//기존의 값과 충돌 할 경우, 새로운 값으로 대체한다(REPLACE)
    suspend fun insertStudent(student: Student) //io작업이기 떄문에 suspend를 붙여야됨

    //새로운 수업 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classInfo: ClassInfo)

    //새로운 수강 기록 생성
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnrollment(enrollment: Enrollment)

    @Query("SELECT * from student_table")
    fun getAllStudents(): LiveData<List<Student>> //LiveData로 해당하는 값들 모두 리턴 -> suspend를 붙이지 않아도 괜찮음

    @Query("SELECT * FROM student_table WHERE name = :sname")   // 메소드 인자를 SQL문에서 :을 붙여 사용
    suspend fun getStudentByName(sname: String): List<Student>

    @Delete
    suspend fun deleteStudent(student: Student)

    @Transaction
    @Query("SELECT * FROM student_table WHERE student_id = :id")
    suspend fun getStudentsWithEnrollment(id: Int): List<StudentWithEnrollments>


    @Query("SELECT name from student_table where student_id = :studentId")
    suspend fun getStudentNameById(studentId: Int): String

    @Query("SELECT * FROM class_table WHERE id = :id")
    suspend fun getClassInfo(id: Int): List<ClassInfo>

}

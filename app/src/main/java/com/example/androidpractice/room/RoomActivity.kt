package com.example.androidpractice.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.androidpractice.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/* <Used Class>
* RoomActivity.kt : Activity
* MyDao.kt : DAO Class
* MyDatabase : database abstract define
* Schema.kt : database table define
*
* <Used layout>
* activity_room.xml
*
* <Dependency>
* - plugin
* id("com.google.devtools.ksp") //room dependency plugin
* - dependencies
* val roomVersion = "2.5.2"
* implementation("androidx.room:room-runtime:$roomVersion")
* annotationProcessor("androidx.room:room-compiler:$roomVersion")
* ksp("androidx.room:room-compiler:$roomVersion")
* implementation("androidx.room:room-ktx:$roomVersion")
* testImplementation("androidx.room:room-testing:$roomVersion")
*
* <Explain>
* room.txt
* */

class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        //Database 통해서 DAO가져오기
        val database = MyDatabase.getDatabase(this)
        val myDao = database.getMyDao() //=> SQL함수 사용 가능

        //코루틴 스코프 작성 => insertStudent는 suspend이므로 CorutineScope에서 작성되어야 함
        CoroutineScope(Dispatchers.IO).launch {
            with(myDao) {
                insertStudent(Student(1, "james"))
                insertStudent(Student(2, "john"))
                insertClass(ClassInfo(1, "c-lang", "Mon 9:00", "E301", 1))
                insertClass(ClassInfo(2, "android prog", "Tue 9:00", "E302", 1))
                insertEnrollment(Enrollment(1, 1))
                insertEnrollment(Enrollment(1, 2))
            }
        }

        //allStudents 리스트 갱신
        val textStudentList = findViewById<TextView>(R.id.text_student_list)
        val allStudents = myDao.getAllStudents() //getAllStudents() => LiveData<> => asynchronized
        allStudents.observe(this) {
            val str = StringBuilder().apply {
                for ((id, name) in it) {
                    append(id)
                    append("-")
                    append(name)
                    append("\n")
                }
            }.toString()
            textStudentList.text = str
        }

        val queryStudent = findViewById<Button>(R.id.query_student)
        val editStudentId = findViewById<EditText>(R.id.edit_student_id)
        val textQueryStudent = findViewById<TextView>(R.id.text_query_student)

        //query student 클릭시 => 학생 테이블에서 검색 수행
        queryStudent.setOnClickListener {
            val id = editStudentId.text.toString().toInt()
            CoroutineScope(Dispatchers.IO).launch {
                val results = myDao.getStudentsWithEnrollment(id)
                if (results.isNotEmpty()) {
                    val str = StringBuilder().apply {
                        append(results[0].student.id)
                        append("-")
                        append(results[0].student.name)
                        append(":")
                        for (c in results[0].enrollments) {
                            append(c.cid)
                            val cls_result = myDao.getClassInfo(c.cid)
                            if (cls_result.isNotEmpty())
                                append("(${cls_result[0].name})")
                            append(",")
                        }
                    }
                    withContext(Dispatchers.Main) {
                        textQueryStudent.text = str
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        textQueryStudent.text = ""
                    }
                }
            }
        }

        //enrollment
        val enrollStudent = findViewById<Button>(R.id.enroll)
        enrollStudent.setOnClickListener{
            //해당 학생을 c-lang수업에 등록
            val id = findViewById<EditText>(R.id.edit_student_id).text.toString().toInt()
            CoroutineScope(Dispatchers.IO).launch {
                myDao.insertEnrollment(Enrollment(id,1))
            }
        }

        val addStudent = findViewById<Button>(R.id.add_student)
        val editStudentName = findViewById<EditText>(R.id.edit_student_name)
        addStudent.setOnClickListener {
            val id = editStudentId.text.toString().toInt()
            val name = editStudentName.text.toString()
            if (id > 0 && name.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    myDao.insertStudent(Student(id, name))
                }
            }
        }

        //delete 버튼 클릭시
        findViewById<Button>(R.id.del_student).setOnClickListener{
            val id = findViewById<EditText>(R.id.edit_student_id).text.toString()
            //suspend 이므로 코루틴에서 호출(동기적, 비동기적)
            CoroutineScope(Dispatchers.IO).launch {
                myDao.deleteStudent(Student(id.toInt(),""))
            }
        }

    }
}
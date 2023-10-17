package com.example.androidpractice.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Database 추상 클래스
@Database(entities = [Student::class, ClassInfo::class, Enrollment::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getMyDao() : MyDao

    companion object{
        fun getDatabase(context: Context): MyDatabase{
            val builder =
                Room.databaseBuilder(context, MyDatabase::class.java, "school_database")
            val db = builder.build()
            return db //실제로는 한번만 만들고 리턴하도록 해야함 => 싱글톤
        }
    }
}
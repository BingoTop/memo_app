package com.example.todoapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter

@Database(entities = arrayOf(MemoEntity::class),version=2)
abstract class MemoDatabase:RoomDatabase() {
    abstract fun memoDAO(): MemoDAO

    companion object{
        var INSTANCE:MemoDatabase?=null

        fun getInstance(context: Context):MemoDatabase?{
            if(INSTANCE == null){
                synchronized(MemoDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,MemoDatabase::class.java,"memo.db").allowMainThreadQueries()
                        .fallbackToDestructiveMigration() // 데이터베이스를 생성하고 나서 중간에 수정이 있을 경우 버전을 올려주고 과거에 데이터를 옮길것이냐 문제가 있는데 fallbackToDestructiveMigration 이전 데이터를 다 드롭함
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
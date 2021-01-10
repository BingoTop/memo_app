package com.example.todoapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var memo: String = ""
    ,
    @ColumnInfo(name = "created_at")
    var createdAt: Long?=null,
    @ColumnInfo(name = "modified_at")
    var modified_at: Long?=null

)
package com.example.todoapp

import androidx.room.*

@Dao
interface MemoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: MemoEntity)

    fun insertWithTimeStamp(memo: MemoEntity){
        insert(memo.apply {
            createdAt = System.currentTimeMillis()
        })
    }


    @Query("SELECT * FROM memo")
    fun getAll() : List<MemoEntity>

    @Query("SELECT * FROM memo WHERE id=:id")
    fun getMemo(id:Long): MemoEntity

    @Delete
    fun delete(memo:MemoEntity)

    @Update
    fun update(memo: MemoEntity)

    fun updateWithTimeStamp(id:Long,text:String){
        val find_memo = getMemo(id)
        if (find_memo.memo != text){
            update(find_memo.apply {
                createdAt =  System.currentTimeMillis()
                memo = text
            })
        }
    }
}
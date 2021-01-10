package com.example.todoapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.memo_write.*

@SuppressLint("StaticFieldLeak")
class CreateMemo:AppCompatActivity() {
    val db = MemoDatabase.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_write)
    }

    override fun onPause() {
        super.onPause()
        val text = writing_memo.text.toString()
        if(text != "") {
            val memo = MemoEntity(null, text)
            insertMemo(memo)
        }
    }
    fun insertMemo(memo:MemoEntity){
        // MainThread vs WorkerThread  UI 화면과 관련된 것들은 MainThread 데이터 받아오거나 쓸때는 WorkerThread(Background Thread)
        val insertTask = object: AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
//                db.memoDAO().insert(memo)
                db?.memoDAO()?.insertWithTimeStamp(memo)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
            }
        }
        insertTask.execute()
    }

}
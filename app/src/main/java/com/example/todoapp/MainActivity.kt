package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_memo.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity(),onDeleteListener,getItemSize {
    var cnt = 0
    lateinit var db:MemoDatabase
    var memoList = listOf<MemoEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = MemoDatabase.getInstance(this)!!
        button_add.setOnClickListener {
            val intent = Intent(this,CreateMemo::class.java)
            this.startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        getAllMemos()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }


    fun insertMemo(memo:MemoEntity){
        // MainThread vs WorkerThread  UI 화면과 관련된 것들은 MainThread 데이터 받아오거나 쓸때는 WorkerThread(Background Thread)
        val insertTask = object: AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
//                db.memoDAO().insert(memo)
                db.memoDAO().insertWithTimeStamp(memo)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos() // 다시 불러온다.
            }
        }
        insertTask.execute()
    }

    fun getAllMemos(){
        val getTask = (object: AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
             memoList = db.memoDAO().getAll()
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }
        }).execute()
    }
    fun deleteMemo(memo: MemoEntity){
        val deleteTask = object:AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        deleteTask.execute()

    }

    fun setRecyclerView(memoList:List<MemoEntity>){
        recyclerView.adapter = MemoAdapter(this,memoList,this,this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }

    override fun getSize(size: Int) {
        memo_size_text.text = "${size.toString()}개의 메모"
    }


}
package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.memo_detail.*
import kotlinx.android.synthetic.main.memo_write.*
import kotlinx.android.synthetic.main.memo_write.detail_toolbar

@SuppressLint("StaticFieldLeak")
class CreateMemo:AppCompatActivity(),onDeleteListener,getItemSize {
    var flag = false
    val db = MemoDatabase.getInstance(this)
    var memoList = db?.memoDAO()?.getAll()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_write)
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        val text = writing_memo.text.toString()
        if(text != "" && flag == false) {
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.otherBtn -> {
                Log.d("메모 디테일 액티비티 - ","공유하기 버튼 클릭")
                val textMessage = writing_memo.text.toString()
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,textMessage)
                    type = "text/plain"
                }
                if(sendIntent.resolveActivity(packageManager) != null){
                    startActivity(sendIntent)
                }
                return true
            }
            R.id.finishBtn ->{
                val intent = Intent(this,MainActivity::class.java)
                val text = writing_memo.text.toString()
                if(text != "") {
                    val memo = MemoEntity(null, text)

                    insertMemo(memo)
                    flag = true
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onDeleteListener(memo: MemoEntity) {
        TODO("Not yet implemented")
    }

    override fun getSize(size: Int) {
        TODO("Not yet implemented")
    }

    fun setView(memoList:List<MemoEntity>){
        recyclerView.adapter = MemoAdapter(this,memoList!!,this,this)
    }
}
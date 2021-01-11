package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class MainActivity: AppCompatActivity(),onDeleteListener,getItemSize {
    lateinit var db:MemoDatabase
    var memoList = listOf<MemoEntity>()
    var displayList = ArrayList<MemoEntity>()
    var TAG = "메인 액티비티"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        db = MemoDatabase.getInstance(this)!!
        search_view.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            this.startActivity(intent)
        }
        button_add.setOnClickListener {
            val intent = Intent(this,CreateMemo::class.java)
            this.startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        getAllMemos()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.mainMenuBtn -> {
                println("menu_clicked")
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_search,menu)
//        val menuItem = menu!!.findItem(R.id.search)
//        if(menuItem != null){
//            val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView
//            searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    if(newText!!.isNotEmpty()){
//                        displayList.clear()
//                        val search = newText.toLowerCase(Locale.getDefault())
//                        memoList.forEach {
//                            if(it.memo.toString().toLowerCase(Locale.getDefault()).contains(search)){
//                                displayList.add(it)
//                            }
//                        }
//                        recyclerView.adapter!!.notifyDataSetChanged()
//                    }
//                    else{
//                        displayList.clear()
//                        displayList.addAll(memoList)
//                        recyclerView.adapter!!.notifyDataSetChanged()
//                    }
//                    return true
//                }
//            })
//        }
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.d(TAG,"검색 뷰 선택")
//        return super.onOptionsItemSelected(item)
//    }


    fun insertMemo(memo:MemoEntity){
        // MainThread vs WorkerThread  UI 화면과 관련된 것들은 MainThread 데이터 받아오거나 쓸때는 WorkerThread(Background Thread)
        val insertTask = object: AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
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
                displayList.clear()
                displayList.addAll(memoList)

            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(displayList)
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
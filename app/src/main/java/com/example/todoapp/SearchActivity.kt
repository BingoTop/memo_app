package com.example.todoapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("StaticFieldLeak")
class SearchActivity:AppCompatActivity(){
    lateinit var db:MemoDatabase
    val mainAct = MainActivity()
    var memoList = mainAct.memoList
    var displayList = mainAct.displayList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_search)
        setSupportActionBar(toolbar)
//        db = MemoDatabase.getInstance(this)!!

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val menuItem = menu!!.findItem(R.id.search)
        if(menuItem != null){
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                    displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        memoList.forEach {
                            if(it.memo.toString().toLowerCase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }else{
                        displayList.clear()
                        displayList.addAll(memoList)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
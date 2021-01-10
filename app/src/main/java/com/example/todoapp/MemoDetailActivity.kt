package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.memo_detail.*
import kotlin.math.log

@SuppressLint("StaticFieldLeak")
class MemoDetailActivity:AppCompatActivity() {
    val TAG = "확인"
    var memo_id:String? = ""
    val db = MemoDatabase.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_detail)
        if(intent.hasExtra("msg"))
        {
            memo_detail.setText(intent.getStringExtra("msg").toString())
        }
        memo_id = intent.getStringExtra("mid")
        root.setOnClickListener {
            hideKeyboard()
        }

    }

    fun hideKeyboard(){
        val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(memo_detail.windowToken,0)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause 실행")
        UpdateMemo(intent.getStringExtra("mid")!!.toLong())
   }

    fun UpdateMemo(id:Long){
        val updateTask = (object:AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                val changedText = memo_detail.text.toString()
                db?.memoDAO()?.updateWithTimeStamp(id,changedText)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
            }
        }).execute()
    }

}
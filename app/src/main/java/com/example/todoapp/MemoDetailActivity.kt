package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(intent.hasExtra("msg"))
        {
            memo_detail.setText(intent.getStringExtra("msg").toString())
        }
        memo_id = intent.getStringExtra("mid")
        root.setOnClickListener {
            hideKeyboard()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.otherBtn -> {
                Log.d("메모 디테일 액티비티 - ","공유하기 버튼 클릭")
                val textMessage = memo_detail.text.toString()
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
                UpdateMemo(intent.getStringExtra("mid")!!.toLong())
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
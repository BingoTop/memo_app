package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_memo.view.*
import java.text.SimpleDateFormat

class MemoAdapter(val context: Context,var list: List<MemoEntity>,val onDeleteListener: onDeleteListener,val getItemSize: getItemSize ): RecyclerView.Adapter<MemoAdapter.MyViewHolder>(){
//    private val memoList:List<MemoEntity>?= null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_memo,parent,false)
        getItemSize.getSize(itemCount)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sdf = SimpleDateFormat("yyyy. MM. dd.")
        val memo = list[position]
        holder.memo.text = memo.memo
        holder.memo_id.text = memo.id.toString()
        holder.created_at.text = sdf.format(memo.createdAt)
        holder.root.setOnLongClickListener(object:View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                onDeleteListener.onDeleteListener(memo)
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val memo = itemView.text_memo
        val created_at = itemView.created_at
        val root = itemView.root
        val memo_id = itemView.id_view
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context,MemoDetailActivity::class.java)
                intent.putExtra("msg",memo.text.toString())
                intent.putExtra("mid",memo_id.text.toString())
                itemView.context.startActivity(intent)

            }
        }

    }
}

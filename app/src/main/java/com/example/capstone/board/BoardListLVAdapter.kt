package com.example.capstone.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.capstone.R
import com.example.capstone.utils.FBAuth

class BoardListLVAdapter(val boardlist : MutableList<BoardModel>):BaseAdapter() {
    override fun getCount(): Int {
        return boardlist.size //boardModel의 사이즈 만큼 반환
    }

    override fun getItem(position: Int): Any {
        return boardlist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

//        if (view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item,parent,false)
//        }

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)

        val title = view?.findViewById<TextView>(R.id.titleArea)
        title?.text = boardlist[position].title

        val content = view?.findViewById<TextView>(R.id.contentArea)
        content?.text = boardlist[position].content

        val time = view?.findViewById<TextView>(R.id.timeArea)
        time?.text = boardlist[position].time

        if(boardlist[position].uid.equals(FBAuth.getUid())){
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#f5f5dc"))
        }

        return view!!
    }
}
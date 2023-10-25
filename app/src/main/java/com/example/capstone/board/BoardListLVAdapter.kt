package com.example.capstone.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.capstone.R
import com.example.capstone.utils.FBAuth

class BoardListLVAdapter(val boardlist: MutableList<BoardModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return boardlist.size // boardModel의 사이즈 만큼 반환
    }

    override fun getItem(position: Int): Any {
        return boardlist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)
        }

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        val title = view?.findViewById<TextView>(R.id.titleArea)
        val content = view?.findViewById<TextView>(R.id.contentArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        val imageArea = view?.findViewById<ImageView>(R.id.imageArea)

        title?.text = boardlist[position].title
        content?.text = boardlist[position].content
        time?.text = boardlist[position].time

        if (boardlist[position].uid == FBAuth.getUid()) {
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#f5f5dc"))

        }

        // 'job' 값을 기반으로 이미지 설정
        val jobImageResource = getImageResource(boardlist[position].job)
        imageArea?.setImageResource(jobImageResource)

        return view!!
    }

    // 'job' 값을 기반으로 이미지 리소스를 반환하는 함수
    private fun getImageResource(job: String): Int {
        return when (job) {
            "직무를 선택하세요" -> R.drawable.board_default // 적절한 기본 이미지 설정
            "사무보조" -> R.drawable.video_office
            "편의점" -> R.drawable.video_cashier
            "도서관 사서" -> R.drawable.video_library
            "의류 매장 보조" -> R.drawable.video_cloth
            "빵집 보조" -> R.drawable.video_bakery
            else -> R.drawable.board_default // 적절한 기본 이미지 설정
        }
    }
}

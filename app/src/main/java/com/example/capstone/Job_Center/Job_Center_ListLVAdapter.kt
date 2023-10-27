package com.example.capstone.Job_Center

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.capstone.R
import com.example.capstone.fragments.data.FcltOpenInfoOWSI
import com.example.capstone.fragments.data.Row

class Job_Center_ListLVAdapter(private val context: Context, private var data: List<Row>) : BaseAdapter() {



    fun setData(data: List<Row>) {
        this.data = data
        notifyDataSetChanged()
    }


    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.job_center_list_item, null)
            viewHolder = ViewHolder()
            viewHolder.nameTextView = view.findViewById(R.id.job_nameArea)
            viewHolder.maxPeopleTextView = view.findViewById(R.id.maximum_people)
            viewHolder.currentPeopleTextView = view.findViewById(R.id.current_people)
            viewHolder.telTextView = view.findViewById(R.id.tel_Area)
            viewHolder.addressTextView = view.findViewById(R.id.address_Area)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = data[position]
        viewHolder.nameTextView.text = item.FCLT_NM
        viewHolder.maxPeopleTextView.text = "정원: " + item.INMT_GRDN_CNT.toString()
        viewHolder.currentPeopleTextView.text = "현재 인원: " + item.LVLH_NMPR.toString()
        viewHolder.telTextView.text = "연락처: "+ item.FCLT_TEL_NO.toString()
        viewHolder.addressTextView.text = "주소 : " + item.FCLT_ADDR.toString()


        return view
    }

    private class ViewHolder {
        lateinit var nameTextView: TextView
        lateinit var maxPeopleTextView: TextView
        lateinit var currentPeopleTextView: TextView
        lateinit var telTextView: TextView
        lateinit var addressTextView: TextView
    }
}



package com.example.capstone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.capstone.databinding.WelfareItemBinding

class WelfareAdapter(val context: Context, val userList: ArrayList<WfList>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            WelfareItemBinding.inflate(inflater, parent, false)
        } else {
            WelfareItemBinding.bind(convertView)
        }

        val profile = binding.ivProfile
        val name = binding.tvName

        val user = userList[position]

        profile.setImageResource(user.profile)
        name.text = user.name

        return binding.root
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
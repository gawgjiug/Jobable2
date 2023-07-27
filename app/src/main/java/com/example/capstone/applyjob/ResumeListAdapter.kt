package com.example.capstone.applyjob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.capstone.R

class ResumeListAdapter(context: Context, resource: Int, objects: List<ResumeData>) :
    ArrayAdapter<ResumeData>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_resume, parent, false)

        val resumeData = getItem(position)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val sexTextView = view.findViewById<TextView>(R.id.sexTextView)
        val typeTextView = view.findViewById<TextView>(R.id.typeTextView)

        nameTextView.text = resumeData?.name
        sexTextView.text = resumeData?.sex
        typeTextView.text = resumeData?.type

        return view
    }
}

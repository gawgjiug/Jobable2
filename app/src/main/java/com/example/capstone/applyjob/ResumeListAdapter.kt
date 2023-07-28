package com.example.capstone.applyjob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.capstone.R

class ResumeListAdapter(
    context: Context,
    private val resource: Int,
    private val dataList: List<ResumeData> // List 데이터를 받도록 수정
) : ArrayAdapter<ResumeData>(context, resource, dataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_resume, parent, false)

        val resumeData = getItem(position)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val sexTextView = view.findViewById<TextView>(R.id.sexTextView)
        val typeTextView = view.findViewById<TextView>(R.id.typeTextView)
        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)

        nameTextView.text = resumeData?.name
        sexTextView.text = resumeData?.sex
        typeTextView.text = resumeData?.type

        Glide.with(context)
            .load(resumeData?.profileImageURL)
            .placeholder(R.drawable.resume_profile) // 기본 이미지를 설정할 수 있습니다. 로딩 중에 표시됩니다.
            .error(R.drawable.resume_profile) // 이미지 로딩에 실패할 경우 표시됩니다.
            .into(profileImageView)

        return view
    }
}

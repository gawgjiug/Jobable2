package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.databinding.ActivityJobEduBinding

class JobeduActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobEduBinding
    private val items = mutableListOf<ContentsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_edu)

        items.add(
            ContentsModel(
                "제과제빵",
            )
        )

        items.add(
            ContentsModel(
                "패션디자이너"

            )
        )

        items.add(
            ContentsModel(
                "사서보조"
            )
        )

        items.add(
            ContentsModel(
                "사무보조"
            )
        )

        val recyclerview = findViewById<RecyclerView>(R.id.rv_job)
        val rvAdapter = RVAdapter(items)
        recyclerview.adapter = rvAdapter

        recyclerview.layoutManager = GridLayoutManager(this, 2)
    }


}
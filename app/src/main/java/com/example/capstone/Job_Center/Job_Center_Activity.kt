package com.example.capstone.Job_Center

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstone.R
import com.example.capstone.fragments.SeoulOpenApi
import com.example.capstone.fragments.SeoulOpenService
import com.example.capstone.fragments.data.Row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Job_Center_Activity : AppCompatActivity() {
    // ListView와 어댑터를 멤버 변수로 선언
    private lateinit var listView: ListView
    private lateinit var adapter: Job_Center_ListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_center)

        // ListView 가져오기
        listView = findViewById(R.id.Job_Center_ListView)

        // 데이터 가져오기
        lifecycleScope.launch {
            try {
                val rows = getDataFromApi()

                // 어댑터 생성 및 ListView에 연결
                adapter = Job_Center_ListLVAdapter(this@Job_Center_Activity, rows)
                listView.adapter = adapter
            } catch (e: Exception) {
                // 오류 처리
                Log.e("Job_Center_Activity", "데이터를 가져오는 동안 오류 발생", e)
                Toast.makeText(this@Job_Center_Activity, "데이터를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터 가져오는 함수
    private suspend fun getDataFromApi(): List<Row> {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(SeoulOpenApi.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(SeoulOpenService::class.java)

            val response = withContext(Dispatchers.IO) {
                service.getLibraries(SeoulOpenApi.APIKEY, 200).execute()
            }

            if (response.isSuccessful) {
                val result = response.body()
                return result?.fcltOpenInfo_OWSI?.row ?: emptyList()
            } else {
                Log.e("getDataFromApi", "API 응답이 실패했습니다. HTTP 코드: ${response.code()}")
                throw Exception("API 응답이 실패했습니다.")
            }
        } catch (e: Exception) {
            Log.e("getDataFromApi", "데이터를 가져오는 동안 오류 발생", e)
            throw e
        }
    }
}


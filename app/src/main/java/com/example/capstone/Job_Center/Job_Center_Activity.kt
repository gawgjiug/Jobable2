package com.example.capstone.Job_Center

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import java.util.Locale

class Job_Center_Activity : AppCompatActivity() {
    // ListView와 어댑터를 멤버 변수로 선언

    private val allRows: MutableList<Row> = mutableListOf()
    private lateinit var listView: ListView
    private lateinit var adapter: Job_Center_ListLVAdapter
    private lateinit var searchView: SearchView
    private val filteredRows: MutableList<Row> = mutableListOf()


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
                adapter = Job_Center_ListLVAdapter(this@Job_Center_Activity, allRows)
                listView.adapter = adapter
            } catch (e: Exception) {
                // 오류 처리
                Log.e("Job_Center_Activity", "데이터를 가져오는 동안 오류 발생", e)
                Toast.makeText(this@Job_Center_Activity, "데이터를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }

        searchView = findViewById(R.id.job_center_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterBoardData(newText.toLowerCase(Locale.getDefault()))
                }
                return true
            }
        })
    }


// Job_Center_Activity의 filterBoardData 메서드 수정
private fun filterBoardData(query: String) {
    filteredRows.clear()

    if (query.isNotEmpty()) {
        val searchQuery = query.toLowerCase(Locale.getDefault())
        for (row in allRows) {
            if (row.FCLT_ADDR.toLowerCase(Locale.getDefault()).contains(searchQuery)) {
                filteredRows.add(row)
            }
        }
    } else {
        filteredRows.addAll(allRows)
    }

    adapter.setData(filteredRows)

    // 디버깅을 위한 Log 메시지 추가
    Log.e("Job_Center_Activity", "검색어: $query, 결과 개수: ${filteredRows.size}")
}

    // 데이터 가져오는 함수
    private suspend fun getDataFromApi() {
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
                allRows.addAll(result?.fcltOpenInfo_OWSI?.row ?: emptyList()) // allRows에 데이터 추가

                // 어댑터 생성 및 ListView에 연결 (여기서 adapter 초기화)
                adapter = Job_Center_ListLVAdapter(this@Job_Center_Activity, allRows)
                listView.adapter = adapter
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


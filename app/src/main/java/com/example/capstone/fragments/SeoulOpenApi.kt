package com.example.capstone.fragments

import android.telecom.Call
import com.example.capstone.fragments.data.FcltOpenInfoOWSI
import com.example.capstone.fragments.data.Rehab
import retrofit2.http.GET
import retrofit2.http.Path

class SeoulOpenApi {
    companion object{
        val DOMAIN = "http://openapi.seoul.go.kr:8088/"
        val APIKEY = "5a7a4e6a676b776a3132335444706b79"
    }

}

interface SeoulOpenService  {
    @GET("{apikey}/json/fcltOpenInfo_OWSI/1/{end}")
    fun getLibraries(@Path("apikey") key:String, @Path("end") limit:Int) : retrofit2.Call<Rehab>
}
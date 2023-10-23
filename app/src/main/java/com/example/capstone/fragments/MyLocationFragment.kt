package com.example.capstone.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.databinding.FragmentMyLocationBinding

import com.example.capstone.fragments.data.Rehab
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MyLocationFragment : Fragment(), OnMapReadyCallback {

    //5a7a4e6a676b776a3132335444706b79

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMyLocationBinding

    private lateinit var locationPermission: ActivityResultLauncher<Array<String>>

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val permission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val PERMFLAG = 99

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLocationBinding.inflate(inflater, container, false)
        val rootView = binding.root

        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results.all { it.value }) {
                startProcess()
            } else {
                Toast.makeText(
                    requireContext(),
                    "권한 승인이 필요합니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (isPermitted()) {
            startProcess()
        } else {
            locationPermission.launch(permission)
        }




        return rootView
    }

    private fun startProcess() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    private fun isPermitted(): Boolean {
        for (perm in permission) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

// Inside onMapReady function
        mMap.setOnMarkerClickListener { marker ->
            // 마커 클릭 시 실행할 동작 구현
            val zoomLevel = 17f // 원하는 줌 레벨 설정
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.position, zoomLevel)
            mMap.animateCamera(cameraUpdate)
            true // 이벤트 처리를 완료했음을 반환
        }



        loadLibraries()
    }


    fun loadLibraries() {
        val retrofit = Retrofit.Builder()
            .baseUrl(SeoulOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 추가
            .build()
        val service = retrofit.create(SeoulOpenService::class.java)
        service.getLibraries(SeoulOpenApi.APIKEY,200)
            .enqueue(object  : Callback<Rehab>{
                override fun onResponse(
                    call: Call<Rehab>,
                    response: Response<Rehab>
                ) {
                    val result = response.body()
                    showLibraries(result)
                }

                override fun onFailure(call: Call<Rehab>, t: Throwable) {
                    Toast.makeText(requireContext(),"데이터를 가져올 수 없습니다",Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun showLibraries(result: Rehab?) {
        result?.let {
            val latlngbounds = LatLngBounds.builder()
            val geocoder = Geocoder(requireContext())

            for (rehab in it.fcltOpenInfo_OWSI.row) {
                val address = rehab.FCLT_ADDR

                try {
                    val locationList = geocoder.getFromLocationName(address, 1)

                    if (locationList != null && locationList.isNotEmpty()) {
                        val location = locationList[0]
                        val latitude = location.latitude
                        val longitude = location.longitude

                        val marker = MarkerOptions()
                            .position(LatLng(latitude, longitude))
                            .title(rehab.FCLT_NM)

                        mMap.addMarker(marker)

                        // 마커 위치를 영역에 포함
                        latlngbounds.include(LatLng(latitude, longitude))
                    } else {
                        // 주소를 찾을 수 없는 경우 처리
                        Log.d("Geocoder", "No location found for address: $address")
                    }
                } catch (e: Exception) {
                    Log.e("Geocoder", "Error converting address to coordinates: $address", e)
                }
            }

            val bound = latlngbounds.build()
            val padding = 0
            // 카메라를 조정하여 모든 마커가 영역 내에 표시되도록 합니다.
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latlngbounds.build(), padding)

            mMap.moveCamera(cameraUpdate)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMFLAG -> {
                val check = grantResults.all { it == PERMISSION_GRANTED }
                if (check) {
                    startProcess()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "권한을 승인해야지만 앱을 사용할 수 있습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
package com.example.capstone

import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.capstone.databinding.ActivityWelfareBinding

class WelfareActivity : AppCompatActivity() {

    private var mBinding: ActivityWelfareBinding? = null
    private val binding get() = mBinding!!

    private val websites = arrayOf(
        "https://www.hscity.go.kr/www/partInfo/femaleFamily/welfare.jsp",
        "https://www.osan.go.kr/depart/contents.do?mId=0408010100",
        "https://www.suwon.go.kr/sw-www/deptHome/dep_welfare.jsp",
        "https://wis.seoul.go.kr/handicap/happySeoul.do"
    )

    var UserList = arrayListOf<WfList>(
        WfList(R.drawable.hwaseong, "화성시청"),
        WfList(R.drawable.osan, "오산시청"),
        WfList(R.drawable.suwon, "수원시청"),
        WfList(R.drawable.seoul, "서울시청")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityWelfareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = WelfareAdapter(this, UserList)
        binding.Wflv.adapter = adapter

        binding.Wflv.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as WfList

                val websiteUrl = websites[position]
                val webViewFragment = WebViewFragment.newInstance(websiteUrl)
                replaceFragment(webViewFragment)
            }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

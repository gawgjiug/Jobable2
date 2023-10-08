package com.example.capstone

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.databinding.ActivityInfoCalculatorBinding
import java.text.NumberFormat

class Info_Calculator_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoCalculatorBinding
    private lateinit var hourlyRateEditText: EditText
    private lateinit var timeSpinner: Spinner
    private lateinit var daySpinner: Spinner
    private lateinit var bonusCheckBox: CheckBox
    private lateinit var rateResultEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // XML 레이아웃에서 View를 찾습니다.
        hourlyRateEditText = binding.hourlyRate
        timeSpinner = binding.timeSpinner
        daySpinner = binding.daySpinner
        bonusCheckBox = binding.bonusCheck
        rateResultEditText = binding.rateResultEdtxt

        val calculateButton = binding.reteBtn

        // "계산하기" 버튼 클릭 이벤트 처리
        calculateButton.setOnClickListener {
            calculateAndDisplayResult()
        }

        // Spinner에 아이템 추가
        val dayNumbers = (1..31).map { it.toString() }
        val timeNumbers = (1..20).map { it.toString() }

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dayNumbers)
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeNumbers)

        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        daySpinner.adapter = dayAdapter
        timeSpinner.adapter = timeAdapter
    }

    private fun calculateAndDisplayResult() {
        // 입력값 가져오기
        val hourlyRateStr = hourlyRateEditText.text.toString()
        val timeStr = timeSpinner.selectedItem.toString()
        val dayStr = daySpinner.selectedItem.toString()
        val bonusChecked = bonusCheckBox.isChecked

        // 입력값이 비어있는지 확인
        if (hourlyRateStr.isEmpty()) {
            Toast.makeText(this, "시급을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 시급, 시간, 일수 문자열을 숫자로 변환
        val hourlyRate = hourlyRateStr.toDouble()
        val time = timeStr.toInt()
        val day = dayStr.toInt()

        // 주휴수당 계산 여부에 따라 월급 계산
        val bonusMultiplier = if (bonusChecked) 1.5 else 1.0
        val monthlyIncome = (hourlyRate * time * day * bonusMultiplier * 0.967).toInt()

        // 결과를 EditText에 표시
        val formattedIncome = NumberFormat.getInstance().format(monthlyIncome)
        rateResultEditText.setText("$formattedIncome 원입니다")
    }
}


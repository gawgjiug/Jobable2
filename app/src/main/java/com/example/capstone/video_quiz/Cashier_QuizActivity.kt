package com.example.capstone.video_quiz

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.capstone.R
import com.example.capstone.databinding.ActivityCashierQuizBinding

class Cashier_QuizActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding :ActivityCashierQuizBinding
    private var currentPosition : Int = 1 //현재 몇번 문제인지 담을 변수
    private var selectedOption : Int = 0 //사용자가 몇번째 답변을 선택했는지
    private var score : Int = 0 //전체 문제 중 몇 문제를 맞췄는지

    private lateinit var questionList : ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCashierQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //질문 리스트 가져오기
        questionList = QuestionData.getQuestion()
        //화면 세팅
        getQuestionData()

        binding.option1Text.setOnClickListener(this)
        binding.option2Text.setOnClickListener(this)
        binding.option3Text.setOnClickListener(this)
        binding.option4Text.setOnClickListener(this)

        //답변 체크 이벤트
        binding.quizSubmitBtn.setOnClickListener {
            if(selectedOption != 0){
                val question =questionList[currentPosition -1]
                //정답 체크 (선택 답변과 정답을 비교)
                if(selectedOption != question.correct_answer){//오답
                setColor(selectedOption,R.drawable.wrong_option_background)
                    callDialog("오답","정답은 ${question.correct_answer} 번 입니다!")


                }else{
                    score++
                }
                setColor(question.correct_answer,R.drawable.correct_option_background)

                if(currentPosition == questionList.size ){

                    binding.quizSubmitBtn.text = getString(R.string.submit,"끝")
                }else{
                    binding.quizSubmitBtn.text = getString(R.string.submit,"다음 문제")

                }

            }
            else{
                //위치 값 상승
                currentPosition++
                when{
                    currentPosition <= questionList.size ->{
                        //다음 문제 셋팅
                        getQuestionData()
                    }

                    else ->{
                        //결과 액티비티로 넘어가는 코드
                        val intent = Intent(this@Cashier_QuizActivity,QuizResultActivity::class.java)
                        intent.putExtra("score",score)
                        intent.putExtra("totalSize",questionList.size)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            //선택 값 초기화
            selectedOption = 0


        }
    } //onCreate

    private fun setColor(opt:Int,color: Int){
        when(opt){
            1 -> binding.option1Text.background = ContextCompat.getDrawable(this,color)
            2 -> binding.option2Text.background = ContextCompat.getDrawable(this,color)
            3 -> binding.option3Text.background = ContextCompat.getDrawable(this,color)
            4 -> binding.option4Text.background = ContextCompat.getDrawable(this,color)
        }
    }

    //다이얼 로그
    private fun callDialog(alertTitle : String,correctAnswer:String){
        AlertDialog.Builder(this)
            .setTitle(alertTitle)
            .setMessage("정답: $correctAnswer")
            .setPositiveButton("OK"){
                dialogInterface,i ->
                dialogInterface.dismiss()

            }
            .setCancelable(false)
            .show()
    }

    private fun getQuestionData(){

        //답변 설정 초기화
        setOptionStyle()

        val question = questionList[currentPosition -1 ]

        binding.quizProgressBar.progress = currentPosition //프로그레스 바 위치를 설정

        binding.quizProgressBar.max = questionList.size //max를 사이즈 값으로 설정

        binding.progressText.text = getString(R.string.count_label,currentPosition,questionList.size)

        binding.questionText.text = question.question

        //답변표시
        binding.option1Text.text = question.option_one
        binding.option2Text.text = question.option_two
        binding.option3Text.text = question.option_three
        binding.option4Text.text = question.option_four


        setSubmitBtn("제출")

    }

    private fun setSubmitBtn (name:String){
        binding.quizSubmitBtn.text= getString(R.string.submit,name)
    }
    private fun setOptionStyle(){
        var optionList : ArrayList<TextView> = arrayListOf()
        optionList.add(0,binding.option1Text)
        optionList.add(0,binding.option2Text)
        optionList.add(0,binding.option3Text)
        optionList.add(0,binding.option4Text)

        //답변 텍스트뷰 설정
        for(op in optionList){
            op.setTextColor(Color.parseColor("#555151"))
            op.background = ContextCompat.getDrawable(this,R.drawable.option_background)
            op.typeface = Typeface.DEFAULT
        }
    }

    private fun selectedOptionStyle(view: TextView,opt :Int){
        setOptionStyle()

        //위치 담기
        selectedOption = opt
        view.setTextColor((Color.parseColor("#5F00FF")))
        view.background = ContextCompat.getDrawable(this,R.drawable.selected_option_background)
        view.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.option1_text -> selectedOptionStyle(binding.option1Text,1)
            R.id.option2_text -> selectedOptionStyle(binding.option2Text,2)
            R.id.option3_text -> selectedOptionStyle(binding.option3Text,3)
            R.id.option4_text -> selectedOptionStyle(binding.option4Text,4)
        }
    }

}
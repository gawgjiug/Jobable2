package com.example.capstone.video_quiz

object QuestionData {

    fun getQuestion(): ArrayList<Question>{
        val quelist : ArrayList<Question> = arrayListOf()
        val q1 = Question (
            1,
            "다음 유통기한 표기를 읽고 \n해당 상품의 유통기한 일자 중 옳은 것을 고르세요 ! " +
                    "\n: 2023.06.01 제조일로부터 6개월",
            " 2023.10.01 ",
            " 2023.11.01 ",
            " 2022.12.01 ",
            " 2023.12.01 ",
            4
        )

        val q2 = Question (
            1,
            "물건을 진열하는 규칙 중에 옳지 않은 것을 고르세요!",
            " 상품에 앞면이 보이게 진열한다 ",
            " 쇼 카드에 맞춰 같은 제품끼리 진열한다",
            " 유통기한에 따라서 진열한다",
            " 상품을 꼭 바르게 세워서 진열할 필요는 없다  ",
            4
        )
        val q3 = Question (
            1,
            "유리를 청소할 때에 세정액 사용방법 중 옳지 않은 것을 고르세요 !",
            " 세정액을 뿌릴 때에는 한 걸음 뒤에서 1~2번 뿌린다. ",
            " 고객들이 자주 만지는 손잡이도 닦아 주어야 한다.",
            " 세정액을 뿌리고 알아서 닦일 때 까지 기다린다.",
            " 사용한 청소 도구는 창고에 정리한다.  ",
            3
        )

        quelist.add(q1)
        quelist.add(q2)
        quelist.add(q3)

        return  quelist

    }
}
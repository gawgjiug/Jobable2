package com.example.capstone.board

data class BoardModel(
    val title: String,
    val content: String,
    val uid: String,
    val time: String,
    val job: String,
    val pay: String,
    val day: String,
    val worktime :String // 이 부분은 이미 추가되어 있어야 합니다.
) {
    // 기본 생성자
    constructor() : this("", "", "", "", "","","","")
}

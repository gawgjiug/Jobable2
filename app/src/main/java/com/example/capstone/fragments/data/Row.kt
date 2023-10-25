package com.example.capstone.fragments.data

data class Row(
    val FCLT_ADDR: String, //도로명 주소
    val FCLT_CD: String,
    val FCLT_KIND_DTL_NM: String,
    val FCLT_KIND_NM: String,
    val FCLT_NM: String, //건물 이름
    val FCLT_TEL_NO: String, // 전화번호
    val FCLT_ZIPCD: String,
    val INMT_GRDN_CNT: Double, //정원
    val JRSD_SGG_CD: String,
    val JRSD_SGG_NM: String,
    val JRSD_SGG_SE: String,
    val LVLH_NMPR: Double, //현 인원
    val RPRSNTV: String
)
package com.muxiyun.bika.bean

data class PunchInBean(
        val res: Res
    ) {

        data class Res(
            val punchInLastDay: String,
            val status: String
        )
    }
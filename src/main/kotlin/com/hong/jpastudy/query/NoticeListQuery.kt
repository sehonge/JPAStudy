package com.hong.jpastudy.query

import java.time.LocalDate

data class NoticeListQuery(
    val isActivated: Boolean? = null,
    val updateBy: String? = null,
    val createBy: String? = null,
    val title: String? = null,
    val content: String? = null,
    val startAt: LocalDate? = null,
    val endAt: LocalDate? = null
) {
}

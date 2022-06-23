package com.hong.jpastudy.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class SearchNoticeDto(
    val isActivated: String? = null,
    val updateBy: String? = null,
    val createBy: String? = null,
    val title: String? = null,
    val content: String? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startAt: LocalDate? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endAt: LocalDate? = null
) {
}

enum class EnumIsActivated(val input: String, val value: Boolean?) {
    ALL("all", null),
    ON("true", true),
    OFF("false", false)
}

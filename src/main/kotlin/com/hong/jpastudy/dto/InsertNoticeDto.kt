package com.hong.jpastudy.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class InsertNoticeDto(
    val id: Long?,
    val title: String,
    val content: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val createBy: String,
    val isActivated: Boolean
) {
}

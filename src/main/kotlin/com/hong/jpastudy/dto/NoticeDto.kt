package com.hong.jpastudy.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NoticeDto(
    val id: Long,
    val title: String,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val startAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val endAt: LocalDateTime,
    val updateBy: String,
    val createBy: String,
    val isActivated: Boolean,
    val isDelete: Boolean
) {

}

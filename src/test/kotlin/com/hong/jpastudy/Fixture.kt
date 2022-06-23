package com.hong.jpastudy

import com.hong.jpastudy.dto.InsertNoticeDto
import com.hong.jpastudy.dto.NoticeDto
import com.hong.jpastudy.dto.UpdateNoticeDto
import com.hong.jpastudy.dto.UserDto
import com.hong.jpastudy.entity.Notice
import com.hong.jpastudy.entity.User
import java.time.LocalDateTime

class Fixture {
    companion object {
        val userDto = UserDto(
            userId = "userId123",
            password = "password123"
        )

        val user: User = User(
            id = "userId123",
            password = "password123"
        )

        val notValidUserDto = UserDto(
            userId = "userId123",
            password = "short"
        )

        val notice: Notice = Notice(
            id = 1L,
            content = "content",
            title = "title",
            startAt = LocalDateTime.now().minusWeeks(1L),
            endAt = LocalDateTime.now().plusDays(3L),
            updateBy = "sehong",
            createBy = "pong",
            isActivated = true
        )

        val noticeDto = NoticeDto(
            id = 1L,
            content = "content",
            title = "title",
            startAt = LocalDateTime.now().minusWeeks(1L),
            endAt = LocalDateTime.now().plusDays(3L),
            updateBy = "sehong",
            createBy = "pong",
            isActivated = true,
            isDelete = false
        )

        val insertNoticeDto = InsertNoticeDto(
            id = null,
            title = "test",
            content = "test_content",
            startAt = LocalDateTime.now().minusDays(1L),
            endAt = LocalDateTime.now().plusDays(3L),
            createBy = "sehong",
            isActivated = true
        )

        val updateNoticeDto = UpdateNoticeDto(
            id = 4L,
            title = "test",
            content = "test_content",
            startAt = LocalDateTime.now().minusDays(1L),
            endAt = LocalDateTime.now().plusDays(2L),
            createBy = "sehong",
            updateBy = "sehong",
            isActivated = true
        )
    }
}

package com.hong.jpastudy.service

import com.hong.jpastudy.Fixture
import com.hong.jpastudy.dto.NoticeDto
import com.hong.jpastudy.dto.SearchNoticeDto
import com.hong.jpastudy.entity.Notice
import com.hong.jpastudy.exception.NoneNoticeException
import com.hong.jpastudy.exception.NoticeConvertException
import com.hong.jpastudy.repository.NoticeRepository
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@SpringBootTest
internal class NoticeEditorTest {

    @InjectMocks
    lateinit var service: NoticeEditor

    @Mock
    lateinit var repository: NoticeRepository


    val notice: Notice = Fixture.notice

    @Nested
    @DisplayName("getAllNotice Test")
    inner class GetAllNoticeTest {

        @Test
        @DisplayName("getAllNotice는 요청한 page의 정보에 맞게 notice들을 page에 감싸서 return 한다.")
        fun test00() {
            // given
            val list: PageImpl<Notice> = PageImpl(listOf(notice, notice))
            val pageRequest: PageRequest = PageRequest.of(0, 10)

            // when
            Mockito.`when`(repository.findAllBy(any(), any())).thenReturn(list)

            // then
            val allNotice: Page<NoticeDto> = service.getAllNotice(pageRequest)
            allNotice.forEach { noticeDto: NoticeDto ->
                assertEquals(notice.id, noticeDto.id)
            }
        }

        @Test
        @DisplayName("DB에 존재하는 notice가 없거나 모두 delete된 상태라면 content가 비어있는 page를 반환한다.")
        fun test01() {
            // given
            val empty = Page.empty<Notice>()
            val pageRequest: PageRequest = PageRequest.of(0, 10)

            // when
            Mockito.`when`(repository.findAllBy(any(), any())).thenReturn(empty)

            // then
            val allNotice: Page<NoticeDto> = service.getAllNotice(pageRequest)
            assertEquals(0, allNotice.content.size)
        }
    }

    @Nested
    @DisplayName("getNotice Test")
    inner class GetNoticeTest {

        @Test
        @DisplayName("요청한 noticeId에 대응되는 notice를 NoticeDto로 변환해서 return 한다.")
        fun test00() {
            // given
            val noticeId: Long = 1L

            // when
            Mockito.`when`(repository.findById  (Mockito.anyLong())).thenReturn(Optional.of(notice))

            // then
            val noticeDto: NoticeDto = service.getNotice(noticeId)
            assertEquals(1L, noticeDto.id)
        }

        @Test
        @DisplayName("요청한 noticeId에 대응되는 notice가 없다면, NoneNoticeException을 던진다.")
        fun test01() {
            // given
            val noticeId: Long = -3L

            // when
            Mockito.`when`(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty())

            // then
            val noneNoticeException: NoneNoticeException = assertThrows<NoneNoticeException> {
                service.getNotice(noticeId)
            }
            println("noneNoticeException = ${noneNoticeException}")
        }
    }

    @Nested
    @DisplayName("getNoticeByQuery Test")
    inner class GetNoticeByQueryTest {

        @Test
        @DisplayName("요청한 query에 대응되는 notice를 NoticeDto로 변환해 Page에 담아서 return 한다.")
        fun test00() {
            // given
            val input: SearchNoticeDto = SearchNoticeDto(title = notice.title, isActivated = "all")
            val list: PageImpl<Notice> = PageImpl(listOf(notice, notice))
            val pageRequest = PageRequest.of(0, 10)

            // when
            Mockito.`when`(repository.findAllBy(any(), any())).thenReturn(list)

            // then
            val notices: Page<NoticeDto> = service.getNoticeByQuery(pageRequest, input)
            notices.forEach { noticeDto ->
                assertEquals(input.title, noticeDto.title)
            }
        }

        @Test
        @DisplayName("isActivated값을 입력하지 않으면 NoticeConvertException을 던진다.")
        fun test01() {
            // given
            val input: SearchNoticeDto = SearchNoticeDto(title = notice.title)
            val list: PageImpl<Notice> = PageImpl(listOf(notice, notice))
            val pageRequest = PageRequest.of(0, 10)

            // when
            Mockito.`when`(repository.findAllBy(any(), any())).thenReturn(list)

            // then
            assertThrows<NoticeConvertException> {
                service.getNoticeByQuery(pageRequest, input)
            }
        }
    }

    @Nested
    @DisplayName("addNotice Test")
    inner class AddNoticeTest {

        @Test
        @DisplayName("전달받은 insertNoticeDto에 대응되는 Notice로 변환해서 db에 save 하고 성공시에 true를 반환한다.")
        fun test00() {
            // given
            val insertNoticeDto = Fixture.insertNoticeDto
            val notice: Notice = Fixture.notice

            // when
            Mockito.`when`(repository.save(any())).thenReturn(notice)

            // then
            val result: Boolean = service.addNotice(insertNoticeDto)
            assertEquals(true, result)
        }
    }

    @Nested
    @DisplayName("updateNotice Test")
    inner class UpdateNoticeTest {

        @Test
        @DisplayName("전달받은 updateNoticeDto에 대응되는 Notice로 변환해서 db에 update 하고 성공시에 true를 반환한다.")
        fun test00() {
            // given
            val updateNoticeDto = Fixture.updateNoticeDto
            val notice = Fixture.notice

            // when
            Mockito.`when`(repository.save(any())).thenReturn(notice)

            // then
            val result: Boolean = service.updateNotice(updateNoticeDto)
            assertEquals(true, result)
        }
    }

    @Nested
    @DisplayName("deleteNotice Test")
    inner class DeleteNoticeTest {

        @Test
        @DisplayName("전달받은 noticeId에 대응되는 Notice의 isDelete를 true로 값을 변경하고 성공시에 true를 반환한다.")
        fun test00() {
            // given
            val noticeId: Long = 4L
            val notice: Notice = Fixture.notice

            // when
            Mockito.`when`(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(notice))

            // then
            val result: Boolean = service.deleteNotice(noticeId)
            assertEquals(true, result)
        }

        @Test
        @DisplayName("전달받은 noticeId에 대응되는 Notice가 없는 경우, NoneNoticeException을 던진다.")
        fun test01() {
            // given
            val noticeId: Long = 100L

            // when
            Mockito.`when`(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty())

            // then
            val exception = assertThrows<NoneNoticeException> {
                service.deleteNotice(noticeId)
            }
            println("exception = ${exception}")
        }
    }
}

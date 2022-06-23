package com.hong.jpastudy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hong.jpastudy.Fixture
import com.hong.jpastudy.SpringMockMvcTestSupport
import com.hong.jpastudy.dto.InsertNoticeDto
import com.hong.jpastudy.dto.NoticeDto
import com.hong.jpastudy.dto.SearchNoticeDto
import com.hong.jpastudy.dto.UpdateNoticeDto
import com.hong.jpastudy.exception.NoneNoticeException
import com.hong.jpastudy.service.NoticeEditable
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@WebMvcTest(controllers = [NoticeController::class])
internal class NoticeControllerTest : SpringMockMvcTestSupport() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var service: NoticeEditable

    val noticeDto: NoticeDto = Fixture.noticeDto

    val insertNoticeDto: InsertNoticeDto = Fixture.insertNoticeDto

    val updateNoticeDto: UpdateNoticeDto = Fixture.updateNoticeDto

    @Nested
    @DisplayName("GET 방식으로 통신할 때")
    inner class GetMappingTest {

        @Test
        @DisplayName("/api/notice endpoint로 접근했을 때, Page에 담긴 NoticeDto를 반환한다.")
        fun test00() {
            // given
            val inputUri = "/api/notice"
            val list: PageImpl<NoticeDto> = PageImpl(listOf(noticeDto))
            val map: MultiValueMap<String, String> = LinkedMultiValueMap()
            map.add("page", "0")
            map.add("size", "10")

            // when
            Mockito.`when`(service.getAllNotice(any())).thenReturn(list)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(inputUri)
                    .params(map)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("/api/notice endpoint로 접근했을 때 Parameter값이 입력이 안되었다면, PageableDefault의 기본값이 service로 넘어가 Page에 담긴 NoticeDto를 반환한다.")
        fun test01() {
            // given
            val inputUri = "/api/notice"
            val list: PageImpl<NoticeDto> = PageImpl(listOf(noticeDto))

            // when
            Mockito.`when`(service.getAllNotice(any())).thenReturn(list)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(inputUri)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("/api/notice/{noticeId} endpoint로 접근했을 때, noticeId에 대응되는 NoticeDto가 반환된다.")
        fun test03() {
            // given
            val noticeNumber = noticeDto.id
            val inputUri = "/api/notice/${noticeNumber}"

            // when
            Mockito.`when`(service.getNotice(Mockito.anyLong())).thenReturn(noticeDto)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(inputUri)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("/api/notice/{noticeId} endpoint로 접근했을 때, noticeId에 대응되는 Notice가 없다면 404를 반환한다.")
        fun test04() {
            // given
            val noticeNumber = noticeDto.id
            val inputUri = "/api/notice/${noticeNumber}"

            // when
            Mockito.`when`(service.getNotice(Mockito.anyLong())).thenThrow(NoneNoticeException("wrong notice Id = ${noticeNumber}"))
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(inputUri)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("/api/notice/search endpoint로 접근했을 때, query에 대응되는 NoticeDto를 Page에 담아 반환한다.")
        fun test05() {
            // given
            val inputUri = "/api/notice/search"
            val input = SearchNoticeDto(title = noticeDto.title)
            val list: PageImpl<NoticeDto> = PageImpl(listOf(noticeDto))
            val map: MultiValueMap<String, String> = LinkedMultiValueMap()
            map.add("page", "0")
            map.add("size", "10")
            map.add("title", input.title)

            // when
            Mockito.`when`(service.getNoticeByQuery(any(), any())).thenReturn(list)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(inputUri)
                    .params(map)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }
    }

    @Nested
    @DisplayName("POST 방식으로 통신할 때")
    inner class PostMappingTest {

        @Test
        @DisplayName("RequestBody에 Notice에 대한 정보를 올바르게 입력하면 200을 반환한다.")
        fun test01() {
            // given
            val inputUri = "/api/notice"

            // when
            Mockito.`when`(service.addNotice(any())).thenReturn(true)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(inputUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(insertNoticeDto))
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("RequestBody에 Notice에 대한 정보를 올바르게 입력되지 않으면 400을 반환한다.")
        fun test02() {
            // given
            val inputUri = "/api/notice"
            val inputWrongStartAt = "{ " +
                    "\"content\" : \"1234\"," +
                    "\"title\" : \"test\"," +
                    "\"startAt\" : \"hi\"," +
                    "\"endAt\" : \"2022-04-13T12:00:00\"," +
                    "\"createBy\" : \"pong\"," +
                    "\"isActivated\" : \"true\"" +
                    "}"

            // when
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(inputUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(inputWrongStartAt)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }
    }

    @Nested
    @DisplayName("PUT 방식으로 통신할 때")
    inner class PutMappingTest {

        @Test
        @DisplayName("RequestBody에 UpdateNotice에 대한 정보를 올바르게 입력하면 200을 반환한다.")
        fun test01() {
            // given
            val inputUri = "/api/notice"

            // when
            Mockito.`when`(service.updateNotice(any())).thenReturn(true)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(inputUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateNoticeDto))
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("RequestBody에 UpdateNotice에 대한 정보를 올바르게 입력하지않으면 400을 반환한다.")
        fun test02() {
            // given
            val inputUri = "/api/notice"
            val inputWrongStartAt = "{ " +
                    "\"id\" : \"1234\"," +
                    "\"content\" : \"1234\"," +
                    "\"title\" : \"test\"," +
                    "\"startAt\" : \"hi\"," +
                    "\"endAt\" : \"2022-04-13T12:00:00\"," +
                    "\"createBy\" : \"pong\"," +
                    "\"updateBy\" : \"sehong\"," +
                    "\"isActivated\" : \"true\"" +
                    "}"

            // when
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(inputUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(inputWrongStartAt)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }
    }

    @Nested
    @DisplayName("DELETE 방식으로 /notice/{noticeId} endpoint로 접근했을 때,")
    inner class DeleteMappingTest {

        @Test
        @DisplayName("noticeId에 대응되는 Notice의 isDelete상태가 변경되면 200을 반환된다.")
        fun test03() {
            // given
            val noticeNumber = 1L
            val inputUri = "/api/notice/${noticeNumber}"

            // when
            Mockito.`when`(service.deleteNotice(Mockito.anyLong())).thenReturn(true)
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(inputUri)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }

        @Test
        @DisplayName("noticeId에 대응되는 Notice가 없다면 404를 반환한다.")
        fun test04() {
            // given
            val noticeNumber = 1L
            val inputUri = "/api/notice/${noticeNumber}"

            // when
            Mockito.`when`(service.deleteNotice(Mockito.anyLong()))
                .thenThrow(NoneNoticeException("Not Exists Notice. NoticeId = ${noticeNumber}"))
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(inputUri)
            )

            // then
            actions
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
        }
    }
}

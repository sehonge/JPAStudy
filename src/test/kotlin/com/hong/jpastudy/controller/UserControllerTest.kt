package com.hong.jpastudy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hong.jpastudy.Fixture
import com.hong.jpastudy.dto.UserDto
import com.hong.jpastudy.exception.NotValidUserForm
import com.hong.jpastudy.service.UserService
import com.hong.jpastudy.service.UserServiceImpl
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
        ) {

    @MockBean
    lateinit var userService: UserService

//    @MockBean // token 생성을 위한 mockBean
//    lateinit var repository: UserRepository

    @Nested
    @DisplayName("GET 방식으로 통신할 때")
    inner class GetMappingTest {

        @Nested
        @DisplayName("/api/user/login endpoint로 접근하는 경우")
        inner class LoginTest {
            @Test
            @DisplayName("로그인에 성공하면 True와 200을 반환하고 session에 id를 저장한다.")
            fun test00() {
                // given
                val inputURi = "/api/user/login"
                val userDto = Fixture.userDto
                val map: MultiValueMap<String, String> = LinkedMultiValueMap();
                map.add("userId", userDto.userId)
                map.add("password", userDto.password)

                // when
                Mockito.`when`(userService.login(any())).thenReturn(true)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .params(map)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            @Test
            @DisplayName("로그인에 실패하면 False와 404를 반환한다.")
            fun test01() {
                // given
                val inputURi = "/api/user/login"
                val userDto = Fixture.userDto
                val map: MultiValueMap<String, String> = LinkedMultiValueMap();
                map.add("userId", userDto.userId)
                map.add("password", userDto.password)

                // when
                Mockito.`when`(userService.login(any())).thenReturn(false)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .params(map)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isNotFound)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            @Test
            @DisplayName("userId or password가 not Valid한 형태라면, 400를 반환한다.")
            fun test02() {
                // given
                val inputURi = "/api/user/login"
                val userDto = Fixture.notValidUserDto
                val map: MultiValueMap<String, String> = LinkedMultiValueMap();
                map.add("userId", userDto.userId)
                map.add("password", userDto.password)

                // when
                Mockito.`when`(userService.login(any())).thenThrow(NotValidUserForm("Not Valid UserForm : ${userDto}"))
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .params(map)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }
        }

        @Nested
        @DisplayName("/api/user/logout endpoint로 접근하는 경우")
        inner class LogoutTest {
            @Test
            @DisplayName("로그아웃에 성공하면 True와 200을 반환하고 session에 존재하는 id를 삭제한다.")
            fun test00() {
                // given
                val inputURi = "/api/user/logout"
                val userId = Fixture.userDto.userId

                // when
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .param("userId", userId)
                        .sessionAttr("userId", userId)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("userId"))
                    .andDo(MockMvcResultHandlers.print())
            }

            @Test
            @DisplayName("로그아웃할 id가 session에 존재하지 않는다면, False와 400을 반환한다.")
            fun test01() {
                // given
                val inputURi = "/api/user/logout"
                val userId = Fixture.userDto.userId

                // when
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .param("userId", userId)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }
        }

        @Nested
        @DisplayName("/api/user/register endpoint로 접근하는 경우")
        inner class CheckDuplicateIdTest {
            @Test
            @DisplayName("DB에 중복된 Id가 존재하지 않으면, True와 200을 반환한다.")
            fun test00() {
                // given
                val inputURi = "/api/user/register"
                val userId = Fixture.userDto.userId

                // when
                Mockito.`when`(userService.duplicateIdCheck(Mockito.anyString())).thenReturn(false)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .param("userId", userId)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            @Test
            @DisplayName("DB에 중복된 id가 존재하면, False와 400을 반환한다.")
            fun test01() {
                // given
                val inputURi = "/api/user/register"
                val userId = Fixture.userDto.userId

                // when
                Mockito.`when`(userService.duplicateIdCheck(Mockito.anyString())).thenReturn(true)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(inputURi)
                        .param("userId", userId)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }
        }
    }

    @Nested
    @DisplayName("POST 방식으로 통신할 때")
    inner class PostMappingTest {

        @Nested
        @DisplayName("/api/user endpoint로 접근하는 경우")
        inner class RegisterUserTest {
            @Test
            @DisplayName("유저 등록에 성공하면 true와 200을 반환한다.")
            fun test00() {
                // given
                val inputUri = "/api/user"
                val userDto = Fixture.userDto

                // when
                Mockito.`when`(userService.register(any())).thenReturn(true)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            @ParameterizedTest(name = "http body에 {0}를 넘기지 않으면, HttpMessageNotReadableException과 BAD_REQUEST(400)를 반환한다.")
            @CsvSource(value = [
                "password, { \"userId\" : \"test\" }",
                "userId, { \"password\" : \"test\" }"
            ])
            fun `http body에 {0}를 넘기지 않으면, HttpMessageNotReadableException과 BAD_REQUEST(400)를 반환한다`(target:String, inputJson: String) {
                // given
                val inputUri = "/api/user"

                // when
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            // NotValidUserForm 던지는거 체크
            @Test
            @DisplayName("유저 등록에 성공하면 true와 200을 반환한다.")
            fun test02() {
                // given
                val inputUri = "/api/user"
                val userDto = Fixture.notValidUserDto

                // when
                Mockito.`when`(userService.register(any())).thenThrow(NotValidUserForm("Not Valid User : ${userDto}"))
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }
        }
    }

    @Nested
    @DisplayName("PUT 방식으로 통신할 때")
    inner class PutMappingTest {

        @Nested
        @DisplayName("/api/user endpoint로 접근하는 경우")
        inner class ChangePasswordTest {
            @Test
            @DisplayName("비밀번호 변경에 성공하면 true와 200을 반환하고 session에 존재하는 userId를 삭제한다.")
            fun test00() {
                // given
                val inputUri = "/api/user"
                val userDto = Fixture.userDto

                // when
                Mockito.`when`(userService.changePassword(any())).thenReturn(true)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.put(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("userId"))
                    .andDo(MockMvcResultHandlers.print())
            }

            @Test
            @DisplayName("비밀번호 변경에 실패하면 false와 404을 반환한다.")
            fun test01() {
                // given
                val inputUri = "/api/user"
                val userDto = Fixture.userDto

                // when
                Mockito.`when`(userService.changePassword(any())).thenReturn(false)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.put(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isNotFound)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            @ParameterizedTest(name = "http body에 {0}를 넘기지 않으면, HttpMessageNotReadableException과 BAD_REQUEST(400)를 반환한다.")
            @CsvSource(value = [
                "password, { \"userId\" : \"test\" }",
                "userId, { \"password\" : \"test\" }"
            ])
            fun `http body에 {0}를 넘기지 않으면, HttpMessageNotReadableException과 BAD_REQUEST(400)를 반환한다`(target:String, inputJson: String) {
                // given
                val inputUri = "/api/user"

                // when
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.put(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson)
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }

            @ParameterizedTest
            @CsvSource(
                "wrong12",
                "wrongwrongwrongwrong1"
            )
            @DisplayName("password의 길이가 ${UserServiceImpl.MIN_PASSWORD_LENGTH}이상 ${UserServiceImpl.MAX_PASSWORD_LENGTH} 이하가 아니라면 NotValidUserForm과 BADREQUEST(400)을 던진다.")
            fun test04(inputVal: String) {
                // given
                val inputUri = "/api/user"
                val userDto = UserDto(
                    userId = Fixture.userDto.userId,
                    password = inputVal
                )

                /// when
                Mockito.`when`(userService.changePassword(any())).thenThrow(NotValidUserForm("Not Valid User = ${userDto}"))
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.put(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }
        }
    }

    @Nested
    @DisplayName("DELETE 방식으로 통신할 때")
    inner class DeleteMappingTest {

        @Nested
        @DisplayName("/api/user endpoint로 접근하는 경우")
        inner class DeleteUserTest {
            @Test
            @DisplayName("유저 삭제에 성공하면 true와 200을 반환하고 session에 존재하는 userId를 삭제한다.")
            fun test00() {
                // given
                val inputUri = "/api/user"
                val userDto = Fixture.userDto

                // when
                Mockito.`when`(userService.deleteUser(Mockito.anyString())).thenReturn(true)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.delete(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("userId"))
                    .andDo(MockMvcResultHandlers.print())
            }

            @Test
            @DisplayName("비밀번호가 틀려서 유저 삭제에 실패하면 false와 404을 반환한다.")
            fun test01() {
                // given
                val inputUri = "/api/user"
                val userDto = Fixture.userDto

                // when
                Mockito.`when`(userService.deleteUser(Mockito.anyString())).thenReturn(false)
                val resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.delete(inputUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto))
                )

                // then
                resultActions
                    .andExpect(MockMvcResultMatchers.status().isNotFound)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
            }
        }
    }

}

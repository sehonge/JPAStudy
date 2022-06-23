package com.hong.jpastudy.service

import com.hong.jpastudy.Fixture
import com.hong.jpastudy.dto.UserDto
import com.hong.jpastudy.entity.User
import com.hong.jpastudy.exception.NotValidUserForm
import com.hong.jpastudy.repository.UserRepository
import com.hong.jpastudy.service.UserServiceImpl.Companion.MAX_PASSWORD_LENGTH
import com.hong.jpastudy.service.UserServiceImpl.Companion.MAX_USERID_LENGTH
import com.hong.jpastudy.service.UserServiceImpl.Companion.MIN_PASSWORD_LENGTH
import com.hong.jpastudy.service.UserServiceImpl.Companion.MIN_USERID_LENGTH
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class UserServiceImplTest {

    @InjectMocks
    lateinit var userService: UserServiceImpl

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder



    @Nested
    @DisplayName("로그인을 시도할 때")
    inner class LoginTest {

        @Test
        @DisplayName("DB에 전달받은 Id, password가 일치하는 row가 존재하면, true를 반환한다.")
        fun test00() {
            // given
            val userDto = Fixture.userDto
            val encodedPassword = "encodedPassword"

            // when
            val user = User(id = userDto.userId, password = encodedPassword)
            Mockito.`when`(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user))
            Mockito.`when`(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true)

            // then
            val result = userService.login(userDto)
            assertTrue(result)
        }

        @Test
        @DisplayName("입력받은 id에 대응되는 user가 db에 존재하지 않으면, false를 반환한다.")
        fun test01() {
            // given
            val userDto = Fixture.userDto

            // when
            Mockito.`when`(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty())

            // then
            val result = userService.login(userDto)
            assertFalse(result)
        }

        @Test
        @DisplayName("입력받은 password와 db에 존재하는 user의 password가 일치하지 않으면, false를 반환한다.")
        fun test02() {
            // given
            val userDto = Fixture.userDto
            val encodedPassword = "encodedPassword"

            // when
            val user = User(id = userDto.userId, password = encodedPassword)
            Mockito.`when`(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user))
            Mockito.`when`(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false)

            // then
            val result = userService.login(userDto)
            assertFalse(result)
        }

        @ParameterizedTest
        @CsvSource(
            "wrong",
            "wrongwrongwrong1"
        )
        @DisplayName("Id의 길이가 ${MIN_USERID_LENGTH}이상 ${MAX_USERID_LENGTH} 이하가 아니라면 NotValidUserForm을 던진다.")
        fun test03(inputVal: String) {
            // given
            val userDto = UserDto(
                userId = inputVal,
                password = "test123"
            )

            // when
            // then
            assertThrows<NotValidUserForm> { userService.login(userDto) }
        }

        @ParameterizedTest
        @CsvSource(
            "wrong12",
            "wrongwrongwrongwrong1"
        )
        @DisplayName("password의 길이가 ${MIN_PASSWORD_LENGTH}이상 ${MAX_PASSWORD_LENGTH} 이하가 아니라면 NotValidUserForm을 던진다.")
        fun test04(inputVal: String) {
            // given
            val userDto = UserDto(
                userId = "userId",
                password = inputVal
            )

            // when
            // then
            assertThrows<NotValidUserForm> { userService.login(userDto) }
        }

    }

    @Nested
    @DisplayName("회원가입을 할 때")
    inner class RegisterTest {

        @Test
        @DisplayName("성공하면 true를 반환한다.")
        fun test00() {
            // given
            val userDto = Fixture.userDto
            val encodedPassword = "!@#$@"

            // when
            Mockito.`when`(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword)
            val user = User(id = userDto.userId, password = encodedPassword)
            Mockito.`when`(userRepository.save(any())).thenReturn(user)

            // then
            val result = userService.register(userDto)
            assertTrue(result)
        }

        @ParameterizedTest
        @CsvSource(
            "wrong",
            "wrongwrongwrong1"
        )
        @DisplayName("Id의 길이가 ${MIN_USERID_LENGTH}이상 ${MAX_USERID_LENGTH} 이하가 아니라면 NotValidUserForm을 던진다.")
        fun test01(inputVal: String) {
            // given
            val userDto = UserDto(
                userId = inputVal,
                password = "test123"
            )

            // when
            // then
            assertThrows<NotValidUserForm> { userService.register(userDto) }
        }

        @ParameterizedTest
        @CsvSource(
            "wrong12",
            "wrongwrongwrongwrong1"
        )
        @DisplayName("password의 길이가 ${MIN_PASSWORD_LENGTH}이상 ${MAX_PASSWORD_LENGTH} 이하가 아니라면 NotValidUserForm을 던진다.")
        fun test02(inputVal: String) {
            // given
            val userDto = UserDto(
                userId = "userId",
                password = inputVal
            )

            // when
            // then
            assertThrows<NotValidUserForm> { userService.register(userDto) }
        }
    }

    @Nested
    @DisplayName("비밀번호 변경을 할 때")
    inner class ChangePasswordTest {

        @Test
        @DisplayName("비밀번호 변경에 성공하면 True를 반환한다")
        fun test00() {
            // given
            val userDto = Fixture.userDto
            val newEncodedPassword = "newEncoded123"
            val encodedPassword = "!@#$@"
            val user = User(
                id = userDto.userId,
                password = encodedPassword
            )

            // when
            Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))
            Mockito.`when`(passwordEncoder.encode(Mockito.anyString())).thenReturn(newEncodedPassword)

            // then
            assertTrue(userService.changePassword(userDto))
        }

        @Test
        @DisplayName("비밀번호를 변경할 userId가 db에 없으면, false를 반환한다")
        fun test01() {
            // given
            val userDto = Fixture.userDto

            // when
            Mockito.`when`(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty())

            // then
            assertFalse(userService.changePassword(userDto))
        }

        @ParameterizedTest
        @CsvSource(
            "wrong12",
            "wrongwrongwrongwrong1"
        )
        @DisplayName("password의 길이가 ${MIN_PASSWORD_LENGTH}이상 ${MAX_PASSWORD_LENGTH} 이하가 아니라면 NotValidUserForm을 던진다.")
        fun test03(inputVal: String) {
            // given
            val userDto = UserDto(
                userId = Fixture.userDto.userId,
                password = inputVal
            )

            // when
            // then
            assertThrows<NotValidUserForm> { userService.changePassword(userDto) }
        }
    }

    @Nested
    @DisplayName("유저 삭제를 할 때")
    inner class DeleteUserTest {

        @Test
        @DisplayName("유저 삭제에 성공하면 true를 반환한다.")
        fun test00() {
            // given
            val userId = Fixture.user.id
            val user = Fixture.user

            // when
            Mockito.`when`(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user))

            // then
            assertTrue(userService.deleteUser(userId))
        }

        @Test
        @DisplayName("삭제할 userId가 db에 없으면 false를 반환한다.")
        fun test01() {
            // given
            val userId = Fixture.userDto.userId

            // when
            Mockito.`when`(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty())

            // then
            assertFalse(userService.deleteUser(userId))
        }
    }

    @Nested
    @DisplayName("중복 유저 검색할 때")
    inner class DuplicateIdCheckTest {

        @Test
        @DisplayName("입력한 id와 중복되는 id가 db에 존재하면, true를 반환한다.")
        fun test00() {
            // given
            val user = Fixture.user
            val userId = user.id

            // when
            Mockito.`when`(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user))

            // then
            assertTrue(userService.duplicateIdCheck(userId))
        }

        @Test
        @DisplayName("입력한 id와 중복되는 id가 db에 존재하지 않으면, false를 반환한다.")
        fun test01() {
            // given
            val userId = Fixture.userDto.userId

            // when
            Mockito.`when`(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty())

            // then
            assertFalse(userService.duplicateIdCheck(userId))
        }
    }
}

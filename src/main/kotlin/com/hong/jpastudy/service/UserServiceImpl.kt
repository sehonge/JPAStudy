package com.hong.jpastudy.service

import com.hong.jpastudy.dto.UserDto
import com.hong.jpastudy.entity.User
import com.hong.jpastudy.exception.NotValidUserForm
import com.hong.jpastudy.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@RequiredArgsConstructor
open class UserServiceImpl(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder): UserService {

    companion object {
        const val MAX_USERID_LENGTH = 15
        const val MIN_USERID_LENGTH = 6
        const val MAX_PASSWORD_LENGTH = 20
        const val MIN_PASSWORD_LENGTH = 8
    }

    @Transactional(readOnly = true)
    override fun login(userDto: UserDto): Boolean {
        if (checkValid(userDto)) {
            val findById: Optional<User> = userRepository.findById(userDto.userId)

            return if (findById.isPresent) {
                val user = findById.get()
                passwordEncoder.matches(userDto.password, user.password)
            } else {
                false
            }
        } else {
            throw NotValidUserForm("Not Valid User = ${userDto}")
        }
    }

    @Transactional
    override fun register(userDto: UserDto): Boolean {
        return if (checkValid(userDto)) {
            val user = User(
                id = userDto.userId,
                password = passwordEncoder.encode(userDto.password)
            )
            val save = userRepository.save(user)
            true
        } else {
            throw NotValidUserForm("Not Valid User = ${userDto}")
        }
    }

    @Transactional
    override fun changePassword(userDto: UserDto): Boolean {
        if (checkValid(userDto)) {
            val findById = userRepository.findById(userDto.userId)
            return if (findById.isPresent) {
                val user = findById.get()
                user.password = passwordEncoder.encode(userDto.password)
                true
            } else {
                false
            }
        } else {
            throw NotValidUserForm("Not Valid User = ${userDto}")
        }
    }

    @Transactional
    override fun deleteUser(userId: String): Boolean {
        val findById: Optional<User> = userRepository.findById(userId)
        return if (findById.isPresent) {
            val user = findById.get()
            user.delete()
            return true
        } else {
            false
        }
    }

    @Transactional(readOnly = true)
    override fun duplicateIdCheck(userId: String): Boolean {
        val findById = userRepository.findById(userId)
        return findById.isPresent
    }

    private fun checkValid(userDto: UserDto): Boolean {
        return (userDto.userId.length in MIN_USERID_LENGTH..MAX_USERID_LENGTH) &&
                (userDto.password.length in MIN_PASSWORD_LENGTH .. MAX_PASSWORD_LENGTH)
    }
}

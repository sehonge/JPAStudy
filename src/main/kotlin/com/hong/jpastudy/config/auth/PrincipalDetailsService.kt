package com.hong.jpastudy.config.auth

import com.hong.jpastudy.entity.User
import com.hong.jpastudy.exception.NoneNoticeException
import com.hong.jpastudy.exception.NotValidUserForm
import com.hong.jpastudy.exception.UserNotFoundException
import com.hong.jpastudy.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class PrincipalDetailsService(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        println("loadUserByUsername")
        val user: User = userRepository.findById(username ?: throw NotValidUserForm("not valid userId = ${username}"))
            .orElseThrow { UserNotFoundException("not found userId : ${username}") }

        return PrincipalDetails(user)
    }


}

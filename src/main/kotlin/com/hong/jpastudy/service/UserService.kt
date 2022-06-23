package com.hong.jpastudy.service

import com.hong.jpastudy.dto.UserDto

interface UserService {
    fun login(userDto: UserDto): Boolean
    fun register(userDto: UserDto): Boolean
    fun changePassword(userDto: UserDto): Boolean
    fun deleteUser(userId: String): Boolean
    fun duplicateIdCheck(userId: String): Boolean
}

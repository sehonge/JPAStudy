package com.hong.jpastudy.repository

import com.hong.jpastudy.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
}

package com.hong.jpastudy.repository

import com.hong.jpastudy.entity.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository


interface NoticeRepository: JpaRepository<Notice, Long>, NoticeCustomRepository {
    fun findAllByIsDelete(isDelete: Boolean = false, pageable: Pageable): Page<Notice>
}

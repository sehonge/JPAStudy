package com.hong.jpastudy.repository

import com.hong.jpastudy.entity.Notice
import com.hong.jpastudy.query.NoticeListQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NoticeCustomRepository {
    fun findAllBy(pageable: Pageable, query: NoticeListQuery): Page<Notice>
}

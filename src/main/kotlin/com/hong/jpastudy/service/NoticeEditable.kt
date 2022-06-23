package com.hong.jpastudy.service

import com.hong.jpastudy.dto.InsertNoticeDto
import com.hong.jpastudy.dto.NoticeDto
import com.hong.jpastudy.dto.SearchNoticeDto
import com.hong.jpastudy.dto.UpdateNoticeDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NoticeEditable {
    fun getAllNotice(pageable: Pageable): Page<NoticeDto>
    fun getNotice(noticeId: Long): NoticeDto
    fun getNoticeByQuery(pageable: Pageable, input: SearchNoticeDto): Page<NoticeDto>
    fun addNotice(insertNoticeDto: InsertNoticeDto): Boolean
    fun updateNotice(updateNoticeDto: UpdateNoticeDto): Boolean
    fun deleteNotice(noticeId: Long): Boolean
}

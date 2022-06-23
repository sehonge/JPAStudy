package com.hong.jpastudy.service

import com.hong.jpastudy.dto.*
import com.hong.jpastudy.entity.Notice
import com.hong.jpastudy.exception.NoneNoticeException
import com.hong.jpastudy.exception.NoticeConvertException
import com.hong.jpastudy.query.NoticeListQuery
import com.hong.jpastudy.repository.NoticeRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Slf4j
@Service
@RequiredArgsConstructor
open class NoticeEditor(val noticeRepository: NoticeRepository) : NoticeEditable {

    @Transactional(readOnly = true)
    override fun getAllNotice(pageable: Pageable): Page<NoticeDto> {
        return noticeRepository.findAllBy(pageable = pageable, NoticeListQuery())
            .map { notice: Notice -> notice.toDataModel() }
    }

    @Transactional(readOnly = true)
    override fun getNotice(noticeId: Long): NoticeDto {
        val findById: Optional<Notice> = noticeRepository.findById(noticeId)

        if (findById.isPresent) {
            return findById.get().toDataModel()
        } else {
            throw NoneNoticeException("Not Exists Notice. NoticeId = ${noticeId}")
        }
    }

    @Transactional(readOnly = true)
    override fun getNoticeByQuery(pageable: Pageable, input: SearchNoticeDto): Page<NoticeDto> {
        val find: EnumIsActivated = EnumIsActivated.values().find {
            it.input == input.isActivated
        } ?: throw NoticeConvertException("wrong isActivated value = ${input.isActivated}")

        val query = NoticeListQuery(
            isActivated = find.value,
            updateBy = input.updateBy,
            createBy = input.createBy,
            title = input.title,
            content = input.content,
            startAt = input.startAt,
            endAt = input.endAt
        )

        return noticeRepository.findAllBy(pageable = pageable, query = query)
            .map { notice: Notice -> notice.toDataModel() }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun addNotice(insertNoticeDto: InsertNoticeDto): Boolean {
        val notice: Notice = Notice.of(insertNoticeDto = insertNoticeDto)

        noticeRepository.save(notice)
        return true
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateNotice(updateNoticeDto: UpdateNoticeDto): Boolean {
        val notice: Notice = Notice.of(updateNoticeDto = updateNoticeDto)

        noticeRepository.save(notice)
        return true
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteNotice(noticeId: Long): Boolean {
        val notice: Optional<Notice> = noticeRepository.findById(noticeId)

        if (notice.isPresent) {
            notice.get().delete()
            return true
        } else {
            throw NoneNoticeException("Not Exists Notice. NoticeId = ${noticeId}")
        }
    }

}

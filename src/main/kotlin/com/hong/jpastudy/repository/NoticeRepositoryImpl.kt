package com.hong.jpastudy.repository

import com.hong.jpastudy.entity.Notice
import com.hong.jpastudy.entity.QNotice
import com.hong.jpastudy.query.NoticeListQuery
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.time.LocalDateTime
import java.time.LocalTime

open class NoticeRepositoryImpl: NoticeCustomRepository, QuerydslRepositorySupport(NoticeRepository::class.java) {

    val queryDSLNotice: QNotice = QNotice.notice

    override fun findAllBy(pageable: Pageable, query: NoticeListQuery): Page<Notice> {
        val optionalAccumulateWhere = BooleanBuilder()

        if (query.isActivated != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.isActivated.eq(query.isActivated)
            )
        }

        if (query.title != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.title.contains(query.title)
            )
        }

        if (query.content != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.content.contains(query.content)
            )
        }

        if (query.createBy != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.createBy.eq(query.createBy)
            )
        }

        if (query.updateBy != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.updateBy.eq(query.updateBy)
            )
        }

        if (query.startAt != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.endAt.goe(query.startAt.atStartOfDay())
            )
        }

        if (query.endAt != null) {
            optionalAccumulateWhere.and(
                queryDSLNotice.startAt.loe(LocalDateTime.of(query.endAt, LocalTime.MAX).withNano(0))
            )
        }

        optionalAccumulateWhere.and(
            queryDSLNotice.isDelete.eq(false)
        )

        val queryDSL = from(queryDSLNotice).where(optionalAccumulateWhere)

        val fetchCount = queryDSL.fetchCount()

        val applyPagination = querydsl!!.applyPagination(pageable, queryDSL)
            .orderBy(queryDSLNotice.endAt.desc())
            .fetch()

        return PageImpl(applyPagination, pageable, fetchCount)
    }
}
